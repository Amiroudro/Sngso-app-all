package com.sarangsho.plus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import com.sarangsho.plus.ui.theme.SarangshoPalette
import com.sarangsho.plus.ui.theme.SarangshoTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*


private data class Page(val route: String, val title: String)
private val pages = listOf(
    Page("welcome", "স্বাগতম"), Page("signin", "সাইন ইন"), Page("home", "আজকের ব্রিফিং"),
    Page("feed", "সব সংবাদ"), Page("article", "সংবাদের বিস্তারিত"), Page("summary", "তিন-স্তরের সারাংশ"),
    Page("timeline", "ঘটনার টাইমলাইন"), Page("perspectives", "পত্রিকার দৃষ্টিভঙ্গি"),
    Page("bangladesh", "বাংলাদেশ বিষয়াবলি"), Page("world", "বিশ্ব বিষয়াবলি"), Page("graph", "জ্ঞান মানচিত্র"),
    Page("data", "তথ্য ও পরিসংখ্যান"), Page("vocabulary", "Vocabulary Lab"), Page("challenge", "BCS & Bank Challenge"),
    Page("answer", "উত্তরের ব্যাখ্যা"), Page("writing", "লিখিত অনুশীলন"), Page("revision", "Revision Deck"),
    Page("search", "অনুসন্ধান ও উৎস"), Page("saved", "সংরক্ষিত"), Page("profile", "আমার প্রোফাইল"),
    Page("onboarding-goals", "আপনার লক্ষ্য"), Page("onboarding-interests", "আপনার আগ্রহ"),
    Page("archive", "পুরোনো সংস্করণ"), Page("edition", "দৈনিক সংস্করণ"),
    Page("topic-detail", "বিষয়ের পাঠ"), Page("sources", "তথ্যসূত্র"),
    Page("quiz-setup", "অনুশীলন সাজান"), Page("quiz-results", "অনুশীলনের ফল"),
    Page("word-detail", "শব্দের বিস্তারিত"), Page("study-plan", "পড়ার পরিকল্পনা"),
    Page("notifications", "নোটিফিকেশন"), Page("settings", "সেটিংস"),
    Page("reading-settings", "পড়ার পছন্দ"), Page("downloads", "অফলাইন লাইব্রেরি"),
    Page("help", "সহায়তা"), Page("about", "Sarangsho Plus সম্পর্কে"),
    Page("feedback", "মতামত জানান")
)
private val tabs = listOf("home", "feed", "challenge", "saved", "profile")
private val labels = listOf("হোম", "অনুসন্ধান", "প্র্যাকটিস", "সংরক্ষিত", "প্রোফাইল")
private val icons = listOf(Icons.Default.Home, Icons.Default.Explore, Icons.Default.EditNote, Icons.Default.Bookmark, Icons.Default.Person)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SarangshoApp() }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SarangshoApp() {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route ?: "welcome"
    val title = pages.first { it.route == route }.title
    SarangshoTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                if (route != "welcome") CenterAlignedTopAppBar(
                    title = { Text(title, fontSize = 17.sp, fontWeight = FontWeight.SemiBold) },
                    navigationIcon = {
                        IconButton(onClick = { if (route in tabs) nav.navigate("search") else nav.popBackStack() }) {
                            Icon(if (route in tabs) Icons.Default.Search else Icons.AutoMirrored.Filled.ArrowBack, "Back or search")
                        }
                    },
                    actions = { IconButton(onClick = { nav.navigate("profile") }) { Icon(Icons.Default.AccountCircle, "Profile") } },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SarangshoPalette.Paper)
                )
            },
            bottomBar = {
                if (route in tabs) NavigationBar(containerColor = Color.White) {
                    tabs.forEachIndexed { index, target ->
                        NavigationBarItem(selected = route == target, onClick = { nav.navigate(target) { launchSingleTop = true } },
                            icon = { Icon(icons[index], labels[index]) }, label = { Text(labels[index], fontSize = 10.sp) })
                    }
                }
            }
        ) { padding ->
            NavHost(nav, startDestination = "welcome", modifier = Modifier.padding(padding)) {
                pages.forEach { page -> composable(page.route) { Screen(page.route, nav) } }
            }
        }
    }
}

@Composable
private fun Screen(route: String, nav: NavHostController) {
    when (route) {
        "welcome" -> Welcome(nav)
        "signin" -> SignIn(nav)
        "home" -> Home(nav)
        "feed" -> Feed(nav)
        "article" -> Article(nav)
        "summary" -> Summary(nav)
        "timeline" -> Timeline()
        "perspectives" -> Perspectives()
        "bangladesh" -> Topics(nav, false)
        "world" -> Topics(nav, true)
        "graph" -> Graph()
        "data" -> DataExplorer()
        "vocabulary" -> Vocabulary(nav)
        "challenge" -> Challenge(nav)
        "answer" -> Answer(nav)
        "writing" -> Writing()
        "revision" -> Revision()
        "search" -> Search(nav)
        "saved" -> Saved(nav)
        "profile" -> Profile(nav)
        "onboarding-goals" -> OnboardingGoals(nav)
        "onboarding-interests" -> OnboardingInterests(nav)
        "archive" -> Archive(nav)
        "edition" -> Edition(nav)
        "topic-detail" -> TopicDetail(nav)
        "sources" -> Sources()
        "quiz-setup" -> QuizSetup(nav)
        "quiz-results" -> QuizResults(nav)
        "word-detail" -> WordDetail()
        "study-plan" -> StudyPlan()
        "notifications" -> Notifications()
        "settings" -> Settings(nav)
        "reading-settings" -> ReadingSettings()
        "downloads" -> Downloads()
        "help" -> Help()
        "about" -> About()
        "feedback" -> Feedback()
    }
}

@Composable
private fun Scroll(content: @Composable ColumnScope.() -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Column(verticalArrangement = Arrangement.spacedBy(14.dp), content = content) }
    }
}
@Composable private fun Intro(title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(title, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = SarangshoPalette.Ink)
        Text(body, fontSize = 14.sp, color = SarangshoPalette.Muted, lineHeight = 22.sp)
    }
}
@Composable private fun Kicker(text: String) { Text(text.uppercase(), fontSize = 10.sp, color = SarangshoPalette.Teal, fontWeight = FontWeight.Bold, letterSpacing = 1.sp) }
@Composable private fun Panel(content: @Composable ColumnScope.() -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(15.dp), border = BorderStroke(1.dp, SarangshoPalette.Line), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}
@Composable private fun Primary(text: String, action: () -> Unit) {
    Button(action, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = SarangshoPalette.Teal), contentPadding = PaddingValues(14.dp)) { Text(text, fontWeight = FontWeight.SemiBold) }
}
@Composable private fun RowLink(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector, action: () -> Unit) {
    Panel {
        Row(Modifier.clickable(onClick = action), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(color = SarangshoPalette.Soft, shape = RoundedCornerShape(10.dp)) { Icon(icon, null, tint = SarangshoPalette.Teal, modifier = Modifier.padding(10.dp).size(22.dp)) }
            Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.SemiBold); Text(detail, color = SarangshoPalette.Muted, fontSize = 12.sp) }
            Icon(Icons.Default.ChevronRight, null, tint = SarangshoPalette.Muted)
        }
    }
}
@Composable private fun Story(title: String, category: String, detail: String, nav: NavHostController) {
    Panel {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Kicker(category); Text("নমুনা", color = SarangshoPalette.Gold, fontSize = 10.sp) }
        Text(title, fontSize = 18.sp, lineHeight = 27.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(detail, color = SarangshoPalette.Muted, fontSize = 11.sp, modifier = Modifier.weight(1f))
            Text("পড়ুন →", color = SarangshoPalette.Teal, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { nav.navigate("article") })
        }
    }
}

