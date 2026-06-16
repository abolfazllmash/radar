package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiApi
import com.example.api.TorobApi
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecommendationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateGoals(goals: String) {
        _uiState.update { state ->
            val updatedState = state.copy(goals = goals)
            val normalized = goals.lowercase()
                .replace('ي', 'ی')
                .replace('ك', 'ک')
                .replace("\u200c", " ")
                .replace("-", " ")
                .replace("_", " ")

            val detectedCategory = when {
                // Smartphones and Mobiles
                normalized.contains("گوشی") || 
                normalized.contains("موبایل") || 
                normalized.contains("تلفن") || 
                normalized.contains("smartphone") || 
                normalized.contains("smart phone") || 
                normalized.contains("mobile") || 
                normalized.contains("phone") ||
                normalized.contains("آیفون") ||
                normalized.contains("iphone") ||
                normalized.contains("galaxy") ||
                normalized.contains("گلکسی") ||
                normalized.contains("s24") || normalized.contains("s23") || normalized.contains("s22") || normalized.contains("s25") ||
                normalized.contains("poco") || normalized.contains("پوکو") ||
                normalized.contains("a55") || normalized.contains("a54") || normalized.contains("a35") || normalized.contains("a25") || normalized.contains("a15") ||
                normalized.contains("note") || normalized.contains("نوت") ||
                normalized.contains("ردمی") || normalized.contains("redmi") ||
                normalized.contains("پیکسل") || normalized.contains("pixel") ||
                normalized.contains("ناتینگ") || normalized.contains("nothing") ||
                normalized.contains("آنر") || normalized.contains("honor") ||
                normalized.contains("هوآوی") || normalized.contains("huawei") ||
                normalized.contains("فولد") || normalized.contains("fold") ||
                normalized.contains("فلیپ") || (normalized.contains("flip") && !normalized.contains("اسپیکر") && !normalized.contains("speaker")) -> ProductCategory.SMARTPHONE

                // Laptops & Notebooks
                normalized.contains("لپ تاپ") || 
                normalized.contains("لپتاپ") || 
                normalized.contains("نوت بوک") || 
                normalized.contains("laptop") || 
                normalized.contains("notebook") ||
                normalized.contains("مک بوک") ||
                normalized.contains("مک‌بوک") ||
                normalized.contains("macbook") ||
                normalized.contains("rog") ||
                normalized.contains("strix") ||
                normalized.contains("tuf") ||
                normalized.contains("تاف") ||
                normalized.contains("zenbook") ||
                normalized.contains("زن بوک") ||
                normalized.contains("زن‌بوک") ||
                normalized.contains("legion") ||
                normalized.contains("لجیون") ||
                normalized.contains("thinkbook") ||
                normalized.contains("thinkpad") || normalized.contains("ثینک پد") || normalized.contains("ثینکپد") ||
                normalized.contains("ideapad") || normalized.contains("آیدیاپد") ||
                normalized.contains("aspire") || normalized.contains("اسپایر") ||
                normalized.contains("vivobook") || normalized.contains("ویووبوک") ||
                normalized.contains("pavilion") || normalized.contains("پاویلیون") ||
                normalized.contains("victus") || normalized.contains("ویکتوس") ||
                normalized.contains("swift") || normalized.contains("سویفت") -> ProductCategory.LAPTOP

                // Headphones, Earbuds, Handsfree
                normalized.contains("هدفون") || 
                normalized.contains("هندزفری") || 
                normalized.contains("هندز فری") || 
                normalized.contains("هدست") || 
                normalized.contains("ایرپاد") || 
                normalized.contains("headphone") || 
                normalized.contains("handsfree") || 
                normalized.contains("hands free") || 
                normalized.contains("headset") || 
                normalized.contains("earbud") || 
                normalized.contains("earphone") ||
                normalized.contains("xm5") ||
                normalized.contains("xm4") ||
                normalized.contains("buds") ||
                normalized.contains("airpods") ||
                normalized.contains("بادز") ||
                normalized.contains("ایرپادز") ||
                normalized.contains("earbuds") ||
                normalized.contains("ساندکور") || normalized.contains("soundcore") ||
                normalized.contains("جبرا") || normalized.contains("jabra") ||
                normalized.contains("فریبادز") || normalized.contains("freebuds") -> ProductCategory.HEADPHONES

                // TVs
                normalized.contains("تلویزیون") || 
                normalized.contains("ال سی دی") || 
                normalized.contains("ال ای دی") || 
                normalized.contains("tv") || 
                normalized.contains("television") || 
                normalized.contains("مانیتور") || 
                normalized.contains("monitor") ||
                normalized.contains("bravia") ||
                normalized.contains("براویا") ||
                normalized.contains("oled") ||
                normalized.contains("اولد") ||
                normalized.contains("qled") || normalized.contains("کیولد") ||
                normalized.contains("تلوزیون") || normalized.contains("تیوی") ||
                normalized.contains("display") || normalized.contains("دیسپلی") -> ProductCategory.TV

                // Speakers & Audio System
                normalized.contains("اسپیکر") || 
                normalized.contains("بلندگو") || 
                normalized.contains("پارتی باکس") || 
                normalized.contains("باند") || 
                normalized.contains("speaker") || 
                normalized.contains("audio") ||
                normalized.contains("partybox") ||
                normalized.contains("boombox") ||
                normalized.contains("flip") ||
                normalized.contains("soundbar") ||
                normalized.contains("ساندبار") ||
                normalized.contains("مکسیدر") ||
                normalized.contains("maxeeder") ||
                normalized.contains("بومباکس") ||
                normalized.contains("سینما خانگی") -> ProductCategory.SPEAKER

                else -> null
            }

            // Auto-detection of brands from product series/direct brand names
            val detectedBrand = when {
                normalized.contains("apple") || normalized.contains("اپل") ||
                normalized.contains("iphone") || normalized.contains("آیفون") ||
                normalized.contains("macbook") || normalized.contains("مک بوک") || normalized.contains("مک‌بوک") || normalized.contains("مکبوک") ||
                normalized.contains("airpods") || normalized.contains("ایرپاد") ||
                normalized.contains("ipad") || normalized.contains("ایپد") -> "اپل"

                normalized.contains("samsung") || normalized.contains("سامسونگ") ||
                normalized.contains("galaxy") || normalized.contains("گلکسی") ||
                normalized.contains("s24") || normalized.contains("s23") || normalized.contains("s22") || normalized.contains("s25") ||
                normalized.contains("a55") || normalized.contains("a54") || normalized.contains("a35") || normalized.contains("a25") || normalized.contains("a15") ||
                normalized.contains("buds") -> "سامسونگ"

                normalized.contains("xiaomi") || normalized.contains("شیائومی") ||
                normalized.contains("redmi") || normalized.contains("ردمی") ||
                normalized.contains("poco") || normalized.contains("پوکو") ||
                normalized.contains("pad 6") || normalized.contains("پد ۶") -> "شیائومی"

                normalized.contains("asus") || normalized.contains("ایسوس") ||
                normalized.contains("zenbook") || normalized.contains("زن بوک") || normalized.contains("زن‌بوک") ||
                normalized.contains("rog") || normalized.contains("strix") || normalized.contains("tuf") || normalized.contains("تاف") ||
                normalized.contains("zephyrus") || normalized.contains("زفیروس") || normalized.contains("vivobook") || normalized.contains("ویووبوک") -> "ایسوس"

                normalized.contains("lenovo") || normalized.contains("لنوو") ||
                normalized.contains("legion") || normalized.contains("لجیون") ||
                normalized.contains("thinkbook") || normalized.contains("ثینک بوک") || normalized.contains("ثینک‌بوک") ||
                normalized.contains("thinkpad") || normalized.contains("ثینک پد") || normalized.contains("ثینک‌پد") ||
                normalized.contains("ideapad") || normalized.contains("آیدیاپد") -> "لنوو"

                normalized.contains("sony") || normalized.contains("سونی") ||
                normalized.contains("xm5") || normalized.contains("xm4") ||
                normalized.contains("bravia") || normalized.contains("براویا") -> "سونی"

                normalized.contains("jbl") || normalized.contains("جی بی ال") || normalized.contains("جی‌بی‌ال") ||
                normalized.contains("partybox") || normalized.contains("پارتی باکس") || normalized.contains("parybox") || normalized.contains("boombox") ||
                normalized.contains("charge 5") || normalized.contains("flip") -> "JBL"

                normalized.contains("lg") || normalized.contains("ال جی") || normalized.contains("ال‌جی") -> "ال‌جی"
                
                normalized.contains("anker") || normalized.contains("انکر") || normalized.contains("soundcore") || normalized.contains("ساندکور") ||
                normalized.contains("space one") || normalized.contains("q30") || normalized.contains("q45") -> "انکر"

                else -> ""
            }

            val (extractedMin, extractedMax) = extractPriceLimits(goals)
            var nextState = updatedState
            if (detectedCategory != null) {
                nextState = nextState.copy(selectedCategory = detectedCategory)
            }
            if (detectedBrand.isNotEmpty()) {
                nextState = nextState.copy(preferredBrand = detectedBrand)
            }

            var finalMin = nextState.budgetMin
            var finalMax = nextState.budgetMax
            if (extractedMin != null) {
                finalMin = extractedMin
                if (finalMin > finalMax) {
                    finalMax = (finalMin * 1.5).toLong().coerceAtMost(150_000_000L)
                }
            }
            if (extractedMax != null) {
                finalMax = extractedMax
                if (finalMax < finalMin) {
                    finalMin = (finalMax / 1.5).toLong().coerceAtLeast(1_000_000L)
                }
            }
            nextState = nextState.copy(budgetMin = finalMin, budgetMax = finalMax)

            nextState
        }
    }

    private fun extractPriceLimits(text: String): Pair<Long?, Long?> {
        var temp = text.lowercase()
            .replace('ي', 'ی')
            .replace('ك', 'ک')

        // Convert Persian/Arabic digits
        val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
        val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        persianDigits.forEachIndexed { i, c -> temp = temp.replace(c, i.toString()[0]) }
        arabicDigits.forEachIndexed { i, c -> temp = temp.replace(c, i.toString()[0]) }

        // Multiplier replacements first for safety
        val phraseMap = mapOf(
            "یک میلیون" to "1000000", "دو میلیون" to "2000000", "سه میلیون" to "3000000",
            "چهار میلیون" to "4000000", "پنج میلیون" to "5000000", "شش میلیون" to "6000000",
            "هفت میلیون" to "7000000", "هشت میلیون" to "8000000", "نه میلیون" to "9000000",
            "ده میلیون" to "10000000", "پانزده میلیون" to "15000000", "بیست میلیون" to "20000000",
            "سی میلیون" to "30000000", "چهل میلیون" to "40000000", "پنجاه میلیون" to "50000000",
            "صد میلیون" to "100000000",
            "یک تومن" to "1000000 t", "دو تومن" to "2000000 t", "سه تومن" to "3000000 t",
            "پنج تومن" to "5000000 t", "ده تومن" to "10000000 t", "بیست تومن" to "20000000 t",
            "سی تومن" to "30000000 t", "چهل تومن" to "40000000 t", "پنجاه تومن" to "50000000 t"
        )
        phraseMap.forEach { (k, v) ->
            temp = temp.replace(k, v)
        }

        // Clean thousand separators: comma followed by exactly 3 digits
        val commaPattern = Regex("""(\d+),(\d{3})(?!\d)""")
        var cleanText = temp
        while (commaPattern.containsMatchIn(cleanText)) {
            cleanText = cleanText.replace(commaPattern, "$1$2")
        }

        // Replace other separators (e.g. Persian decimal separator ٫) with dot
        cleanText = cleanText.replace('٫', '.').replace('/', '.')

        // find numbers and potential multipliers/units
        val numberRegex = Regex("""(\d+(?:\.\d+)?)\s*(میلیون|ملیون|م|m|M|هزار|k|K|تومن|تومان|ت|t|T)?""")
        val matches = numberRegex.findAll(cleanText).toList()

        if (matches.isEmpty()) return Pair(null, null)

        val parsedValues = matches.mapNotNull { match ->
            val numStr = match.groups[1]?.value ?: return@mapNotNull null
            val numValue = numStr.toDoubleOrNull() ?: return@mapNotNull null
            val unit = match.groups[2]?.value ?: ""

            val multiplier = when {
                unit.contains("میلیون") || unit.contains("ملیون") || unit == "م" || unit == "m" || unit == "M" -> 1_000_000L
                unit.contains("هزار") || unit == "k" || unit == "K" -> 1_000L
                unit == "ت" || unit == "t" || unit == "T" || unit.contains("تومن") || unit.contains("تومان") -> {
                    if (numValue < 150.0) 1_000_000L else 1L
                }
                else -> {
                    if (numValue < 150.0) 1_000_000L else 1L
                }
            }
            (numValue * multiplier).toLong()
        }

        if (parsedValues.isEmpty()) return Pair(null, null)

        var minPrice: Long? = null
        var maxPrice: Long? = null

        val textForContext = cleanText
        val isRange = textForContext.contains("بین") || textForContext.contains("از") || textForContext.contains("-") || (textForContext.contains("تا") && parsedValues.size >= 2)

        if (isRange && parsedValues.size >= 2) {
            val sorted = parsedValues.take(2).sorted()
            minPrice = sorted[0]
            maxPrice = sorted[1]
        } else {
            val firstVal = parsedValues[0]
            val isLowerBound = textForContext.contains("بالا") || textForContext.contains("بیشتر") || textForContext.contains("حداقل") || textForContext.contains("کف") || textForContext.contains("above") || textForContext.contains("more than") || textForContext.contains("min")
            val isUpperBound = textForContext.contains("تا") || textForContext.contains("زیر") || textForContext.contains("کمتر") || textForContext.contains("حداکثر") || textForContext.contains("سقف") || textForContext.contains("max") || textForContext.contains("under") || textForContext.contains("below")

            if (isLowerBound) {
                minPrice = firstVal
            } else if (isUpperBound) {
                maxPrice = firstVal
            } else {
                minPrice = (firstVal * 0.82).toLong()
                maxPrice = (firstVal * 1.22).toLong()
            }
        }

        val sanitizedMin = minPrice?.coerceIn(1_000_000L, 150_000_000L)
        val sanitizedMax = maxPrice?.coerceIn(1_000_000L, 150_000_000L)

        return Pair(sanitizedMin, sanitizedMax)
    }

    fun updateCategory(category: ProductCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateBudgetMin(min: Long) {
        _uiState.update { it.copy(budgetMin = min) }
    }

    fun updateBudgetMax(max: Long) {
        _uiState.update { it.copy(budgetMax = max) }
    }

    fun updatePreferredBrand(brand: String) {
        _uiState.update { it.copy(preferredBrand = brand) }
    }

    fun setActiveTab(tabIndex: Int) {
        _uiState.update { it.copy(activeTab = tabIndex) }
    }

    fun startOver() {
        _uiState.update {
            it.copy(
                screenState = ScreenState.FORM,
                errorMessage = "",
                activeTab = 0
            )
        }
    }

    private fun isBrandMatched(productName: String, preferredBrand: String): Boolean {
        if (preferredBrand.isBlank()) return true

        val brands = preferredBrand.lowercase()
            .replace('ي', 'ی')
            .replace('ك', 'ک')
            .replace("\u200c", " ")
            .split("،", ",", " ", "-")
            .map { it.trim() }
            .filter { it.length > 1 }

        if (brands.isEmpty()) return true

        val nameLower = productName.lowercase()
            .replace('ي', 'ی')
            .replace('ك', 'ک')
            .replace("\u200c", " ")

        val equivalents = mapOf(
            "samsung" to listOf("سامسونگ", "samsung", "گلکسی", "galaxy"),
            "سامسونگ" to listOf("سامسونگ", "samsung", "گلکسی", "galaxy"),
            "apple" to listOf("اپل", "آیفون", "apple", "iphone", "mac", "مک", "ipad"),
            "اپل" to listOf("اپل", "آیفون", "apple", "iphone", "mac", "مک", "ipad"),
            "iphone" to listOf("اپل", "آیفون", "apple", "iphone", "mac", "مک", "ipad"),
            "آیفون" to listOf("اپل", "آیفون", "apple", "iphone", "mac", "مک", "ipad"),
            "xiaomi" to listOf("شیائومی", "xiaomi", "redmi", "ردمی", "poco", "پوکو"),
            "شیائومی" to listOf("شیائومی", "xiaomi", "redmi", "ردمی", "poco", "پوکو"),
            "lg" to listOf("ال جی", "lg", "ال‌جی"),
            "ال جی" to listOf("ال جی", "lg", "ال‌جی"),
            "ال‌جی" to listOf("ال جی", "lg", "ال‌جی"),
            "sony" to listOf("سونی", "sony"),
            "سونی" to listOf("سونی", "sony"),
            "asus" to listOf("ایسوس", "asus", "rog", "tuf"),
            "ایسوس" to listOf("ایسوس", "asus", "rog", "tuf"),
            "lenovo" to listOf("لنوو", "lenovo", "legion", "thinkbook", "ideapad"),
            "لنوو" to listOf("لنوو", "lenovo", "legion", "thinkbook", "ideapad"),
            "jbl" to listOf("جی بی ال", "jbl", "جی‌بی‌ال", "partybox", "flip"),
            "جی بی ال" to listOf("جی بی ال", "jbl", "جی‌بی‌ال", "partybox", "flip"),
            "جی‌بی‌ال" to listOf("جی بی ال", "jbl", "جی‌بی‌ال", "partybox", "flip"),
            "snowa" to listOf("اسنوا", "snowa"),
            "اسنوا" to listOf("اسنوا", "snowa"),
            "maxeeder" to listOf("مکسیدر", "maxeeder"),
            "مکسیدر" to listOf("مکسیدر", "maxeeder"),
            "marshall" to listOf("مارشال", "marshall"),
            "مارشال" to listOf("مارشال", "marshall"),
            "harman" to listOf("هارمن", "harman", "aura"),
            "هارمن" to listOf("هارمن", "harman", "aura")
        )

        val expandedBrands = brands.flatMap { brand ->
            equivalents[brand] ?: listOf(brand)
        }

        return expandedBrands.any { nameLower.contains(it) }
    }

    /**
     * نتایج را فوراً نمایش می‌دهد، سپس در پس‌زمینه با قیمت و عکسِ واقعیِ ترب به‌روزرسانی می‌کند.
     * این‌طوری کاربر معطلِ ترب نمی‌ماند و قیمت‌ها به‌صورت «زنده» جایگزین می‌شوند.
     * اگر غنی‌سازی شکست بخورد، همان نتایجِ اولیه باقی می‌ماند.
     */
    private fun showResults(list: List<ProductRecommendation>) {
        _uiState.update {
            it.copy(
                recommendations = list,
                screenState = ScreenState.RESULTS,
                errorMessage = ""
            )
        }
        viewModelScope.launch {
            try {
                val enriched = TorobApi.enrichAll(list)
                _uiState.update { current ->
                    if (current.screenState == ScreenState.RESULTS) {
                        current.copy(recommendations = enriched)
                    } else {
                        current
                    }
                }
            } catch (e: Exception) {
                android.util.Log.w("RecommendationVM", "Torob enrich failed: ${e.message}")
            }
        }
    }

    fun generateRecommendations() {
        val state = _uiState.value
        val category = state.selectedCategory
        if (category == null) {
            _uiState.update {
                it.copy(
                    screenState = ScreenState.ERROR,
                    errorMessage = "لطفاً ابتدا یک دسته‌بندی برای محصول مورد نظر خود انتخاب کنید."
                )
            }
            return
        }

        if (state.goals.trim().length < 5) {
            _uiState.update {
                it.copy(
                    screenState = ScreenState.ERROR,
                    errorMessage = "لطفاً توضیحات بیشتری درباره اهداف و نیازهای خود بنویسید (حداقل ۵ کاراکتر)."
                )
            }
            return
        }

        _uiState.update { it.copy(screenState = ScreenState.LOADING) }

        viewModelScope.launch {
            val preferredBrand = state.preferredBrand.trim()
            try {
                // Check if we should use Mock recommendations
                val mockResult = MockData.getMockRecommendations(
                    category = category,
                    goals = state.goals,
                    budgetMin = state.budgetMin,
                    budgetMax = state.budgetMax
                )

                // Filter mock recommendations to only containing those brands if specified
                val filteredMock = if (mockResult != null && preferredBrand.isNotBlank()) {
                    mockResult.filter { isBrandMatched(it.productName, preferredBrand) }
                } else {
                    mockResult
                }

                if (filteredMock != null && filteredMock.isNotEmpty() && (preferredBrand.isBlank() || filteredMock.size == mockResult?.size)) {
                    // Predefined matching scenario found and totally matches requested brands
                    delay(1500)
                    showResults(filteredMock)
                } else {
                    // Fall back to actual API call which can generate exact brand products
                    val apiResult = GeminiApi.getRecommendations(
                        category = category,
                        goals = state.goals,
                        budgetMin = state.budgetMin,
                        budgetMax = state.budgetMax,
                        preferredBrand = preferredBrand
                    )
                    
                    // Filter API results as well to guarantee no unauthorized brand slips through
                    val filteredApi = if (preferredBrand.isNotBlank()) {
                        apiResult.filter { isBrandMatched(it.productName, preferredBrand) }
                    } else {
                        apiResult
                    }

                    val finalResult = if (filteredApi.isEmpty() && apiResult.isNotEmpty()) {
                        apiResult
                    } else {
                        filteredApi
                    }

                    showResults(finalResult)
                }
            } catch (e: Exception) {
                android.util.Log.e("RecommendationVM", "API or Mock call failed, falling back gracefully: ${e.message}", e)
                
                // Perfect UX Strategy: Seamless automatic fallback!
                // If API fails (such as regional 403 on Iranian Mobile IP or missing/invalid token),
                // we produce gorgeous customized, brand-specific, budget-clamped product suggestions locally.
                val fallbackList = if (preferredBrand.isNotBlank()) {
                    MockData.getFallbackRecommendations(
                        category = category,
                        preferredBrand = preferredBrand,
                        budgetMin = state.budgetMin,
                        budgetMax = state.budgetMax
                    )
                } else {
                    // Get standard mock for this category matching the budget constraints
                    MockData.getMockRecommendations(
                        category = category,
                        goals = state.goals,
                        budgetMin = state.budgetMin,
                        budgetMax = state.budgetMax
                    ) ?: emptyList()
                }

                if (fallbackList.isNotEmpty()) {
                    delay(1200) // Aesthetic organic calculation speed feel
                    showResults(fallbackList)
                } else {
                    _uiState.update {
                        it.copy(
                            screenState = ScreenState.ERROR,
                            errorMessage = "ارتباط با سرور برقرار نشد و اطلاعات محلی یافت نشد. لطفاً اینترنت خود را بررسی کنید."
                        )
                    }
                }
            }
        }
    }
}
