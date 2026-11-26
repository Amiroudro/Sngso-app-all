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
import com.sarangsho.plus.navigation.AppDestination
import com.sarangsho.plus.features.learning.*
import com.sarangsho.plus.features.reading.*
import com.sarangsho.plus.features.settings.*
import com.sarangsho.plus.ui.components.*
import com.sarangsho.plus.ui.theme.SarangshoTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*


private data class Page(val destination: AppDestination, val title: String)
private val pages = listOf(
    Page(AppDestination.Welcome, "স্বাগতম"), Page(AppDestination.Signin, "সাইন ইন"), Page(AppDestination.Home, "আজকের ব্রিফিং"), Page(AppDestination.Learn, "শিখুন"),
    Page(AppDestination.Feed, "সব সংবাদ"), Page(AppDestination.Article, "সংবাদের বিস্তারিত"), Page(AppDestination.Summary, "তিন-স্তরের সারাংশ"),
    Page(AppDestination.Timeline, "ঘটনার টাইমলাইন"), Page(AppDestination.Perspectives, "পত্রিকার দৃষ্টিভঙ্গি"),
    Page(AppDestination.Bangladesh, "বাংলাদেশ বিষয়াবলি"), Page(AppDestination.World, "বিশ্ব বিষয়াবলি"), Page(AppDestination.Graph, "জ্ঞান মানচিত্র"),
    Page(AppDestination.Data, "তথ্য ও পরিসংখ্যান"), Page(AppDestination.Vocabulary, "Vocabulary Lab"), Page(AppDestination.Challenge, "BCS & Bank Challenge"),
    Page(AppDestination.Answer, "উত্তরের ব্যাখ্যা"), Page(AppDestination.Writing, "লিখিত অনুশীলন"), Page(AppDestination.Revision, "Revision Deck"),
    Page(AppDestination.Search, "অনুসন্ধান ও উৎস"), Page(AppDestination.Saved, "সংরক্ষিত"), Page(AppDestination.Profile, "আমার প্রোফাইল"),
    Page(AppDestination.OnboardingGoals, "আপনার লক্ষ্য"), Page(AppDestination.OnboardingInterests, "আপনার আগ্রহ"),
    Page(AppDestination.Archive, "পুরোনো সংস্করণ"), Page(AppDestination.Edition, "দৈনিক সংস্করণ"),
    Page(AppDestination.TopicDetail, "বিষয়ের পাঠ"), Page(AppDestination.Sources, "তথ্যসূত্র"),
    Page(AppDestination.QuizSetup, "অনুশীলন সাজান"), Page(AppDestination.QuizResults, "অনুশীলনের ফল"),
    Page(AppDestination.WordDetail, "শব্দের বিস্তারিত"), Page(AppDestination.StudyPlan, "পড়ার পরিকল্পনা"),
    Page(AppDestination.Notifications, "নোটিফিকেশন"), Page(AppDestination.Settings, "সেটিংস"),
    Page(AppDestination.ReadingSettings, "পড়ার পছন্দ"), Page(AppDestination.Downloads, "অফলাইন লাইব্রেরি"),
    Page(AppDestination.Help, "সহায়তা"), Page(AppDestination.About, "Sarangsho Plus সম্পর্কে"),
    Page(AppDestination.Feedback, "মতামত জানান"), Page(AppDestination.Privacy, "গোপনীয়তা"), Page(AppDestination.Language, "ভাষা"),
    Page(AppDestination.PracticeHistory, "অনুশীলনের ইতিহাস"), Page(AppDestination.ReadingHistory, "পড়ার ইতিহাস"),
    Page(AppDestination.SavedCollections, "সংরক্ষিত সংগ্রহ"), Page(AppDestination.SourceDetail, "উৎসের পরিচিতি"),
    Page(AppDestination.IndicatorDetail, "সূচকের বিস্তারিত"), Page(AppDestination.Signup, "নতুন অ্যাকাউন্ট"),
    Page(AppDestination.PasswordReset, "পাসওয়ার্ড পুনরুদ্ধার"), Page(AppDestination.ReportStory, "তথ্য সংশোধনের অনুরোধ")
)
private val tabs = listOf(AppDestination.Home, AppDestination.Learn, AppDestination.Saved, AppDestination.Feed)
private val labels = listOf("আজ", "শিখুন", "লাইব্রেরি", "এক্সপ্লোর")
private val icons = listOf(Icons.Default.Home, Icons.Default.School, Icons.Default.Bookmark, Icons.Default.Explore)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startDestination = AppDestination.fromDeepLink(intent?.dataString) ?: AppDestination.Welcome
        setContent { SarangshoApp(startDestination) }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SarangshoApp(startDestination: AppDestination) {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val destination = AppDestination.fromRoute(entry?.destination?.route) ?: AppDestination.Welcome
    val title = pages.firstOrNull { it.destination == destination }?.title ?: "Sarangsho Plus"
    SarangshoTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                if (destination != AppDestination.Welcome) CenterAlignedTopAppBar(
                    title = {
                        Text(
                            if (destination == AppDestination.Home) "Sarangsho+" else title,
                            fontSize = if (destination == AppDestination.Home) 20.sp else 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            when {
                                destination == AppDestination.Home -> nav.navigate(AppDestination.Settings.route)
                                destination in tabs -> nav.navigate(AppDestination.Search.route)
                                else -> nav.popBackStack()
                            }
                        }) {
                            Icon(
                                when {
                                    destination == AppDestination.Home -> Icons.Default.Menu
                                    destination in tabs -> Icons.Default.Search
                                    else -> Icons.AutoMirrored.Filled.ArrowBack
                                },
                                contentDescription = when {
                                    destination == AppDestination.Home -> "মেনু"
                                    destination in tabs -> "অনুসন্ধান"
                                    else -> "পেছনে"
                                }
                            )
                        }
                    },
                    actions = {
                        if (destination == AppDestination.Home) {
                            IconButton(onClick = { nav.navigate(AppDestination.Notifications.route) }) {
                                Icon(Icons.Default.NotificationsNone, "নোটিফিকেশন")
                            }
                        }
                        if (destination != AppDestination.Profile) {
                            IconButton(onClick = { nav.navigate(AppDestination.Profile.route) }) {
                                Icon(Icons.Default.AccountCircle, "প্রোফাইল")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            bottomBar = {
                if (destination in tabs) NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    tabs.forEachIndexed { index, target ->
                        NavigationBarItem(selected = destination == target, onClick = { nav.navigate(target.route) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            } },
                            icon = { Icon(icons[index], labels[index]) }, label = { Text(labels[index], fontSize = 10.sp) })
                    }
                }
            }
        ) { padding ->
            NavHost(nav, startDestination = startDestination.route, modifier = Modifier.padding(padding)) {
                pages.forEach { page -> composable(page.destination.route) { Screen(page.destination, nav) } }
            }
        }
    }
}