@Composable private fun Welcome(nav: NavHostController) {
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(25.dp), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(8.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Surface(color = SarangshoPalette.Deep, shape = RoundedCornerShape(26.dp), modifier = Modifier.size(104.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.MenuBook, null, tint = Color.White, modifier = Modifier.size(52.dp)) }
            }
            Text("Sarangsho+", fontSize = 32.sp, color = SarangshoPalette.Deep, fontWeight = FontWeight.Bold)
            Text("সঠিক তথ্য। গভীর উপলব্ধি। প্রস্তুতিতে এগিয়ে যান।", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
            Text("বাংলাদেশ ও বিশ্বের গুরুত্বপূর্ণ ঘটনা বুঝুন, প্রেক্ষাপট জানুন এবং BCS ও Bank পরীক্ষার জন্য শিখুন।", color = SarangshoPalette.Muted, lineHeight = 25.sp)
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Primary("শুরু করুন") { nav.navigate("onboarding-goals") }
            OutlinedButton(onClick = { nav.navigate("signin") }, modifier = Modifier.fillMaxWidth()) { Text("সাইন ইন") }
            Text("নমুনা সংস্করণ • অ্যাকাউন্ট ছাড়াও দেখা যাবে", color = SarangshoPalette.Muted, fontSize = 11.sp)
        }
    }
}
@Composable private fun SignIn(nav: NavHostController) {
    var email by remember { mutableStateOf("") }; var password by remember { mutableStateOf("") }
    Scroll {
        Intro("আপনার পড়াশোনা সঙ্গে রাখুন", "অ্যাকাউন্টে প্রবেশ করুন অথবা আপাতত স্থানীয়ভাবে চালিয়ে যান।")
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("ইমেইল বা ফোন নম্বর") }, singleLine = true)
        OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(), label = { Text("পাসওয়ার্ড") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
        Primary("সাইন ইন") { nav.navigate("home") }
        Text("ডেমো UI: প্রকৃত authentication এখনো সংযুক্ত নয়।", color = SarangshoPalette.Muted, fontSize = 12.sp)
        OutlinedButton(onClick = { nav.navigate("home") }, modifier = Modifier.fillMaxWidth()) { Text("অ্যাকাউন্ট ছাড়া চালিয়ে যান") }
    }
}
@Composable private fun Home(nav: NavHostController) {
    Scroll {
        Kicker("দৈনিক ব্রিফিং • নমুনা সংস্করণ")
        Panel {
            Text("আজকের প্রস্তুতি", color = SarangshoPalette.Teal, fontWeight = FontWeight.Bold)
            Text("প্রতিদিন অল্প করে পড়ুন, নিয়মিত ফিরে দেখুন।", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text("৩টি বিষয় • ১৫ মিনিট", color = SarangshoPalette.Muted)
            Primary("আজকের অনুশীলন শুরু করুন") { nav.navigate("challenge") }
        }
        Text("আজকের প্রধান বিষয়", fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Story("আন্তর্জাতিক আদালতের বিচারিক প্রক্রিয়া ও রোহিঙ্গা সংকট", "আন্তর্জাতিক আইন", "৮ মিনিট • যাচাইযোগ্য উৎস প্রয়োজন", nav)
        Story("উপকূলীয় দুর্যোগ প্রস্তুতি ও সরকারি সতর্কতা", "বাংলাদেশ", "৪ মিনিট • সরকারি তথ্য", nav)
        Text("শিখুন ও অনুশীলন করুন", fontSize = 19.sp, fontWeight = FontWeight.Bold)
        RowLink("তিন-স্তরের সারাংশ", "৩০ সেকেন্ড, ২ মিনিট অথবা বিস্তারিত", Icons.Default.Subject) { nav.navigate("summary") }
        RowLink("ঘটনার টাইমলাইন", "তারিখ ও প্রেক্ষাপট আলাদা করে দেখুন", Icons.Default.Timeline) { nav.navigate("timeline") }
        RowLink("পত্রিকার দৃষ্টিভঙ্গি", "উপলভ্য ও অনুমোদিত উৎস তুলনা", Icons.Default.CompareArrows) { nav.navigate("perspectives") }
        RowLink("Vocabulary Lab", "শব্দ শিখুন, মনে রাখুন", Icons.Default.Translate) { nav.navigate("vocabulary") }
        Text("খবর ও অগ্রগতির সব তথ্য এই পর্যায়ে UI নমুনা; লাইভ ইঞ্জিন ডেটা নয়।", color = SarangshoPalette.Muted, fontSize = 11.sp)
    }
}
@Composable private fun Feed(nav: NavHostController) {
    var filter by remember { mutableStateOf("সব") }
    var query by remember { mutableStateOf("") }
    val stories = listOf(
        Triple("বাংলাদেশের অর্থনীতি: নীতি, মূল্যস্ফীতি ও প্রবৃদ্ধি", "অর্থনীতি", "৬ মিনিট"),
        Triple("জাতিসংঘ ও জলবায়ু আলোচনার সাম্প্রতিক অগ্রগতি", "বিশ্ব", "৫ মিনিট"),
        Triple("আন্তর্জাতিক আদালতের এখতিয়ার ও বিচারিক প্রক্রিয়া", "আইন ও বিচার", "৭ মিনিট")
    )
    val filtered = stories.filter { story ->
        val categoryMatches = filter == "সব" ||
            (filter == "বাংলাদেশ" && story.second != "বিশ্ব") ||
            (filter == "বিশ্ব" && story.second == "বিশ্ব") ||
            (filter == "অর্থনীতি" && story.second == "অর্থনীতি")
        val queryMatches = query.isBlank() || story.first.contains(query, ignoreCase = true)
        categoryMatches && queryMatches
    }
    Scroll {
        Intro("সংবাদ অনুসন্ধান", "বিষয় ও গুরুত্ব অনুযায়ী পড়ার তালিকা সাজান।")
        OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(),
            placeholder = { Text("শিরোনাম বা বিষয় খুঁজুন") },
            leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("সব", "বাংলাদেশ", "বিশ্ব", "অর্থনীতি").forEach {
                FilterChip(selected = filter == it, onClick = { filter = it }, label = { Text(it) })
            }
        }
        if (filtered.isEmpty()) {
            Panel {
                Text("কোনো নমুনা ফল পাওয়া যায়নি", fontWeight = FontWeight.SemiBold)
                Text("অন্য শব্দ লিখুন অথবা ফিল্টার বদলান।", color = SarangshoPalette.Muted)
            }
        } else {
            filtered.forEach { Story(it.first, it.second, it.third, nav) }
        }
        Text("এগুলো UI পরীক্ষার নমুনা শিরোনাম, লাইভ সংবাদ নয়।", color = SarangshoPalette.Muted, fontSize = 11.sp)
    }
}
@Composable private fun Article(nav: NavHostController) {
    Scroll {
        Kicker("আন্তর্জাতিক আইন • নমুনা নিবন্ধ")
        Text("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন", fontSize = 25.sp, fontWeight = FontWeight.Bold, lineHeight = 34.sp)
        Text("প্রকাশকাল ও হালনাগাদ সময়: উৎস-ডেটা সংযুক্ত হলে এখানে দেখানো হবে", color = SarangshoPalette.Muted, fontSize = 12.sp)
        Panel { Text("যাচাইয়ের অবস্থা", fontWeight = FontWeight.Bold); Text("প্রকৃত সংস্করণে প্রতিটি গুরুত্বপূর্ণ দাবির পাশে উৎস, তারিখ এবং যাচাইয়ের অবস্থা থাকবে।"); AssistChip(onClick = {}, enabled = false, label = { Text("প্রাথমিক উৎস") }) }
        RowLink("তিন-স্তরের সারাংশ", "দ্রুত ধারণা থেকে বিস্তারিত ব্যাখ্যা", Icons.Default.Subject) { nav.navigate("summary") }
        RowLink("ঘটনার টাইমলাইন", "তারিখ অনুযায়ী ঘটনাপ্রবাহ", Icons.Default.Timeline) { nav.navigate("timeline") }
        RowLink("পত্রিকার দৃষ্টিভঙ্গি", "শুধু অনুমোদিত ও পাওয়া উৎস", Icons.Default.CompareArrows) { nav.navigate("perspectives") }
        RowLink("জ্ঞান মানচিত্র", "প্রতিষ্ঠান ও ধারণার সম্পর্ক", Icons.Default.Hub) { nav.navigate("graph") }
        OutlinedButton(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.BookmarkBorder, null); Text("  সংরক্ষণ সুবিধা আসছে") }
    }
}
@Composable @OptIn(ExperimentalMaterial3Api::class)
private fun Summary(nav: NavHostController) {
    var level by remember { mutableIntStateOf(0) }
    val labels = listOf("৩০ সেকেন্ড", "২ মিনিট", "বিস্তারিত")
    val texts = listOf("ঘটনাটির মূল বিষয় এক বাক্যে বুঝুন।", "মূল সিদ্ধান্ত, সংশ্লিষ্ট প্রতিষ্ঠান এবং বাংলাদেশের জন্য সম্ভাব্য তাৎপর্য সংক্ষেপে পড়ুন।", "বিস্তারিত সংস্করণে পটভূমি, প্রক্রিয়া, মূল তথ্য, সংশ্লিষ্ট পক্ষের অবস্থান, অনিশ্চয়তা এবং পরীক্ষায় প্রাসঙ্গিক ধারণা থাকবে।")
    Scroll {
        Intro("একই ঘটনা, তিনভাবে বুঝুন", "আপনার হাতে যত সময় আছে, সেই অনুযায়ী পড়ুন।")
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) { labels.forEachIndexed { i, label -> SegmentedButton(level == i, { level = i }, SegmentedButtonDefaults.itemShape(i, labels.size)) { Text(label, fontSize = 11.sp) } } }
        Panel { Kicker(labels[level]); Text(texts[level], fontSize = 16.sp, lineHeight = 28.sp); Text("নমুনা লেখা • যাচাইকৃত সংবাদ সারাংশ নয়", color = SarangshoPalette.Muted, fontSize = 11.sp) }
        RowLink("টাইমলাইন", "ঘটনার পূর্বাপর", Icons.Default.Timeline) { nav.navigate("timeline") }
    }
}
@Composable private fun Timeline() {
    val events = listOf("ঐতিহাসিক পটভূমি", "আইনি প্রক্রিয়া", "বর্তমান পর্যায়", "পরবর্তী ধাপ")
    Scroll {
        Intro("ঘটনার ধারাবাহিকতা", "ঘটনার তারিখ, প্রকাশের তারিখ এবং পরবর্তী আপডেট আলাদা করে দেখানো হবে।")
        Panel { Text("টাইমলাইন নীতি", fontWeight = FontWeight.Bold); Text("শুধু যাচাইকৃত তারিখ ও ঘটনার সম্পর্ক দেখানো হবে। অনুমানকে নিশ্চিত ঘটনা হিসেবে দেখানো হবে না।") }
        events.forEachIndexed { i, event -> Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(color = SarangshoPalette.Teal, shape = RoundedCornerShape(50.dp)) { Text((i + 1).toString(), Modifier.padding(8.dp), color = Color.White) }
            Column { Text(event, fontWeight = FontWeight.Bold); Text("যাচাইকৃত ঘটনার বিবরণ ও তারিখ প্রয়োজন", color = SarangshoPalette.Muted, fontSize = 12.sp) }
        } }
    }
}
@Composable private fun Perspectives() {
    Scroll {
        Intro("একাধিক প্রকাশনার দৃষ্টিভঙ্গি", "প্রতিবেদনের ভাষা, গুরুত্ব, তথ্য ও অনুপস্থিত প্রসঙ্গ তুলনা করুন।")
        Panel { Icon(Icons.Default.Info, null, tint = SarangshoPalette.Gold); Text("এই নমুনা সংস্করণে কোনো সংবাদপত্রের নিবন্ধ সংগ্রহ বা তুলনা করা হয়নি।", fontWeight = FontWeight.SemiBold); Text("উৎসের অনুমতি, প্রবেশযোগ্যতা এবং মেটাডেটা যাচাই না হওয়া পর্যন্ত তুলনা ‘চালানো হয়নি’ হিসেবে থাকবে।", color = SarangshoPalette.Muted) }
        listOf("শিরোনাম ও ফ্রেমিং", "যাচাইযোগ্য তথ্য", "উদ্ধৃত বক্তব্য", "অনুপস্থিত বা অনিশ্চিত বিষয়").forEach { Panel { Text(it, fontWeight = FontWeight.SemiBold); Text("উৎস-নির্ভর তথ্য যুক্ত হলে এই অংশ পূরণ হবে।", color = SarangshoPalette.Muted, fontSize = 13.sp) } }
    }
}
@Composable private fun Topics(nav: NavHostController, world: Boolean) {
    val list = if (world) listOf("জাতিসংঘ ও আন্তর্জাতিক সংস্থা", "আন্তর্জাতিক আইন ও চুক্তি", "বিশ্ব অর্থনীতি", "জলবায়ু ও পরিবেশ", "দক্ষিণ এশিয়া", "ভূরাজনীতি") else listOf("সংবিধান ও সরকারব্যবস্থা", "অর্থনীতি ও ব্যাংকিং", "সমাজ ও উন্নয়ন", "পরিবেশ ও জলবায়ু", "শিক্ষা ও স্বাস্থ্য", "আইন ও বিচার")
    Scroll {
        Intro(if (world) "বিশ্ব বিষয়াবলি" else "বাংলাদেশ বিষয়াবলি", "বর্তমান ঘটনা, মৌলিক ধারণা ও পরীক্ষার প্রস্তুতি একসঙ্গে দেখুন।")
        list.forEach { RowLink(it, "বিষয়ভিত্তিক নোট, সাম্প্রতিক ঘটনা ও অনুশীলন", Icons.Default.Public) { nav.navigate("topic-detail") } }
    }
}
@Composable private fun Graph() {
    Scroll {
        Intro("জ্ঞান মানচিত্র", "একটি ঘটনার সঙ্গে যুক্ত প্রতিষ্ঠান, ব্যক্তি, আইন ও ধারণার সম্পর্ক দেখুন।")
        Panel {
            Box(Modifier.fillMaxWidth().height(190.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(color = SarangshoPalette.Deep, shape = RoundedCornerShape(18.dp)) { Text("মূল বিষয়", Modifier.padding(14.dp), color = Color.White, fontWeight = FontWeight.Bold) }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("প্রতিষ্ঠান", "আইন", "রাষ্ট্র").forEach { Surface(color = SarangshoPalette.Soft, shape = RoundedCornerShape(10.dp)) { Text(it, Modifier.padding(10.dp), fontSize = 12.sp) } } }
                    Surface(color = Color(0xFFF5E8D4), shape = RoundedCornerShape(18.dp)) { Text("প্রভাব ও প্রেক্ষাপট", Modifier.padding(12.dp)) }
                }
            }
            Text("চিত্রটি ইন্টারঅ্যাকটিভ গ্রাফের নমুনা; সম্পর্কের ডেটা এখনো যুক্ত হয়নি।", color = SarangshoPalette.Muted, fontSize = 11.sp)
        }
        Text("সম্পর্ক: কারণ • আইনি এখতিয়ার • সদস্যপদ • প্রভাব • সময়গত সম্পর্ক", color = SarangshoPalette.Muted)
    }
}
@Composable private fun DataExplorer() {
    Scroll {
        Intro("তথ্য ও পরিসংখ্যান", "সংখ্যার সঙ্গে একক, সময়কাল, সংজ্ঞা ও মূল উৎস দেখুন।")
        Panel { Kicker("ইন্ডিকেটর কার্ড • নমুনা"); Text("মূল্যস্ফীতি", fontSize = 20.sp, fontWeight = FontWeight.Bold); Text("সর্বশেষ মান: লাইভ ডেটা সংযুক্ত নয়", color = SarangshoPalette.Muted); HorizontalDivider(color = SarangshoPalette.Line); Text("সময়কাল: উল্লেখ করতে হবে"); Text("একক: শতাংশ"); Text("উৎস: সংশ্লিষ্ট সরকারি পরিসংখ্যান সংস্থা") }
        Panel {
            Text("তথ্য পড়ার নিয়ম", fontWeight = FontWeight.Bold)
            listOf("স্টক ও প্রবাহ আলাদা করুন", "নামমাত্র ও প্রকৃত মান গুলিয়ে ফেলবেন না", "পূর্বাভাস ও পর্যবেক্ষণ আলাদা রাখুন", "প্রাথমিক ও সংশোধিত উপাত্ত চিহ্নিত করুন").forEach { Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Check, null, tint = SarangshoPalette.Green); Spacer(Modifier.width(8.dp)); Text(it) } }
        }
    }
}
@Composable private fun Vocabulary(nav: NavHostController) {
    var reveal by remember { mutableStateOf(false) }
    Scroll {
        Intro("আজকের শব্দ", "শব্দের অর্থ, ব্যবহার ও পরীক্ষায় প্রয়োগ শিখুন।")
        Panel {
            Kicker("NOUN"); Text("Resilience", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = SarangshoPalette.Deep); Text("/rɪˈzɪliəns/ • উচ্চারণ", color = SarangshoPalette.Muted)
            if (reveal) {
                Text("বাংলা অর্থ: প্রতিকূলতা কাটিয়ে ওঠার সক্ষমতা", fontWeight = FontWeight.SemiBold)
                Text("Definition: The ability to recover quickly from difficulty.")
                Text("Synonym: toughness • Antonym: fragility", color = SarangshoPalette.Muted, fontSize = 12.sp)
                Text("Example: Communities need resilience after a natural disaster.")
            } else Text("অর্থ ও উদাহরণ দেখতে নিচের বোতাম চাপুন।", color = SarangshoPalette.Muted)
            Primary(if (reveal) "আবার লুকান" else "অর্থ দেখুন") { reveal = !reveal }
        }
        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.BookmarkBorder, null); Text("  শব্দটি সংরক্ষণ করুন") }
        listOf("Accountability", "Sustainable", "Mitigate", "Consensus").forEach { word ->
            Text(word, Modifier.fillMaxWidth().clickable { nav.navigate("word-detail") }.padding(vertical = 6.dp), fontWeight = FontWeight.Medium)
        }
    }
}
@Composable private fun Challenge(nav: NavHostController) {
    var selected by remember { mutableIntStateOf(-1) }
    Scroll {
        Kicker("BCS & BANK • নমুনা প্রশ্ন"); Intro("বাংলাদেশের সংবিধান", "প্রশ্ন ১ / ১২ • একটি উত্তর নির্বাচন করুন।")
        Panel {
            Text("বাংলাদেশের সংবিধান কবে গণপরিষদে গৃহীত হয়?", fontSize = 19.sp, fontWeight = FontWeight.Bold, lineHeight = 28.sp)
            listOf("৪ নভেম্বর ১৯৭২", "১৬ ডিসেম্বর ১৯৭২", "২৬ মার্চ ১৯৭১", "১০ জানুয়ারি ১৯৭২").forEachIndexed { i, option ->
                Surface(color = if (selected == i) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(10.dp), border = BorderStroke(1.dp, if (selected == i) SarangshoPalette.Teal else SarangshoPalette.Line), modifier = Modifier.fillMaxWidth().clickable { selected = i }) {
                    Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) { RadioButton(selected == i, { selected = i }); Text("Option " + (i + 1) + "  " + option) }
                }
            }
            Primary("উত্তর জমা দিন") { if (selected >= 0) nav.navigate("answer") }
            Text("নমুনা প্রশ্ন; প্রকৃত পরীক্ষার প্রশ্ন বা পূর্বাভাস নয়।", color = SarangshoPalette.Muted, fontSize = 11.sp)
        }
    }
}
@Composable private fun Answer(nav: NavHostController) {
    Scroll {
        Panel {
            Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.CheckCircle, null, tint = SarangshoPalette.Green); Spacer(Modifier.width(8.dp)); Text("উত্তর ব্যাখ্যা", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            Text("সঠিক উত্তর: ৪ নভেম্বর ১৯৭২", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("গণপরিষদে বাংলাদেশের সংবিধান ১৯৭২ সালের ৪ নভেম্বর গৃহীত হয় এবং ১৬ ডিসেম্বর ১৯৭২ কার্যকর হয়। গৃহীত হওয়ার তারিখ ও কার্যকর হওয়ার তারিখ আলাদা।")
            Text("পরীক্ষার কৌশল: ‘গৃহীত’, ‘স্বাক্ষরিত’ ও ‘কার্যকর’ শব্দের পার্থক্য খেয়াল করুন।", color = SarangshoPalette.Muted)
        }
        Primary("আরও অনুশীলন") { nav.navigate("challenge") }
        OutlinedButton(onClick = { nav.navigate("quiz-results") }, modifier = Modifier.fillMaxWidth()) { Text("সেশন শেষ করুন") }
        OutlinedButton(onClick = { nav.navigate("revision") }, modifier = Modifier.fillMaxWidth()) { Text("রিভিশনে যোগ করুন") }
    }
}
@Composable private fun Writing() {
    var draft by remember { mutableStateOf("") }
    Scroll {
        Intro("লিখিত অনুশীলন", "নিজের ভাষায় উত্তর লিখুন। খসড়া সংরক্ষণ এখনো সংযুক্ত নয়।")
        Panel { Kicker("বিশ্লেষণী প্রশ্ন"); Text("জলবায়ু পরিবর্তন বাংলাদেশের উন্নয়ন পরিকল্পনায় কী ধরনের চ্যালেঞ্জ তৈরি করে? উদাহরণসহ আলোচনা করুন।", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 28.sp) }
        OutlinedTextField(draft, { draft = it }, Modifier.fillMaxWidth().height(220.dp), label = { Text("আপনার উত্তর লিখুন") }, placeholder = { Text("ভূমিকা, মূল আলোচনা, উদাহরণ ও উপসংহার...") })
        Text(draft.length.toString() + " অক্ষর", color = SarangshoPalette.Muted, fontSize = 11.sp)
        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("খসড়া সংরক্ষণ শিগগিরই চালু হবে") }
    }
}
@Composable private fun Revision() {
    var flipped by remember { mutableStateOf(false) }
    Scroll {
        Intro("রিভিশন ডেক", "সক্রিয় স্মরণ দিয়ে শেখা বিষয় মনে রাখুন।")
        LinearProgressIndicator(progress = { 0.35f }, modifier = Modifier.fillMaxWidth(), color = SarangshoPalette.Teal, trackColor = SarangshoPalette.Line)
        Text("কার্ড ৭ / ২০ • নমুনা সেশন", color = SarangshoPalette.Muted, fontSize = 12.sp)
        Panel {
            Text(if (flipped) "উত্তর" else "প্রশ্ন", color = SarangshoPalette.Teal, fontWeight = FontWeight.Bold)
            Text(if (flipped) "জাতিসংঘ প্রতিষ্ঠিত হয় ১৯৪৫ সালের ২৪ অক্টোবর।" else "জাতিসংঘ কবে প্রতিষ্ঠিত হয়?", fontSize = 21.sp, fontWeight = FontWeight.Bold, lineHeight = 31.sp)
            Text(if (flipped) "২৪ অক্টোবর, United Nations Day." else "উত্তর দেখতে কার্ডে চাপ দিন।", color = SarangshoPalette.Muted)
            OutlinedButton(onClick = { flipped = !flipped }, modifier = Modifier.fillMaxWidth()) { Text(if (flipped) "প্রশ্ন দেখুন" else "উত্তর দেখুন") }
        }
    }
}
@Composable private fun Search(nav: NavHostController) {
    var query by remember { mutableStateOf("") }; var searched by remember { mutableStateOf(false) }
    Scroll {
        Intro("এক জায়গায় খুঁজুন", "সংবাদ, বিষয়, শব্দ, প্রতিষ্ঠান ও প্রকাশনার নাম লিখুন।")
        OutlinedTextField(query, { query = it; searched = false }, Modifier.fillMaxWidth(), placeholder = { Text("যেমন: মূল্যস্ফীতি, ICJ, resilience") }, leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true)
        Primary("খুঁজুন") { searched = query.isNotBlank() }
        if (searched) { Text("অনুসন্ধানের নমুনা ফলাফল", fontWeight = FontWeight.Bold); RowLink("বিষয়ভিত্তিক নোট", "প্রকৃত সূচি যুক্ত হলে ফলাফল আসবে", Icons.Default.Article) { nav.navigate("feed") } }
        else { Text("জনপ্রিয় অনুসন্ধান", fontWeight = FontWeight.Bold); listOf("বাংলাদেশ ব্যাংক", "সংবিধান", "জাতিসংঘ", "জলবায়ু অর্থায়ন").forEach { Text(it, Modifier.fillMaxWidth().clickable { query = it }.padding(vertical = 8.dp), color = SarangshoPalette.Teal) } }
        Text("প্রতিটি ফলাফলে প্রকাশক, মূল লিংক, প্রকাশের সময় ও প্রবেশযোগ্যতার অবস্থা দেখানো হবে।", color = SarangshoPalette.Muted)
    }
}
@Composable private fun Saved(nav: NavHostController) {
    Scroll {
        Intro("আপনার সংরক্ষিত বিষয়", "পরে পড়ার জন্য রাখা সংবাদ, শব্দ ও রিভিশন কার্ড।")
        Panel { Icon(Icons.Default.Bookmarks, null, tint = SarangshoPalette.Teal, modifier = Modifier.size(30.dp)); Text("এখনো কিছু সংরক্ষণ করা হয়নি", fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("সংবাদ বা শব্দের পাশে বুকমার্ক আইকনে চাপ দিলে এখানে পাওয়া যাবে।", color = SarangshoPalette.Muted); Primary("সংবাদ দেখুন") { nav.navigate("feed") } }
    }
}
@Composable private fun Profile(nav: NavHostController) {
    Scroll {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Surface(color = SarangshoPalette.Soft, shape = RoundedCornerShape(50.dp)) { Icon(Icons.Default.Person, null, tint = SarangshoPalette.Teal, modifier = Modifier.padding(16.dp).size(34.dp)) }
            Column { Text("পাঠক প্রোফাইল", fontSize = 20.sp, fontWeight = FontWeight.Bold); Text("স্থানীয় ডেমো • সাইন ইন করা হয়নি", color = SarangshoPalette.Muted, fontSize = 12.sp) }
        }
        Panel { Text("শেখার অগ্রগতি", fontWeight = FontWeight.Bold); Text("প্রকৃত ব্যবহার-ডেটা যুক্ত না হওয়া পর্যন্ত কোনো স্কোর বা ধারাবাহিকতার সংখ্যা দেখানো হবে না।", color = SarangshoPalette.Muted) }
        RowLink("আমার শব্দভান্ডার", "সংরক্ষিত শব্দ ও পুনরাবৃত্তি", Icons.Default.Translate) { nav.navigate("vocabulary") }
        RowLink("রিভিশন ডেক", "মনে রাখার অনুশীলন", Icons.Default.Style) { nav.navigate("revision") }
        RowLink("লিখিত অনুশীলন", "আপনার উত্তর ও খসড়া", Icons.Default.EditNote) { nav.navigate("writing") }
        RowLink("সাইন ইন", "অ্যাকাউন্ট ও সিঙ্ক", Icons.Default.Login) { nav.navigate("signin") }
        RowLink("নোটিফিকেশন", "আপডেট ও রিভিশন স্মরণ", Icons.Default.Notifications) { nav.navigate("notifications") }
        RowLink("সেটিংস ও পছন্দ", "ভাষা, ফন্ট, ডাউনলোড ও গোপনীয়তা", Icons.Default.Settings) { nav.navigate("settings") }
        RowLink("সহায়তা", "প্রশ্ন, সমস্যা ও মতামত", Icons.Default.HelpOutline) { nav.navigate("help") }
    }
}


