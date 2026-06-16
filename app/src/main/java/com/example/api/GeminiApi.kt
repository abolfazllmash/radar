package com.example.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.ProductCategory
import com.example.data.ProductRecommendation
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiApi {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun getRecommendations(
        category: ProductCategory,
        goals: String,
        budgetMin: Long,
        budgetMax: Long,
        preferredBrand: String
    ): List<ProductRecommendation> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey == "MY_GEMINI_API_KEY" || apiKey.isBlank()) {
            throw IllegalStateException("کلید API متصل نشده است. از جعبه ابزار AI Studio کلید خود را جایگذاری کنید.")
        }

        val prompt = """
            شما یک دستیار و مشاور سخت‌افزار و تکنولوژی خبره با نام «رادار» (Radar) برای کاربران ایرانی هستید.
            وظیفه شما این است که بر اساس پارامترهای زیر، حداکثر ۳ محصول مناسب را پیشنهاد دهید:
            دسته بندی: ${category.displayName} (${category.englishName})
            بودجه: از $budgetMin تومان تا $budgetMax تومان
            اهداف و نیازها: $goals
            برندهای مورد نظر (اختیاری): $preferredBrand

            قوانین سخت‌گیرانه:
            ۱. خروجی فقط و فقط باید یک رشته JSON معتبر باشد بدون هیچ متن اضافه، کاراکترهای بک‌تیک یا توضیحات مارک داون خارج از بلاک JSON.
            ۲. فرمت JSON باید دقیقاً به این شکل باشد و به هیچ عنوان فیلدی کم یا تغییر نام پیدا نکند:
            {
              "products": [
                {
                  "productName": "نام محصول به فارسی روان و کامل",
                  "keyFeatures": "۳ تا ۵ ویژگی مهم کلیدی مربوط به این محصول (به عنوان مثال: دوربین ۲۰۰ مگاپیکسلی، رم ۱۶ گیگابایت، پردازنده Snapdragon 8 Gen 3)",
                  "imageUrl": "https://picsum.photos/seed/[نام محصول به انگلیسی به صورت خلاصه]/600/400",
                  "reasoning": "توضیح کامل به فارسی درباره اینکه چرا این محصول بر اساس نیازهای کاربر بهترین گزینه است و به جای تاکید بر قیمت، تفکیک عملکردی بدهید.",
                  "price": [یک عدد به تومان، به عنوان مثال: 58000000]
                }
              ]
            }
            ۳. پیشنهادها از بهترین مطابقت (قوی‌ترین) به ضعیف‌ترین ردیف شده و مرتب قرار گیرند.
            ۴. قیمت محصول معرفی شده حتماً باید مابین بازه بودجه کاربر باشد (یعنی بین $budgetMin تا $budgetMax تومان).
            ۵. در صورتی که فیلد «برندهای مورد نظر» مشخص گردیده است و خالی نیست (${preferredBrand})، حتماً و بدون استثنا تمام محصولات پیشنهادی باید دقیقاً متعلق به برند(های) نام‌برده باشند و به هیچ عنوان نباید محصولی از برندهای دیگر (حتی در صورت شایستگی بیشتر) پیشنهاد دهید. این قاعده کاملاً الزامی است.
        """.trimIndent()

        val jsonBody = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.4)
            })
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val errorBody = response.body?.string() ?: "Empty body"
                Log.e("GeminiApi", "Error response: $errorBody")
                throw Exception("درخواست با خطا مواجه شد: کد ${response.code}")
            }

            val bodyString = response.body?.string() ?: throw Exception("پاسخ سرور خالی است")
            val cleanJson = cleanAndParseResponse(bodyString)
            parseProducts(cleanJson)
        }
    }

    private fun cleanAndParseResponse(responseString: String): String {
        try {
            val root = JSONObject(responseString)
            val candidates = root.getJSONArray("candidates")
            val content = candidates.getJSONObject(0).getJSONObject("content")
            val parts = content.getJSONArray("parts")
            val rawText = parts.getJSONObject(0).getString("text")

            var cleaned = rawText.trim()
            if (cleaned.startsWith("```json")) {
                cleaned = cleaned.substringAfter("```json").substringBeforeLast("```")
            } else if (cleaned.startsWith("```")) {
                cleaned = cleaned.substringAfter("```").substringBeforeLast("```")
            }
            return cleaned.trim()
        } catch (e: Exception) {
            Log.e("GeminiApi", "Failed to extract text from response", e)
            throw Exception("داده دریافتی معتبر نبود: ${e.message}")
        }
    }

    private fun parseProducts(jsonString: String): List<ProductRecommendation> {
        val list = mutableListOf<ProductRecommendation>()
        try {
            val obj = JSONObject(jsonString)
            val productsArr = obj.getJSONArray("products")
            for (i in 0 until productsArr.length()) {
                val p = productsArr.getJSONObject(i)
                val name = p.getString("productName")
                val features = p.getString("keyFeatures")
                val img = p.getString("imageUrl")
                val reason = p.getString("reasoning")
                val price = if (p.has("price") && !p.isNull("price")) p.getLong("price") else null

                list.add(
                    ProductRecommendation(
                        productName = name,
                        keyFeatures = features,
                        imageUrl = img,
                        reasoning = reason,
                        price = price
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("GeminiApi", "JSON Parsing failed", e)
            throw Exception("قالب‌بندی محصولات به درستی انجام نشد. لطفاً مجدداً تلاش کنید.")
        }
        return list
    }
}
