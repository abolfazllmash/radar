package com.example.api

import android.util.Log
import com.example.data.ProductRecommendation
import com.example.data.TorobOffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * کلاینت سبک برای API داخلیِ ترب (Torob) — هم‌سبکِ GeminiApi.
 *
 * ⚠️ ترب API عمومیِ مستند ندارد. این اندپوینت‌ها و نام فیلدها از روی کلاینت وبِ ترب
 * بازسازی شده‌اند و ممکن است هر زمان عوض شوند. اگر یک روز خروجی غیرمنتظره گرفتی:
 *   torob.com → DevTools → Network → فیلتر XHR/Fetch → عملیات را تکرار کن
 *   → روی درخواستِ api.torob.com کلیک کن → URL و کلیدهای واقعیِ JSON را کپی کن
 *   و در توابعِ extractOffers / optStringOrNull همین فایل تطبیق بده.
 *
 * جریان: search(query) → نتایج + search_id ، سپس details(prk, search_id) → فروشگاه‌ها.
 */
object TorobApi {
    private const val BASE_URL = "https://api.torob.com"
    private const val SOURCE = "next_desktop"
    private const val CACHE_TTL_MS = 5 * 60 * 1000L // ۵ دقیقه
    private const val MIN_INTERVAL_MS = 600L        // فاصله‌ی حداقلی بین درخواست‌ها

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    // کش ساده‌ی درون‌حافظه‌ای (بدنه‌ی خام JSON)
    private data class CacheEntry(val body: String, val expiresAt: Long)
    private val cache = ConcurrentHashMap<String, CacheEntry>()

    // throttle: درخواست‌ها را با فاصله سریالی می‌کند تا بلاک/ریت‌لیمیت نشویم
    private val throttleMutex = Mutex()
    @Volatile private var lastRequestAt = 0L

    // ---------- لایه‌ی پایه ----------

    private suspend fun throttle() = throttleMutex.withLock {
        val wait = lastRequestAt + MIN_INTERVAL_MS - System.currentTimeMillis()
        if (wait > 0) delay(wait)
        lastRequestAt = System.currentTimeMillis()
    }

    private fun buildUrl(path: String, params: Map<String, String>): String {
        val builder = (BASE_URL + path).toHttpUrl().newBuilder()
        for ((k, v) in params) builder.addQueryParameter(k, v)
        return builder.build().toString()
    }