@Composable private fun OnboardingGoals(nav: NavHostController) {
    var goal by remember { mutableStateOf("BCS প্রস্তুতি") }
    Scroll {
        Intro("আপনি কী অর্জন করতে চান?", "আপনার লক্ষ্য অনুযায়ী পড়ার অভিজ্ঞতা সাজানো হবে।")
        listOf("BCS প্রস্তুতি", "Bank recruitment", "বিশ্ববিদ্যালয় ও সাধারণ জ্ঞান", "প্রতিদিনের খবর বুঝে পড়া").forEach { option ->
            Surface(color = if (goal == option) MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, if (goal == option) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth().clickable { goal = option }) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = goal == option, onClick = { goal = option }); Text(option, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Primary("এগিয়ে যান") { nav.navigate("onboarding-interests") }
        Text("পছন্দ পরে সেটিংস থেকে বদলানো যাবে।", color = SarangshoPalette.Muted, fontSize = 12.sp)
    }
}

@Composable private fun OnboardingInterests(nav: NavHostController) {
    val interests = listOf("বাংলাদেশ", "আন্তর্জাতিক বিষয়", "অর্থনীতি ও ব্যাংকিং", "আইন ও সংবিধান", "বিজ্ঞান ও প্রযুক্তি", "পরিবেশ ও জলবায়ু", "সমাজ ও উন্নয়ন")
    val selected = remember { mutableStateListOf("বাংলাদেশ", "অর্থনীতি ও ব্যাংকিং") }
    Scroll {
        Intro("কোন বিষয়গুলো পড়তে চান?", "শুরুতে কয়েকটি বেছে নিন। পরে যেকোনো সময় পরিবর্তন করতে পারবেন।")
        interests.forEach { item ->
            Row(Modifier.fillMaxWidth().clickable { if (item in selected) selected.remove(item) else selected.add(item) }.padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = item in selected, onCheckedChange = { checked -> if (checked) selected.add(item) else selected.remove(item) }); Text(item)
            }
        }
        Primary("আমার ব্রিফিং দেখুন") { nav.navigate("home") }
        Text("এই পছন্দগুলো শুধু ডেমো সেশনে থাকে; অ্যাকাউন্টে সিঙ্ক হয় না।", color = SarangshoPalette.Muted, fontSize = 12.sp)
    }
}

