package com.example.data

object MockData {
    fun getMockRecommendations(
        category: ProductCategory?,
        goals: String,
        budgetMin: Long,
        budgetMax: Long
    ): List<ProductRecommendation>? {
        val lowercaseGoals = goals.lowercase()
        val text = lowercaseGoals + " " + goals

        if (category == null) return null

        return when (category) {
            ProductCategory.SMARTPHONE -> {
                when {
                    text.contains("عکاسی") || text.contains("دوربین") || text.contains("camera") || text.contains("photography") -> {
                        listOf(
                            ProductRecommendation(
                                productName = "سامسونگ Galaxy S24 Ultra",
                                keyFeatures = "دوربین اصلی ۲۰۰ مگاپیکسلی، زوم اپتیکال ۵ برابری، پردازنده Snapdragon 8 Gen 3، صفحه نمایش Dynamic AMOLED 2X",
                                imageUrl = "https://picsum.photos/seed/s24u/600/400",
                                reasoning = "این گوشی با داشتن سنسور ۲۰۰ مگاپیکسلی و پردازش تصویر هوشمند، بهترین گزینه برای عکاسی حرفه‌ای و شرایط نوری ضعیف است.",
                                price = clampPrice(68_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "آیفون ۱۵ پرو مکس",
                                keyFeatures = "لنز تله‌فوتو با زوم ۵ برابری، سنسور ۴۸ مگاپیکسلی، فیلم‌برداری بی‌نظیر ProRes، بدنه تیتانیومی سبک",
                                imageUrl = "https://picsum.photos/seed/iphone15/600/400",
                                reasoning = "مناسب برای افرادی که علاوه بر عکاسی با کیفیت، به فیلم‌برداری سینمایی و ویرایش ویدیو روی گوشی اهمیت می‌دهند.",
                                price = clampPrice(82_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "شیائومی 14 Ultra",
                                keyFeatures = "لنزهای لایکا، سنسور بزرگ ۱ اینچی، دیافراگم متغیر فیزیکی، شارژ فوق سریع ۹۰ واتی",
                                imageUrl = "https://picsum.photos/seed/xiaomi14/600/400",
                                reasoning = "به لطف همکاری با لایکا و حسگرهای فوق‌العاده باکیفیت، خروجی رنگ خیره‌کننده‌ای برای عکاسی هنری ارائه می‌دهد.",
                                price = clampPrice(59_000_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                    text.contains("بازی") || text.contains("گیمینگ") || text.contains("gaming") || text.contains("game") -> {
                        listOf(
                            ProductRecommendation(
                                productName = "ایسوس ROG Phone 8 Pro",
                                keyFeatures = "صفحه نمایش ۱۶۵ هرتز AMOLED، کلیدهای فیزیکی AirTrigger، سیستم خنک‌کننده پیشرفته، باتری ۵۵۰۰ میلی‌آمپر",
                                imageUrl = "https://picsum.photos/seed/rog8/600/400",
                                reasoning = "طراحی کاملاً تخصصی برای گیمرها با سخت‌افزار قدرتمند، دکمه‌های کنترلی تاچ و پایداری حرارتی بی‌نظیر در بازی‌های سنگین.",
                                price = clampPrice(65_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "پوکو F6 Pro",
                                keyFeatures = "پردازنده Snapdragon 8 Gen 2، صفحه نمایش WQHD+ ۱۲۰ هرتز، شارژر ۱۲۰ واتی داخل جعبه، قیمت رقابتی عالی",
                                imageUrl = "https://picsum.photos/seed/f6pro/600/400",
                                reasoning = "یک غول گیمینگ اقتصادی که مشخصات پرچمدار را با قیمتی بسیار عالی برای گیمرهای مقتصد مهیا می‌سازد.",
                                price = clampPrice(28_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "آیفون ۱۵ پرو",
                                keyFeatures = "پردازنده A17 Pro با پشتیبانی از Ray Tracing سخت‌افزاری، اجرای زنده بازی‌های کنسولی، اندازه خوش‌دست",
                                imageUrl = "https://picsum.photos/seed/iphone15pro/600/400",
                                reasoning = "به لطف موتور پردازشی فوق‌العاده قدرتمند، قادر است بازی‌های سنگین در حد کنسول نظیر Resident Evil را کاملاً روان اجرا کند.",
                                price = clampPrice(74_000_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                    else -> { // Everyday Use
                        listOf(
                            ProductRecommendation(
                                productName = "سامسونگ Galaxy A55",
                                keyFeatures = "نمایشگر ۱۲۰ هرتز Super AMOLED، بدنه با کیفیت شیشه‌ای و فریم فلزی، شارژدهی فوق‌العاده باتری، پشتیبانی نرم‌افزاری بلندمدت",
                                imageUrl = "https://picsum.photos/seed/a55/600/400",
                                reasoning = "تعادلی استثنایی از کیفیت طراحی، دوربین‌های کارآمد، باتری با دوام بالا و قیمت مناسب برای کارهای روزمره.",
                                price = clampPrice(19_500_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "ناتینگ فون 2a",
                                keyFeatures = "طراحی خاص و شفاف با رابط نوری Glyph، سیستم‌عامل سبک Nothing OS، نمایشگر حاشیه یکنواخت، شارژ سریع ۴۵ واتی",
                                imageUrl = "https://picsum.photos/seed/np2a/600/400",
                                reasoning = "برای کسانی که مایلند تجربه‌ای روان، متفاوت و از نظر بصری جذاب در استفاده‌های روزمره خود داشته باشند.",
                                price = clampPrice(17_200_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "ردمی نوت ۱۳ پرو پلاس",
                                keyFeatures = "صفحه نمایش خمیده مدرن 1.5K، سنسور دوربین ۲۰۰ مگاپیکسلی، گواهی ضد آب کامل IP68، شارژدهی فوق‌سریع ۱۲۰ واتی",
                                imageUrl = "https://picsum.photos/seed/rn13/600/400",
                                reasoning = "امکاناتی نظیر شارژر اعجاب‌انگیز و دوربین فوق‌العاده بالا را در کالبد یک گوشی میان‌رده شیک در اختیار شما می‌گذارد.",
                                price = clampPrice(21_800_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                }
            }
            ProductCategory.LAPTOP -> {
                when {
                    text.contains("بازی") || text.contains("گیمینگ") || text.contains("gaming") || text.contains("game") -> {
                        listOf(
                            ProductRecommendation(
                                productName = "ایسوس ROG Strix G16",
                                keyFeatures = "پردازنده Core i9 نسل ۱۳، کارت گرافیک RTX 4070، نمایشگر ۱۶ اینچ ۱۶۵ هرتز، سیستم خنک‌کننده سه فنه",
                                imageUrl = "https://picsum.photos/seed/g16/600/400",
                                reasoning = "یک مهندسی بی‌نظیر برای گیمرها که فریم‌ریت‌های نجومی را در تمامی بازی‌های روز دنیا به ثمر می‌نشاند.",
                                price = clampPrice(115_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "لنوو Legion Pro 5",
                                keyFeatures = "پردازنده Ryzen 7، گرافیک RTX 4060، حافظه رم ۱۶ گیگابایت DDR5، سیستم هوش مصنوعی تنظیم فریم",
                                imageUrl = "https://picsum.photos/seed/legion/600/400",
                                reasoning = "پادشاه میان‌رده‌های گیمینگ بازار با شاسی تمام آلومینیومی، کیبورد با پاسخ‌دهی عالی و ارزش خرید در برابر عملکرد استثنایی.",
                                price = clampPrice(84_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "ایسوس TUF Gaming A15",
                                keyFeatures = "پردازنده Ryzen 5، گرافیک RTX 4050، استاندارد نظامی بدنه، شارژدهی بسیار عالی باتری گیمینگ",
                                imageUrl = "https://picsum.photos/seed/tuf/600/400",
                                reasoning = "یک گزینه جان‌سخت و اقتصادی با راندمان حرارتی بهینه و گستره بالایی از پورت‌ها برای گیمرها و مهندسان.",
                                price = clampPrice(49_000_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                    text.contains("ادیت") || text.contains("تدوین") || text.contains("رندر") || text.contains("editing") || text.contains("video") -> {
                        listOf(
                            ProductRecommendation(
                                productName = "مک‌بوک پرو ۱۶ اینچی M3 Pro",
                                keyFeatures = "تراشه ۱۲ هسته‌ای M3 Pro، صفحه نمایش Liquid Retina XDR، عمر باتری ۲۲ ساعته، موتور اختصاصی رمزگذاری مدیا",
                                imageUrl = "https://picsum.photos/seed/macbook16/600/400",
                                reasoning = "بدون شک برترین ابزار برای تدوین‌گران ویدیویی با پنل نمایشگر فوق‌دقیق و بی‌رقیب با قابلیت حمل عالی تحت باتری بدون افت فریم.",
                                price = clampPrice(138_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "ایسوس Zenbook Pro 14 OLED",
                                keyFeatures = "پردازنده Core i9، کارت گرافیک RTX 4060، صفحه نمایش تاچ OLED با رزولوشن 2.8K، کلید چرخشی DialPad برای ابزارهای ادوبی",
                                imageUrl = "https://picsum.photos/seed/zenbook/600/400",
                                reasoning = "ویژه گرافیست‌ها و ادیتورها به واسطه کالیبراسیون عالی پنل رنگی OLED و حلقه کنترل فیزیکی دیال‌پد جهت تسریع تدوین حرکتی فیلم.",
                                price = clampPrice(98_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "مک‌بوک ایر ۱۵ اینچی M3",
                                keyFeatures = "تراشه ۸ هسته‌ای M3، طراحی فوق باریک و بدون فن، صفحه نمایش رتینا خیره‌کننده، پایداری استثنایی عملکرد",
                                imageUrl = "https://picsum.photos/seed/macbook15/600/400",
                                reasoning = "لپ‌تاپی سبک و کارآمد که برای ادیت‌های ویدیویی سبک تا نیمه‌سنگین روزمره و تولیدکنندگان محتوا کاملاً ایده آل است.",
                                price = clampPrice(72_000_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                    else -> { // Student Use
                        listOf(
                            ProductRecommendation(
                                productName = "لنوو ThinkBook 15 G5",
                                keyFeatures = "پردازنده Core i5 نسل ۱۳، ۸ گیگابایت رم (قابل ارتقا)، بدنه شیک با لولای ۱۸۰ درجه، انواع پورت‌های ارتباطی",
                                imageUrl = "https://picsum.photos/seed/thinkbook/600/400",
                                reasoning = "طراحی بادوام و قیمت ایده‌آل؛ یک لپ‌تاپ همه‌فن‌حریف عالی برای کارهای پژوهشی، تایپ، وب‌گردی و فیلم دیدن دانشجویان.",
                                price = clampPrice(24_500_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "ایسوس Vivobook 15 OLED",
                                keyFeatures = "پردازنده Core i3/i5 نسل ۱۲ راندمان بالا، پنل نمایش حیرت‌انگیز OLED، وزن شیک و باریک ۱.۷ کیلوگرمی",
                                imageUrl = "https://picsum.photos/seed/vivobook/600/400",
                                reasoning = "تنها دستگاه دانشجویی با نمایشگر باکیفیت اولد که لذت فوق‌العاده‌ای به مطالعه پی‌دی‌اف و نگارش مقالات می‌بخشد.",
                                price = clampPrice(29_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "لنوو IdeaPad Slim 3",
                                keyFeatures = "پردازنده Ryzen 3، بدنه تمام پلاستیکی مقاوم و خوش‌وزن، شارژدهی شارژر سریع، تاچ‌پد دقیق و بزرگ",
                                imageUrl = "https://picsum.photos/seed/ideapad/600/400",
                                reasoning = "ارزان‌ترین گزینه خوش‌قیمت و باکیفیت برای دانشجویان رشته‌های غیر فنی جهت برآوردن نیازهای روزانه.",
                                price = clampPrice(17_800_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                }
            }
            ProductCategory.HEADPHONES -> {
                when {
                    text.contains("موسیقی") || text.contains("آهنگ") || text.contains("music") || text.contains("song") -> {
                        listOf(
                            ProductRecommendation(
                                productName = "سونی WH-1000XM5",
                                keyFeatures = "برترین تکنولوژی حذف نویز جهان (ANC)، درایور نوین ۳۰ میلی‌متری شفاف، شارژدهی ۳۰ ساعته، پشتیبانی صوتی LDAC با نرخ بالا",
                                imageUrl = "https://picsum.photos/seed/xm5/600/400",
                                reasoning = "بهترین انتخاب برای عشاق واقعی موسیقی با ارائه‌ تفکیک صوتی بی نظیر و فضاسازی کاملاً آکوستیک بدون مداخله صدای محیط.",
                                price = clampPrice(15_800_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "سنهایزر MOMENTUM 4",
                                keyFeatures = "شارژدهی نجومی ۶۰ ساعته، بازتولید صدای باکیفیت استودیویی آلمانی، پد چرمی نرم راحتی مطلق، دکمه‌های کنترلی لمسی هوشمند",
                                imageUrl = "https://picsum.photos/seed/momentum4/600/400",
                                reasoning = "با دوام باتری خیره‌کننده و امضای صوتی طبیعی و عمیق سنهایزر، موسیقی را دقیقاً به همان نحو که خواننده ثبت کرده برایتان فاش می‌کند.",
                                price = clampPrice(17_500_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "انکر Soundcore Space Q45",
                                keyFeatures = "حذف نویز فعال تا ۹۸ درصد، شارژر فست، گواهی Hi-Res صوتی، قیمت فوق‌العاده پایدار و عالی",
                                imageUrl = "https://picsum.photos/seed/q45/600/400",
                                reasoning = "یک معجزه اقتصادی برای تجربه صدای های‌رس و حذف نویز عالی بدون فشار مالی به سبد خرید.",
                                price = clampPrice(5_900_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                    text.contains("بازی") || text.contains("گیمینگ") || text.contains("gaming") || text.contains("game") -> {
                        listOf(
                            ProductRecommendation(
                                productName = "ریزر BlackShark V2 Pro",
                                keyFeatures = "میکروفون فوق‌پهن ۸.۲ کیلوهرتز لایو، درایورهای ۵۰ میلی‌متر تیتانیوم، صدای فضایی THX، اتصال فوق سریع بی‌سیم ۲.۴ گیگاهرتز",
                                imageUrl = "https://picsum.photos/seed/blackshark/600/400",
                                reasoning = "طراحی تخصصی با هدف غوطه‌وری کامل در بازی‌های رقابتی شوتر و امکان شنیدن جزئی‌ترین صداهای پای رقیب.",
                                price = clampPrice(9_200_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "استیل‌سریوز Arctis Nova 7",
                                keyFeatures = "طراحی پدهای پارچه‌ای خنک‌کننده، اتصال همزمان بلوتوث و شبکه رادیویی، میکروفون مخفی‌ شونده با حذف نویز هوش مصنوعی",
                                imageUrl = "https://picsum.photos/seed/arctis/600/400",
                                reasoning = "قابلیت اتصال صوتی به گوشی و کنسول هم‌زمان و راحتی بی‌نظیر ارگونومیک در ماراتن‌های بی‌پایان گیمینگ.",
                                price = clampPrice(11_800_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "ای‌فورتک Bloody G575",
                                keyFeatures = "صدای مجازی ۷.۱ کاناله، سیستم نوری RGB زیبا، هدبند پددار ارگونومیک، اتصال سیمی یواس‌بی مقاوم",
                                imageUrl = "https://picsum.photos/seed/bloody/600/400",
                                reasoning = "یک هدست گیمینگ سیمی پرقدرت و خوش‌قیمت با بیس کوبنده برای لذت بالا در بازی‌های حادثه‌ای.",
                                price = clampPrice(2_400_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                    else -> { // Calls & Meetings
                        listOf(
                            ProductRecommendation(
                                productName = "اپل AirPods Pro 2",
                                keyFeatures = "تراشه پیشرفته H2، حالت مکالمه تطبیقی پیشرفته، صدای ۳بعدی شخصی‌سازی شده، میکروفون‌های پرتو‌شکل مکالمه شفاف",
                                imageUrl = "https://picsum.photos/seed/airpods/600/400",
                                reasoning = "سازگاری کامل با آیفون و ارائه شفاف‌ترین کیفیت تماس در شلوغ‌ترین محیط‌های اداری و پروازها.",
                                price = clampPrice(11_500_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "جبرا Evolve2 65",
                                keyFeatures = "دارای چراغ وضعیت مشغول آنلاین در حین تماس، میکروفون بوم‌دار چرخشی فوق دقیق، اتصال همزمان به لپ‌تاپ و گوشی",
                                imageUrl = "https://picsum.photos/seed/jabra/600/400",
                                reasoning = "طراحی شده منحصراً برای کارمندان و فریلنسرها جهت برگزاری حرفه‌ای جلسات فاقد نویزهای محیطی خانه.",
                                price = clampPrice(14_500_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "سامسونگ Galaxy Buds2 Pro",
                                keyFeatures = "مقاوم در برابر عرق طبق استاندارد IPX7، مکالمه با پورت ۳ جهته ضد فرکانس باد، طراحی ارگونومی کوچک و ضد چرخش",
                                imageUrl = "https://picsum.photos/seed/buds2/600/400",
                                reasoning = "مکالمه تلفنی ایمن و شفاف در زمان حرکت و رانندگی با قابلیت عاطفی تشخیص هوشمند آغاز صحبت کاربر برای کاهش صدا.",
                                price = clampPrice(6_200_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                }
            }
            ProductCategory.TV -> {
                when {
                    text.contains("سینما") || text.contains("فیلم") || text.contains("سریال") || text.contains("movies") || text.contains("series") -> {
                        listOf(
                            ProductRecommendation(
                                productName = "ال‌جی OLED C3",
                                keyFeatures = "پنل خودنورده اولد باشکوه، مشکی مطلق و کنتراست بی‌نهایت، پشتیبانی از Dolby Vision و Dolby Atmos، پردازنده هوش مصنوعی a9 نسل ۶",
                                imageUrl = "https://picsum.photos/seed/lgc3/600/400",
                                reasoning = "پرچمدار رویایی برای تجربه‌ای دقیقاً مشابه سالن سینما در خانه به لطف نمایش غنی رنگ‌ها و عمق مطلق رنگ مشکی.",
                                price = clampPrice(88_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "سامسونگ QN90C Neo QLED",
                                keyFeatures = "روشنایی اعجاب‌انگیز با بکلایت Mini LED، ضد بازتاب ضریب تابش آفتاب، پردازنده نئورال کوانتوم ۴K، زاویه دید فوق عریض",
                                imageUrl = "https://picsum.photos/seed/qn90c/600/400",
                                reasoning = "یک تلویزیون فوق‌درخشان فوق‌العاده برای تماشای فیلم‌ها در پذیرایی‌های پرنور بدون ترس از بازتاب نور پنجره.",
                                price = clampPrice(78_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "تی‌سی‌ال C745 Mini LED",
                                keyFeatures = "تکنولوژی mini-LED خوش‌قیمت، رفرش ریت ۱۴۴ هرتز، سیستم‌عامل روان Google TV، پشتیبانی کامل HDR10+",
                                imageUrl = "https://picsum.photos/seed/tclc/600/400",
                                reasoning = "بهترین تعادل میان امکانات مدرن و قیمت نهایی که کیفیت تصویر عالی را برای تماشای خانوادگی فیلم به صرفه می‌کند.",
                                price = clampPrice(33_000_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                    else -> { // Home Entertainment & Sports
                        listOf(
                            ProductRecommendation(
                                productName = "سونی X90L",
                                keyFeatures = "پنل پردازشگر شناختی انقلابی XR، بکلایت Full Array مستقیم، حرکت تصویر بی‌نظیر سونی در پخش فوتبال و مسابقات، ارگونومی بهینه",
                                imageUrl = "https://picsum.photos/seed/sonyx90/600/400",
                                reasoning = "با رندر طبیعی‌ترین رنگ‌های صورت و جزئیات زمین فوتبال، تلویزیونی خارق‌العاده برای پخش مسابقات شاد و زنده ورزشی دورهمی.",
                                price = clampPrice(62_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "سامسونگ Q70C QLED",
                                keyFeatures = "کوانتوم دات با نمایش ۱ میلیارد رنگ، بدنه فوق باریک AirSlim، هماهنگی کامل صوتی با ساندبار به کمک Q-Symphony",
                                imageUrl = "https://picsum.photos/seed/q70c/600/400",
                                reasoning = "مناسب برای بازی‌های خانوادگی و سرگرمی روزانه با دوام کاری بالا و ظاهر شیک با قیمت متعادل کیولد.",
                                price = clampPrice(37_000_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "اسنوا SSD-55QD100",
                                keyFeatures = "رزولوشن 4K Ultra HD، صفحه نمایش ۵۵ اینچ تمام صفحه بدون حاشیه، پنل IPS با مقاومت بالا ضربه، سیستم‌عامل اندروید بومی",
                                imageUrl = "https://picsum.photos/seed/snowa/600/400",
                                reasoning = "یک تلویزیون ملی خوش‌ساخت با خدمات پس از فروش فوری و مناسب برای استفاده روزانه والدین و کودکان.",
                                price = clampPrice(19_800_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                }
            }
            ProductCategory.SPEAKER -> {
                when {
                    text.contains("مهمانی") || text.contains("پارتی") || text.contains("جشن") || text.contains("party") || text.contains("loud") -> {
                        listOf(
                            ProductRecommendation(
                                productName = "جی‌بی‌ال PartyBox 310",
                                keyFeatures = "توان خروجی ریتمیک ۲۴۰ وات RMS، رقص نور جادویی همگام با بیس موزیک، چرخ‌های روان چمدانی و دستگیره، شارژدهی ۱۸ ساعته",
                                imageUrl = "https://picsum.photos/seed/pb310/600/400",
                                reasoning = "برترین لرزاننده جادویی مجالس و تولدها با ارتعاش بم بسیار عمیق و نورپردازی که هر محیطی را به یک کلاب شبانه تبدیل می‌کند.",
                                price = clampPrice(28_500_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "جی‌بی‌ال PartyBox Essential",
                                keyFeatures = "توان ۱۰۰ وات پرقدرت، ورودی فیزیکی میکروفون همراه افکت وکال، ضد پاشش آب IPX4، رقص نور حلقه‌ای پویا",
                                imageUrl = "https://picsum.photos/seed/pbe/600/400",
                                reasoning = "یک پارتی‌باکس جمع‌و‌جورتر و جذاب برای مهمانی‌های خانوادگی و باغ‌های فضای باز متوسط.",
                                price = clampPrice(14_200_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "مکسیدر MX-ES122",
                                keyFeatures = "۲ عدد ووفر ۱۲ اینچ غول‌آسا، قدرت خروجی ۳۸۰۰۰ وات فاند، بدنه چوبی مستحکم، همراه با رقص نور سقفی به علاوه ریموت کنترل",
                                imageUrl = "https://picsum.photos/seed/maxeeder/600/400",
                                reasoning = "اگر قدرت صدای مبهوت‌کننده و در ابعاد بزرگ برای باغ و ویلا با قیمت ارزان را جست‌و‌جو می‌کنید، این برندی بومی و فوق‌العاده است.",
                                price = clampPrice(18_900_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                    else -> { // Home Use & Premium Sound (Everyday)
                        listOf(
                            ProductRecommendation(
                                productName = "مارشال Stanmore III",
                                keyFeatures = "طراحی رترو عتیقه و اصیل چرمی مارشال، درایورهای صوتی تفکیک‌شده بالا به پایین، بدنه منعطف ضد طنین مزاحم، ولوم‌های آنالوگ برنجی",
                                imageUrl = "https://picsum.photos/seed/stanmore/600/400",
                                reasoning = "مناسب برای زیباسازی اتاق پذیرایی و شنیدن موسیقی سنتی و کلاسیک با غنای صوتی بالا و ظاهری فوق‌العاده لوکس.",
                                price = clampPrice(19_500_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "هارمن کاردن Aura Studio 4",
                                keyFeatures = "پخش صوتی فرکانسی ۳۶۰ درجه، ساب‌ووفر قدرتمند رو به پایین ۵.۲ اینچی، گنبد کریستالی با رقص نور متحرک و رویایی با تم طبیعت",
                                imageUrl = "https://picsum.photos/seed/aura4/600/400",
                                reasoning = "تلفیقی شکوهمند از شاهکار معماری هنری و بیس نرم جادویی هارمن کاردن که قلب هر فضای مدرن خانگی است.",
                                price = clampPrice(16_500_000L, budgetMin, budgetMax)
                            ),
                            ProductRecommendation(
                                productName = "جی‌بی‌ال Flip 6",
                                keyFeatures = "دوام باتری ۱۲ ساعته، بدنه ضد آب و خاک کامل IP67 مناسب سفر، درایور صوتی مجزای توییتر و ووفر، بند حمل فیزیکی",
                                imageUrl = "https://picsum.photos/seed/flip6/600/400",
                                reasoning = "یک همدم صوتی کوچک و همه‌فن‌حریف کوهنوردی با ارائه‌ وضوح و بیب فوق العاده باورنکردنی فراتر از ابعاد خود.",
                                price = clampPrice(5_200_000L, budgetMin, budgetMax)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun clampPrice(price: Long, min: Long, max: Long): Long {
        if (price < min) return min + (1_000_000L..3_000_000L).random()
        if (price > max) return max - (1_000_000L..5_000_000L).random()
        return price
    }

    fun getFallbackRecommendations(
        category: ProductCategory,
        preferredBrand: String,
        budgetMin: Long,
        budgetMax: Long
    ): List<ProductRecommendation> {
        val brandLower = preferredBrand.trim().lowercase()
            .replace('ي', 'ی')
            .replace('ك', 'ک')
            .replace("\u200c", " ")

        val hasSamsung = brandLower.contains("سامسونگ") || brandLower.contains("samsung") || brandLower.contains("گلکسی") || brandLower.contains("galaxy")
        val hasApple = brandLower.contains("اپل") || brandLower.contains("آیفون") || brandLower.contains("apple") || brandLower.contains("iphone") || brandLower.contains("mac") || brandLower.contains("مک") || brandLower.contains("ipad")
        val hasXiaomi = brandLower.contains("شیائومی") || brandLower.contains("xiaomi") || brandLower.contains("redmi") || brandLower.contains("ردمی")
        val hasPoco = brandLower.contains("poco") || brandLower.contains("پوکو")
        val hasAsus = brandLower.contains("ایسوس") || brandLower.contains("asus") || brandLower.contains("rog") || brandLower.contains("tuf")
        val hasLenovo = brandLower.contains("لنوو") || brandLower.contains("lenovo") || brandLower.contains("legion")
        val hasHp = brandLower.contains("اچ پی") || brandLower.contains("hp") || brandLower.contains("اچ‌پی")
        val hasSony = brandLower.contains("سونی") || brandLower.contains("sony")
        val hasAnker = brandLower.contains("انکر") || brandLower.contains("anker") || brandLower.contains("soundcore")
        val hasJabra = brandLower.contains("جبرا") || brandLower.contains("jabra")
        val hasLg = brandLower.contains("ال جی") || brandLower.contains("lg") || brandLower.contains("ال‌جی")
        val hasGplus = brandLower.contains("جی پلاس") || brandLower.contains("gplus") || brandLower.contains("g-plus") || brandLower.contains("جی‌پلاس")
        val hasSnowa = brandLower.contains("اسنوا") || brandLower.contains("snowa")
        val hasJbl = brandLower.contains("جی بی ال") || brandLower.contains("jbl") || brandLower.contains("جی‌بی‌ال")
        val hasMaxeeder = brandLower.contains("مکسیدر") || brandLower.contains("maxeeder")
        val hasMarshall = brandLower.contains("مارشال") || brandLower.contains("marshall")

        val brandName = preferredBrand.trim().ifBlank { "برند منتخب" }

        return when (category) {
            ProductCategory.SMARTPHONE -> {
                when {
                    hasSamsung -> listOf(
                        ProductRecommendation(
                            productName = "سامسونگ Galaxy S24 Ultra",
                            keyFeatures = "دوربین اصلی ۲۰۰ مگاپیکسلی، زوم اپتیکال ۵ برابری، پردازنده Snapdragon 8 Gen 3، بدنه تیتانیوم مقاوم",
                            imageUrl = "https://picsum.photos/seed/s24u/600/400",
                            reasoning = "این پرچمدار سامسونگ با بهره‌گیری از هوش مصنوعی پیشرفته و سنسور ۲۰۰ مگاپیکسلی، بالاترین پایداری سخت‌افزاری و عکاسی را به شما ارائه می‌دهد.",
                            price = clampPrice(68_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "سامسونگ Galaxy S24 FE",
                            keyFeatures = "صفحه نمایش Dynamic AMOLED 2X، تراشه Exynos 2400e، دوربین سه‌گانه حرفه‌ای، پشتیبانی از Galaxy AI",
                            imageUrl = "https://picsum.photos/seed/s24fe/600/400",
                            reasoning = "نسخه اقتصادی پرچمدار سامسونگ که تمام ویژگی‌های اساسی عکاسی و توان پردازشی عالی را در برجی خوش‌قیمت فراهم می‌کند.",
                            price = clampPrice(37_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "سامسونگ Galaxy A55 5G",
                            keyFeatures = "نمایشگر ۱۲۰ هرتز Super AMOLED، فریم آلومینیومی، باتری با دوام ۵۰۰۰ میلی‌آمپر، محافظ Gorilla Glass Victus+",
                            imageUrl = "https://picsum.photos/seed/a55/600/400",
                            reasoning = "بهترین گوشی میان‌رده سامسونگ با طراحی مقاوم شیشه‌ای، طول عمر فوق‌العاده باتری و ارائه‌ای متعادل از دوربین و بازدهی روزانه.",
                            price = clampPrice(19_500_000L, budgetMin, budgetMax)
                        )
                    )
                    hasApple -> listOf(
                        ProductRecommendation(
                            productName = "آیفون ۱۵ پرو مکس",
                            keyFeatures = "بدنه تیتانیومی بسیار سبک، لنز تله‌فوتو پریسکوپ با زوم ۵ برابری، تراشه قدرتمند A17 Pro، لرزشگیر سنسور شیفت نسل دوم",
                            imageUrl = "https://picsum.photos/seed/iphone15/600/400",
                            reasoning = "برترین گوشی اپل با دکمه اکشن کاربری، سرعت انتقال بسیار بالا و پایداری گرافیکی تحسین‌برانگیز در فیلم‌برداری و بازی حرفه‌ای.",
                            price = clampPrice(82_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "آیفون ۱۵ معمولی",
                            keyFeatures = "جزیره پویا (Dynamic Island)، تراشه A16 Bionic، سنسور اصلی دوربین ۴۸ مگاپیکسلی، درگاه شارژر Type-C جهانی",
                            imageUrl = "https://picsum.photos/seed/iphone15nor/600/400",
                            reasoning = "به لطف جزیره هوشمند جدید و ارتقای دوربین اصلی به ۴۸ مگاپیکسل، تجربه‌ای روان و لوکس از اکوسیستم اپل با ابعادی شیک تقدیم می‌کند.",
                            price = clampPrice(52_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "آیفون ۱۳ پرومکس (کارکرده تمیز)",
                            keyFeatures = "شارژدهی افسانه‌ای باتری، صفحه نمایش ۱۲۰ هرتز Promotion، دوربین ۳گانه بهینه، بدنه باصلابت استیل ضدزنگ",
                            imageUrl = "https://picsum.photos/seed/iphone13pm/600/400",
                            reasoning = "برای خریداران اپل که طول عمر فوق‌العاده زیاد باتری و روان بودن نمایشگر پرو‌موشن برایشان در اولویت است.",
                            price = clampPrice(44_000_000L, budgetMin, budgetMax)
                        )
                    )
                    hasXiaomi || hasPoco -> listOf(
                        ProductRecommendation(
                            productName = "شیائومی 14 Ultra",
                            keyFeatures = "لنزهای ساخت Leica آلمان، سنسور صوتی بزرگ ۱ اینچی، بدنه با چرم مصنوعی مرغوب، پردازنده Snapdragon 8 Gen 3",
                            imageUrl = "https://picsum.photos/seed/xiaomi14/600/400",
                            reasoning = "غول عکاسی شیائومی با دیافراگم متغیر فیزیکی و فیلترهای نوری لایکا، بالاترین جزئیات عکاسی جهان موبایل را ارمغان می‌دهد.",
                            price = clampPrice(62_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "پوکو F6 Pro",
                            keyFeatures = "صفحه نمایش فوق‌العاده درخشان 4000 nits، شارژر اعجاب‌انگیز ۱۲۰ واتی، سخت‌افزار قدرتمند فلگ‌شیپ، قیمت عالی",
                            imageUrl = "https://picsum.photos/seed/f6pro/600/400",
                            reasoning = "مخصوص گیمرها و عکاسانی که سخت‌افزار تاپ و سرعت شارژ نجومی را بدون هزینه‌ بالای پرچمدار طلب می‌کنند.",
                            price = clampPrice(28_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "ردمی نوت ۱۳ پرو پلاس",
                            keyFeatures = "طراحی جذاب لبه خمیده، سنسور دوربین ۲۰۰ مگاپیکسلی، گواهی مقاومت آب IP68، بدنه ظریف با چرم هیبریدی",
                            imageUrl = "https://picsum.photos/seed/rn13/600/400",
                            reasoning = "محبوب‌ترین گوشی شیک شیائومی که تعادلی شگفت‌آور بین زیبایی بی عیب‌ونقص، فست‌شارژ عالی و لنز قدرتمند برقرار ساخته است.",
                            price = clampPrice(21_800_000L, budgetMin, budgetMax)
                        )
                    )
                    else -> listOf(
                        ProductRecommendation(
                            productName = "$brandName Pro Max Extreme",
                            keyFeatures = "پردازنده چند هسته‌ای راندمان بالا، دوربین چندگانه فوق عریض، صفحه نمایش جذاب AMOLED، پشتیبانی شبکه نسل پنجم ۵G",
                            imageUrl = "https://picsum.photos/seed/genphone1/600/400",
                            reasoning = "محصولی شیک و با کیفیت از برند $brandName که با ارائه صفحه نمایشی مدرن و توازن کارآمد قطعات، انتخابی ایده‌آل است.",
                            price = clampPrice(32_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "$brandName Active Neo",
                            keyFeatures = "باتری غول‌پیکر با شارژدهی بالا، طراحی بدنه ارگونومیک ضدضربه، دوربین متکی بر هوش مصنوعی، اسپیکر قوی استریو",
                            imageUrl = "https://picsum.photos/seed/genphone2/600/400",
                            reasoning = "اگر به ارگونومی بدنه خوب، ماندگاری فوق‌العاده باتری و صدای پرقدرت از برند $brandName اهمیت می‌دهید، این گزینه جذابی است.",
                            price = clampPrice(18_500_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "$brandName Lite Edition",
                            keyFeatures = "وزن بسیار سبک و قطر بدنه نازک، سنسور سلفی بهینه، پردازنده اقتصادی بهینه، شارژ سریع سریع",
                            imageUrl = "https://picsum.photos/seed/genphone3/600/400",
                            reasoning = "طراحی مدرن و پویایی بالا با وزنی پروازگونه که برای استفاده سبک رومزه بسیار کاربرپسند است.",
                            price = clampPrice(11_200_000L, budgetMin, budgetMax)
                        )
                    )
                }
            }
            ProductCategory.LAPTOP -> {
                when {
                    hasAsus -> listOf(
                        ProductRecommendation(
                            productName = "ایسوس ROG Strix G16",
                            keyFeatures = "پردازنده غول Core i9، کارت گرافیک Nvidia RTX 4070، نمایشگر ۱۶ اینچ با نرخ ۱۶۵ هرتز، کیبورد Aura RGB",
                            imageUrl = "https://picsum.photos/seed/g16/600/400",
                            reasoning = "جزء قوی‌ترین لپ‌تاپ‌های گیمینگ و مهندسی ایسوس با کولینگ انقلابی و فریم‌ریتی بدون لرزش در برنامه‌های سه‌بعدی و رندر.",
                            price = clampPrice(115_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "ایسوس Zenbook 14 OLED",
                            keyFeatures = "صفحه نمایش باکیفیت فوق‌العاده OLED 2.8K، قلم لمسی فعال، شاسی تمام فلزی فوق نازک، باتری با دوام طولانی",
                            imageUrl = "https://picsum.photos/seed/zenbook/600/400",
                            reasoning = "آمیزه‌ای از شاهکار مهندسی بصری و سبکی بی حد که هر محتوا یا عکسی را با دقت رنگی بالا و نهایت شیک بودن به تصویر می‌کشد.",
                            price = clampPrice(64_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "ایسوس TUF Gaming A15",
                            keyFeatures = "پردازنده نسل جدید Ryzen 7، گرافیک قدرتمند RTX 4050، استاندارد ضدضربه نظامی، کیبورد جزیره‌ای",
                            imageUrl = "https://picsum.photos/seed/tuf/600/400",
                            reasoning = "با دوام فوق العاده بدنه و تعادل راندمان اقتصادی، برترین مهندسی اقتصادی لپ تاپ ایسوس را برای کارهای سنگین جلوه می‌دهد.",
                            price = clampPrice(49_000_000L, budgetMin, budgetMax)
                        )
                    )
                    hasLenovo -> listOf(
                        ProductRecommendation(
                            productName = "لنوو Legion Pro 5",
                            keyFeatures = "پردازنده Ryzen 7، گرافیک RTX 4060، رم ۱۶ گیگابایت DDR5، بهینه‌سازی شده با چیپ هوشمند Lenovo LA1 AI",
                            imageUrl = "https://picsum.photos/seed/legion/600/400",
                            reasoning = "شاهکار بی بدیل دوام گیمینگ لنوو با بدنه مستحکم آلومینیوم و کیبورد ارگونومیک، بهترین کارایی را برای کارهای سنگین رقم می‌زند.",
                            price = clampPrice(84_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "لنوو ThinkBook 15 G5",
                            keyFeatures = "پردازنده Core i5، شاسی ظریف آلومینیومی، پورت‌های متنوع اداری، حافظه قابل ارتقا تا دور بالا",
                            imageUrl = "https://picsum.photos/seed/thinkbook/600/400",
                            reasoning = "ویژه کارها و مقالات دانشگاهی با تاچ‌پد بزرگ و راحتی استفاده مداوم، گزینه‌ای بی‌رقیب در قیمت متوسط تلقی می‌شود.",
                            price = clampPrice(24_500_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "لنوو IdeaPad Slim 3",
                            keyFeatures = "پردازنده سری Ryzen 3، سبک و فوق‌العاده باریک ۱.۵ کیلوگرم، شارژ سریع سریع، نمایشگر ضد بازتاب",
                            imageUrl = "https://picsum.photos/seed/ideapad/600/400",
                            reasoning = "اقتصادی‌ترین و کارآمدترین گزینه لنوو برای کارهای وبگردی، تماشای فیلم و محاسبات تحقیقاتی روزمره.",
                            price = clampPrice(17_800_000L, budgetMin, budgetMax)
                        )
                    )
                    hasApple -> listOf(
                        ProductRecommendation(
                            productName = "مک‌بوک پرو ۱۶ اینچی M3 Pro",
                            keyFeatures = "تراشه فوق‌پیشرفته ۱۲ هسته‌ای Apple M3 Pro، نمایشگر Liquid Retina XDR، شارژدهی فرای ۲۲ ساعت، موتور ویدیویی ProRes",
                            imageUrl = "https://picsum.photos/seed/macbook16/600/400",
                            reasoning = "ابزار بلامنازع تدوین‌گران و برنامه‌نویسان مطرح جهان که پایداری اعجاب‌انگیز عملکرد تحت استفاده از باتری را به نمایش می‌گذارد.",
                            price = clampPrice(138_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "مک‌بوک ایر ۱۵ اینچی M3",
                            keyFeatures = "تراشه مدرن M3، ضخامت دیوانه‌کننده ۱۱ میلی‌متری، فاقد هرگونه صدای فن، رزولوشن تصویر رتینا کالیبره",
                            imageUrl = "https://picsum.photos/seed/macbook15/600/400",
                            reasoning = "یک لپ‌تاپ بی‌صدا، سبک و با کارآمدی صعودی عملکرد که برای طراحان وب و همراهان دائمی سفر بهترین است.",
                            price = clampPrice(72_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "مک‌بوک ایر ۱۳ اینچی M2",
                            keyFeatures = "تراشه خوش‌قیمت M2، بدنه مقاوم مدرن، سبک‌ترین وزن ممکن مابین لپ‌تاپ‌های اداری، وبکم فول‌اچ‌دی",
                            imageUrl = "https://picsum.photos/seed/macbook13/600/400",
                            reasoning = "ارزش خرید استثنایی برای دانشجویان و مدیران جهت کارهای دفتری و تولید محتوای مداوم.",
                            price = clampPrice(54_000_000L, budgetMin, budgetMax)
                        )
                    )
                    hasHp -> listOf(
                        ProductRecommendation(
                            productName = "اچ‌پی Omen 16",
                            keyFeatures = "پردازنده Core i7، گرافیک RTX 4060 برودت توسعه یافته، نمایشگر با فرکانس ۱۶۵ هرتز، کیبورد با رنگ اختصاصی",
                            imageUrl = "https://picsum.photos/seed/hpomen/600/400",
                            reasoning = "طراحی جذاب با خنک‌کننده پرقدرت اختصاصی Omen Tempest که بازدهی ثابتی به کارهای پردازشی شما اعطا می‌کند.",
                            price = clampPrice(74_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "اچ‌پی Victus 15",
                            keyFeatures = "پردازنده قدرتمند Core i5، کارت گرافیک RTX 3050، وبکم حذف نویز، فریم‌ ریت بسیار روان",
                            imageUrl = "https://picsum.photos/seed/hpvic/600/400",
                            reasoning = "پرطرفدارترین لپ‌تاپ نیمه‌سنگین اقتصادی که راندمان قابل قبولی در برنامه‌های مهندسی و نرم‌افزارهای تجاری دارد.",
                            price = clampPrice(39_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "اچ‌پی ProBook 450 G10",
                            keyFeatures = "بدنه آلومینیوم برس‌خورده مستحکم، کیبورد ضد نفوذ مایعات، حسگر اثر‌انگشت امنیتی، خروجی‌های متنوع",
                            imageUrl = "https://picsum.photos/seed/hppro/600/400",
                            reasoning = "بقا و دوام بی‌نظیر بدنه برای استفاده طولانی در شرکت‌ها و دفاتر کار و کارهای پژوهشی مداوم.",
                            price = clampPrice(28_000_000L, budgetMin, budgetMax)
                        )
                    )
                    else -> listOf(
                        ProductRecommendation(
                            productName = "لپ‌تاپ $brandName UltraBook 15",
                            keyFeatures = "پردازنده نسل جدید بهینه، حافظه رم ۱۶ گیگابایت، صفحه نمایش باریک با زاویه عریض، بدنه ظریف مقاوم",
                            imageUrl = "https://picsum.photos/seed/genlap1/600/400",
                            reasoning = "لپ تاپی بسیار شکیل و پرسرعت برای کارهای اداری، حسابداری و تحقیقات از برند محبوب $brandName.",
                            price = clampPrice(32_500_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "لپ‌تاپ $brandName Force Gaming X",
                            keyFeatures = "سیستم خنک‌کننده قدرتمند، گرافیک رندرساز اختصاصی، کیبورد ارگونومیک با نور بدنه، پورت اتصال سریع چندگانه",
                            imageUrl = "https://picsum.photos/seed/genlap2/600/400",
                            reasoning = "سرعت پردازش بالا و سازگاری بهینه قطعات جهت تامین راندمان روان در پروژه‌های پردازشی از مارک $brandName.",
                            price = clampPrice(58_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "لپ‌تاپ $brandName Student Carbon",
                            keyFeatures = "وزن سبک قابل حمل، صفحه‌نمایش ضد خستگی چشم، باتری بهینه با دوام طولانی، قیمت بسیار مناسب اقتصادی",
                            imageUrl = "https://picsum.photos/seed/genlap3/600/400",
                            reasoning = "تولید با دوام بالا برای استفاده روزمره دانش‌آموزان و دانشجویان با قیمتی اقتصادی از برند $brandName.",
                            price = clampPrice(19_200_000L, budgetMin, budgetMax)
                        )
                    )
                }
            }
            ProductCategory.HEADPHONES -> {
                when {
                    hasSony -> listOf(
                        ProductRecommendation(
                            productName = "سونی WH-1000XM5",
                            keyFeatures = "بهترین تراشه حدف نویز جهان V1، درایور صوتی با کیفیت بالا، عمر باتری ۳۰ ساعته، پشتیبانی از کدک تفکیک صوتی LDAC",
                            imageUrl = "https://picsum.photos/seed/xm5/600/400",
                            reasoning = "حرفه‌ای‌ترین هدفون گوش‌نواز موسیقی بازار که تجربه‌ای کاملاً خاص و مجذوب‌کننده را به عشاق سبک موسیقی هدیه می‌دهد.",
                            price = clampPrice(15_800_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "سونی WF-1000XM5",
                            keyFeatures = "طراحی کوچک و کاملاً داخل گوش، حذف نویز هوشمند، میکروفون‌های عایق نویز باد، شارژ فوری سریع",
                            imageUrl = "https://picsum.photos/seed/wfxm5/600/400",
                            reasoning = "یک عایق صوتی کوچک بی‌رقیب با کوبش بیس شگفت‌آور برای موسیقی و مکالمه در زمان پیاده‌روی.",
                            price = clampPrice(11_200_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "samsung سونی CH-720N",
                            keyFeatures = "هدفون روگوشی بیسیم خوش‌قیمت، باتری اعجاب‌انگیز ۵۰ ساعته با یک شارژ، سیستم نویز کنسلینگ خودکار، وزن سبک",
                            imageUrl = "https://picsum.photos/seed/ch720/600/400",
                            reasoning = "سبک‌ترین هدفون دورگوش حذف نویز سونی با باتری با دوام باورنکردنی برای کلاس‌های آموزشی و سفرها.",
                            price = clampPrice(5_300_000L, budgetMin, budgetMax)
                        )
                    )
                    hasAnker -> listOf(
                        ProductRecommendation(
                            productName = "انکر Soundcore Space Q45",
                            keyFeatures = "حذف نویز بسیار قوی تا ۹۸ درصد، گواهینامه صوتی Hi-Res Wireless، شارژدهی فوق‌العاده ۵۰ ساعته با ANC روشن",
                            imageUrl = "https://picsum.photos/seed/q45/600/400",
                            reasoning = "یک هدفون معجزه‌آسای اقتصادی که تجربه‌ای نزدیک به پرچمداران گران‌قیمت بازار را با قیمتی پایدار تقدیم می‌کند.",
                            price = clampPrice(5_900_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "انکر Soundcore Liberty 4",
                            keyFeatures = "ایرباد داخل گوشی صوتی با وضوح بالا، سنسور هوشمند سنجش ضربان قلب، صدای فضایی صوتی ۳۶۰ درجه، ضد رطوبت",
                            imageUrl = "https://picsum.photos/seed/lib4/600/400",
                            reasoning = "امکانات فوق مدرن صوتی مانند تشخیص ژیروسکوپ جهت چرخش سر مضاف بر سنسورهای سلامتی در قالب یک ایرباد شیک.",
                            price = clampPrice(4_900_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "انکر Soundcore R50i",
                            keyFeatures = "بیس کوبنده با درایور ۱۰ میلی‌متری، نرم‌افزار حرفه‌ای اکولایزر چندگانه، بدنه ضد رطوبت IPX5، قیمت اقتصادی",
                            imageUrl = "https://picsum.photos/seed/r50i/600/400",
                            reasoning = "محبوب‌ترین و ارزان‌ترین هندزفری باکیفیت و با دوام بازار برای مکالمات و فیلم دیدن روزمره.",
                            price = clampPrice(1_200_000L, budgetMin, budgetMax)
                        )
                    )
                    hasJabra -> listOf(
                        ProductRecommendation(
                            productName = "جبرا Evolve2 65",
                            keyFeatures = "دارای بوم میکروفن چرخشی فوق دقیق، عایق صوتی فیزیکی عالی، اتصال سه جانبه همزمان، چراغ هشدار عدم مزاحمت",
                            imageUrl = "https://picsum.photos/seed/jabra/600/400",
                            reasoning = "بهترین هدست بی‌سیم اداری و جلسات آنلاین برای فریلنسرها جهت مخابره کردن صدای کاملاً زلال.",
                            price = clampPrice(14_500_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "جبرا Elite 7 Pro",
                            keyFeatures = "فناوری ارتعاش استخوانی جهت مکالمه شفاف در تندباد، طراحی ضدآب کامل IP57، باتری ۳۰ ساعته، فیت عالی در گوش",
                            imageUrl = "https://picsum.photos/seed/elite7/600/400",
                            reasoning = "برای کسانی که خواستار هندزفری باکیفیت بالا، ارگونومی صددرصد و میکروفونی مطمئن در ورزش و سفر هستند.",
                            price = clampPrice(7_400_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "جبرا Elite 4 Active",
                            keyFeatures = "سیستم حذف نویز فعال، صدای بم تمیز، طراحی ضد عرق ویژه ورزشکاران، فیت محکم بدون سقوط",
                            imageUrl = "https://picsum.photos/seed/elite4/600/400",
                            reasoning = "یک هدست اقتصادی از جبرا با بدنه اسپرت ویژه دویدن و فعالیت‌های فشرده روزانه.",
                            price = clampPrice(4_200_000L, budgetMin, budgetMax)
                        )
                    )
                    hasApple -> listOf(
                        ProductRecommendation(
                            productName = "اپل AirPods Max",
                            keyFeatures = "بدنه لوکس آلومینیومی، درایور صوتی داینامیک اختصاصی اپل، حذف نویز بسیار پرقدرت، حالت شفافیت تطبیقی",
                            imageUrl = "https://picsum.photos/seed/airmax/600/400",
                            reasoning = "خاص‌ترین و باابهت‌ترین هدفون اپل با بدنه پرمیوم و تفکیک طنین صوتی فوق‌العاده برای دارندگان پرشور اکوسیستم اپل.",
                            price = clampPrice(34_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "اپل AirPods Pro 2",
                            keyFeatures = "تراشه هوشمند نسل نوین H2، حالت مکالمه تطبیقی، کیس مجهز به اسپیکر مکان‌یاب، درگاه شارژ بی سیم",
                            imageUrl = "https://picsum.photos/seed/airpro2/600/400",
                            reasoning = "سازگارترین، بادوام‌ترین و باکیفیت‌ترین ایرباد اپل برای دارنده آیفون با عایق نویز فوق‌العاده خارق‌العاده.",
                            price = clampPrice(11_500_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "اپل AirPods 3",
                            keyFeatures = "طراحی بدون سری سیلیکونی برای عدم فشار به کانال گوش، صدای هوشمند فضایی، سنسور تشخیص تماس پویا، ضدآب",
                            imageUrl = "https://picsum.photos/seed/air3/600/400",
                            reasoning = "با طراحی راحت که هرگز لاله گوش را خسته نکرده و صدای پویایی را برای گفت‌وگوهای تلفنی روزانه منعکس می‌کند.",
                            price = clampPrice(7_400_000L, budgetMin, budgetMax)
                        )
                    )
                    else -> listOf(
                        ProductRecommendation(
                            productName = "هدفون بی سیم $brandName SoundPro",
                            keyFeatures = "حذف نویز فعال مجهز به میکروتراشه، صدای بم وضوح فوق العاده، باتری با دوام ۳۰ ساعتی، بالشتک چرمی ارگونومی",
                            imageUrl = "https://picsum.photos/seed/genaudio1/600/400",
                            reasoning = "تفکیک صوتی متناوب بالا و تعادلی ایده‌آل از فرکانس‌ها به همراه زیبایی عالی روی لاله گوش از برند $brandName.",
                            price = clampPrice(4_800_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "هندزفری $brandName Active Buds",
                            keyFeatures = "طراحی فیت محکم داخل مجرای گوش، فست شارژ سریع، بدنه مستحکم مقاوم در برابر رطوبت، کنترلر لمسی حساس",
                            imageUrl = "https://picsum.photos/seed/genaudio2/600/400",
                            reasoning = "مناسب برای مکالمات روزانه، رفت‌وآمد و تمرینات پرورشی با پایداری بلوتوث بالا از مارک $brandName.",
                            price = clampPrice(2_400_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "ایرباد اقتصادی $brandName Mini Wire",
                            keyFeatures = "طراحی فوق جمع‌و‌جور سبک، تاخیر فرکانسی پایین در بازی، باتری با دوام روزانه، کیس شارژر براق",
                            imageUrl = "https://picsum.photos/seed/genaudio3/600/400",
                            reasoning = "یک گزینه ارزان و مقرون‌به‌صرفه با طراحی آرگونومیک زیبا برای تماشای فیلم و بازی از برند $brandName.",
                            price = clampPrice(1_100_000L, budgetMin, budgetMax)
                        )
                    )
                }
            }
            ProductCategory.TV -> {
                when {
                    hasLg -> listOf(
                        ProductRecommendation(
                            productName = "ال‌جی OLED C3 55",
                            keyFeatures = "پنل برتر خودنورده انقلابی OLED، پردازنده فوق قدرت a9 AI، نرخ رفرش ۱۲۰ هرتز، مجهز به دالبی ویژن و دالبی اتموس",
                            imageUrl = "https://picsum.photos/seed/lgc3/600/400",
                            reasoning = "پادشاه تلویزیون‌های تصویر با رنگ‌های تیره عمق مطلق، کنتراست بی‌نهایت و پاسخ‌دهی آنی تصاویر اکشن سینمایی.",
                            price = clampPrice(88_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "ال‌جی QNED 81 55",
                            keyFeatures = "تلفیق فناوری ذرات کوانتوم و نانوسل، بکلایت درخشان صعودی، رنگ‌های فوق‌العاده زنده، سیستم عامل هوشمند webOS",
                            imageUrl = "https://picsum.photos/seed/lgqned/600/400",
                            reasoning = "ارائه‌ رنگ‌های باشکوه به واسطه ذرات کوانتوم دات با ارزش خرید فراتر نسبت به تلویزیون‌های سنتی.",
                            price = clampPrice(44_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "ال‌جی UR8000 55",
                            keyFeatures = "رزولوشن 4K Ultra HD، پردازنده بهینه Alpha 5 ردیابی صوت، بدنه شیک باریک، دارای دستیار صوتی هوشمند",
                            imageUrl = "https://picsum.photos/seed/lgur/600/400",
                            reasoning = "یک گزینه خوش‌ساخت و خوش‌قیمت از برند ال‌جی برای استفاده‌های روزانه، تماشای سریال‌ها و فوتبال خانوادگی.",
                            price = clampPrice(25_500_000L, budgetMin, budgetMax)
                        )
                    )
                    hasSamsung -> listOf(
                        ProductRecommendation(
                            productName = "سامسونگ QN90C Neo QLED",
                            keyFeatures = "فناوری درخشان Mini LED با کنترل نقطه‌ای نور، حداکثر طیف روشنایی، پردازنده انقلابی Neural Quantum",
                            imageUrl = "https://picsum.photos/seed/qn90c/600/400",
                            reasoning = "بالاترین سطح روشنایی تصویر که بازتاب نور خورشید پذیرایی را کاملاً مهار ساخته و فیلم‌های HDR را جادویی پخش می‌کند.",
                            price = clampPrice(78_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "سامسونگ Q70C QLED",
                            keyFeatures = "ذرات نوری رفلکتور کوانتوم دات، طراحی فوق‌باریک هوایی AirSlim، پردازنده تصویر ۴K کوانتوم، صدای هماهنگ",
                            imageUrl = "https://picsum.photos/seed/q70c/600/400",
                            reasoning = "یک محصول فوق العاده لوکس از بدنه با یک میلیارد غنای رنگی کوانتومی برای بازی‌های پر سرعت و فیلم دوستان.",
                            price = clampPrice(37_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "سامسونگ CU8000 55",
                            keyFeatures = "رزولوشن زلال وضوح بالا، رنگ‌های کریستالی داینامیک، سیستم صوتی ردیابی سوژه، ریموت کنترلر خورشیدی",
                            imageUrl = "https://picsum.photos/seed/samsungcu/600/400",
                            reasoning = "مدرن‌ترین تلویزیون اقتصادی سری کریستال سامسونگ با بدنه نازک بی نظیر برای کارهای تماشای ماهواره و کنسول بازی ساده.",
                            price = clampPrice(23_800_000L, budgetMin, budgetMax)
                        )
                    )
                    hasSony -> listOf(
                        ProductRecommendation(
                            productName = "سونی Bravia X90L 55",
                            keyFeatures = "پنل پردازشی مغزی Cognitive Processor XR، بکلایت سراسری ارتقا یافته، رنگ‌های بومی تریلومینوس، پایه فلزی چند حالته",
                            imageUrl = "https://picsum.photos/seed/sonyx90/600/400",
                            reasoning = "بهترین پردازشگر تصویر دنیا که جزئیات و چهره‌ها را دقیقاً مشابه درک چشم طبیعی تولید کرده و حرکتی بسیار نرم ارائه می‌دهد.",
                            price = clampPrice(62_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "سونی Bravia X85L 55",
                            keyFeatures = "بکلایت Full Array مستقیم ارگونومیک، رفرش ریت ۱۲۰ هرتز روان، سیستم عامل بومی Google TV، کیفیت صوتی رسای عالی",
                            imageUrl = "https://picsum.photos/seed/sonyx85/600/400",
                            reasoning = "تطابق ایده‌آل برای دارندگان پلی‌استیشن ۵ به دلیل نرخ نوسازی بالا و کنتراست بکلایت عالی با قیمتی بسیار متعادل‌تر.",
                            price = clampPrice(46_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "سونی Bravia X75K 50",
                            keyFeatures = "رزولوشن 4K روان، پردازنده اختصاصی X1، کیفیت رنگ‌های گرم سونی، محافظ رطوبت و نواسانات برق داخلی",
                            imageUrl = "https://picsum.photos/seed/sonyx75/600/400",
                            reasoning = "دروازه ورودی ارزان و باکیفیت به محصولات صوتی تصویری بااصالت سونی برای عمر کاری چند ده ساله.",
                            price = clampPrice(28_000_000L, budgetMin, budgetMax)
                        )
                    )
                    hasGplus || hasSnowa -> listOf(
                        ProductRecommendation(
                            productName = "$brandName Smart QLED 4K",
                            keyFeatures = "پنل درخشان کوانتوم با پنل IPS ضدضربه، تراشه سریع هوش مصنوعی، سیستم عامل اندروید تلویزیونی، بلندگوی استریو قوی",
                            imageUrl = "https://picsum.photos/seed/localtv1/600/400",
                            reasoning = "یک دستگاه عالی و با غنای رنگ کیولدی که با گارانتی معتبر بومی و پشتیبانی عالی خود، آرامش خرید را فراهم می‌سازد.",
                            price = clampPrice(24_800_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "$brandName Smart Google TV",
                            keyFeatures = "سیستم عامل روان و دسترسی راحت به برنامه‌ها، تیونر داخلی دیجیتال قوی، دارای خروجی صوتی نوری، بدنه بدون حاشیه",
                            imageUrl = "https://picsum.photos/seed/localtv2/600/400",
                            reasoning = "وضوح تصویر بالا و هماهنگی فوق العاده اتصال گوشی به تلویزیون با قیمتی فوق العاده رقابتی.",
                            price = clampPrice(16_500_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "$brandName HD Compact Smart",
                            keyFeatures = "صفحه خوش‌اندازه، مصرف انرژی فوق‌العاده پایین، نرم‌افزارهای بومی تعبیه شده، درگاه‌های ورودی فرستنده",
                            imageUrl = "https://picsum.photos/seed/localtv3/600/400",
                            reasoning = "مناسب برای اتاق خواب، مغازه‌ها یا محیط‌های اداری جهت پخش بدون وقفه با پرداخت هزینه‌ای کارآمد.",
                            price = clampPrice(11_200_000L, budgetMin, budgetMax)
                        )
                    )
                    else -> listOf(
                        ProductRecommendation(
                            productName = "تلویزیون هوشمند $brandName UHD Pro",
                            keyFeatures = "کیفیت تصویر زلال 4K، سیستم عامل هوشمند سرعت بالا، فریم فوق العاده نازک مدرن، بکلایت یکدست درخشان",
                            imageUrl = "https://picsum.photos/seed/gentv1/600/400",
                            reasoning = "ارائه‌ تصویری زنده و با کنتراست بالا جهت تماشای شبانه برنامه‌ها و بازی با قیمتی فوق العاده مناسب از برند $brandName.",
                            price = clampPrice(28_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "تلویزیون $brandName QLED Vision X",
                            keyFeatures = "طیف وسیع رنگ‌های کریستالی کوانتوم دات، پنل صوتی استریو قوی، مجهز به گیرنده دیجیتال کشوری، ضد بازتاب نور",
                            imageUrl = "https://picsum.photos/seed/gentv2/600/400",
                            reasoning = "رنگ‌های درخشان کوانتومی و ظاهری بسیار برازنده پذیرایی‌های مجلل از مارک $brandName.",
                            price = clampPrice(38_500_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "تلویزیون $brandName Smart Eco LED",
                            keyFeatures = "بدنه بدون زاویه حاشیه، پنل زاویه دید بسیار عریض، سیستم هدایت محتوای صوتی، هاب اتصالات کامل",
                            imageUrl = "https://picsum.photos/seed/gentv3/600/400",
                            reasoning = "با دوام قطعات بسیار بالا و ارائه قیمتی کاملاً بهینه جهت تماشای شبکه‌های خانوادگی از مارک $brandName.",
                            price = clampPrice(16_800_000L, budgetMin, budgetMax)
                        )
                    )
                }
            }
            ProductCategory.SPEAKER -> {
                when {
                    hasJbl -> listOf(
                        ProductRecommendation(
                            productName = "جی‌بی‌ال PartyBox 310",
                            keyFeatures = "توان خروجی واقعی ۲۴۰ وات RMS، رقص نور حلقه‌ای و نقطه‌ای چشم‌گیر، افکت‌های صوتی وکال دی‌جی، چرخ‌ها و دستگیره پویای چمدان",
                            imageUrl = "https://picsum.photos/seed/pb310/600/400",
                            reasoning = "محبوب‌ترین پارتی‌باکس چرخ‌دار جهان با بیس تکان‌دهنده ارتعاشات عمیق و نور خیره کننده جادویی برای شادترین جشن‌ها.",
                            price = clampPrice(28_500_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "جی‌بی‌ال PartyBox Encore",
                            keyFeatures = "توان صوتی خوب ۱۰۰ وات، مجهز به ۲ عدد میکروفون بی سیم جی‌بی‌ال، ضد پاشش باران IPX4، رقص نور جذاب",
                            imageUrl = "https://picsum.photos/seed/pbencore/600/400",
                            reasoning = "یک اسپیکر مربعی غول کوبش صوتی با میکروفون باکیفیت جهت ماراتن آوازخوانی کارائوکه با دوستان.",
                            price = clampPrice(15_800_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "جی‌بی‌ال Flip 6",
                            keyFeatures = "طراحی استوانه‌ای کوچک، با دوام باتری ۱۲ ساعته، ضد ضربه و آب کامل IP67، تفکیک صوتی ووفر و توییتر مجزا",
                            imageUrl = "https://picsum.photos/seed/flip6/600/400",
                            reasoning = "بهترین و پرتابل‌ترین اسپیکر جی‌بی‌ال برای آفرود، جنگل و سفرهای ماجراجویانه با پرتاب صدای فراتر از تصور.",
                            price = clampPrice(5_200_000L, budgetMin, budgetMax)
                        )
                    )
                    hasMaxeeder -> listOf(
                        ProductRecommendation(
                            productName = "مکسیدر MX-ES122 Double",
                            keyFeatures = "۲ عدد ساب‌ووفر ۱۲ اینچی قدرتمند، توان ۳۸۰۰۰ وات صوتی، بدنه چوب مستحکم عایق شده، رقص نور سقفی به علاوه ریموت ارتعاش",
                            imageUrl = "https://picsum.photos/seed/maxeeder/600/400",
                            reasoning = "غول ایستاده مکسیدر با ارتفاع بالا مخصوص لرزاندن ویلاها و باغات متوسط با ظاهری بسیار باصلابت و نورافکن سقف.",
                            price = clampPrice(18_900_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "مکسیدر MX-KT88 Speaker",
                            keyFeatures = "پرتابل با ورودی گیتار الکتریک فیزیکی، مجهز به میکروفون بی سیم، چرخ هدایت پشتی، بدنه مستحکم فایبر گلاس",
                            imageUrl = "https://picsum.photos/seed/maxkt/600/400",
                            reasoning = "اسپیکر مسافرتی مکسیدر با کارایی صوتی بالا و کوبش بیس ریتمیک جهت سفرهای کمپینگ گروهی.",
                            price = clampPrice(10_800_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "مکسیدر MX-TS310 Portable",
                            keyFeatures = "طراحی مکعبی بسیار سبک، باتری با دوام تا ۷ ساعت مداوم، رقص نور نئونی، گیرنده رادیوی قوی",
                            imageUrl = "https://picsum.photos/seed/maxeco/600/400",
                            reasoning = "یک گزینه پرتوان و ارزان مکسیدر برای استفاده‌های روزانه در مغازه یا جشن‌های تولد کوچک آپارتمانی.",
                            price = clampPrice(5_800_000L, budgetMin, budgetMax)
                        )
                    )
                    hasMarshall -> listOf(
                        ProductRecommendation(
                            productName = "مارشال Woburn III",
                            keyFeatures = "ارائه‌ صوتی با طنین ۳ جهته عمیق، غول طراحی کلاسیک مارشال با دکمه‌های فلزی طلاکاری، جک ۳.۵ و درگاه HDMI برای اتصال تلویزیون",
                            imageUrl = "https://picsum.photos/seed/woburn/600/400",
                            reasoning = "بزرگ‌ترین و خوش‌صداترین اسپیکر خانگی مارشال که جلال بی‌نظیری به خانه‌های مجلل می‌بخشد و موسیقی سنتی را ملکوتی می‌نوازد.",
                            price = clampPrice(36_000_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "مارشال Stanmore III",
                            keyFeatures = "توییترهای زاویه‌دار رو به بیرون، شاسی چرمی عایق صدا، دکمه‌های کنترل فرکانس‌های بم مستقل، بیس جادویی نرم",
                            imageUrl = "https://picsum.photos/seed/stanmore/600/400",
                            reasoning = "مناسب‌ترین و پرطرفدارترین شاهکار مارشال برای پر کردن صدای اتاق‌های پذیرایی متوسط با دکوراسیون رترو شیک.",
                            price = clampPrice(19_500_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "مارشال Emberton II",
                            keyFeatures = "صدای ۳۶۰ درجه نمادین پرتابل، بیش از ۳۰ ساعت شارژدهی باتری، گواهی بدنه ضدآب IP67، حالت جفت‌شدن Stack به هم",
                            imageUrl = "https://picsum.photos/seed/emberton/600/400",
                            reasoning = "لوکس‌ترین و جیبی‌ترین اسپیکر سفر مارشال با شاسی مقاوم در برابر رطوبت و صدای استریو فوق العاده عمیق.",
                            price = clampPrice(9_200_000L, budgetMin, budgetMax)
                        )
                    )
                    hasAnker -> listOf(
                        ProductRecommendation(
                            productName = "انکر Soundcore Select Pro",
                            keyFeatures = "توان خروجی پرقدرت ۳۰ وات، فناوری صوتی انحصاری BassUp، رقص نور حلقه‌ای جذاب، رادیاتور پسیو ارتعاشی",
                            imageUrl = "https://picsum.photos/seed/anksel/600/400",
                            reasoning = "اسپیکری بسیار مستحکم و با پرتاب صدای فوق العاده بم که برای دورهمی‌های چند نفره بیرون شهر عالی است.",
                            price = clampPrice(6_400_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "انکر Soundcore Motion Boom",
                            keyFeatures = "درایورهای ۱۰۰ درصد تیتانیوم خالص، بدنه شناور روی آب، باتری حجیم حاصل از پاوربانک‌های انکر، بند دستی محکم",
                            imageUrl = "https://picsum.photos/seed/ankboom/600/400",
                            reasoning = "اسپیکری منحصربه‌فرد که حتی در صورت سقوط در آب استخر روی آب شناور مانده و با کوبش عمیق خود موسیقی را قطع نمی‌کند.",
                            price = clampPrice(4_800_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "انکر Soundcore 3",
                            keyFeatures = "پخش صوتی فرکانسی بسیار بالا، عمر باتری ۲۴ ساعته فرای انتظار، بدنه ظریف سیلیکونی عایق، قیمت عالی",
                            imageUrl = "https://picsum.photos/seed/ankthree/600/400",
                            reasoning = "یک گزینه جیبی بادوام با تفکیک صوتی عالی برای شنیدن پادکست‌ها، موسیقی ملایم و تماشای ویدیو با لپ‌تاپ.",
                            price = clampPrice(2_800_000L, budgetMin, budgetMax)
                        )
                    )
                    else -> listOf(
                        ProductRecommendation(
                            productName = "اسپیکر قدرتمند $brandName Party Bass",
                            keyFeatures = "توان خروجی بالا به همراه رقص نور حلقه‌ای، باتری با دوام سفر، ورودی فیزیکی میکروفون کارهای کارائوکه، بیس عمیق ریتمیک",
                            imageUrl = "https://picsum.photos/seed/gensp1/600/400",
                            reasoning = "اسپیکر همه‌فن‌حریف پرقدرت برای شادی بخشیدن به مجالس و سفرها با برندی شیک و صدای طوفانی از مارک $brandName.",
                            price = clampPrice(14_500_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "اسپیکر قابل حمل $brandName Wave Studio",
                            keyFeatures = "پخش صوتی فرکانسی ۳۶۰ درجه، ساب‌ووفر ارتعاشی رو به پایین، گنبد بدنه کریستالی نوری متحرک، بلوتوث پایدار مدرن",
                            imageUrl = "https://picsum.photos/seed/gensp2/600/400",
                            reasoning = "ترکیبی فوق‌العاده چشم‌نواز از هنر مدرن نوری و کوبش صوتی بم که زیبایی اتاق شما را دوچندان می‌سازد از $brandName.",
                            price = clampPrice(8_500_000L, budgetMin, budgetMax)
                        ),
                        ProductRecommendation(
                            productName = "اسپیکر سفری $brandName Go Active",
                            keyFeatures = "طراحی جیبی مجهز به بند چرمی، گواهی مقاومت رطوبت بالا، شارژ فست با باتری بهینه، وزن سبک پروازگونه",
                            imageUrl = "https://picsum.photos/seed/gensp3/600/400",
                            reasoning = "یک همراه صوتی پرتابل بسیار مقاوم و دوست‌داشتنی برای پرتاب بیس و موسیقی زنده در حین حرکت از برند $brandName.",
                            price = clampPrice(3_200_000L, budgetMin, budgetMax)
                        )
                    )
                }
            }
        }
    }
}
