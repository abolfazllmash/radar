package com.example.data

import com.squareup.moshi.JsonClass

enum class ProductCategory(val displayName: String, val englishName: String) {
    SMARTPHONE("گوشی هوشمند", "Smartphone"),
    LAPTOP("لپ‌تاپ", "Laptop"),
    HEADPHONES("هدفون", "Headphones"),
    TV("تلویزیون", "Television"),
    SPEAKER("اسپیکر", "Speaker");

    companion object {
        fun fromDisplayName(name: String): ProductCategory? {
            return values().find { it.displayName == name || it.englishName.equals(name, ignoreCase = true) }
        }
    }
}

@JsonClass(generateAdapter = true)
data class ProductRecommendation(
    val productName: String,
    val keyFeatures: String, // String of 3-5 comma/newline-separated key features
    val imageUrl: String,
    val reasoning: String,
    val price: Long? = null,
    // --- غنی‌سازی از ترب (Torob): با مقادیر واقعی پر می‌شوند ---
    val offers: List<TorobOffer>? = null, // فروشگاه‌ها و قیمت‌هایشان برای مقایسه
    val torobUrl: String? = null          // لینک صفحه‌ی محصول در ترب
)

@JsonClass(generateAdapter = true)
data class TorobOffer(
    val shopName: String?,
    val price: Long?,
    val priceText: String?,
    val url: String?
)

enum class ScreenState {
    FORM,
    LOADING,
    RESULTS,
    ERROR
}

data class UiState(
    val goals: String = "",
    val budgetMin: Long = 5_000_000L,
    val budgetMax: Long = 50_000_000L,
    val selectedCategory: ProductCategory? = null,
    val preferredBrand: String = "",
    val screenState: ScreenState = ScreenState.FORM,
    val recommendations: List<ProductRecommendation> = emptyList(),
    val errorMessage: String = "",
    val activeTab: Int = 0 // 0 = Recommendations, 1 = Comparison
)