@Composable private fun Archive(nav: NavHostController) {
    Scroll {
        Intro("দৈনিক সংস্করণ", "আগের দিনের ব্রিফিং খুঁজুন এবং পরে পড়ার জন্য খুলুন।")
        Panel { Text("সাম্প্রতিক সংস্করণ", fontWeight = FontWeight.Bold); Text("প্রকাশিত সংস্করণের তালিকা ইঞ্জিন থেকে যুক্ত হলে এখানে তারিখ, ভাষা ও অবস্থা দেখানো হবে।", color = SarangshoPalette.Muted) }
        RowLink("আজকের সংস্করণ", "লাইভ কনটেন্ট সংযুক্ত নয়", Icons.Default.Today) { nav.navigate("edition") }
        RowLink("গতকালের সংস্করণ", "আর্কাইভ ডেটা অপেক্ষমাণ", Icons.Default.CalendarMonth) { nav.navigate("edition") }
    }
}

@Composable private fun Edition(nav: NavHostController) {
    Scroll {
        Intro("দৈনিক ব্রিফিং", "একটি সংস্করণের সব পাঠ, অনুশীলন ও তথ্যসূত্র এক জায়গায়।")
        Panel { Text("সংস্করণের অবস্থা", fontWeight = FontWeight.Bold); Text("এই প্রোটোটাইপে কোনো প্রকাশিত সংস্করণ লোড হয়নি।", color = SarangshoPalette.Muted); AssistChip(onClick = {}, enabled = false, label = { Text("কনটেন্ট অপেক্ষমাণ") }) }
        RowLink("প্রধান সংবাদ", "সংস্করণ থেকে যাচাইকৃত গল্প", Icons.Default.Article) { nav.navigate("article") }
        RowLink("বাংলাদেশ বিষয়াবলি", "দেশের গুরুত্বপূর্ণ ঘটনা", Icons.Default.Flag) { nav.navigate("bangladesh") }
        RowLink("বিশ্ব বিষয়াবলি", "আন্তর্জাতিক প্রেক্ষাপট", Icons.Default.Public) { nav.navigate("world") }
        RowLink("আজকের অনুশীলন", "BCS ও Bank প্রশ্ন", Icons.Default.Quiz) { nav.navigate("quiz-setup") }
        RowLink("তথ্যসূত্র ও যাচাই", "উৎস, সময় ও প্রবেশযোগ্যতা", Icons.Default.Source) { nav.navigate("sources") }
    }
}

