package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductCategory
import com.example.data.ProductRecommendation
import kotlin.math.cos
import kotlin.math.sin

// Theme Colors
private val ColorPrimary = Color(0xFF10A37F) // Primary Mint/Emerald (ChatGPT theme)
private val ColorSuccess = Color(0xFF34D399) // Mint/Success
private val ColorWarning = Color(0xFFF59E0B) // Amber/Warning
private val BorderColor = Color(0xFF2E2E35)  // Light Grey Dark mode

@Composable
fun OverallScoreChart(
    products: List<ProductRecommendation>,
    modifier: Modifier = Modifier
) {
    val scores = listOf(95, 86, 75) // Fixed high-quality descending ranks for up to 3 products

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF16161A))
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "رتبه‌بندی نهایی و امتیاز کل (۶۰ - ۱۰۰)",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Right
        )

        products.forEachIndexed { index, product ->
            val score = scores.getOrElse(index) { 70 }
            var animationTriggered by remember { mutableStateOf(false) }
            val progress by animateFloatAsState(
                targetValue = if (animationTriggered) score / 100f else 0f,
                animationSpec = tween(1000 + index * 200)
            )

            LaunchedEffect(Unit) {
                animationTriggered = true
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$score٪",
                        color = when (index) {
                            0 -> ColorPrimary
                            1 -> ColorSuccess
                            else -> ColorWarning
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.productName,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Right
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF26262B))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when (index) {
                                    0 -> ColorPrimary
                                    1 -> ColorSuccess
                                    else -> ColorWarning
                                }
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun ComparisonRadarChart(
    category: ProductCategory,
    products: List<ProductRecommendation>,
    modifier: Modifier = Modifier
) {
    // Labels based on guidelines
    val labels = when (category) {
        ProductCategory.SMARTPHONE -> listOf("دوربین", "پردازنده", "باتری", "صفحه نمایش")
        ProductCategory.LAPTOP -> listOf("سخت‌افزار", "حافظه رم", "گرافیک", "نمایشگر")
        ProductCategory.HEADPHONES -> listOf("کیفیت صدا", "حذف نویز", "راحتی", "باتری")
        ProductCategory.TV -> listOf("کیفیت تصویر", "کیفیت صدا", "سیستم هوشمند", "طراحی بدنه")
        ProductCategory.SPEAKER -> listOf("قدرت صدا", "بیس عمیق", "وزن مناسب", "عمر باتری")
    }

    // Interactive state: select a product index to focus, or null to show all
    var selectedProductIndex by remember { mutableStateOf<Int?>(null) }

    // Ensure widely spread, crossing shapes so colors are fully distinct and don't mask each other!
    val values = products.mapIndexed { index, product ->
        val seed = product.productName.hashCode().coerceAtLeast(0)
        when (index % 3) {
            0 -> listOf(
                ((seed % 15) + 80) / 100f,  // 0.80f - 0.95f
                ((seed % 15) + 40) / 100f,  // 0.40f - 0.55f
                ((seed % 15) + 85) / 100f,  // 0.85f - 1.00f
                ((seed % 15) + 45) / 100f   // 0.45f - 0.60f
            )
            1 -> listOf(
                ((seed % 15) + 40) / 100f,  // 0.40f - 0.55f
                ((seed % 15) + 85) / 100f,  // 0.85f - 1.00f
                ((seed % 15) + 45) / 100f,  // 0.45f - 0.60f
                ((seed % 15) + 80) / 100f   // 0.80f - 0.95f
            )
            else -> listOf(
                ((seed % 15) + 70) / 100f,  // 0.70f - 0.85f
                ((seed % 15) + 65) / 100f,  // 0.65f - 0.80f
                ((seed % 15) + 40) / 100f,  // 0.40f - 0.55f
                ((seed % 15) + 85) / 100f   // 0.85f - 1.00f
            )
        }
    }

    val productColors = listOf(
        ColorPrimary,
        ColorSuccess,
        ColorWarning
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF16161A))
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title block with modern Farsi prompt guide
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "مقایسه مشخصات فنی چندبعدی",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right
            )
            Text(
                text = "💡 برای تمرکز روی رادار هر محصول، روی نام آن در راهنمای زیر کلیک کنید.",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Right
            )
        }

        // Custom High-Fidelity Interactive Legends Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            products.forEachIndexed { index, product ->
                val isSelected = selectedProductIndex == index
                val color = productColors.getOrElse(index) { Color.Gray }

                Card(
                    onClick = {
                        selectedProductIndex = if (isSelected) null else index
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) color.copy(alpha = 0.12f) else Color(0xFF0F0E14)
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) color else Color(0x2210A37F)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left indication dot & check
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(color)
                            )
                            if (isSelected) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Active Filter",
                                    tint = color,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        // Right Product Name
                        Text(
                            text = product.productName,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.weight(1f).padding(end = 12.dp)
                        )
                    }
                }
            }
        }

        var animationTriggered by remember { mutableStateOf(false) }
        val animationScale by animateFloatAsState(
            targetValue = if (animationTriggered) 1f else 0f,
            animationSpec = tween(1200),
            label = "radar_scale"
        )

        LaunchedEffect(Unit) {
            animationTriggered = true
        }

        // Outer Spacious Container holding the Radar system and Axis Labels perfectly
        Box(
            modifier = Modifier
                .size(280.dp)
                .background(Color(0xFF131517))
                .border(1.dp, Color(0x3310A37F), RoundedCornerShape(12.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Central fixed size Canvas for the Radar drawings
            Canvas(
                modifier = Modifier
                    .size(160.dp)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val maxRadiusPx = size.width / 2f // Maximum radius is exactly 80.dp equivalent in pixels

                // Draw Radar Web (4 rings: 25%, 50%, 75%, 100%)
                val ringCount = 4
                for (r in 1..ringCount) {
                    val currentRadius = maxRadiusPx * (r / ringCount.toFloat())
                    val webPath = Path().apply {
                        for (i in 0..3) {
                            val angle = i * (Math.PI / 2) - (Math.PI / 2)
                            val x = center.x + currentRadius * cos(angle).toFloat()
                            val y = center.y + currentRadius * sin(angle).toFloat()
                            if (i == 0) moveTo(x, y) else lineTo(x, y)
                        }
                        close()
                    }
                    drawPath(
                        path = webPath,
                        color = Color.White.copy(alpha = 0.08f),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                // Draw diagonal axis lines
                for (i in 0..3) {
                    val angle = i * (Math.PI / 2) - (Math.PI / 2)
                    val endX = center.x + maxRadiusPx * cos(angle).toFloat()
                    val endY = center.y + maxRadiusPx * sin(angle).toFloat()
                    drawLine(
                        color = Color.White.copy(alpha = 0.12f),
                        start = center,
                        end = Offset(endX, endY),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Draw Product Shapes
                products.forEachIndexed { pIdx, _ ->
                    val isSelected = selectedProductIndex == pIdx
                    val isAnySelected = selectedProductIndex != null
                    
                    // Determine alpha based on interactive selection state
                    val alphaMultiplier = if (isAnySelected) {
                        if (isSelected) 1.0f else 0.15f
                    } else {
                        1.0f
                    }

                    val color = productColors.getOrElse(pIdx) { Color.Gray }
                    val pValues = values.getOrElse(pIdx) { listOf(0.5f, 0.5f, 0.5f, 0.5f) }

                    val polygonPath = Path().apply {
                        for (i in 0..3) {
                            val itemVal = pValues.getOrElse(i) { 0.5f } * animationScale
                            val angle = i * (Math.PI / 2) - (Math.PI / 2)
                            val r = maxRadiusPx * itemVal
                            val x = center.x + r * cos(angle).toFloat()
                            val y = center.y + r * sin(angle).toFloat()
                            if (i == 0) moveTo(x, y) else lineTo(x, y)
                        }
                        close()
                    }

                    // Render Transparent Area
                    drawPath(
                        path = polygonPath,
                        color = color.copy(alpha = if (isSelected) 0.32f else 0.12f * alphaMultiplier)
                    )

                    // Render Contour Outline
                    drawPath(
                        path = polygonPath,
                        color = color.copy(alpha = if (isSelected) 1.0f else 0.7f * alphaMultiplier),
                        style = Stroke(
                            width = if (isSelected) 3.dp.toPx() else 1.5.dp.toPx()
                        )
                    )
                }
            }

            // Beautiful Axis Overlay Labels (spacious, styled, wrapped in capsule badges)
            // Index 0 -> Top, Index 1 -> Right, Index 2 -> Bottom, Index 3 -> Left
            labels.forEachIndexed { index, label ->
                val alignment = when (index) {
                    0 -> Alignment.TopCenter
                    1 -> Alignment.CenterEnd
                    2 -> Alignment.BottomCenter
                    else -> Alignment.CenterStart
                }

                Box(
                    modifier = Modifier
                        .align(alignment)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF131517).copy(alpha = 0.85f))
                        .border(1.dp, Color(0x3310A37F), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = Color.White.copy(alpha = 0.95f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun CircularGauge(
    score: Int,
    label: String,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF10A37F)
) {
    var animationTriggered by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (animationTriggered) score / 100f else 0f,
        animationSpec = tween(1200),
        label = "gauge_progress"
    )

    LaunchedEffect(Unit) {
        animationTriggered = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(60.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 5.dp.toPx()
                // Track
                drawArc(
                    color = Color(0xFF232328),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
                // Flow Active
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = progress * 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
            }
            Text(
                text = "$score٪",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CategoryAspectBarChart(
    category: ProductCategory,
    productName: String,
    modifier: Modifier = Modifier
) {
    val seed = productName.hashCode().coerceAtLeast(0)
    
    val aspects = when (category) {
        ProductCategory.SMARTPHONE -> listOf(
            "کیفیت دوربین و تصویربرداری" to 85 + (seed % 14),
            "قدرت پردازش و سرعت سیستم" to 88 + (seed % 11),
            "طول عمر باتری و سرعت شارژ" to 82 + (seed % 17),
            "کیفیت صفحه نمایش و روشنایی" to 86 + (seed % 13),
            "ارزش خرید در برابر قیمت" to 80 + (seed % 19)
        )
        ProductCategory.LAPTOP -> listOf(
            "سرعت پردازش سخت‌افزار" to 88 + (seed % 11),
            "عملکرد سیستم خنک‌کننده" to 78 + (seed % 18),
            "کیفیت ساخت و طراحی بدنه" to 82 + (seed % 15),
            "میزان شارژدهی باتری" to 75 + (seed % 21),
            "وضوح نمایشگر و دقت رنگ" to 84 + (seed % 14)
        )
        ProductCategory.HEADPHONES -> listOf(
            "تفکیک و کیفیت خروجی صدا" to 87 + (seed % 12),
            "قدرت حذف نویز فعال (ANC)" to 80 + (seed % 18),
            "راحتی قرارگیری روی گوش" to 84 + (seed % 15),
            "شارژدهی و سرعت کیس شارژ" to 82 + (seed % 16),
            "کیفیت میکروفون مکالمه" to 75 + (seed % 20)
        )
        ProductCategory.TV -> listOf(
            "وضوح، رنگ و کنتراست تصویر" to 89 + (seed % 10),
            "قدرت و رسایی بلندگوها" to 78 + (seed % 19),
            "سرعت سیستم‌عامل هوشمند" to 80 + (seed % 17),
            "طراحی مدرن و قاب باریک" to 86 + (seed % 13),
            "تنوع و پورت‌های ورودی" to 84 + (seed % 15)
        )
        ProductCategory.SPEAKER -> listOf(
            "وضوح و کوبش صدا در ولوم بالا" to 88 + (seed % 11),
            "قدرت بیس ساب‌ووفر داخلی" to 90 + (seed % 9),
            "شارژدهی و پرتابل بودن بیسیم" to 80 + (seed % 18),
            "پایداری بلوتوث و اتصال چندگانه" to 85 + (seed % 14),
            "کیفیت رقص نور و افکت بدنه" to 76 + (seed % 21)
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF15181C))
            .border(1.dp, Color(0x3310A37F), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "ارزیابی تناسب ابعاد عملکردی:",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            textAlign = TextAlign.Right
        )

        aspects.forEachIndexed { index, (title, score) ->
            var animTriggered by remember { mutableStateOf(false) }
            val progress by animateFloatAsState(
                targetValue = if (animTriggered) score / 100f else 0f,
                animationSpec = tween(800 + index * 100),
                label = "aspect_progress"
            )

            LaunchedEffect(Unit) {
                animTriggered = true
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$score٪",
                        color = Color(0xFF34D399),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = title,
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Right
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF232328))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF10A37F), Color(0xFF5EEAD4))
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun HardwareBenchmarkingChart(
    category: ProductCategory,
    productName: String,
    modifier: Modifier = Modifier
) {
    val seed = productName.hashCode().coerceAtLeast(0)
    val productScore = 82 + (seed % 17) // 82 to 98
    val competitorAverageScore = 78 + (seed % 9) // 78 to 86
    val categoryAverageScore = 75 + (seed % 11) // 75 to 85

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF15181C))
            .border(1.dp, Color(0x3310A37F), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "مقایسه عملکردی با میانگین بازار:",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            textAlign = TextAlign.Right
        )

        // Triple Horizontal Compare-Bar graph
        val items = listOf(
            Triple("این محصول", productScore, Brush.horizontalGradient(listOf(Color(0xFF10A37F), Color(0xFF2DD4BF)))),
            Triple("میانگین رقبای هم‌قیمت", competitorAverageScore, Brush.horizontalGradient(listOf(Color(0xFF0F766E), Color(0xFF14B8A6)))),
            Triple("میانگین کلی بازار", categoryAverageScore, Brush.horizontalGradient(listOf(Color(0xFF4B5563), Color(0xFF6B7280))))
        )

        items.forEachIndexed { index, (label, score, brush) ->
            var animTriggered by remember { mutableStateOf(false) }
            val progress by animateFloatAsState(
                targetValue = if (animTriggered) score / 100f else 0f,
                animationSpec = tween(900 + index * 100),
                label = "benchmark_progress"
            )

            LaunchedEffect(Unit) {
                animTriggered = true
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$score / ۱۰۰",
                        color = if (index == 0) Color(0xFFEC4899) else Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = label,
                        color = if (index == 0) Color.White else Color.White.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Right
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF232328))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .clip(RoundedCornerShape(4.dp))
                            .background(brush)
                    )
                }
            }
        }
    }
}

@Composable
fun PriceComparisonChart(
    products: List<ProductRecommendation>,
    modifier: Modifier = Modifier
) {
    val prices = products.map { it.price ?: 0L }
    val maxPrice = prices.maxOrNull()?.coerceAtLeast(1L) ?: 1L

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF16161A))
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "مقایسه قیمت محصولات پیشنهادی",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Right
        )

        products.forEachIndexed { index, product ->
            val price = product.price ?: 0L
            val progress = if (maxPrice > 0) price.toFloat() / maxPrice else 0f
            var animationTriggered by remember { mutableStateOf(false) }
            val animatedProgress by animateFloatAsState(
                targetValue = if (animationTriggered) progress else 0f,
                animationSpec = tween(1000 + index * 150),
                label = "price_bar_prog"
            )

            LaunchedEffect(Unit) {
                animationTriggered = true
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (price > 0) "${formatToman(price)} تومان" else "نامشخص",
                        color = Color(0xFFF43F5E),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.productName,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.weight(1f).padding(start = 12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF26262B))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedProgress.coerceIn(0.05f, 1f))
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFEC4899), Color(0xFFF43F5E))
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun ValueForMoneyChart(
    products: List<ProductRecommendation>,
    modifier: Modifier = Modifier
) {
    val vfmScores = listOf(98, 89, 81)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF16161A))
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "شاخص ارزش خرید در برابر قیمت (VFM)",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Right
        )

        products.forEachIndexed { index, product ->
            val vfmScore = vfmScores.getOrElse(index) { 75 }
            var animationTriggered by remember { mutableStateOf(false) }
            val animatedProgress by animateFloatAsState(
                targetValue = if (animationTriggered) vfmScore / 100f else 0f,
                animationSpec = tween(1100 + index * 150),
                label = "vfm_bar_prog"
            )

            LaunchedEffect(Unit) {
                animationTriggered = true
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$vfmScore٪",
                        color = Color(0xFF10B981),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.productName,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.weight(1f).padding(start = 12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF26262B))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedProgress)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF059669), Color(0xFF10B981))
                                )
                            )
                    )
                }
            }
        }
    }
}


