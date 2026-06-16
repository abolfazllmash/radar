package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.ProductCategory
import com.example.data.ProductRecommendation
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ShimmerSkeletonCard(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val shimmerColors = listOf(
        Color(0xFF1E1E22),
        Color(0xFF2C2C32),
        Color(0xFF1E1E22)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 300f, translateAnim - 300f),
        end = Offset(translateAnim, translateAnim)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16161A))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(20.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(16.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(32.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
        }
    }
}

private typealias Offset = androidx.compose.ui.geometry.Offset

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductCard(
    product: ProductRecommendation,
    index: Int = 0,
    category: ProductCategory = ProductCategory.SMARTPHONE,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium),
        label = "press_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .scale(scale)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16191D)),
        border = BorderStroke(
            width = if (index == 0) 1.2.dp else 1.dp,
            color = if (index == 0) Color(0xFF10A37F).copy(alpha = 0.6f) else Color(0x3310A37F)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Right Side: Small Image square/rectangular container
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1A1D21)),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl.takeIf { it.isNotBlank() } ?: getOptimalProductImageUrl(product.productName, category.englishName))
                        .crossfade(true)
                        .build(),
                    contentDescription = product.productName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize().padding(6.dp),
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF15181C)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF10A37F),
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    },
                    error = {
                        ProductCategoryFallback(category = category)
                    }
                )

                // Small top rank indicator instead of big text badge!
                val (badgeLabel, badgeBrush) = when (index) {
                    0 -> "رتبه ۱ ✦" to Brush.horizontalGradient(listOf(Color(0xFFFBBF24), Color(0xFFF59E0B)))
                    1 -> "رتبه ۲ ✧" to Brush.horizontalGradient(listOf(Color(0xFFE5E7EB), Color(0xFF9CA3AF)))
                    else -> "رتبه ۳" to Brush.horizontalGradient(listOf(Color(0xFFFDBA74), Color(0xFFD97706)))
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBrush)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badgeLabel,
                        color = if (index == 1) Color(0xFF111827) else Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Left Side: Content
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                // Top: Title & Score Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.productName,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    val score = when (index) {
                        0 -> "۹.۶ / ۱۰"
                        1 -> "۸.۸ / ۱۰"
                        else -> "۷.۹ / ۱۰"
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF10A37F).copy(alpha = 0.15f))
                            .border(0.5.dp, Color(0x4D10A37F), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.5.dp)
                    ) {
                        Text(
                            text = score,
                            color = Color(0xFF34D399),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Parse key features into glowing capsule tags (maximum 2 tag) to keep it compact
                val listTags = product.keyFeatures.split("،", ",", "\n", "-")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .take(2)

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listTags.forEach { feature ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF151A1E))
                                .border(1.dp, Color(0x3334D399), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = feature,
                                color = Color(0xFF99F6E4),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Row: Price and arrow
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Details button (link text)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onClick() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "مشاهده جزئیات",
                            tint = Color(0xFF2DD4BF),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "مشاهده جزئیات فنی",
                            color = Color(0xFF2DD4BF),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Price tag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF10A37F).copy(alpha = 0.25f), Color(0xFF2DD4BF).copy(alpha = 0.15f))
                                )
                            )
                            .border(1.dp, Color(0x332DD4BF), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = product.price?.let { "${formatToman(it)} تومان" } ?: "نامشخص",
                            color = Color(0xFFFDBA74),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailsDialog(
    product: ProductRecommendation,
    category: ProductCategory,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF121214)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFF1A1D21)),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.imageUrl.takeIf { it.isNotBlank() } ?: getOptimalProductImageUrl(product.productName, category.englishName))
                            .crossfade(true)
                            .build(),
                        contentDescription = product.productName,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF15181C)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xFF10A37F),
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        },
                        error = {
                            ProductCategoryFallback(category = category)
                        }
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Title Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = product.productName,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.price?.let { "قیمت تقریبی: ${formatToman(it)} تومان" } ?: "قیمت نامشخص",
                        color = Color(0xFF34D399),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Modal Tabs capsule-shaped Tab switcher
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F0E17))
                        .border(1.dp, Color(0x2210A37F), RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Tab 0: Specs
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .then(
                                    if (selectedTab == 0) {
                                        Modifier.background(
                                            Brush.horizontalGradient(
                                                colors = listOf(Color(0xFF10A37F), Color(0xFF34D399))
                                            )
                                        )
                                    } else Modifier
                                )
                                .clickable { selectedTab = 0 }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "مشخصات",
                                color = if (selectedTab == 0) Color.White else Color.White.copy(alpha = 0.55f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Tab 1: AI Analysis
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .then(
                                    if (selectedTab == 1) {
                                        Modifier.background(
                                            Brush.horizontalGradient(
                                                colors = listOf(Color(0xFF10A37F), Color(0xFF34D399))
                                            )
                                        )
                                    } else Modifier
                                )
                                .clickable { selectedTab = 1 }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "تحلیل تخصصی",
                                color = if (selectedTab == 1) Color.White else Color.White.copy(alpha = 0.55f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Tab 2: Stores Comparison
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .then(
                                    if (selectedTab == 2) {
                                        Modifier.background(
                                            Brush.horizontalGradient(
                                                colors = listOf(Color(0xFF10A37F), Color(0xFF34D399))
                                            )
                                        )
                                    } else Modifier
                                )
                                .clickable { selectedTab = 2 }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "فروشگاه‌ها (ترب)",
                                color = if (selectedTab == 2) Color.White else Color.White.copy(alpha = 0.55f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab Content - Scrollable to prevent visual overflow on small/medium screens
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                ) {
                    if (selectedTab == 0) {
                        // Specs Grid
                        Text(
                            text = "ویژگی‌های اصلی سخت‌افزار:",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val featuresList = product.keyFeatures.split("،", ",", "\n", "-").filter { it.isNotBlank() }
                        featuresList.forEach { feature ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = feature.trim(),
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Right
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    imageVector = getCategorySpecIcon(category),
                                    contentDescription = "feature icon",
                                    tint = Color(0xFF10A37F),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    } else if (selectedTab == 1) {
                        // AI Commentary analysis
                        Text(
                            text = "چرا این محصول برای شما پیشنهاد شده؟",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = product.reasoning,
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Row of satisfaction/performance metrics circular gauges
                        val seed = product.productName.hashCode().coerceAtLeast(0)
                        val satisfactionScore = 88 + (seed % 11)
                        val performanceScore = 85 + (seed % 14)
                        val economyScore = 82 + (seed % 17)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularGauge(score = economyScore, label = "ارزش خرید", color = Color(0xFF10B981))
                            CircularGauge(score = performanceScore, label = "شاخص کارایی", color = Color(0xFF10A37F))
                            CircularGauge(score = satisfactionScore, label = "رضایت نهایی", color = Color(0xFF34D399))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        CategoryAspectBarChart(category = category, productName = product.productName)

                        Spacer(modifier = Modifier.height(16.dp))

                        HardwareBenchmarkingChart(category = category, productName = product.productName)
                    } else {
                        // Torob Comparison and Offers Real-time tab!
                        val uriHandler = LocalUriHandler.current

                        Text(
                            text = "لیست فروشگاه‌ها و قیمت‌های بازار:",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val offers = product.offers
                        if (!offers.isNullOrEmpty()) {
                            offers.forEach { offer ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF16191D)),
                                    border = BorderStroke(1.dp, Color(0x2210A37F))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Left: Go to Store button (if URL is present)
                                        if (!offer.url.isNullOrBlank()) {
                                            val targetUrl = if (offer.url.startsWith("http")) offer.url else "https://torob.com${offer.url}"
                                            Button(
                                                onClick = {
                                                    try {
                                                        uriHandler.openUri(targetUrl)
                                                    } catch (e: Exception) {}
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10A37F)),
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = "خرید مستقیم",
                                                    color = Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        // Center: Price / Currency
                                        Text(
                                            text = offer.priceText ?: offer.price?.let { "${formatToman(it)} تومان" } ?: "قیمت نامشخص",
                                            color = Color(0xFF34D399),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Right
                                        )

                                        // Right: Shop name
                                        Text(
                                            text = offer.shopName ?: "فروشگاه",
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Right
                                        )
                                    }
                                }
                            }
                        } else {
                            // Empty / No offers or still loading from Torob
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xFF10A37F),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "در حال دریافت اطلاعات زنده و مقایسه قیمت‌ها از ترب...",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // General Torob Page Redirect Button
                        val torobPageUrl = if (!product.torobUrl.isNullOrBlank()) {
                            if (product.torobUrl.startsWith("http")) product.torobUrl else "https://torob.com${product.torobUrl}"
                        } else {
                            try {
                                "https://torob.com/search/?query=" + java.net.URLEncoder.encode(product.productName, "UTF-8")
                            } catch (e: Exception) {
                                "https://torob.com"
                            }
                        }

                        Button(
                            onClick = {
                                try {
                                    uriHandler.openUri(torobPageUrl)
                                } catch (e: Exception) {}
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15181C)),
                            border = BorderStroke(1.dp, Color(0xFF10A37F))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Torob website icon",
                                    tint = Color(0xFF10A37F),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "صفحه مقایسه قیمت کل بازار در ترب",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatToman(value: Long): String {
    val formatter = NumberFormat.getInstance(Locale("fa", "IR"))
    return formatter.format(value)
}

private fun getCategorySpecIcon(category: ProductCategory): ImageVector {
    return when (category) {
        ProductCategory.SMARTPHONE -> Icons.Default.PhoneAndroid
        ProductCategory.LAPTOP -> Icons.Default.Laptop
        ProductCategory.HEADPHONES -> Icons.Default.Headset
        ProductCategory.TV -> Icons.Default.Tv
        ProductCategory.SPEAKER -> Icons.Default.VolumeUp
    }
}

@Composable
fun RadarScannerView(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "holographic_radar")
    
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_beam"
    )
    
    val pulseRatio by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radar_pulse"
    )

    // Cycling through realistic hardware analyzing status logs in Persian
    val statusMessages = listOf(
        "در حال فراخوانی پایگاه داده مرکزی تک‌یاب...",
        "دوم: بررسی فیلترهای ترجیحات بودجه و قیمت...",
        "سوم: تحلیل کارایی و راندمان چندبعدی سخت‌افزار...",
        "چهارم: مدل‌سازی و استخراج تناسب‌های منطقی با نیاز شما...",
        "نهایتاً: تدوین گزینش‌های نهایی با موتور هوش مصنوعی..."
    )
    var currentMsgIdx by remember { mutableStateOf(0) }
    
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1800)
            currentMsgIdx = (currentMsgIdx + 1) % statusMessages.size
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Futuristic Scanner Radar Canvas
        Box(
            modifier = Modifier
                .size(190.dp)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val maxRadius = size.width / 2f

                // Concentric scanner rings with dynamic pulsing opacity
                drawCircle(
                    color = Color(0xFF10A37F).copy(alpha = 0.05f * pulseRatio),
                    radius = maxRadius * pulseRatio
                )
                drawCircle(
                    color = Color(0xFF34D399).copy(alpha = 0.12f),
                    radius = maxRadius,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFF10A37F).copy(alpha = 0.22f),
                    radius = maxRadius * 0.7f,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFF2DD4BF).copy(alpha = 0.35f),
                    radius = maxRadius * 0.35f,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                )

                // Grid axis crosses simulating a lens reticle
                drawLine(
                    color = Color(0xFF10A37F).copy(alpha = 0.18f),
                    start = Offset(center.x - maxRadius, center.y),
                    end = Offset(center.x + maxRadius, center.y),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color(0xFF10A37F).copy(alpha = 0.18f),
                    start = Offset(center.x, center.y - maxRadius),
                    end = Offset(center.x, center.y + maxRadius),
                    strokeWidth = 1.dp.toPx()
                )

                // Rotating scanning beam vectors
                val radAngle = Math.toRadians(angle.toDouble())
                val beamX = center.x + maxRadius * kotlin.math.cos(radAngle).toFloat()
                val beamY = center.y + maxRadius * kotlin.math.sin(radAngle).toFloat()

                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF10A37F), Color(0xFF2DD4BF))
                    ),
                    start = center,
                    end = Offset(beamX, beamY),
                    strokeWidth = 2.5.dp.toPx()
                )

                // Radar point reflections
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF2DD4BF), Color.Transparent),
                        center = Offset(beamX, beamY),
                        radius = 24.dp.toPx()
                    ),
                    center = Offset(beamX, beamY),
                    radius = 24.dp.toPx()
                )
                
                // Extra decorative tech coordinate values
                drawCircle(
                    color = Color(0xFFFFFFFF),
                    radius = 3.dp.toPx(),
                    center = Offset(center.x + maxRadius*0.5f, center.y - maxRadius*0.4f)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Futuristic processing texts with a smooth crossfade effect
        androidx.compose.animation.AnimatedContent(
            targetState = statusMessages[currentMsgIdx],
            transitionSpec = {
                (fadeIn(animationSpec = tween(400)) + slideInVertically(animationSpec = tween(400)) { it / 2 }) togetherWith
                (fadeOut(animationSpec = tween(300)) + slideOutVertically(animationSpec = tween(300)) { -it / 2 })
            },
            label = "status_logs"
        ) { status ->
            Text(
                text = status,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "در حال انتخاب برترین ویژگی‌های پردازش موازی...",
            color = Color.White.copy(alpha = 0.45f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

fun getOptimalProductImageUrl(productName: String, categoryName: String = ""): String {
    val name = productName.lowercase()
    val cat = categoryName.lowercase()
    return when {
        // Smartphones & Mobile Phones
        name.contains("s24") || name.contains("s24u") || (name.contains("ultra") && name.contains("galaxy")) -> {
            "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("15 pro max") || name.contains("16 pro max") || name.contains("iphone 15 pro") || name.contains("iphone 16") -> {
            "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("iphone") || name.contains("آیفون") -> {
            "https://images.unsplash.com/photo-1510557880182-3d4d3cba35a5?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("xiaomi") || name.contains("شیائومی") -> {
            "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("rog phone") || name.contains("ایسوس rog") -> {
            "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("poco") || name.contains("پوکو") -> {
            "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("nothing") || name.contains("ناتینگ") -> {
            "https://images.unsplash.com/photo-1635321604169-be96a1a148ed?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("redmi") || name.contains("نوت") || name.contains("note") || name.contains("a55") || name.contains("a54") -> {
            "https://images.unsplash.com/photo-1565630916779-e303be97b6f5?w=600&auto=format&fit=crop&q=80"
        }

        // Laptops
        name.contains("strix") || name.contains("rog strix") -> {
            "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("legion") || name.contains("لجیون") -> {
            "https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("tuf") || name.contains("تاف") -> {
            "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("macbook pro") || name.contains("مک‌بوک پرو") -> {
            "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("macbook air") || name.contains("مک‌بوک ایر") || name.contains("macbook") || name.contains("مک‌بوک") -> {
            "https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("zenbook") || name.contains("زن‌بوک") || name.contains("زنبوک") -> {
            "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("thinkbook") || name.contains("ثینک‌بوک") || name.contains("vivobook") || name.contains("ویووبوک") || name.contains("ideapad") || name.contains("آیدیاپد") -> {
            "https://images.unsplash.com/photo-1496181130204-755241524eab?w=600&auto=format&fit=crop&q=80"
        }

        // Headphones & Earbuds
        name.contains("1000xm5") || name.contains("xm5") || name.contains("xm4") || name.contains("sony wh") -> {
            "https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("momentum") || name.contains("مومنتوم") -> {
            "https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("q45") || name.contains("space q") || name.contains("soundcore") || name.contains("انکر") -> {
            "https://images.unsplash.com/photo-1487215078519-e21cc028cb29?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("blackshark") || name.contains("بلک‌شارک") || name.contains("arctis") || name.contains("آرکتیس") || name.contains("bloody") || name.contains("بلادی") -> {
            "https://images.unsplash.com/photo-1606220588913-b3aacb4d2f46?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("airpods") || name.contains("ایرپاد") || name.contains("buds") || name.contains("بادز") || name.contains("jabra") || name.contains("جبرا") -> {
            "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=600&auto=format&fit=crop&q=80"
        }

        // TVs
        name.contains("lg oled") || name.contains("c3") || name.contains("b3") || name.contains("oled") -> {
            "https://images.unsplash.com/photo-1593305841991-05c297ba4575?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("neo qled") || name.contains("qn90") || name.contains("q70") || name.contains("qled") || name.contains("سونی") || name.contains("sony") || name.contains("tcl") || name.contains("tclc") || name.contains("snowa") || name.contains("اسنوا") -> {
            "https://images.unsplash.com/photo-1601944179066-297bdd5baf64?w=600&auto=format&fit=crop&q=80"
        }

        // Speakers
        name.contains("partybox") || name.contains("پارتی باکس") || name.contains("essential") || name.contains("maxeeder") || name.contains("مکسیدر") -> {
            "https://images.unsplash.com/photo-1545128485-c400e7702796?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("stanmore") || name.contains("marshall") || name.contains("مارشال") -> {
            "https://images.unsplash.com/photo-1589416828458-7e4a64010da5?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("aura") || name.contains("هارمن") || name.contains("studio") -> {
            "https://images.unsplash.com/photo-1516280440614-37939bbacd6a?w=600&auto=format&fit=crop&q=80"
        }
        name.contains("flip") || name.contains("portable") -> {
            "https://images.unsplash.com/photo-1589003077984-894e133dabab?w=600&auto=format&fit=crop&q=80"
        }

        // Fallbacks by names/categories
        cat.contains("phone") || cat.contains("mobile") || cat.contains("گوشی") -> {
            "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600&auto=format&fit=crop&q=80"
        }
        cat.contains("laptop") || cat.contains("لپ") -> {
            "https://images.unsplash.com/photo-1496181130204-755241524eab?w=600&auto=format&fit=crop&q=80"
        }
        cat.contains("headphone") || cat.contains("earphone") || cat.contains("هدفون") -> {
            "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80"
        }
        cat.contains("tv") || cat.contains("television") || cat.contains("تلویزیون") -> {
            "https://images.unsplash.com/photo-1593305841991-05c297ba4575?w=600&auto=format&fit=crop&q=80"
        }
        cat.contains("speaker") || cat.contains("اسپیکر") -> {
            "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600&auto=format&fit=crop&q=80"
        }

        else -> {
            "https://images.unsplash.com/photo-1531297484001-80022131f5a1?w=600&auto=format&fit=crop&q=80"
        }
    }
}

@Composable
fun ProductCategoryFallback(category: ProductCategory, modifier: Modifier = Modifier) {
    val gradient = when (category) {
        ProductCategory.SMARTPHONE -> Brush.linearGradient(
            colors = listOf(Color(0xFF10A37F), Color(0xFF34D399))
        )
        ProductCategory.LAPTOP -> Brush.linearGradient(
            colors = listOf(Color(0xFF3B82F6), Color(0xFF2563EB))
        )
        ProductCategory.HEADPHONES -> Brush.linearGradient(
            colors = listOf(Color(0xFFF59E0B), Color(0xFFEF4444))
        )
        ProductCategory.TV -> Brush.linearGradient(
            colors = listOf(Color(0xFF10B981), Color(0xFF047857))
        )
        ProductCategory.SPEAKER -> Brush.linearGradient(
            colors = listOf(Color(0xFFEC4899), Color(0xFFF43F5E))
        )
    }

    val icon = when (category) {
        ProductCategory.SMARTPHONE -> Icons.Default.PhoneAndroid
        ProductCategory.LAPTOP -> Icons.Default.Laptop
        ProductCategory.HEADPHONES -> Icons.Default.Headphones
        ProductCategory.TV -> Icons.Default.Tv
        ProductCategory.SPEAKER -> Icons.Default.Speaker
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(26.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = category.displayName,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