@Composable private fun TopicDetail(nav: NavHostController) {
    Scroll {
        Intro("বিষয়ভিত্তিক পাঠ", "একটি বিষয়ের মৌলিক ধারণা, সাম্প্রতিক ঘটনা ও পরীক্ষার সংযোগ একত্রে।")
        Panel { Text("মূল ধারণা", fontWeight = FontWeight.Bold); Text("বিষয়ভিত্তিক ব্যাখ্যা ও সংজ্ঞা যাচাইকৃত পাঠ্যসূত্র থেকে যুক্ত হবে।") }
        RowLink("সংশ্লিষ্ট সংবাদ", "এই বিষয়ের সাম্প্রতিক ঘটনা", Icons.Default.Article) { nav.navigate("feed") }
        RowLink("জ্ঞান মানচিত্র", "প্রতিষ্ঠান ও ধারণার সম্পর্ক", Icons.Default.Hub) { nav.navigate("graph") }
        RowLink("তথ্য ও পরিসংখ্যান", "উৎসসহ প্রাসঙ্গিক সূচক", Icons.Default.BarChart) { nav.navigate("data") }
        RowLink("অনুশীলন", "বিষয়ভিত্তিক প্রশ্ন", Icons.Default.EditNote) { nav.navigate("quiz-setup") }
    }
}