    private suspend fun getJson(url: String): JSONObject = withContext(Dispatchers.IO) {
        cache[url]?.let { entry ->
            if (entry.expiresAt > System.currentTimeMillis()) {
                return@withContext JSONObject(entry.body)
            }
            cache.remove(url)
        }
        throttle()
        val request = Request.Builder()
            .url(url)
            .header("Accept", "application/json")
            .header("Accept-Language", "fa-IR,fa;q=0.9,en;q=0.8")
            .header(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 13; SM-G991B) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/124.0 Mobile Safari/537.36"
            )
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Torob HTTP ${response.code}")
            val body = response.body?.string() ?: throw Exception("Empty Torob body")
            cache[url] = CacheEntry(body, System.currentTimeMillis() + CACHE_TTL_MS)
            JSONObject(body)
        }
    }

    // ---------- اندپوینت‌ها ----------

    /** جستجوی محصول؛ خروجی: لیستِ آیتم‌های خام + search_id */
    suspend fun search(query: String, size: Int = 24): Pair<List<JSONObject>, String?> {
        val url = buildUrl(
            "/v4/base-product/search/",
            mapOf(
                "page" to "0",
                "sort" to "popularity",
                "size" to size.toString(),
                "query" to query,
                "q" to query,
                "source" to SOURCE
            )
        )
        val json = getJson(url)
        val searchId = optStringOrNull(json, "search_id")
        val results = json.optJSONArray("results").toObjectList()
        return results to searchId
    }

    /** جزئیات محصول؛ نیازمند prk (همان random_key) و search_id از خروجی search */
    suspend fun details(prk: String, searchId: String?): JSONObject {
        val params = linkedMapOf("prk" to prk, "source" to SOURCE)
        if (!searchId.isNullOrBlank()) params["search_id"] = searchId
        return getJson(buildUrl("/v4/base-product/details/", params))
    }

    // ---------- غنی‌سازیِ پیشنهادها ----------

    /**
     * یک پیشنهاد را با دادهٔ واقعیِ ترب غنی می‌کند: ارزان‌ترین قیمت، عکسِ واقعی و فروشگاه‌ها.
     * اگر چیزی پیدا نشد یا خطا داد، همان پیشنهادِ اصلی برمی‌گردد (هرگز کرش نمی‌کند).
     */
    suspend fun enrich(rec: ProductRecommendation): ProductRecommendation {
        return try {
            val (results, searchId) = search(rec.productName, size = 5)
            val top = results.firstOrNull() ?: return rec
            val prk = optStringOrNull(top, "random_key")
                ?: optStringOrNull(top, "prk")
                ?: return rec

            val detail = details(prk, searchId)
            val offers = extractOffers(detail)

            val cheapest = offers.mapNotNull { it.price }.minOrNull()
            val realPrice = cheapest ?: optLongOrNull(top, "price") ?: rec.price
            val realImage = optStringOrNull(detail, "image_url")
                ?: optStringOrNull(top, "image_url")
                ?: rec.imageUrl
            val torobUrl = optStringOrNull(top, "web_client_absolute_url")
                ?: optStringOrNull(top, "more_info_url")

            rec.copy(
                price = realPrice,
                imageUrl = realImage,
                offers = offers.ifEmpty { null },
                torobUrl = torobUrl
            )
        } catch (e: Exception) {
            Log.w("TorobApi", "enrich failed for '${rec.productName}': ${e.message}")
            rec
        }
    }

    /** غنی‌سازیِ موازیِ چند پیشنهاد با محدودیتِ هم‌زمانی. */
    suspend fun enrichAll(
        recommendations: List<ProductRecommendation>,
        concurrency: Int = 3
    ): List<ProductRecommendation> = coroutineScope {
        val gate = Semaphore(concurrency)
        recommendations
            .map { rec -> async(Dispatchers.IO) { gate.withPermit { enrich(rec) } } }
            .awaitAll()
    }

    // ---------- استخراج/نرمال‌سازی (نام فیلدها را با خروجیِ واقعی تطبیق بده) ----------

    private fun extractOffers(detail: JSONObject): List<TorobOffer> {
        val arr = detail.optJSONArray("products")
            ?: detail.optJSONObject("products_info")?.optJSONArray("products")
            ?: detail.optJSONArray("shops")
            ?: return emptyList()

        val out = ArrayList<TorobOffer>(arr.length())
        for (i in 0 until arr.length()) {
            val p = arr.optJSONObject(i) ?: continue
            out.add(
                TorobOffer(
                    shopName = optStringOrNull(p, "shop_name_fa")
                        ?: optStringOrNull(p, "shop_name")
                        ?: optStringOrNull(p, "name"),
                    price = optLongOrNull(p, "price"),
                    priceText = optStringOrNull(p, "price_text"),
                    url = optStringOrNull(p, "page_url")
                        ?: optStringOrNull(p, "web_client_absolute_url")
                        ?: optStringOrNull(p, "url")
                )
            )
        }
        return out
    }

    // ---------- کمکی‌ها ----------

    private fun JSONArray?.toObjectList(): List<JSONObject> {
        if (this == null) return emptyList()
        val out = ArrayList<JSONObject>(length())
        for (i in 0 until length()) optJSONObject(i)?.let { out.add(it) }
        return out
    }

    private fun optStringOrNull(o: JSONObject, key: String): String? =
        if (o.has(key) && !o.isNull(key)) o.optString(key).takeIf { it.isNotBlank() } else null

    private fun optLongOrNull(o: JSONObject, key: String): Long? {
        if (!o.has(key) || o.isNull(key)) return null
        return when (val v = o.opt(key)) {
            is Number -> v.toLong()
            is String -> v.toLongOrNull()
            else -> null
        }
    }
}
