package com.sarangsho.plus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.sarangsho.plus.features.learning.*
import com.sarangsho.plus.features.reading.*
import com.sarangsho.plus.features.settings.*
import com.sarangsho.plus.ui.components.*
import com.sarangsho.plus.ui.theme.SarangshoTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*


private data class Page(val route: String, val title: String)
private val pages = listOf(
    Page("welcome", "স্বাগতম"), Page("signin", "সাইন ইন"), Page("home", "আজকের ব্রিফিং"), Page("learn", "শিখুন"),
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
    Page("feedback", "মতামত জানান"), Page("privacy", "গোপনীয়তা"), Page("language", "ভাষা"),
    Page("practice-history", "অনুশীলনের ইতিহাস"), Page("reading-history", "পড়ার ইতিহাস"),
    Page("saved-collections", "সংরক্ষিত সংগ্রহ"), Page("source-detail", "উৎসের পরিচিতি"),
    Page("indicator-detail", "সূচকের বিস্তারিত"), Page("signup", "নতুন অ্যাকাউন্ট"),
    Page("password-reset", "পাসওয়ার্ড পুনরুদ্ধার"), Page("report-story", "তথ্য সংশোধনের অনুরোধ")
)
private val tabs = listOf("home", "learn", "saved", "feed")
private val labels = listOf("আজ", "শিখুন", "লাইব্রেরি", "এক্সপ্লোর")
private val icons = listOf(Icons.Default.Home, Icons.Default.School, Icons.Default.Bookmark, Icons.Default.Explore)

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
    val title = pages.firstOrNull { it.route == route }?.title ?: "Sarangsho Plus"
    SarangshoTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                if (route != "welcome") CenterAlignedTopAppBar(
                    title = {
                        Text(
                            if (route == "home") "Sarangsho+" else title,
                            fontSize = if (route == "home") 20.sp else 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            when {
                                route == "home" -> nav.navigate("settings")
                                route in tabs -> nav.navigate("search")
                                else -> nav.popBackStack()
                            }
                        }) {
                            Icon(
                                when {
                                    route == "home" -> Icons.Default.Menu
                                    route in tabs -> Icons.Default.Search
                                    else -> Icons.AutoMirrored.Filled.ArrowBack
                                },
                                contentDescription = when {
                                    route == "home" -> "মেনু"
                                    route in tabs -> "অনুসন্ধান"
                                    else -> "পেছনে"
                                }
                            )
                        }
                    },
                    actions = {
                        if (route == "home") {
                            IconButton(onClick = { nav.navigate("notifications") }) {
                                Icon(Icons.Default.NotificationsNone, "নোটিফিকেশন")
                            }
                        }
                        if (route != "profile") {
                            IconButton(onClick = { nav.navigate("profile") }) {
                                Icon(Icons.Default.AccountCircle, "প্রোফাইল")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            bottomBar = {
                if (route in tabs) NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    tabs.forEachIndexed { index, target ->
                        NavigationBarItem(selected = route == target, onClick = { nav.navigate(target) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            } },
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
        "learn" -> LearningHub(nav)
        "feed" -> Feed(nav)
        "article" -> Article(nav)
        "summary" -> Summary(nav)
        "timeline" -> Timeline()
        "perspectives" -> Perspectives()
        "bangladesh" -> Topics(nav, false)
        "world" -> Topics(nav, true)
        "graph" -> Graph()
        "data" -> DataExplorer(nav)
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
        "sources" -> Sources(nav)
        "quiz-setup" -> QuizSetup(nav)
        "quiz-results" -> QuizResults(nav)
        "word-detail" -> WordDetail(nav)
        "study-plan" -> StudyPlan()
        "notifications" -> Notifications()
        "settings" -> Settings(nav)
        "reading-settings" -> ReadingSettings()
        "downloads" -> Downloads()
        "help" -> Help()
        "about" -> About()
        "feedback" -> Feedback()
        "privacy" -> Privacy()
        "language" -> LanguageSettings()
        "practice-history" -> PracticeHistory()
        "reading-history" -> ReadingHistory(nav)
        "saved-collections" -> SavedCollections()
        "source-detail" -> SourceDetail()
        "indicator-detail" -> IndicatorDetail()
        "signup" -> SignUp(nav)
        "password-reset" -> PasswordReset()
        "report-story" -> ReportStory()
    }
}