@Composable private fun Sources() {
    Scroll {
        Intro("উৎস ও যাচাই", "প্রতিটি তথ্য কোথা থেকে এসেছে এবং কীভাবে যাচাই হয়েছে তা দেখুন।")
        Panel { Text("উৎস-তথ্য", fontWeight = FontWeight.Bold); Text("প্রকৃত সংস্করণ যুক্ত হলে প্রকাশক, শিরোনাম, মূল URL, প্রকাশকাল, হালনাগাদ সময়, প্রবেশযোগ্যতা ও যাচাইয়ের অবস্থা দেখানো হবে।") }
        listOf("প্রাথমিক সরকারি নথি", "আদালত ও আইনগত নথি", "আন্তর্জাতিক সংস্থা", "সংবাদ প্রকাশনা").forEach {
            Panel { Text(it, fontWeight = FontWeight.SemiBold); Text("উৎস-রেজিস্ট্রি থেকে তথ্য আসবে", color = SarangshoPalette.Muted, fontSize = 12.sp) }
        }
    }
}

@Composable private fun QuizSetup(nav: NavHostController) {
    var exam by remember { mutableStateOf("BCS") }; var duration by remember { mutableStateOf("১৫ মিনিট") }
    Scroll {
        Intro("অনুশীলন সাজান", "আপনার প্রস্তুতির ধরন অনুযায়ী প্রশ্ন বেছে নিন।")
        Text("পরীক্ষা", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("BCS", "Bank", "Mixed").forEach { FilterChip(selected = exam == it, onClick = { exam = it }, label = { Text(it) }) } }
        Text("সময়", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("৫ মিনিট", "১৫ মিনিট", "৩০ মিনিট").forEach { FilterChip(selected = duration == it, onClick = { duration = it }, label = { Text(it) }) } }
        Panel { Text("আপনার নির্বাচন", fontWeight = FontWeight.Bold); Text(exam + " • " + duration); Text("প্রশ্নের সংখ্যা ও বিষয়বস্তু প্রকৃত প্রশ্নব্যাংক যুক্ত হলে নির্ধারিত হবে।", color = SarangshoPalette.Muted) }
        Primary("অনুশীলন শুরু করুন") { nav.navigate("challenge") }
    }
}