@Composable
private fun Screen(destination: AppDestination, nav: NavHostController) {
    when (destination) {
        AppDestination.Welcome -> Welcome(nav)
        AppDestination.Signin -> SignIn(nav)
        AppDestination.Home -> Home(nav)
        AppDestination.Learn -> LearningHub(nav)
        AppDestination.Feed -> Feed(nav)
        AppDestination.Article -> Article(nav)
        AppDestination.Summary -> Summary(nav)
        AppDestination.Timeline -> Timeline()
        AppDestination.Perspectives -> Perspectives()
        AppDestination.Bangladesh -> Topics(nav, false)
        AppDestination.World -> Topics(nav, true)
        AppDestination.Graph -> Graph()
        AppDestination.Data -> DataExplorer(nav)
        AppDestination.Vocabulary -> Vocabulary(nav)
        AppDestination.Challenge -> Challenge(nav)
        AppDestination.Answer -> Answer(nav)
        AppDestination.Writing -> Writing()
        AppDestination.Revision -> Revision()
        AppDestination.Search -> Search(nav)
        AppDestination.Saved -> Saved(nav)
        AppDestination.Profile -> Profile(nav)
        AppDestination.OnboardingGoals -> OnboardingGoals(nav)
        AppDestination.OnboardingInterests -> OnboardingInterests(nav)
        AppDestination.Archive -> Archive(nav)
        AppDestination.Edition -> Edition(nav)
        AppDestination.TopicDetail -> TopicDetail(nav)
        AppDestination.Sources -> Sources(nav)
        AppDestination.QuizSetup -> QuizSetup(nav)
        AppDestination.QuizResults -> QuizResults(nav)
        AppDestination.WordDetail -> WordDetail(nav)
        AppDestination.StudyPlan -> StudyPlan()
        AppDestination.Notifications -> Notifications()
        AppDestination.Settings -> Settings(nav)
        AppDestination.ReadingSettings -> ReadingSettings()
        AppDestination.Downloads -> Downloads()
        AppDestination.Help -> Help()
        AppDestination.About -> About()
        AppDestination.Feedback -> Feedback()
        AppDestination.Privacy -> Privacy()
        AppDestination.Language -> LanguageSettings()
        AppDestination.PracticeHistory -> PracticeHistory()
        AppDestination.ReadingHistory -> ReadingHistory(nav)
        AppDestination.SavedCollections -> SavedCollections(nav)
        AppDestination.SourceDetail -> SourceDetail()
        AppDestination.IndicatorDetail -> IndicatorDetail()
        AppDestination.Signup -> SignUp(nav)
        AppDestination.PasswordReset -> PasswordReset()
        AppDestination.ReportStory -> ReportStory()
    }
}
