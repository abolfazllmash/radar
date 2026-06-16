package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductCategory
import com.example.data.ScreenState
import com.example.ui.components.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.RecommendationViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: RecommendationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Ensure premium RTL interface layout
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // High-fidelity dynamic ambient background inspired by Grok
                        GrokAmbientBackground()

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            containerColor = Color.Transparent
                        ) { innerPadding ->
                            MainContent(
                                viewModel = viewModel,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    viewModel: RecommendationViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    // Observe error message and show native destructive Toast
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage.isNotEmpty() && uiState.screenState == ScreenState.ERROR) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    var selectedProductForDetail by remember { mutableStateOf<com.example.data.ProductRecommendation?>(null) }

    // Intercept physical or system level back button/swipe gesture
    BackHandler(enabled = uiState.screenState != ScreenState.FORM || selectedProductForDetail != null) {
        if (selectedProductForDetail != null) {
            selectedProductForDetail = null
        } else {
            viewModel.startOver()
        }
    }

    // Dialog trigger
    selectedProductForDetail?.let { product ->
        uiState.selectedCategory?.let { category ->
            ProductDetailsDialog(
                product = product,
                category = category,
                onDismiss = { selectedProductForDetail = null }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
    ) {
        // App top logo branding
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            RadarLogo()

            if (uiState.screenState != ScreenState.FORM) {
                IconButton(
                    onClick = {
                        if (selectedProductForDetail != null) {
                            selectedProductForDetail = null
                        } else {
                            viewModel.startOver()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF16191D).copy(alpha = 0.8f))
                        .border(1.dp, Color(0xFF10A37F).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "بازگشت",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Screen state dispatcher
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            when (uiState.screenState) {
                ScreenState.FORM -> {
                    FormScreen(
                        goalsText = uiState.goals,
                        onGoalsChange = { viewModel.updateGoals(it) },
                        selectedCategory = uiState.selectedCategory,
                        onCategorySelect = { viewModel.updateCategory(it) },
                        budgetRange = uiState.budgetMin.toFloat()..uiState.budgetMax.toFloat(),
                        onBudgetChange = {
                            viewModel.updateBudgetMin(it.start.toLong())
                            viewModel.updateBudgetMax(it.endInclusive.toLong())
                        },
                        brandText = uiState.preferredBrand,
                        onBrandChange = { viewModel.updatePreferredBrand(it) },
                        onSubmit = {
                            focusManager.clearFocus()
                            viewModel.generateRecommendations()
                        }
                    )
                }

                ScreenState.LOADING -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        RadarScannerView()
                    }
                }

                ScreenState.ERROR -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF7F1D1D)) // Deep Red Error background
                                .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = "Error Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "خطایی رخ داده است",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = uiState.errorMessage,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 22.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.startOver() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10A37F))
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "تلاش دوباره")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "تلاش دوباره", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                ScreenState.RESULTS -> {
                    ResultsScreen(
                        selectedCategory = uiState.selectedCategory ?: ProductCategory.SMARTPHONE,
                        activeTab = uiState.activeTab,
                        onTabChange = { viewModel.setActiveTab(it) },
                        products = uiState.recommendations,
                        onProductClick = { selectedProductForDetail = it },
                        onStartOver = { viewModel.startOver() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormScreen(
    goalsText: String,
    onGoalsChange: (String) -> Unit,
    selectedCategory: ProductCategory?,
    onCategorySelect: (ProductCategory) -> Unit,
    budgetRange: ClosedFloatingPointRange<Float>,
    onBudgetChange: (ClosedFloatingPointRange<Float>) -> Unit,
    brandText: String,
    onBrandChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    var isGoalsFocused by remember { mutableStateOf(false) }
    // Progressive Disclosure criteria: Goals prompt is focused or filled
    val isExpanded = isGoalsFocused || goalsText.trim().length >= 3

    var currentPlaceholderIndex by remember { mutableStateOf(0) }

    // List of premium, highly targeted default placeholders per category
    val placeholdersMap = remember {
        mapOf(
            ProductCategory.SMARTPHONE to listOf(
                "مثلا: گوشی پرچمدار با دوربین فوق‌العاده برای عکاسی شب و زوم بالا...",
                "مثلا: یک گوشی میان‌رده گیمینگ با پردازنده قوی و باتری ۵۰۰۰ میلی‌آمپر...",
                "مثلا: گوشی اقتصادی شیک با صفحه‌نمایش ۱۲۰ هرتز آمولد و شارژ سریع..."
            ),
            ProductCategory.LAPTOP to listOf(
                "مثلا: لپ‌تاپ گیمینگ قوی برای رندرینگ، تدوین ویدیو و بازی‌های سنگین...",
                "مثلا: یک لپ‌تاپ باریک، سبک و ظریف با شارژدهی طولانی مناسب برنامه‌نویسی...",
                "مثلا: لپ‌تاپ مهندسی اقتصادی با پردازنده نسل جدید و رم ۱۶ گیگابایت..."
            ),
            ProductCategory.HEADPHONES to listOf(
                "مثلا: هدفون دورگوشی با نویز کنسلینگ عالی برای موسیقی و سفر طولانی...",
                "مثلا: هندزفری بی سیم ارگونومیک با بیس عمیق و میکروفون باکیفیت...",
                "مثلا: هدست گیمینگ با صدای فراگیر جزئیات بالا و تاخیر صفر برای بازی..."
            ),
            ProductCategory.TV to listOf(
                "مثلا: تلویزیون ۴کی اولد برای تماشای فیلم با رنگ مشکی بی‌نهایت...",
                "مثلا: یک تلویزیون گیمینگ ۱۲۰ هرتز برای بازی راحت با پی اس ۵...",
                "مثلا: تلویزیون اقتصادی ۵۵ اینچ هوشمند با سیستم عامل اندرويد شیوا..."
            ),
            ProductCategory.SPEAKER to listOf(
                "مثلا: اسپیکر پارتی‌باکس بزرگ با رقص نور هیجان‌انگیز و بیس کوبنده...",
                "مثلا: اسپیکر بلوتوثی پرتابل ضدآب با عمر باتری طولانی مناسب سفر...",
                "مثلا: اسپیکر رومیزی دسکتاپ مینیمال و شیک با وضوح فوق‌العاده..."
            )
        )
    }

    // Determine the active placeholder list dynamically
    val activePlaceholders = remember(selectedCategory) {
        if (selectedCategory != null) {
            placeholdersMap[selectedCategory] ?: emptyList()
        } else {
            // A premium shuffled mix of all categories if none is selected
            placeholdersMap.values.flatten().shuffled()
        }
    }

    // Reset index when category or the placeholder options change
    LaunchedEffect(activePlaceholders) {
        currentPlaceholderIndex = 0
    }

    // Timer effect to cycle through placeholders every 3 seconds
    LaunchedEffect(activePlaceholders) {
        while (true) {
            kotlinx.coroutines.delay(3000L)
            if (activePlaceholders.isNotEmpty()) {
                currentPlaceholderIndex = (currentPlaceholderIndex + 1) % activePlaceholders.size
            }
        }
    }

    val currentPlaceholderText = activePlaceholders.getOrNull(currentPlaceholderIndex) ?: "نیاز خود را در حوزه تکنولوژی بنویسید..."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        // Section: User Needs
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0x6615181C)),
            border = BorderStroke(1.dp, Color(0x1A10A37F))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Target icon",
                        tint = Color(0xFF34D399),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "شما به دنبال چه محصولی هستید؟ نیاز و کاربرد آن را بنویسید:",
                        color = Color.White.copy(alpha = 0.95f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedTextField(
                    value = goalsText,
                    onValueChange = onGoalsChange,
                    placeholder = {
                        Crossfade(
                            targetState = currentPlaceholderText,
                            animationSpec = tween(500),
                            label = "placeholder_animation"
                        ) { text ->
                            Text(
                                text = text,
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.35f)
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .onFocusChanged { status ->
                            if (status.isFocused) {
                                isGoalsFocused = true
                            }
                        },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF10A37F),
                        unfocusedBorderColor = Color(0xFF2E2E35),
                        focusedContainerColor = Color(0xFF0F1113),
                        unfocusedContainerColor = Color(0xFF131517),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Animated reveal of step details based on progressive disclosure
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(animationSpec = tween(600)) + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut(animationSpec = tween(450)) + shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Section: Category Selection
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x6615181C)),
                    border = BorderStroke(1.dp, Color(0x1A10A37F))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Widgets,
                                contentDescription = "Category Selector icon",
                                tint = Color(0xFF34D399),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "۱. دسته‌بندی محصول مورد نظر:",
                                color = Color.White.copy(alpha = 0.95f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Category chips grid layout
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            maxItemsInEachRow = 3
                        ) {
                            ProductCategory.values().forEach { category ->
                                val isSelected = selectedCategory == category
                                val categoryIcon = when (category) {
                                    ProductCategory.SMARTPHONE -> Icons.Default.PhoneAndroid
                                    ProductCategory.LAPTOP -> Icons.Default.Laptop
                                    ProductCategory.HEADPHONES -> Icons.Default.Headset
                                    ProductCategory.TV -> Icons.Default.Tv
                                    ProductCategory.SPEAKER -> Icons.Default.VolumeUp
                                }

                                var chipPressed by remember { mutableStateOf(false) }
                                val scaleAnim by animateFloatAsState(
                                    targetValue = if (isSelected) 1.04f else 1.0f,
                                    animationSpec = spring(stiffness = Spring.StiffnessHigh)
                                )

                                Card(
                                    onClick = { onCategorySelect(category) },
                                    modifier = Modifier
                                        .height(48.dp)
                                        .weight(1f)
                                        .scale(scaleAnim),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) Color(0xFF10A37F).copy(alpha = 0.18f) else Color(0xAA13161A)
                                    ),
                                    border = BorderStroke(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) Color(0xFF34D399) else Color(0x3310A37F)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = categoryIcon,
                                            contentDescription = category.displayName,
                                            tint = if (isSelected) Color(0xFF34D399) else Color.White.copy(alpha = 0.5f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = category.displayName,
                                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.75f),
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                        if (isSelected) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(Color(0xFF34D399))
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section: Budget Slider
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x6615181C)),
                    border = BorderStroke(1.dp, Color(0x1A10A37F))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CurrencyExchange,
                                contentDescription = "Budget icon",
                                tint = Color(0xFF34D399),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "۲. تعیین دامنه بودجه سرمایه‌گذاری:",
                                color = Color.White.copy(alpha = 0.95f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Premium Double Capsules display for budget thresholds
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Min Budget Card
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1113)),
                                border = BorderStroke(1.dp, Color(0x2210A37F))
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "کف بودجه (حداقل)",
                                        color = Color.White.copy(alpha = 0.4f),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${formatToman(budgetRange.start.toLong())} تومان",
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Max Budget Card
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1113)),
                                border = BorderStroke(1.dp, Color(0x2210A37F))
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "سقف بودجه (حداکثر)",
                                        color = Color(0xFF34D399).copy(alpha = 0.6f),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${formatToman(budgetRange.endInclusive.toLong())} تومان",
                                        color = Color(0xFF34D399),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Styled M3 RangeSlider
                        RangeSlider(
                            value = budgetRange,
                            onValueChange = onBudgetChange,
                            valueRange = 1_000_000f..150_000_000f,
                            colors = SliderDefaults.colors(
                                activeTrackColor = Color(0xFF10A37F),
                                inactiveTrackColor = Color(0xFF15181C),
                                thumbColor = Color(0xFF34D399),
                                activeTickColor = Color.Transparent,
                                inactiveTickColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section: Brand Preference & Quick Tags
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x6615181C)),
                    border = BorderStroke(1.dp, Color(0x1A10A37F))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.StarHalf,
                                contentDescription = "Brand selection icon",
                                tint = Color(0xFF34D399),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "۳. برندهای برگزیده مورد علاقه (اختیاری):",
                                color = Color.White.copy(alpha = 0.95f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedTextField(
                            value = brandText,
                            onValueChange = onBrandChange,
                            placeholder = {
                                Text(
                                    text = "مثلاً: Sony، Apple، Samsung",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.35f)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF10A37F),
                                unfocusedBorderColor = Color(0xFF2E2E35),
                                focusedContainerColor = Color(0xFF0F1113),
                                unfocusedContainerColor = Color(0xFF131517),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Clever UX: Populating adaptive brand quick tags based on selected category!
                        selectedCategory?.let { category ->
                            val quickBrands = when (category) {
                                ProductCategory.SMARTPHONE -> listOf("سامسونگ", "اپل", "شیائومی", "پوکو")
                                ProductCategory.LAPTOP -> listOf("ایسوس", "لنوو", "مک‌بوک", "اچ‌پی")
                                ProductCategory.HEADPHONES -> listOf("سونی", "انکر", "جبرا", "اپل")
                                ProductCategory.TV -> listOf("ال‌جی", "سامسونگ", "سونی", "جی‌پلاس")
                                ProductCategory.SPEAKER -> listOf("جی‌بی‌ال", "مکسیدر", "مارشال", "انکر")
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "پیشنهاد سریع:",
                                    color = Color.White.copy(alpha = 0.45f),
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    items(quickBrands) { brand ->
                                        val isTagActive = brandText.contains(brand)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isTagActive) Color(0xFF10A37F).copy(alpha = 0.25f)
                                                    else Color(0xFF15181C)
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = if (isTagActive) Color(0xFF10A37F) else Color(0x33FFFFFF),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable {
                                                    val newBrandText = if (brandText.isBlank()) {
                                                        brand
                                                    } else if (brandText.contains(brand)) {
                                                        brandText
                                                            .replace(brand, "")
                                                            .replace(", ,", ",")
                                                            .trim()
                                                            .removePrefix(",")
                                                            .removeSuffix(",")
                                                    } else {
                                                        "$brandText، $brand"
                                                    }
                                                    onBrandChange(newBrandText)
                                                }
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = brand,
                                                color = if (isTagActive) Color.White else Color.White.copy(alpha = 0.6f),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Submit button with stunning linear gradient, high elevation effect, and pulse state
                val isButtonEnabled = selectedCategory != null
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isButtonEnabled) {
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF10A37F), Color(0xFF34D399))
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF15181C), Color(0xFF131517))
                                )
                            }
                        )
                        .clickable(enabled = isButtonEnabled) {
                            onSubmit()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Submit analysis icon",
                            tint = if (isButtonEnabled) Color.White else Color.White.copy(alpha = 0.25f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "تحلیل هوشمند و انتخاب برترین‌ها",
                            color = if (isButtonEnabled) Color.White else Color.White.copy(alpha = 0.3f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultsScreen(
    selectedCategory: ProductCategory,
    activeTab: Int,
    onTabChange: (Int) -> Unit,
    products: List<com.example.data.ProductRecommendation>,
    onProductClick: (com.example.data.ProductRecommendation) -> Unit,
    onStartOver: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Output title header status bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "محصولات پیشنهادی برای شما",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedButton(
                onClick = onStartOver,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF34D399)),
                border = BorderStroke(1.dp, Color(0xFF2E2E35)),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "شروع مجدد",
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "شروع مجدد", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Custom M3 Results Selector TabRow - Redesigned as a beautiful capsule pill switcher
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF15181C))
                .border(1.dp, Color(0x2210A37F), RoundedCornerShape(14.dp))
                .padding(4.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Tab 0: Smart Recommendations
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .then(
                            if (activeTab == 0) {
                                Modifier.background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF10A37F), Color(0xFF34D399))
                                    )
                                )
                            } else Modifier
                        )
                        .clickable { onTabChange(0) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "suggested",
                            tint = if (activeTab == 0) Color.White else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "پیشنهادهای هوشمند",
                            color = if (activeTab == 0) Color.White else Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Tab 1: Comparison & Charts
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .then(
                            if (activeTab == 1) {
                                Modifier.background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF10A37F), Color(0xFF34D399))
                                    )
                                )
                            } else Modifier
                        )
                        .clickable { onTabChange(1) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "comparison",
                            tint = if (activeTab == 1) Color.White else Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "مشخصات فنی و رادار",
                            color = if (activeTab == 1) Color.White else Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Active Tab contents
        if (activeTab == 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)
            ) {
                if (products.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "هیچ پیشنهادی منطبق بر بودجه یافت نشد. بازه بودجه را وسیع‌تر کنید.",
                            color = Color.White.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    products.forEachIndexed { index, product ->
                        ProductCard(
                            product = product,
                            index = index,
                            category = selectedCategory,
                            onClick = { onProductClick(product) }
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)
            ) {
                if (products.isNotEmpty()) {
                    OverallScoreChart(products = products)
                    Spacer(modifier = Modifier.height(16.dp))
                    PriceComparisonChart(products = products)
                    Spacer(modifier = Modifier.height(16.dp))
                    ValueForMoneyChart(products = products)
                    Spacer(modifier = Modifier.height(16.dp))
                    ComparisonRadarChart(category = selectedCategory, products = products)
                }
            }
        }
    }
}