@Composable private fun QuizResults(nav: NavHostController) {
    Scroll {
        Intro("অনুশীলন সম্পন্ন", "এই স্ক্রিনটি ফলাফল উপস্থাপনের কাঠামো।")
        Panel { Text("ফলাফল এখনো তৈরি হয়নি", fontWeight = FontWeight.Bold, fontSize = 20.sp); Text("প্রশ্ন-উত্তর যাচাই, স্কোরিং ও সেশন সংরক্ষণ যুক্ত না হওয়া পর্যন্ত কোনো নম্বর বা পারফরম্যান্স দেখানো হবে না।", color = SarangshoPalette.Muted) }
        Primary("আবার অনুশীলন করুন") { nav.navigate("quiz-setup") }
        OutlinedButton(onClick = { nav.navigate("revision") }, modifier = Modifier.fillMaxWidth()) { Text("রিভিশন ডেক খুলুন") }
    }
}

@Composable private fun WordDetail() {
    Scroll {
        Intro("শব্দের ব্যবহার", "শব্দের অর্থের পাশাপাশি বাক্যে ব্যবহার ও পরীক্ষায় প্রয়োগ বুঝুন।")
        Panel {
            Text("Resilience", fontSize = 28.sp, fontWeight = FontWeight.Bold); Text("/rɪˈzɪliəns/ • noun", color = SarangshoPalette.Muted)
            Text("বাংলা অর্থ: প্রতিকূলতা কাটিয়ে ওঠার সক্ষমতা")
            Text("Definition: The ability to recover quickly from difficulty.")
            Text("Synonyms: toughness, adaptability"); Text("Antonym: fragility")
            Text("Collocation: community resilience, economic resilience")
            Text("Example: Resilience helps communities recover after disasters.")
        }
        Text("এই শব্দের ডেটা নমুনা; অভিধান-উৎস যুক্ত হলে উৎসের তথ্য দেখানো হবে।", color = SarangshoPalette.Muted, fontSize = 12.sp)
    }
}

@Composable private fun StudyPlan() {
    val tasks = listOf("আজকের ব্রিফিং পড়া", "১০টি MCQ অনুশীলন", "৫টি নতুন শব্দ শেখা", "আগের ভুলগুলো রিভিশন")
    val done = remember { mutableStateListOf<String>() }
    Scroll {
        Intro("আপনার পড়ার পরিকল্পনা", "ছোট, বাস্তবসম্মত সেশন দিয়ে ধারাবাহিকতা তৈরি করুন।")
        Panel {
            Text("আজকের কাজ", fontWeight = FontWeight.Bold)
            tasks.forEach { task ->
                Row(Modifier.fillMaxWidth().clickable { if (task in done) done.remove(task) else done.add(task) }.padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = task in done, onCheckedChange = { checked -> if (checked) done.add(task) else done.remove(task) }); Text(task)
                }
            }
            Text(done.size.toString() + " / " + tasks.size + " সম্পন্ন", color = SarangshoPalette.Muted)
        }
        Text("ডেমো পরিকল্পনা: অগ্রগতি এই স্ক্রিনে থাকে, স্থায়ীভাবে সংরক্ষিত হয় না।", color = SarangshoPalette.Muted, fontSize = 12.sp)
    }
}

@Composable private fun Notifications() {
    Scroll {
        Intro("নোটিফিকেশন", "নতুন সংস্করণ, ডাউনলোড ও রিভিশনের আপডেট এখানে দেখা যাবে।")
        Panel { Icon(Icons.Default.NotificationsNone, null, tint = SarangshoPalette.Teal, modifier = Modifier.size(30.dp)); Text("এখনো কোনো নোটিফিকেশন নেই", fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("নতুন আপডেট এলে এখানে তারিখ ও প্রয়োজনীয় পদক্ষেপসহ দেখানো হবে।", color = SarangshoPalette.Muted) }
    }
}

@Composable private fun Settings(nav: NavHostController) {
    Scroll {
        Intro("সেটিংস", "অ্যাপের ব্যবহার, পড়া ও ব্যক্তিগত তথ্য নিয়ন্ত্রণ করুন।")
        RowLink("পড়ার পছন্দ", "ফন্ট, আকার, লাইন স্পেসিং ও থিম", Icons.Default.TextFields) { nav.navigate("reading-settings") }
        RowLink("অফলাইন লাইব্রেরি", "ডাউনলোড ও স্টোরেজ", Icons.Default.Download) { nav.navigate("downloads") }
        RowLink("নোটিফিকেশন", "কোন আপডেট পাবেন", Icons.Default.Notifications) { nav.navigate("notifications") }
        RowLink("গোপনীয়তা", "ডেটা ও অ্যাকাউন্ট নিয়ন্ত্রণ", Icons.Default.PrivacyTip) { nav.navigate("help") }
        RowLink("সহায়তা ও মতামত", "সমস্যা জানান বা প্রশ্ন করুন", Icons.Default.SupportAgent) { nav.navigate("help") }
        RowLink("অ্যাপ সম্পর্কে", "সংস্করণ ও প্রকল্পের তথ্য", Icons.Default.Info) { nav.navigate("about") }
    }
}

@Composable private fun ReadingSettings() {
    var fontSize by remember { mutableFloatStateOf(18f) }; var lineSpacing by remember { mutableFloatStateOf(1.5f) }
    var sepia by remember { mutableStateOf(false) }
    Scroll {
        Intro("পড়ার পছন্দ", "দীর্ঘ লেখা আরাম করে পড়ার জন্য নিজের মতো সাজান।")
        Panel {
            Text("লেখার আকার: " + fontSize.toInt().toString() + " sp", fontWeight = FontWeight.SemiBold)
            Slider(value = fontSize, onValueChange = { fontSize = it }, valueRange = 16f..26f)
            Text("লাইন স্পেসিং: " + "%.1f".format(lineSpacing), fontWeight = FontWeight.SemiBold)
            Slider(value = lineSpacing, onValueChange = { lineSpacing = it }, valueRange = 1.2f..2.0f)
            Row(verticalAlignment = Alignment.CenterVertically) { Text("উষ্ণ কাগজের ব্যাকগ্রাউন্ড", Modifier.weight(1f)); Switch(checked = sepia, onCheckedChange = { sepia = it }) }
        }
        Panel { Text("নমুনা পাঠ", fontSize = fontSize.sp, lineHeight = (fontSize * lineSpacing).sp); Text("বাংলা ও ইংরেজি একসঙ্গে পড়ার সময় অক্ষরের গঠন এবং লাইনের ফাঁক যেন পরিষ্কার থাকে।", fontSize = fontSize.sp, lineHeight = (fontSize * lineSpacing).sp) }
        Text("এই পছন্দগুলো এখনো স্থায়ীভাবে সংরক্ষিত হয় না।", color = SarangshoPalette.Muted, fontSize = 12.sp)
    }
}

@Composable private fun Downloads() {
    Scroll {
        Intro("অফলাইন লাইব্রেরি", "ডাউনলোড করা সংস্করণ নেটওয়ার্ক ছাড়াও পড়ুন।")
        Panel { Icon(Icons.Default.CloudOff, null, tint = SarangshoPalette.Teal, modifier = Modifier.size(30.dp)); Text("কোনো সংস্করণ ডাউনলোড করা নেই", fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("ডাউনলোড সুবিধা যুক্ত হলে সংরক্ষিত সংস্করণ, ফাইলের আকার, শেষ সিঙ্ক ও স্টোরেজ ব্যবহার এখানে দেখানো হবে।", color = SarangshoPalette.Muted) }
    }
}

@Composable private fun Help() {
    val questions = listOf(
        "সংবাদ কীভাবে যাচাই করা হয়?" to "প্রতিটি গল্পে উৎস, তারিখ এবং যাচাইয়ের অবস্থা দেখানো হবে।",
        "ইন্টারনেট ছাড়া পড়া যাবে?" to "অফলাইন ডাউনলোড ও ক্যাশিং সুবিধা তৈরি হলে সংরক্ষিত সংস্করণ পড়া যাবে।",
        "BCS ও Bank প্রস্তুতি কীভাবে সাজাব?" to "প্র্যাকটিস, শব্দভান্ডার, লিখিত অনুশীলন ও রিভিশন একত্রে ব্যবহার করুন."
    )
    val expanded = remember { mutableStateListOf<String>() }
    Scroll {
        Intro("সহায়তা কেন্দ্র", "সাধারণ প্রশ্নের উত্তর ও সমস্যা সমাধানের পথ।")
        questions.forEach { (question, answer) ->
            Panel {
                Row(Modifier.fillMaxWidth().clickable { if (question in expanded) expanded.remove(question) else expanded.add(question) }, verticalAlignment = Alignment.CenterVertically) {
                    Text(question, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    Icon(if (question in expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null)
                }
                if (question in expanded) Text(answer, color = SarangshoPalette.Muted, lineHeight = 22.sp)
            }
        }
    }
}

@Composable private fun About() {
    Scroll {
        Intro("Sarangsho Plus", "সংবাদকে বোঝা, শেখা ও মনে রাখার একটি বাংলা-প্রথম প্ল্যাটফর্ম।")
        Panel { Text("সংস্করণ 0.1.0 • প্রোটোটাইপ", fontWeight = FontWeight.Bold); Text("এই অ্যাপের লক্ষ্য হলো যাচাইকৃত বর্তমান ঘটনাকে প্রেক্ষাপট, ধারণা, তথ্যসূত্র ও পরীক্ষার অনুশীলনের সঙ্গে যুক্ত করা।") }
        Text("স্বাধীনভাবে যাচাই না করা পর্যন্ত কোনো সংবাদ, সংখ্যা বা উৎসকে লাইভ বলে উপস্থাপন করা হবে না।", color = SarangshoPalette.Muted)
    }
}

@Composable private fun Feedback() {
    var message by remember { mutableStateOf("") }; var category by remember { mutableStateOf("সাধারণ মতামত") }
    Scroll {
        Intro("আপনার মতামত", "কোন জায়গাটি কাজ করছে না বা কী উন্নত করা দরকার, লিখে জানান।")
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { listOf("সাধারণ মতামত", "সমস্যা", "ভুল তথ্য").forEach { FilterChip(selected = category == it, onClick = { category = it }, label = { Text(it) }) } }
        OutlinedTextField(message, { message = it }, Modifier.fillMaxWidth().height(180.dp), label = { Text("বিস্তারিত লিখুন") }, placeholder = { Text("কী ঘটেছে, কোন স্ক্রিনে, কী আশা করেছিলেন...") })
        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("পাঠানো শিগগিরই চালু হবে") }
        Text("মতামত পাঠানোর সার্ভিস এখনো সংযুক্ত নয়; এই খসড়া কোথাও পাঠানো হবে না।", color = SarangshoPalette.Muted, fontSize = 12.sp)
    }
}
