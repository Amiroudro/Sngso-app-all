package com.sarangsho.plus.features.reading

import com.sarangsho.plus.navigation.AppDestination
import com.sarangsho.plus.ui.components.*

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.sarangsho.plus.data.LocalLearningStore
import com.sarangsho.plus.data.edition.EditionFeedRepository
import com.sarangsho.plus.data.ReaderPreferencesRepository
import com.sarangsho.plus.data.ReaderSettings
import com.sarangsho.plus.features.learning.searchVocabulary
import androidx.navigation.compose.rememberNavController
import com.sarangsho.plus.ui.theme.SarangshoTheme
import androidx.navigation.NavHostController
import com.sarangsho.plus.ui.theme.SarangshoPalette


private data class FeedStory(
    val title: String,
    val category: String,
    val detail: String,
    val hasFullArticle: Boolean = false
)

private data class ReaderPreferences(
    val fontSize: androidx.compose.ui.unit.TextUnit,
    val lineHeight: androidx.compose.ui.unit.TextUnit,
    val sepia: Boolean
)

/** Observe persisted reader controls so settings changes update open reading screens. */
@Composable
private fun rememberReaderPreferences(context: Context): ReaderPreferences {
    val settings by ReaderPreferencesRepository.observe(context).collectAsState(initial = ReaderSettings())
    return ReaderPreferences(
        fontSize = settings.fontSize.sp,
        lineHeight = (settings.fontSize * settings.lineSpacing).sp,
        sepia = settings.sepia
    )
}


@Composable internal fun Welcome(nav: NavHostController) {
    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Kicker("EDITORIAL INTELLIGENCE")
            Text("বাংলা • English", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Surface(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(30.dp), modifier = Modifier.size(112.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(58.dp))
                }
            }
            Text("Sarangsho+", fontSize = 34.sp, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.ExtraBold)
            Text("জানুন, শিখুন, এগিয়ে যান", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
            Text(
                "বাংলাদেশ ও বিশ্বের গুরুত্বপূর্ণ ঘটনা বুঝুন। নির্ভরযোগ্য উৎস, সহজ ব্যাখ্যা এবং BCS ও Bank প্রস্তুতি এক জায়গায়।",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 25.sp,
                textAlign = TextAlign.Center
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("দৈনিক ব্রিফিং", "শব্দভান্ডার", "পরীক্ষা প্রস্তুতি").forEach {
                    Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(50.dp)) {
                        Text(it, Modifier.padding(horizontal = 10.dp, vertical = 7.dp), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Primary("আমার প্রস্তুতি সাজাই") { nav.navigate(AppDestination.OnboardingGoals.route) }
            OutlinedButton(onClick = { nav.navigate(AppDestination.Signin.route) }, modifier = Modifier.fillMaxWidth()) { Text("আগে থেকেই অ্যাকাউন্ট আছে? সাইন ইন") }
            Text("অ্যাকাউন্ট ছাড়াও নমুনা সংস্করণ দেখা যাবে", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }
    }
}

@Composable internal fun SignIn(nav: NavHostController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    Scroll {
        Kicker("SARANGSHO PLUS")
        Intro("আবার স্বাগতম", "আপনার পড়া, সংরক্ষিত শব্দ ও শেখার পরিকল্পনায় ফিরে আসুন।")
        Panel {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(11.dp))
                }
                Column {
                    Text("নিরাপদে সাইন ইন", fontWeight = FontWeight.Bold)
                    Text("ইমেইল বা ফোন নম্বর ব্যবহার করুন", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                }
            }
        }
        OutlinedTextField(
            value = email, onValueChange = { email = it.trimStart() }, modifier = Modifier.fillMaxWidth(),
            label = { Text("ইমেইল বা ফোন নম্বর") }, leadingIcon = { Icon(Icons.Default.PersonOutline, null) },
            singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth(),
            label = { Text("পাসওয়ার্ড") }, leadingIcon = { Icon(Icons.Default.Lock, null) },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { IconButton(onClick = { showPassword = !showPassword }) { Icon(if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility, "পাসওয়ার্ড দেখুন") } },
            singleLine = true
        )
        Text(
            "প্রকৃত অ্যাকাউন্ট-সেবা এখনো যুক্ত হয়নি। তাই এই ফর্ম কোনো লগইন অনুরোধ পাঠায় না।",
            color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, lineHeight = 17.sp
        )
        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("সাইন ইন সেবা সংযুক্তির অপেক্ষায়") }
        OutlinedButton(onClick = { nav.navigate(AppDestination.Home.route) }, modifier = Modifier.fillMaxWidth()) { Text("অ্যাকাউন্ট ছাড়া চালিয়ে যান") }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = { nav.navigate(AppDestination.Signup.route) }) { Text("নতুন অ্যাকাউন্ট তৈরি") }
            TextButton(onClick = { nav.navigate(AppDestination.PasswordReset.route) }) { Text("পাসওয়ার্ড ভুলে গেছেন?") }
        }
    }
}


private data class PublishedFeedUiState(
    val snapshot: EditionFeedRepository.Snapshot?,
    val loading: Boolean,
    val failed: Boolean,
    val refresh: () -> Unit
)

@Composable
private fun rememberPublishedFeed(): PublishedFeedUiState {
    val context = LocalContext.current
    var snapshot by remember { mutableStateOf(EditionFeedRepository.cached(context)) }
    var loading by remember { mutableStateOf(snapshot == null || snapshot.stale) }
    var failed by remember { mutableStateOf(false) }
    var refreshToken by remember { mutableIntStateOf(0) }
    LaunchedEffect(refreshToken) {
        val cached = EditionFeedRepository.cached(context)
        if (cached != null && !cached.stale && refreshToken == 0) {
            snapshot = cached
            loading = false
            return@LaunchedEffect
        }
        loading = true
        failed = false
        try {
            snapshot = EditionFeedRepository.refresh(context)
        } catch (_: Exception) {
            snapshot = EditionFeedRepository.cached(context)
            failed = true
        } finally {
            loading = false
        }
    }
    val refresh = remember { { refreshToken += 1 } }
    return PublishedFeedUiState(snapshot, loading, failed, refresh)
}

private fun categoryLabel(category: String): String = when (category) {
    "Bangladesh" -> "বাংলাদেশ"
    "Economy & Banking" -> "অর্থনীতি ও ব্যাংকিং"
    "World" -> "বিশ্ব"
    "Editorial & Opinion" -> "সম্পাদকীয় ও মতামত"
    else -> category
}


@Composable internal fun Home(nav: NavHostController) {
    val context = LocalContext.current
    val feed = rememberPublishedFeed()
    val snapshot = feed.snapshot
    val learningGoal = LocalLearningStore.getOnboardingGoal(context)
    val learningInterests = LocalLearningStore.getOnboardingInterests(context).sorted()
    Scroll {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("সারাংশ+", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Text("পড়ুন • বুঝুন • প্রস্তুতি নিন", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(50.dp)) {
                Text("DAILY BRIEFING", Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontSize = 9.sp,
                    fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Intro("আজকের ব্রিফিং", "প্রকাশিত দৈনিক সংস্করণ থেকে সর্বশেষ সংবাদ ও পরীক্ষার উপযোগী ব্যাখ্যা।")
        Panel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Kicker("প্রকাশিত সংস্করণ")
                    Text(snapshot?.let { "সংস্করণ ${it.date}" } ?: "দৈনিক সংবাদ", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(snapshot?.coverage ?: "প্রকাশিত সংস্করণ লোড হচ্ছে", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                }
                IconButton(onClick = feed.refresh, enabled = !feed.loading) {
                    Icon(Icons.Default.Refresh, contentDescription = "সংবাদ হালনাগাদ করুন")
                }
            }
            if (feed.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
            if (snapshot?.stale == true) Text("ইন্টারনেট সংযোগ না থাকায় সংরক্ষিত সংস্করণ দেখানো হচ্ছে।", color = MaterialTheme.colorScheme.secondary, fontSize = 11.sp)
            if (feed.failed && snapshot == null) Text("সংবাদ লোড করা যায়নি। ইন্টারনেট সংযোগ পরীক্ষা করে আবার চেষ্টা করুন।", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }
        Surface(
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Kicker("আপনার প্রস্তুতি")
                Text(learningGoal, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    if (learningInterests.isEmpty()) "আপনার আগ্রহের বিষয় বেছে নিন।"
                    else "আপনার বিষয়: ${learningInterests.joinToString(" • ")}",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.82f), fontSize = 12.sp, lineHeight = 19.sp
                )
                Button(onClick = { nav.navigate(AppDestination.OnboardingGoals.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary, contentColor = MaterialTheme.colorScheme.onSecondary)) {
                    Text("পছন্দ পরিবর্তন করুন")
                    Spacer(Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(16.dp))
                }
            }
        }
        Text("আজকের গুরুত্বপূর্ণ প্রতিবেদন", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        if (snapshot == null) {
            Panel {
                if (feed.loading) CircularProgressIndicator()
                Text(if (feed.loading) "প্রকাশিত সংবাদ আনা হচ্ছে…" else "এখনো কোনো সংস্করণ পাওয়া যায়নি", fontWeight = FontWeight.SemiBold)
                Text("সংযোগ ফিরে এলে আবার চেষ্টা করুন।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                TextButton(onClick = feed.refresh) { Text("আবার চেষ্টা করুন") }
            }
        } else {
            snapshot.stories.take(4).forEachIndexed { index, story ->
                Panel {
                    Kicker("${index + 1}. ${categoryLabel(story.category)}")
                    Text(story.headline, fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold)
                    Text(story.oneLiner.ifBlank { story.summary }, fontSize = 13.sp, lineHeight = 21.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${story.sources.size}টি তথ্যসূত্র", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                    Primary("প্রতিবেদনটি পড়ুন") {
                        EditionFeedRepository.selectStory(context, story.id)
                        nav.navigate(AppDestination.Article.route)
                    }
                }
            }
        }
        RowLink("সব প্রতিবেদন দেখুন", "প্রকাশিত সংস্করণের সব সংবাদ", Icons.AutoMirrored.Filled.ArrowForward) { nav.navigate(AppDestination.Feed.route) }
        RowLink("পুরোনো সংস্করণ", "আগের দিনের ব্রিফিং ও প্রকাশনা", Icons.Default.CalendarMonth) { nav.navigate(AppDestination.Archive.route) }
        Text("সংবাদ থেকে শেখা", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Text("একটি ঘটনার পেছনের তথ্য, সময়রেখা ও উৎস আলাদা করে দেখুন.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        RowLink("তিন-স্তরের সারাংশ", "৩০ সেকেন্ড, ২ মিনিট অথবা বিস্তারিত", Icons.AutoMirrored.Filled.Subject) { nav.navigate(AppDestination.Summary.route) }
        RowLink("ঘটনার টাইমলাইন", "তারিখ ও প্রেক্ষাপট আলাদা করে দেখুন", Icons.Default.Timeline) { nav.navigate(AppDestination.Timeline.route) }
        RowLink("পত্রিকার দৃষ্টিভঙ্গি", "উপলভ্য ও অনুমোদিত উৎস তুলনা", Icons.AutoMirrored.Filled.CompareArrows) { nav.navigate(AppDestination.Perspectives.route) }
        RowLink("তথ্য ও পরিসংখ্যান", "সংজ্ঞা, একক, সময়কাল ও মূল উৎস", Icons.Default.BarChart) { nav.navigate(AppDestination.Data.route) }
        Text("প্রস্তুতি চালিয়ে যান", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Text("পড়া বিষয়কে শব্দভান্ডার ও পরীক্ষার অনুশীলনের সঙ্গে যুক্ত করুন.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        RowLink("Vocabulary Lab", "শব্দ, অর্থ, collocation ও ব্যবহার", Icons.Default.Translate) { nav.navigate(AppDestination.Vocabulary.route) }
        RowLink("BCS ও Bank অনুশীলন", "প্রশ্নের ধরন বেছে প্রস্তুতি নিন", Icons.Default.EditNote) { nav.navigate(AppDestination.QuizSetup.route) }
        RowLink("পড়ার পরিকল্পনা", "নিজের সময় অনুযায়ী প্রস্তুতি", Icons.Default.CalendarToday) { nav.navigate(AppDestination.StudyPlan.route) }
    }
}


@Composable internal fun Feed(nav: NavHostController) {
    val context = LocalContext.current
    val feed = rememberPublishedFeed()
    var selectedFilter by rememberSaveable { mutableStateOf("সব") }
    var query by rememberSaveable { mutableStateOf("") }
    val stories = feed.snapshot?.stories.orEmpty()
    val filtered = stories.filter { story ->
        val categoryMatches = when (selectedFilter) {
            "বাংলাদেশ" -> story.category in listOf("Bangladesh", "Economy & Banking")
            "বিশ্ব" -> story.category == "World"
            "অর্থনীতি" -> story.category == "Economy & Banking"
            else -> true
        }
        val queryMatches = query.isBlank() ||
            story.headline.contains(query, ignoreCase = true) ||
            story.summary.contains(query, ignoreCase = true) ||
            categoryLabel(story.category).contains(query, ignoreCase = true)
        categoryMatches && queryMatches
    }
    Scroll {
        Intro("সকল সংবাদ", "প্রকাশিত দৈনিক সংস্করণের প্রতিবেদন, ব্যাখ্যা ও মূল তথ্যসূত্র।")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(feed.snapshot?.let { "সংস্করণ ${it.date}" } ?: "প্রকাশিত সংবাদ", fontWeight = FontWeight.Bold)
                Text(feed.snapshot?.coverage ?: "সংবাদ লোড হচ্ছে", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = feed.refresh, enabled = !feed.loading) {
                Icon(Icons.Default.Refresh, contentDescription = "সংবাদ হালনাগাদ করুন")
            }
        }
        if (feed.snapshot?.stale == true) Text("অফলাইন সংরক্ষিত সংস্করণ", color = MaterialTheme.colorScheme.secondary, fontSize = 11.sp)
        if (feed.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
        OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), placeholder = { Text("শিরোনাম বা বিষয় খুঁজুন") }, leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(
                listOf("সব" to "feed-filter-all", "বাংলাদেশ" to "feed-filter-bangladesh"),
                listOf("বিশ্ব" to "feed-filter-world", "অর্থনীতি" to "feed-filter-economy")
            ).forEach { filterRow ->
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    filterRow.forEach { (label, tag) ->
                        FilterChip(selected = selectedFilter == label, onClick = { selectedFilter = label },
                            modifier = Modifier.testTag(tag), label = { Text(label) })
                    }
                }
            }
        }
        Text("ফিল্টার: $selectedFilter", modifier = Modifier.testTag("feed-active-filter"),
            color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        if (feed.snapshot == null) {
            Panel {
                if (feed.loading) CircularProgressIndicator()
                Text(if (feed.loading) "প্রকাশিত সংবাদ আনা হচ্ছে…" else "সংবাদ পাওয়া যায়নি", fontWeight = FontWeight.SemiBold)
                Text("ইন্টারনেট সংযোগ পরীক্ষা করে হালনাগাদ করুন।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                TextButton(onClick = feed.refresh) { Text("আবার চেষ্টা করুন") }
            }
        } else if (filtered.isEmpty()) {
            Panel {
                Icon(Icons.Default.SearchOff, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                Text("কোনো ফল পাওয়া যায়নি", fontWeight = FontWeight.SemiBold)
                Text("বানান বদলে দেখুন অথবা অন্য বিষয় বেছে নিন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            filtered.forEachIndexed { index, story ->
                Panel {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(8.dp)) {
                            Text((index + 1).toString().padStart(2, '0'), Modifier.padding(horizontal = 9.dp, vertical = 7.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                        }
                        Kicker(categoryLabel(story.category))
                        Spacer(Modifier.weight(1f))
                        AssistChip(onClick = {}, enabled = false, label = { Text("প্রকাশিত", fontSize = 10.sp) })
                    }
                    Text(story.headline, fontSize = 18.sp, fontWeight = FontWeight.Bold, lineHeight = 26.sp)
                    Text(story.oneLiner.ifBlank { story.summary }, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 19.sp)
                    Text("${story.sources.size}টি তথ্যসূত্র", color = MaterialTheme.colorScheme.secondary, fontSize = 11.sp)
                    TextButton(onClick = {
                        EditionFeedRepository.selectStory(context, story.id)
                        nav.navigate(AppDestination.Article.route)
                    }) { Text("বিস্তারিত পড়ুন") }
                }
            }
        }
    }
}


@Composable internal fun Article(nav: NavHostController) {
    val context = LocalContext.current
    val storyId = EditionFeedRepository.selectedStoryId(context)
    val story = EditionFeedRepository.story(context, storyId)
    val readerPreferences = rememberReaderPreferences(context)
    val readerFontSize = readerPreferences.fontSize
    val readerLineHeight = readerPreferences.lineHeight
    val sepia = readerPreferences.sepia
    var saved by rememberSaveable(storyId) {
        mutableStateOf(story?.let { LocalLearningStore.isArticleSaved(context, it.id) } ?: false)
    }
    LaunchedEffect(story?.id) {
        story?.let { LocalLearningStore.recordArticleRead(context, it.id) }
    }
    Scroll {
        if (story == null) {
            Panel {
                Text("প্রতিবেদনটি পাওয়া যায়নি", fontWeight = FontWeight.Bold)
                Text("সংস্করণটি হালনাগাদ করুন অথবা সংবাদ তালিকা থেকে আবার খুলুন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Primary("সব সংবাদ") { nav.navigate(AppDestination.Feed.route) }
            }
        } else {
            Surface(
                color = if (sepia) SarangshoPalette.Paper else MaterialTheme.colorScheme.background,
                contentColor = if (sepia) SarangshoPalette.Ink else MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(if (sepia) 12.dp else 0.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Kicker(categoryLabel(story.category))
                            Text("প্রকাশিত দৈনিক সংস্করণ", fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary)
                        }
                        IconButton(onClick = {
                            saved = !saved
                            LocalLearningStore.setArticleSaved(context, story.id, saved)
                        }) {
                            Icon(if (saved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = if (saved) "সংরক্ষিত" else "সংরক্ষণ করুন")
                        }
                    }
                    Text(story.headline, fontSize = 25.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold,
                        color = if (sepia) SarangshoPalette.Ink else MaterialTheme.colorScheme.onBackground)
                    Panel {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.VerifiedUser, null, tint = MaterialTheme.colorScheme.primary)
                            Column(Modifier.weight(1f)) {
                                Text("উৎস যাচাইয়ের অবস্থা", fontWeight = FontWeight.SemiBold)
                                Text("প্রকাশিত দৈনিক সংস্করণ থেকে", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                            }
                            AssistChip(onClick = {}, enabled = false, label = { Text("প্রকাশিত") })
                        }
                    }
                    Panel {
                        Kicker("সংক্ষিপ্ত সারাংশ")
                        Text(story.summary.ifBlank { story.oneLiner }, fontSize = readerFontSize, lineHeight = readerLineHeight)
                    }
                    if (story.whyItMatters.isNotBlank()) Panel {
                        Kicker("কেন গুরুত্বপূর্ণ")
                        Text(story.whyItMatters, fontSize = readerFontSize, lineHeight = readerLineHeight)
                    }
                    if (story.examFacts.isNotBlank()) Panel {
                        Kicker("পরীক্ষার জন্য তথ্য")
                        Text(story.examFacts, fontSize = readerFontSize, lineHeight = readerLineHeight)
                    }
                    if (story.detail.isNotBlank()) Panel {
                        Kicker("বিস্তারিত পাঠ")
                        Text(story.detail, fontSize = readerFontSize, lineHeight = readerLineHeight)
                    }
                    if (story.keyFacts.isNotEmpty()) {
                        Text("মূল তথ্য", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        story.keyFacts.forEach { fact ->
                            Panel {
                                Text(fact.label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                                Text(fact.value, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                                if (fact.original.isNotBlank()) Text(fact.original, fontSize = 12.sp, lineHeight = 19.sp)
                            }
                        }
                    }
                    Text("মূল তথ্যসূত্র", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    if (story.sources.isEmpty()) {
                        Text("এই প্রতিবেদনের সঙ্গে প্রকাশ্য উৎস-লিংক সংযুক্ত নেই।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    } else story.sources.forEach { source ->
                        Panel {
                            Kicker(source.publication)
                            Text(source.headline, fontWeight = FontWeight.SemiBold)
                            Text(source.date, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                            TextButton(onClick = {
                                runCatching {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(source.url)))
                                }
                            }) {
                                Icon(Icons.Default.OpenInNew, null, modifier = Modifier.size(15.dp))
                                Spacer(Modifier.width(5.dp))
                                Text("মূল প্রতিবেদন খুলুন")
                            }
                        }
                    }
                }
            }
        }
        Text("এই প্রতিবেদনের পাঠ-সরঞ্জাম", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        RowLink("পড়ার পছন্দ", "ফন্ট ও লাইন স্পেসিং বদলান", Icons.Default.TextFields) { nav.navigate(AppDestination.ReadingSettings.route) }
        RowLink("তিন-স্তরের সারাংশ", "৩০ সেকেন্ড, ২ মিনিট ও বিস্তারিত পাঠ", Icons.AutoMirrored.Filled.Subject) { nav.navigate(AppDestination.Summary.route) }
        RowLink("ঘটনার টাইমলাইন", "তারিখ ও ঘটনার সম্পর্ক যাচাই করে পড়ুন", Icons.Default.Timeline) { nav.navigate(AppDestination.Timeline.route) }
        RowLink("পত্রিকার দৃষ্টিভঙ্গি", "উৎস পাওয়া গেলে তুলনা করুন", Icons.AutoMirrored.Filled.CompareArrows) { nav.navigate(AppDestination.Perspectives.route) }
        RowLink("জ্ঞান মানচিত্র", "প্রতিষ্ঠান, আইন ও ধারণার সম্পর্ক", Icons.Default.Hub) { nav.navigate(AppDestination.Graph.route) }
        RowLink("উৎস ও যাচাই", "মূল নথি ও প্রকাশনার তথ্য", Icons.Default.Source) { nav.navigate(AppDestination.Sources.route) }
        RowLink("তথ্য সংশোধনের অনুরোধ", "ভুল বা অস্পষ্ট তথ্য জানান", Icons.Default.Flag) { nav.navigate(AppDestination.ReportStory.route) }
    }
}

@Composable @OptIn(ExperimentalMaterial3Api::class)
internal fun Summary(nav: NavHostController) {
    val context = LocalContext.current
    val readerPreferences = rememberReaderPreferences(context)
    val readerFontSize = readerPreferences.fontSize
    val readerLineHeight = readerPreferences.lineHeight
    val sepia = readerPreferences.sepia
    val story = EditionFeedRepository.story(context, EditionFeedRepository.selectedStoryId(context))
    var level by rememberSaveable { mutableIntStateOf(0) }
    val labels = listOf("৩০ সেকেন্ড", "২ মিনিট", "বিস্তারিত")
    val texts = listOf(
        story?.oneLiner?.takeIf { it.isNotBlank() } ?: "ঘটনাটির মূল বিষয় এক বাক্যে বুঝুন।",
        story?.summary?.takeIf { it.isNotBlank() } ?: "মূল সিদ্ধান্ত, সংশ্লিষ্ট প্রতিষ্ঠান এবং বাংলাদেশের জন্য সম্ভাব্য তাৎপর্য সংক্ষেপে পড়ুন.",
        story?.detail?.takeIf { it.isNotBlank() } ?: story?.examFacts?.takeIf { it.isNotBlank() }
            ?: "বিস্তারিত সংস্করণে পটভূমি, প্রক্রিয়া, মূল তথ্য, সংশ্লিষ্ট পক্ষের অবস্থান, অনিশ্চয়তা এবং পরীক্ষায় প্রাসঙ্গিক ধারণা থাকবে।"
    )
    Scroll {
        Intro("একই ঘটনা, তিনভাবে বুঝুন", "আপনার হাতে যত সময় আছে, সেই অনুযায়ী পড়ুন।")
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) { labels.forEachIndexed { i, label -> SegmentedButton(level == i, { level = i }, SegmentedButtonDefaults.itemShape(i, labels.size)) { Text(label, fontSize = 11.sp) } } }
        Surface(
            color = if (sepia) SarangshoPalette.Paper else MaterialTheme.colorScheme.surface,
            contentColor = if (sepia) SarangshoPalette.Ink else MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text(
                    labels[level].uppercase(),
                    fontSize = 10.sp,
                    color = if (sepia) SarangshoPalette.Teal else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(texts[level], fontSize = readerFontSize, lineHeight = readerLineHeight)
                Text(
                    story?.headline ?: "প্রকাশিত সংস্করণ থেকে নির্বাচিত পাঠ",
                    color = if (sepia) SarangshoPalette.Muted else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
        RowLink("পড়ার পছন্দ", "ফন্ট ও লাইন স্পেসিং বদলান", Icons.Default.TextFields) { nav.navigate(AppDestination.ReadingSettings.route) }
        RowLink("টাইমলাইন", "ঘটনার পূর্বাপর", Icons.Default.Timeline) { nav.navigate(AppDestination.Timeline.route) }
    }
}

@Composable internal fun Timeline() {
    val events = listOf(
        "ঐতিহাসিক পটভূমি" to "প্রাসঙ্গিক প্রেক্ষাপট ও প্রাথমিক নথি যুক্ত হলে এখানে দেখানো হবে।",
        "আইনি প্রক্রিয়া" to "আদালতের আবেদন, শুনানি ও আদেশের তারিখ মূল নথি থেকে যাচাই করতে হবে।",
        "বর্তমান পর্যায়" to "সর্বশেষ অবস্থা হালনাগাদ সরকারি বা আদালতের নথি ছাড়া নিশ্চিত করা হবে না।",
        "পরবর্তী ধাপ" to "নির্ধারিত শুনানি বা সম্ভাব্য পদক্ষেপকে নিশ্চিত ঘটনা হিসেবে দেখানো হবে না."
    )
    Scroll {
        Intro("ঘটনার টাইমলাইন", "ঘটনার তারিখ ও সংবাদ প্রকাশের তারিখ আলাদা করে দেখুন।")
        Panel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Timeline, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("যাচাইয়ের নিয়ম", fontWeight = FontWeight.Bold)
            }
            Text("প্রতিটি বিন্দুর সঙ্গে তারিখ, ঘটনার বিবরণ এবং মূল উৎস থাকবে। যাচাই না হওয়া তারিখ এখানে বানিয়ে বসানো হয় না।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp, lineHeight = 21.sp)
        }
        events.forEachIndexed { index, event ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50.dp)) {
                        Box(Modifier.size(30.dp), contentAlignment = Alignment.Center) {
                            Text((index + 1).toString(), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    if (index != events.lastIndex) Box(Modifier.width(2.dp).height(76.dp).background(MaterialTheme.colorScheme.outlineVariant))
                }
                Column(Modifier.weight(1f).padding(bottom = 18.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(event.first, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(event.second, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 19.sp)
                    Text("তারিখ: উৎস-যাচাই প্রয়োজন", color = MaterialTheme.colorScheme.secondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable internal fun Perspectives() {
    val comparisons = listOf(
        Triple("শিরোনাম ও গুরুত্ব", "প্রতিটি প্রকাশনা কোন দিকটিকে সামনে এনেছে?", "শিরোনাম ও প্রতিবেদন সংগ্রহ করা হয়নি"),
        Triple("তথ্য ও বক্তব্য", "সংখ্যা, উদ্ধৃতি ও দাবির উৎস মিলিয়ে দেখুন।", "মূল প্রতিবেদন ও প্রাথমিক নথি প্রয়োজন"),
        Triple("অনুপস্থিত প্রসঙ্গ", "কোন তথ্য বাদ পড়েছে বা অনিশ্চিত রয়ে গেছে?", "তুলনার জন্য পর্যাপ্ত উৎস নেই")
    )
    Scroll {
        Intro("পত্রিকার দৃষ্টিভঙ্গি", "একই ঘটনা বিভিন্ন প্রকাশনা কীভাবে তুলে ধরে, তা পাশাপাশি বিচার করুন।")
        Panel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.CompareArrows, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("তুলনার অবস্থা", fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                AssistChip(onClick = {}, enabled = false, label = { Text("চালানো হয়নি") })
            }
            Text("এই নমুনা সংস্করণে কোনো সংবাদমাধ্যমের প্রতিবেদন সংগ্রহ করা হয়নি। তাই কোনো প্রকাশনার অবস্থান বা বক্তব্য এখানে আরোপ করা হচ্ছে না.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp, lineHeight = 21.sp)
        }
        comparisons.forEachIndexed { index, item ->
            Panel {
                Kicker("তুলনার ক্ষেত্র ০${index + 1}")
                Text(item.first, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text(item.second, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(7.dp))
                    Text(item.third, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Text("উৎসের প্রবেশযোগ্যতা, প্রকাশের সময় এবং একই ঘটনার সঙ্গে প্রাসঙ্গিকতা যাচাই না করে তুলনা প্রকাশ করা হবে না.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, lineHeight = 17.sp)
    }
}

@Composable internal fun Topics(nav: NavHostController, world: Boolean) {
    val context = LocalContext.current
    val list = if (world) listOf("জাতিসংঘ ও আন্তর্জাতিক সংস্থা", "আন্তর্জাতিক আইন ও চুক্তি", "বিশ্ব অর্থনীতি", "জলবায়ু ও পরিবেশ", "দক্ষিণ এশিয়া", "ভূরাজনীতি") else listOf("সংবিধান ও সরকারব্যবস্থা", "অর্থনীতি ও ব্যাংকিং", "সমাজ ও উন্নয়ন", "পরিবেশ ও জলবায়ু", "শিক্ষা ও স্বাস্থ্য", "আইন ও বিচার")
    val selected = LocalLearningStore.getSelectedTopic(context)
    Scroll {
        Intro(if (world) "বিশ্ব বিষয়াবলি" else "বাংলাদেশ বিষয়াবলি", "বর্তমান ঘটনা, মৌলিক ধারণা ও পরীক্ষার প্রস্তুতি একসঙ্গে দেখুন।")
        Text("বিষয় বেছে নিন", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        list.forEach { topic ->
            RowLink(
                topic,
                if (topic == selected) "সর্বশেষ নির্বাচিত বিষয় • পাঠ খুলুন" else "ধারণা, গুরুত্বপূর্ণ দিক ও পরীক্ষার প্রস্তুতি",
                Icons.Default.Public
            ) {
                LocalLearningStore.setSelectedTopic(context, topic)
                nav.navigate(AppDestination.TopicDetail.route)
            }
        }
    }
}

@Composable internal fun Graph() {
    val nodes = listOf("আন্তর্জাতিক আদালত" to "প্রতিষ্ঠান", "রাষ্ট্রের দায়বদ্ধতা" to "আইনি ধারণা", "আন্তর্জাতিক চুক্তি" to "আইনি কাঠামো", "মানবাধিকার" to "বিষয়", "আঞ্চলিক প্রেক্ষাপট" to "প্রেক্ষাপট")
    Scroll {
        Intro("জ্ঞান মানচিত্র", "একটি বিষয়কে প্রতিষ্ঠান, আইন, ধারণা ও প্রভাবের সঙ্গে যুক্ত করে বুঝুন।")
        Panel {
            Kicker("সম্পর্কের মানচিত্র • নমুনা")
            Surface(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Text("আন্তর্জাতিক বিচারিক প্রক্রিয়া", Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }
            Box(Modifier.fillMaxWidth().height(42.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.AccountTree, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(26.dp))
            }
            nodes.forEachIndexed { index, node ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(color = if (index % 2 == 0) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(10.dp)) {
                        Icon(Icons.Default.Hub, null, modifier = Modifier.padding(10.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                    Column(Modifier.weight(1f)) {
                        Text(node.first, fontWeight = FontWeight.SemiBold)
                        Text(node.second, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = MaterialTheme.colorScheme.outline)
                }
                if (index != nodes.lastIndex) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
            Text("নমুনা কাঠামো। বিষয়গুলোর মধ্যে প্রমাণিত সম্পর্কের ডেটা এখনো সংযুক্ত নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }
        Text("সম্পর্কের ধরন", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        listOf("কারণ ও প্রভাব", "আইনি এখতিয়ার", "সদস্যপদ ও প্রতিষ্ঠান", "সময়গত সম্পর্ক", "নীতি ও বাস্তবায়ন").forEach {
            RowLink(it, "সম্পর্কের ব্যাখ্যা ও উৎস যুক্ত হলে দেখানো হবে", Icons.Default.Share) { }
        }
    }
}

@Composable internal fun DataExplorer(nav: NavHostController) {
    Scroll {
        Intro("তথ্য ও পরিসংখ্যান", "সংখ্যার সঙ্গে সংজ্ঞা, একক, সময়কাল ও মূল উৎস মিলিয়ে পড়ুন।")
        Panel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Insights, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("ডেটা ক্যাটালগ", fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                AssistChip(onClick = {}, enabled = false, label = { Text("ডেমো") })
            }
            Text("যাচাইকৃত উপাত্ত সংযুক্ত না হওয়ায় এখানে কোনো মান বা চার্ট দেখানো হচ্ছে না।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        listOf(
            Triple("মূল্যস্ফীতি", "শতাংশ • সময়কাল ও ভিত্তিবছর প্রয়োজন", "উৎস: বাংলাদেশ পরিসংখ্যান ব্যুরো / বাংলাদেশ ব্যাংক"),
            Triple("জিডিপি প্রবৃদ্ধি", "বার্ষিক হার • চলতি/স্থির মূল্য আলাদা করুন", "উৎস: সরকারি জাতীয় হিসাব"),
            Triple("বৈদেশিক মুদ্রার রিজার্ভ", "মার্কিন ডলার • সংজ্ঞা ও গণনাপদ্ধতি প্রয়োজন", "উৎস: বাংলাদেশ ব্যাংক")
        ).forEach { item ->
            Panel {
                Kicker("সূচক • ডেটা অপেক্ষমাণ")
                Text(item.first, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(item.second, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Text(item.third, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                TextButton(onClick = { nav.navigate(AppDestination.IndicatorDetail.route) }) { Text("সংজ্ঞা ও যাচাইয়ের নিয়ম") }
            }
        }
        Text("ডেটা যাচাইয়ের চেকলিস্ট", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        listOf("একক ও সময়কাল", "মূল্য বা পরিমাণের সংজ্ঞা", "প্রাথমিক নাকি সংশোধিত উপাত্ত", "পর্যবেক্ষণ নাকি পূর্বাভাস", "মূল প্রকাশনা ও প্রকাশের তারিখ").forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircleOutline, null, tint = MaterialTheme.colorScheme.tertiary)
                Spacer(Modifier.width(8.dp))
                Text(it, fontSize = 13.sp)
            }
        }
    }
}

@Composable internal fun Search(nav: NavHostController) {
    var query by rememberSaveable { mutableStateOf("") }
    var scope by rememberSaveable { mutableStateOf("সব") }
    var searched by rememberSaveable { mutableStateOf(false) }
    val suggestions = listOf("বাংলাদেশ ব্যাংক", "সংবিধান", "জাতিসংঘ", "জলবায়ু অর্থায়ন", "Resilience")
    val normalizedQuery = query.trim()
    val storyTitle = "গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন"
    val storyTerms = listOf(storyTitle, "আন্তর্জাতিক আইন", "রোহিঙ্গা সংকট", "বিচারিক প্রক্রিয়া")
    val topicTerms = listOf("বাংলাদেশ ব্যাংক", "সংবিধান", "জাতিসংঘ", "জলবায়ু অর্থায়ন", "আন্তর্জাতিক আইন")
    val storyMatches = searched && scope in listOf("সব", "সংবাদ") &&
        storyTerms.any { it.contains(normalizedQuery, ignoreCase = true) || normalizedQuery.contains(it, ignoreCase = true) }
    val topicMatches = if (searched && scope in listOf("সব", "বিষয়")) {
        topicTerms.filter { it.contains(normalizedQuery, ignoreCase = true) }
    } else emptyList()
    val wordMatches = if (searched && scope in listOf("সব", "শব্দ")) {
        searchVocabulary(normalizedQuery)
    } else emptyList()
    val resultCount = (if (storyMatches) 1 else 0) + topicMatches.size + wordMatches.size

    Scroll {
        Intro("অনুসন্ধান ও উৎস", "সংবাদ, বিষয় ও শব্দ খুঁজুন। ফলাফলগুলো বর্তমানে অ্যাপের নমুনা কনটেন্ট ও স্থানীয় শব্দতালিকা থেকে আসে।")
        OutlinedTextField(
            query, { query = it; searched = false }, Modifier.fillMaxWidth(),
            placeholder = { Text("কী খুঁজছেন?") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = {
                if (query.isNotBlank()) IconButton(onClick = { query = ""; searched = false }) {
                    Icon(Icons.Default.Close, "মুছুন")
                }
            },
            singleLine = true
        )
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("সব", "সংবাদ", "শব্দ", "বিষয়").forEach {
                FilterChip(selected = scope == it, onClick = { scope = it; searched = false }, label = { Text(it) })
            }
        }
        Primary("খুঁজুন") { searched = normalizedQuery.isNotBlank() }
        if (!searched) {
            Text("জনপ্রিয় অনুসন্ধান", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            suggestions.forEach { item ->
                Row(
                    Modifier.fillMaxWidth().clickable { query = item; searched = true }.padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(10.dp))
                    Text(item, Modifier.weight(1f))
                    Icon(Icons.Default.NorthWest, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        } else {
            Text("$resultCount টি ফলাফল", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            if (resultCount == 0) {
                Panel {
                    Icon(Icons.Default.SearchOff, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                    Text("কোনো ফল পাওয়া যায়নি", fontWeight = FontWeight.Bold)
                    Text("অন্য বানান, বাংলা অর্থ অথবা অন্য বিভাগ দিয়ে খুঁজে দেখুন।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }
            }
            if (storyMatches) {
                Panel {
                    Kicker("সংবাদ • নমুনা নিবন্ধ")
                    Text(storyTitle, fontWeight = FontWeight.Bold, fontSize = 17.sp, lineHeight = 24.sp)
                    Text("আন্তর্জাতিক আইন • রোহিঙ্গা সংকট", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    TextButton(onClick = { nav.navigate(AppDestination.Article.route) }) { Text("নিবন্ধ খুলুন") }
                }
            }
            topicMatches.forEach { topic ->
                Panel {
                    Kicker("বিষয়")
                    Text(topic, fontWeight = FontWeight.SemiBold)
                    TextButton(onClick = { nav.navigate(AppDestination.TopicDetail.route) }) { Text("বিষয়ভিত্তিক পাঠ খুলুন") }
                }
            }
            wordMatches.forEach { (term, meaning) ->
                Panel {
                    Kicker("শব্দভান্ডার")
                    Text(term, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                    Text(meaning, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    TextButton(onClick = { nav.navigate(AppDestination.Vocabulary.route) }) { Text("Vocabulary Lab খুলুন") }
                }
            }
        }
        Text(
            "এটি স্থানীয় প্রোটোটাইপ অনুসন্ধান। লাইভ সংবাদ, প্রকাশক, মূল URL বা সম্পূর্ণ শব্দভান্ডারের সূচি এখনো সংযুক্ত নয়।",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp,
            lineHeight = 17.sp
        )
    }
}

@Composable internal fun Saved(nav: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val bookmarkedWords = remember { mutableStateListOf<String>().apply { addAll(LocalLearningStore.getBookmarkedWords(context).sorted()) } }
    val savedArticles = remember { mutableStateListOf<String>().apply { addAll(LocalLearningStore.getSavedArticles(context).sorted()) } }
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf("সব") }
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                bookmarkedWords.clear()
                bookmarkedWords.addAll(LocalLearningStore.getBookmarkedWords(context).sorted())
                savedArticles.clear()
                savedArticles.addAll(LocalLearningStore.getSavedArticles(context).sorted())
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    val normalizedQuery = query.trim()
    val visibleArticles = if (filter == "শব্দ") emptyList() else savedArticles.filter { id ->
        val title = EditionFeedRepository.story(context, id)?.headline
            ?: if (id == "sample-international-justice") "গাম্বিয়া বনাম মিয়ানমার আন্তর্জাতিক বিচারিক প্রক্রিয়া" else id
        normalizedQuery.isBlank() || id.contains(normalizedQuery, ignoreCase = true) ||
            title.contains(normalizedQuery, ignoreCase = true)
    }
    val visibleWords = if (filter == "নিবন্ধ") emptyList() else bookmarkedWords.filter { word ->
        normalizedQuery.isBlank() || word.contains(normalizedQuery, ignoreCase = true)
    }
    Scroll {
        Intro("আপনার সংরক্ষিত বিষয়", "পরে পড়ার জন্য রাখা নিবন্ধ ও শব্দ এখানে পাবেন।")
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().testTag("saved-search-field"),
            singleLine = true,
            label = { Text("সংরক্ষিত বিষয় খুঁজুন") },
            placeholder = { Text("নিবন্ধ বা শব্দ লিখুন") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "অনুসন্ধান মুছুন")
                    }
                }
            }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("সব", "নিবন্ধ", "শব্দ").forEach { option ->
                FilterChip(
                    selected = filter == option,
                    onClick = { filter = option },
                    label = { Text(option) }
                )
            }
        }
        Text(
            "দেখানো হচ্ছে • ${visibleArticles.size + visibleWords.size} / ${savedArticles.size + bookmarkedWords.size}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
        )
        if (filter != "শব্দ") {
            Text("সংরক্ষিত নিবন্ধ • ${visibleArticles.size}", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            if (visibleArticles.isEmpty() && normalizedQuery.isBlank() && savedArticles.isEmpty() && (savedArticles.isNotEmpty() || bookmarkedWords.isNotEmpty()) && (filter == "নিবন্ধ" || bookmarkedWords.isEmpty())) {
                Panel {
                    Text(if (savedArticles.isEmpty()) "এখনো কোনো নিবন্ধ সংরক্ষণ করা হয়নি" else "এই অনুসন্ধানে কোনো নিবন্ধ পাওয়া যায়নি", fontWeight = FontWeight.SemiBold)
                    Text("নিবন্ধের বুকমার্ক আইকন ব্যবহার করে পরে পড়ার জন্য সংরক্ষণ করুন.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }
            }
            visibleArticles.forEach { id ->
                val story = EditionFeedRepository.story(context, id)
                Panel {
                    Kicker("সংরক্ষিত নিবন্ধ")
                    Text(story?.headline ?: if (id == "sample-international-justice") "গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন" else id,
                        fontWeight = FontWeight.SemiBold)
                    if (story != null || id == "sample-international-justice") {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = {
                                EditionFeedRepository.selectStory(context, if (story != null) id else id)
                                nav.navigate(AppDestination.Article.route)
                            }) { Text("পড়ুন") }
                            TextButton(onClick = {
                                LocalLearningStore.setArticleSaved(context, id, false)
                                savedArticles.remove(id)
                            }) { Text("সংরক্ষণ সরান") }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("এই নিবন্ধের বিস্তারিত এখনো উপলভ্য নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                            TextButton(onClick = {
                                LocalLearningStore.setArticleSaved(context, id, false)
                                savedArticles.remove(id)
                            }) { Text("সংরক্ষণ সরান") }
                        }
                    }
                }
            }
        }
        if (filter != "নিবন্ধ") {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Text("সংরক্ষিত শব্দ • ${visibleWords.size}", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            if (visibleWords.isEmpty() && normalizedQuery.isBlank() && bookmarkedWords.isEmpty() && (savedArticles.isNotEmpty() || bookmarkedWords.isNotEmpty()) && (filter == "শব্দ" || savedArticles.isEmpty())) {
                Panel {
                    Text(if (bookmarkedWords.isEmpty()) "এখনো কোনো শব্দ বুকমার্ক করা হয়নি" else "এই অনুসন্ধানে কোনো শব্দ পাওয়া যায়নি", fontWeight = FontWeight.SemiBold)
                    Text("Vocabulary Lab-এ গিয়ে কোনো শব্দ বুকমার্ক করলে সেটি এখানে দেখা যাবে।", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Primary("Vocabulary Lab খুলুন") { nav.navigate(AppDestination.Vocabulary.route) }
                }
            }
            visibleWords.forEach { word ->
                Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f).padding(vertical = 9.dp)) {
                        Text(word, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text("Vocabulary Lab • এই ডিভাইসে সংরক্ষিত", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    }
                    TextButton(onClick = {
                        nav.currentBackStackEntry?.savedStateHandle?.set("selected_word", word)
                        nav.navigate(AppDestination.WordDetail.route)
                    }) { Text("খুলুন") }
                    TextButton(onClick = {
                        LocalLearningStore.setWordBookmarked(context, word, false)
                        bookmarkedWords.remove(word)
                    }) { Text("সরান") }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
        if (savedArticles.isEmpty() && bookmarkedWords.isEmpty() && normalizedQuery.isBlank()) {
            Panel {
                Text("আপনার লাইব্রেরি এখনো খালি", fontWeight = FontWeight.SemiBold)
                Text("নিবন্ধ ও শব্দ বুকমার্ক করলে সেগুলো এখানে একসঙ্গে পাবেন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else if (visibleArticles.isEmpty() && visibleWords.isEmpty() && normalizedQuery.isNotBlank()) {
            Panel {
                Text("কোনো সংরক্ষিত বিষয় মেলেনি", fontWeight = FontWeight.SemiBold)
                Text("বানান পরিবর্তন করে দেখুন অথবা অনুসন্ধান মুছে সব বিষয় দেখুন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        RowLink("Vocabulary Lab", "আরও শব্দ শিখুন ও বুকমার্ক করুন", Icons.Default.Translate) { nav.navigate(AppDestination.Vocabulary.route) }
        RowLink("সংরক্ষিত সংগ্রহ", "ফোল্ডার ও বিষয়ভিত্তিক তালিকা", Icons.Default.Folder) { nav.navigate(AppDestination.SavedCollections.route) }
        Text("সংরক্ষিত নিবন্ধ ও শব্দ শুধু এই ডিভাইসে থাকে। অ্যাকাউন্ট বা অন্য ডিভাইসে সিঙ্ক হয় না।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
    }
}

@Composable internal fun Profile(nav: NavHostController) {
    val context = LocalContext.current
    val learningGoal = LocalLearningStore.getOnboardingGoal(context)
    val learningInterests = LocalLearningStore.getOnboardingInterests(context).sorted()
    Scroll {
        Intro("আমার প্রোফাইল", "আপনার পড়া, শেখা ও অ্যাপের পছন্দ এক জায়গায়।")
        Panel {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(50.dp)) {
                    Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(17.dp).size(34.dp))
                }
                Column(Modifier.weight(1f)) {
                    Text("সারাংশ পাঠক", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    Text("স্থানীয় ডেমো • সাইন ইন করা হয়নি", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    AssistChip(onClick = { nav.navigate(AppDestination.Signin.route) }, label = { Text("সাইন ইন / অ্যাকাউন্ট") })
                }
            }
        }
        Panel {
            Kicker("আপনার শেখার পছন্দ")
            Text(learningGoal, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text(
                if (learningInterests.isEmpty()) "এখনো কোনো বিষয় নির্বাচন করা হয়নি।"
                else learningInterests.joinToString(" • "),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                lineHeight = 19.sp
            )
            TextButton(onClick = { nav.navigate(AppDestination.OnboardingGoals.route) }) {
                Text("লক্ষ্য ও বিষয় পরিবর্তন")
            }
        }
        Text("আপনার শেখার জায়গা", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        RowLink("আমার শব্দভান্ডার", "সংরক্ষিত শব্দ ও পুনরাবৃত্তি", Icons.Default.Translate) { nav.navigate(AppDestination.Vocabulary.route) }
        RowLink("রিভিশন ডেক", "মনে রাখার অনুশীলন", Icons.Default.Style) { nav.navigate(AppDestination.Revision.route) }
        RowLink("লিখিত অনুশীলন", "আপনার উত্তর ও খসড়া", Icons.Default.EditNote) { nav.navigate(AppDestination.Writing.route) }
        RowLink("অনুশীলনের ইতিহাস", "আগের সেশন ও ভুলের তালিকা", Icons.Default.History) { nav.navigate(AppDestination.PracticeHistory.route) }
        RowLink("পড়ার ইতিহাস", "সম্প্রতি খোলা পাঠ", Icons.AutoMirrored.Filled.MenuBook) { nav.navigate(AppDestination.ReadingHistory.route) }
        Text("অ্যাপ ও পছন্দ", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        RowLink("নোটিফিকেশন", "আপডেট ও রিভিশন স্মরণ", Icons.Default.Notifications) { nav.navigate(AppDestination.Notifications.route) }
        RowLink("সেটিংস ও পছন্দ", "ভাষা, ফন্ট, ডাউনলোড ও গোপনীয়তা", Icons.Default.Settings) { nav.navigate(AppDestination.Settings.route) }
        RowLink("সহায়তা", "প্রশ্ন, সমস্যা ও মতামত", Icons.AutoMirrored.Filled.HelpOutline) { nav.navigate(AppDestination.Help.route) }
        RowLink("Sarangsho Plus সম্পর্কে", "সংস্করণ ও অ্যাপের নীতিমালা", Icons.Default.Info) { nav.navigate(AppDestination.About.route) }
        Panel {
            Kicker("গোপনীয়তা ও অগ্রগতি")
            Text("প্রকৃত ব্যবহার-ডেটা যুক্ত না হওয়া পর্যন্ত কোনো স্কোর, ধারাবাহিকতা বা সাপ্তাহিক অ্যানালিটিক্স দেখানো হবে না।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 20.sp)
        }
    }
}

@Composable internal fun OnboardingGoals(nav: NavHostController) {
    val context = LocalContext.current
    var goal by rememberSaveable { mutableStateOf(LocalLearningStore.getOnboardingGoal(context)) }
    val options = listOf(
        "BCS প্রস্তুতি" to "প্রিলিমিনারি, লিখিত ও সাম্প্রতিক বিষয়",
        "Bank recruitment" to "ব্যাংক নিয়োগ পরীক্ষা ও সাধারণ জ্ঞান",
        "বিশ্ববিদ্যালয় ও সাধারণ জ্ঞান" to "একাডেমিক শেখা ও ধারণাভিত্তিক প্রস্তুতি",
        "প্রতিদিনের খবর বুঝে পড়া" to "বাংলাদেশ, বিশ্ব ও অর্থনীতির প্রেক্ষাপট"
    )
    Scroll {
        Kicker("আপনার অভিজ্ঞতা সাজান • ১/২")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(Modifier.weight(1f).height(4.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)))
            Box(Modifier.weight(1f).height(4.dp).background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp)))
        }
        Intro("আপনার প্রধান লক্ষ্য কী?", "একটি লক্ষ্য বেছে নিন। পরে সেটিংস থেকে পরিবর্তন করা যাবে।")
        options.forEach { (name, detail) ->
            Surface(
                color = if (goal == name) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(1.dp, if (goal == name) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth().clickable { goal = name }
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = goal == name, onClick = { goal = name })
                    Spacer(Modifier.width(9.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(name, fontWeight = FontWeight.SemiBold)
                        Text(detail, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    }
                }
            }
        }
        Primary("বিষয় বেছে নিন") {
            LocalLearningStore.setOnboardingGoal(context, goal)
            nav.navigate(AppDestination.OnboardingInterests.route)
        }
        Text("আপনার লক্ষ্য এই ডিভাইসে সংরক্ষিত হবে। অ্যাকাউন্ট সিঙ্ক এখনো যুক্ত হয়নি।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
    }
}

@Composable internal fun OnboardingInterests(nav: NavHostController) {
    val interests = listOf(
        "বাংলাদেশ" to "রাষ্ট্র, সমাজ, ইতিহাস ও নীতি",
        "আন্তর্জাতিক বিষয়" to "বিশ্ব রাজনীতি, সংস্থা ও সম্পর্ক",
        "অর্থনীতি ও ব্যাংকিং" to "অর্থনীতি, বাজার ও আর্থিক প্রতিষ্ঠান",
        "আইন ও সংবিধান" to "আইনি কাঠামো, অধিকার ও শাসন",
        "বিজ্ঞান ও প্রযুক্তি" to "আবিষ্কার, উদ্ভাবন ও ডিজিটাল বিষয়",
        "পরিবেশ ও জলবায়ু" to "পরিবর্তন, ঝুঁকি ও অভিযোজন",
        "সমাজ ও উন্নয়ন" to "শিক্ষা, স্বাস্থ্য ও উন্নয়ন"
    )
    val context = LocalContext.current
    var selected by rememberSaveable {
        mutableStateOf(LocalLearningStore.getOnboardingInterests(context).toList().sorted())
    }
    Scroll {
        Kicker("আপনার অভিজ্ঞতা সাজান • ২/২")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(2) { index ->
                Box(Modifier.weight(1f).height(4.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)))
            }
        }
        Intro("কোন বিষয়গুলো পড়তে চান?", "আপনার আগ্রহের বিষয়গুলো বেছে নিন। একাধিক নির্বাচন করা যাবে।")
        interests.forEach { (name, detail) ->
            val checked = name in selected
            Surface(
                color = if (checked) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(13.dp),
                border = BorderStroke(1.dp, if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth().clickable {
                    selected = if (checked) selected - name else selected + name
                }
            ) {
                Row(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = checked, onCheckedChange = { value ->
                        selected = if (value) selected + name else selected - name
                    })
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(name, fontWeight = FontWeight.SemiBold)
                        Text(detail, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    }
                }
            }
        }
        Primary("আমার ব্রিফিং দেখুন") {
            LocalLearningStore.setOnboardingInterests(context, selected.toSet())
            LocalLearningStore.setOnboardingCompleted(context, true)
            nav.navigate(AppDestination.Home.route) {
                popUpTo(AppDestination.Welcome.route) { inclusive = true }
                launchSingleTop = true
            }
        }
        Text("নির্বাচিত বিষয়: ${selected.size}টি • পছন্দগুলো এই ডিভাইসে সংরক্ষিত হবে; অ্যাকাউন্ট সিঙ্ক এখনো যুক্ত হয়নি।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
    }
}


@Composable internal fun Archive(nav: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val feed = rememberPublishedFeed()
    val editions = EditionFeedRepository.publishedEditions(context)
    Scroll {
        Intro("দৈনিক সংস্করণ", "প্রকাশিত দৈনিক ব্রিফিং খুলুন এবং আগের দিনের সংস্করণে ফিরে যান।")
        if (feed.snapshot != null) {
            Panel {
                Kicker("সর্বশেষ প্রকাশিত সংস্করণ")
                Text(feed.snapshot.date, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(feed.snapshot.coverage, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                TextButton(onClick = { nav.navigate(AppDestination.Edition.route) }) { Text("বর্তমান সংস্করণ খুলুন") }
            }
        }
        if (editions.isEmpty()) {
            Panel {
                Text("সংস্করণের তালিকা লোড হচ্ছে", fontWeight = FontWeight.SemiBold)
                Text("ইন্টারনেট সংযোগ থাকলে প্রকাশিত আর্কাইভ এখানে দেখা যাবে।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
        } else {
            editions.forEachIndexed { index, edition ->
                Panel {
                    Kicker(if (index == 0) "সর্বশেষ" else "আর্কাইভ")
                    Text(edition.date, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text("${edition.stories}টি প্রতিবেদন • ${edition.coverage}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    TextButton(onClick = {
                        scope.launch {
                            if (feed.snapshot?.date != edition.date) {
                                runCatching { EditionFeedRepository.loadEdition(context, edition.date) }
                            }
                            nav.navigate(AppDestination.Edition.route)
                        }
                    }) { Text(when (index) { 0 -> "আজকের সংস্করণ"; 1 -> "গতকালের সংস্করণ"; else -> "এই সংস্করণ খুলুন" }) }
                }
            }
        }
    }
}

@Composable internal fun Edition(nav: NavHostController) {
    val context = LocalContext.current
    val feed = rememberPublishedFeed()
    val snapshot = feed.snapshot
    Scroll {
        Intro("দৈনিক ব্রিফিং", "একটি প্রকাশিত সংস্করণের সব প্রতিবেদন ও তথ্যসূত্র এক জায়গায়।")
        if (snapshot == null) {
            Panel {
                if (feed.loading) CircularProgressIndicator()
                Text(if (feed.loading) "সংস্করণ লোড হচ্ছে…" else "প্রকাশিত সংস্করণ পাওয়া যায়নি", fontWeight = FontWeight.SemiBold)
                TextButton(onClick = feed.refresh) { Text("আবার চেষ্টা করুন") }
            }
        } else {
            Panel {
                Kicker("প্রকাশিত সংস্করণ")
                Text(snapshot.date, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(snapshot.coverage, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                Text("${snapshot.stories.size}টি প্রতিবেদন", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
            }
            snapshot.stories.forEach { story ->
                Panel {
                    Kicker(categoryLabel(story.category))
                    Text(story.headline, fontWeight = FontWeight.Bold, fontSize = 17.sp, lineHeight = 24.sp)
                    Text(story.oneLiner.ifBlank { story.summary }, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    TextButton(onClick = {
                        EditionFeedRepository.selectStory(context, story.id)
                        nav.navigate(AppDestination.Article.route)
                    }) { Text("প্রতিবেদন পড়ুন") }
                }
            }
        }
        RowLink("বাংলাদেশ বিষয়াবলি", "দেশের গুরুত্বপূর্ণ ঘটনা", Icons.Default.Flag) { nav.navigate(AppDestination.Bangladesh.route) }
        RowLink("বিশ্ব বিষয়াবলি", "আন্তর্জাতিক প্রেক্ষাপট", Icons.Default.Public) { nav.navigate(AppDestination.World.route) }
        RowLink("আজকের অনুশীলন", "BCS ও Bank প্রশ্ন", Icons.Default.Quiz) { nav.navigate(AppDestination.QuizSetup.route) }
        RowLink("তথ্যসূত্র ও যাচাই", "উৎস, সময় ও প্রবেশযোগ্যতা", Icons.Default.Source) { nav.navigate(AppDestination.Sources.route) }
    }
}

private data class TopicGuide(
    val overview: String,
    val concepts: List<String>,
    val examFocus: String,
    val readingPrompt: String
)

private fun topicGuide(topic: String): TopicGuide {
    val guides = mapOf(
        "সংবিধান ও সরকারব্যবস্থা" to TopicGuide(
            "রাষ্ট্রের মৌলিক কাঠামো, নাগরিকের অধিকার, রাষ্ট্রীয় প্রতিষ্ঠান এবং ক্ষমতার সীমা কীভাবে নির্ধারিত হয়, তা এই বিষয়ের মূল আলোচ্য।",
            listOf("সংবিধানের ভূমিকা ও প্রাধান্য", "মৌলিক অধিকার ও রাষ্ট্র পরিচালনার নীতি", "আইনসভা, নির্বাহী বিভাগ ও বিচার বিভাগের ভূমিকা"),
            "সংবিধানের অনুচ্ছেদ, সংশোধনী ও প্রতিষ্ঠানের ক্ষমতা পড়ার সময় মূল পাঠ ও সংশোধনের তারিখ মিলিয়ে নিন।",
            "কোনো সাংবিধানিক বিধান বাস্তবে কীভাবে প্রয়োগ হয়, উদাহরণসহ ব্যাখ্যা করুন।"
        ),
        "অর্থনীতি ও ব্যাংকিং" to TopicGuide(
            "উৎপাদন, আয়, মূল্যস্তর, কর্মসংস্থান, মুদ্রা, ঋণ ও আর্থিক প্রতিষ্ঠানের পারস্পরিক সম্পর্ক নিয়ে এই পাঠ।",
            listOf("চাহিদা, যোগান ও বাজার", "মুদ্রানীতি ও রাজস্বনীতি", "ব্যাংকের ভূমিকা, ঋণ ও আর্থিক অন্তর্ভুক্তি"),
            "সংজ্ঞা, পরিমাপের একক, সময়কাল এবং বাংলাদেশ ব্যাংক বা পরিসংখ্যান সংস্থার মূল প্রকাশনা আলাদা করে যাচাই করুন।",
            "মূল্যস্ফীতি বাড়লে পরিবার, ব্যবসা ও নীতিনির্ধারণে কী ধরনের প্রভাব পড়তে পারে?"
        ),
        "সমাজ ও উন্নয়ন" to TopicGuide(
            "সামাজিক প্রতিষ্ঠান, বৈষম্য, দারিদ্র্য, জনসংখ্যা, সামাজিক সুরক্ষা এবং উন্নয়নের সুযোগ ও বাধা বিশ্লেষণ করা হয়।",
            listOf("সামাজিক স্তরবিন্যাস ও বৈষম্য", "দারিদ্র্য ও সামাজিক সুরক্ষা", "অংশগ্রহণমূলক ও টেকসই উন্নয়ন"),
            "ধারণার সংজ্ঞা, সূচকের পদ্ধতি এবং জাতীয় ও আন্তর্জাতিক তথ্যের তুলনাযোগ্যতা বুঝে পড়ুন।",
            "একটি উন্নয়ন উদ্যোগের সুফল কারা পায় এবং কারা বাদ পড়ে যেতে পারে?"
        ),
        "পরিবেশ ও জলবায়ু" to TopicGuide(
            "পরিবেশগত পরিবর্তন, জলবায়ু ঝুঁকি, অভিযোজন, প্রশমন এবং প্রাকৃতিক সম্পদ ব্যবস্থাপনার বিষয়গুলো এখানে যুক্ত।",
            listOf("জলবায়ু পরিবর্তন ও ঝুঁকি", "অভিযোজন ও প্রশমন", "দুর্যোগ প্রস্তুতি ও পরিবেশ শাসন"),
            "বৈজ্ঞানিক পর্যবেক্ষণ, নীতিগত লক্ষ্য এবং পূর্বাভাসকে একই ধরনের তথ্য হিসেবে ধরবেন না।",
            "জলবায়ু ঝুঁকি কমাতে স্থানীয় অভিযোজন ও জাতীয় নীতির মধ্যে কী সম্পর্ক থাকা দরকার?"
        ),
        "শিক্ষা ও স্বাস্থ্য" to TopicGuide(
            "মানব উন্নয়নের দুইটি ভিত্তি হিসেবে শিক্ষা ও স্বাস্থ্যসেবার প্রবেশাধিকার, মান, অর্থায়ন এবং বৈষম্য আলোচিত হয়।",
            listOf("শিক্ষায় প্রবেশাধিকার ও শেখার ফল", "প্রাথমিক স্বাস্থ্যসেবা", "জনস্বাস্থ্য, পুষ্টি ও স্বাস্থ্যসমতা"),
            "ভর্তি বা সেবাগ্রহণের হারকে শিক্ষার মান বা স্বাস্থ্যফলের সরাসরি বিকল্প হিসেবে ব্যবহার করবেন না।",
            "সেবা আছে, কিন্তু সবাই সমানভাবে ব্যবহার করতে পারে না, এমন পরিস্থিতির কারণ বিশ্লেষণ করুন।"
        ),
        "আইন ও বিচার" to TopicGuide(
            "আইনের শাসন, বিচারিক স্বাধীনতা, ন্যায়বিচারে প্রবেশাধিকার এবং বিরোধ নিষ্পত্তির প্রতিষ্ঠান এই বিষয়ের কেন্দ্র।",
            listOf("আইনের শাসন ও due process", "আদালতের কাঠামো ও এখতিয়ার", "ন্যায়বিচারে প্রবেশাধিকার"),
            "আইনের ধারা, আদালতের রায় এবং নীতিগত মতামতকে পৃথক ধরনের উৎস হিসেবে শনাক্ত করুন।",
            "ন্যায়বিচারে প্রবেশাধিকার বাড়াতে আইনি সহায়তা কেন প্রয়োজন হতে পারে?"
        ),
        "জাতিসংঘ ও আন্তর্জাতিক সংস্থা" to TopicGuide(
            "আন্তর্জাতিক সহযোগিতা, শান্তি ও নিরাপত্তা, উন্নয়ন এবং মানবিক কার্যক্রমে বিভিন্ন সংস্থার ম্যান্ডেট ও সীমাবদ্ধতা বোঝা হয়।",
            listOf("জাতিসংঘ সনদ ও প্রধান অঙ্গ", "বিশেষায়িত সংস্থার ম্যান্ডেট", "সদস্য রাষ্ট্র ও আন্তর্জাতিক সহযোগিতা"),
            "প্রতিটি সংস্থার আনুষ্ঠানিক ম্যান্ডেট, সদস্যপদ এবং সিদ্ধান্ত গ্রহণের ক্ষমতা আলাদা করে পড়ুন।",
            "একটি আন্তর্জাতিক সংস্থা কোন কাজ করতে পারে, আর কোন কাজের জন্য রাষ্ট্রের সহযোগিতা প্রয়োজন?"
        ),
        "আন্তর্জাতিক আইন ও চুক্তি" to TopicGuide(
            "রাষ্ট্রের পারস্পরিক সম্পর্ক, আন্তর্জাতিক বাধ্যবাধকতা, চুক্তি, প্রথাগত আইন এবং বিরোধ নিষ্পত্তির নিয়ম এই পাঠের অংশ।",
            listOf("চুক্তি ও সম্মতি", "রাষ্ট্রের দায়বদ্ধতা", "আন্তর্জাতিক আদালতের এখতিয়ার"),
            "চুক্তির পক্ষ হওয়া, সংরক্ষণ, কার্যকারিতা এবং আদালতের এখতিয়ারকে এক বিষয় হিসেবে গুলিয়ে ফেলবেন না।",
            "আন্তর্জাতিক চুক্তি ভঙ্গের অভিযোগ উঠলে দায় নির্ধারণে কী কী প্রশ্ন দেখা হয়?"
        ),
        "বিশ্ব অর্থনীতি" to TopicGuide(
            "বাণিজ্য, বিনিয়োগ, বিনিময় হার, বৈশ্বিক সরবরাহব্যবস্থা এবং আন্তর্জাতিক আর্থিক প্রতিষ্ঠানের ভূমিকা এখানে আলোচিত হয়।",
            listOf("আন্তর্জাতিক বাণিজ্য ও শুল্ক", "বিনিময় হার ও মূলধন প্রবাহ", "বৈশ্বিক অর্থনৈতিক প্রতিষ্ঠান"),
            "নামমাত্র ও প্রকৃত মান, মুদ্রা ও ভিত্তিবছর, এবং তথ্যের প্রকাশকাল উল্লেখ করে তুলনা করুন।",
            "বিশ্ববাজারের একটি পরিবর্তন বাংলাদেশের আমদানি, রপ্তানি বা কর্মসংস্থানে কীভাবে প্রভাব ফেলতে পারে?"
        ),
        "জলবায়ু ও পরিবেশ" to TopicGuide(
            "জলবায়ু ব্যবস্থা, জীববৈচিত্র্য, দূষণ, অভিযোজন এবং আন্তর্জাতিক পরিবেশ শাসনের যোগসূত্র এই বিষয়ের কেন্দ্র।",
            listOf("জলবায়ু বিজ্ঞান ও প্রভাব", "জীববৈচিত্র্য ও বাস্তুতন্ত্র", "জলবায়ু অর্থায়ন ও নীতিমালা"),
            "পর্যবেক্ষিত তথ্য, মডেলভিত্তিক প্রক্ষেপণ এবং রাজনৈতিক অঙ্গীকারের পার্থক্য বজায় রাখুন।",
            "জলবায়ু অর্থায়নের প্রয়োজন, উৎস এবং জবাবদিহি কীভাবে মূল্যায়ন করা যায়?"
        ),
        "দক্ষিণ এশিয়া" to TopicGuide(
            "দক্ষিণ এশিয়ার রাষ্ট্রগুলোর ইতিহাস, ভূগোল, আঞ্চলিক সহযোগিতা, বাণিজ্য, নিরাপত্তা ও অভিন্ন চ্যালেঞ্জ নিয়ে এই পাঠ।",
            listOf("ঐতিহাসিক ও ভৌগোলিক প্রেক্ষাপট", "আঞ্চলিক সহযোগিতা ও সংযোগ", "পানি, বাণিজ্য ও অভিবাসন"),
            "দেশভিত্তিক তথ্যের বছর ও সংজ্ঞা মিলিয়ে নিন; আঞ্চলিক গড় সব দেশের বাস্তবতা দেখায় না।",
            "আঞ্চলিক সহযোগিতার একটি ক্ষেত্র বেছে নিয়ে সুযোগ ও সীমাবদ্ধতা লিখুন।"
        ),
        "ভূরাজনীতি" to TopicGuide(
            "ভূগোল, সম্পদ, নিরাপত্তা, বাণিজ্যপথ, জোট এবং রাষ্ট্রীয় স্বার্থ কীভাবে আন্তর্জাতিক সম্পর্ককে প্রভাবিত করে, তা বিশ্লেষণ করা হয়।",
            listOf("ভূগোল ও কৌশলগত অবস্থান", "জ্বালানি ও বাণিজ্যপথ", "জোট, নিরাপত্তা ও কূটনীতি"),
            "বিশ্লেষণ, সরকারি অবস্থান এবং যাচাইকৃত ঘটনাকে আলাদা করে উপস্থাপন করুন।",
            "কোনো ভৌগোলিক অবস্থান কীভাবে একটি রাষ্ট্রের কূটনৈতিক গুরুত্ব বাড়াতে পারে?"
        )
    )
    return guides[topic] ?: TopicGuide(
        "এই বিষয়ের নির্দিষ্ট পাঠ সাজানোর জন্য যাচাইকৃত বিষয়বস্তু যুক্ত করা প্রয়োজন।",
        listOf("মৌলিক সংজ্ঞা", "প্রধান প্রতিষ্ঠান", "সাম্প্রতিক প্রেক্ষাপট"),
        "পরীক্ষার আগে নির্ভরযোগ্য মূল উৎস ও হালনাগাদ পাঠ্যসূচি মিলিয়ে নিন।",
        "বিষয়টির একটি মূল ধারণা নিজের ভাষায় ব্যাখ্যা করুন।"
    )
}

@Composable internal fun TopicDetail(nav: NavHostController) {
    val context = LocalContext.current
    val topic = LocalLearningStore.getSelectedTopic(context).ifBlank { "সংবিধান ও সরকারব্যবস্থা" }
    val guide = topicGuide(topic)
    Scroll {
        Intro(topic, "মৌলিক ধারণা, বিশ্লেষণের দিক এবং পরীক্ষার প্রস্তুতি এক জায়গায়।")
        Panel {
            Kicker("বিষয়ভিত্তিক পাঠ • স্থানীয় গাইড")
            Text(guide.overview, lineHeight = 23.sp)
            AssistChip(onClick = {}, enabled = false, label = { Text("যাচাইকৃত সাম্প্রতিক সংবাদ নয়") })
        }
        Text("যে ধারণাগুলো বুঝে নেবেন", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        guide.concepts.forEachIndexed { index, concept ->
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
                Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(9.dp)) {
                    Text("${index + 1}", Modifier.padding(horizontal = 11.dp, vertical = 7.dp), color = MaterialTheme.colorScheme.onSecondaryContainer, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(10.dp))
                Text(concept, Modifier.weight(1f), fontWeight = FontWeight.Medium, lineHeight = 21.sp)
            }
        }
        Panel {
            Kicker("BCS ও BANK প্রস্তুতি")
            Text("পরীক্ষায় কীভাবে পড়বেন", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text(guide.examFocus, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 21.sp)
        }
        Panel {
            Kicker("নিজেকে যাচাই করুন")
            Text(guide.readingPrompt, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp)
            Text("এটি অনুশীলনের নির্দেশনা, যাচাইকৃত পরীক্ষার প্রশ্ন নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            Primary("লিখিত অনুশীলন খুলুন") { nav.navigate(AppDestination.Writing.route) }
        }
        Text("এই গাইডটি অ্যাপের স্থানীয় শিক্ষামূলক কাঠামো। নির্দিষ্ট আইন, পরিসংখ্যান, সাম্প্রতিক ঘটনা ও উৎসের লিংক আলাদাভাবে যাচাই করে যুক্ত করতে হবে.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, lineHeight = 17.sp)
        RowLink("সংশ্লিষ্ট সংবাদ", "এই বিষয়ের সাম্প্রতিক ঘটনা", Icons.AutoMirrored.Filled.Article) { nav.navigate(AppDestination.Feed.route) }
        RowLink("জ্ঞান মানচিত্র", "প্রতিষ্ঠান ও ধারণার সম্পর্ক", Icons.Default.Hub) { nav.navigate(AppDestination.Graph.route) }
        RowLink("তথ্য ও পরিসংখ্যান", "উৎসসহ প্রাসঙ্গিক সূচক", Icons.Default.BarChart) { nav.navigate(AppDestination.Data.route) }
        RowLink("অনুশীলন", "সাধারণ BCS ও Bank নমুনা অনুশীলন", Icons.Default.EditNote) { nav.navigate(AppDestination.QuizSetup.route) }
    }
}

@Composable internal fun Sources(nav: NavHostController) {
    val context = LocalContext.current
    val story = EditionFeedRepository.story(context, EditionFeedRepository.selectedStoryId(context))
    Scroll {
        Intro("উৎস ও যাচাই", "নির্বাচিত প্রতিবেদনের মূল প্রকাশনা ও লিংক দেখুন।")
        if (story == null || story.sources.isEmpty()) {
            Panel {
                Text("এই প্রতিবেদনের উৎস-তালিকা পাওয়া যায়নি", fontWeight = FontWeight.SemiBold)
                Text("প্রতিবেদন থেকে আবার খুলুন অথবা প্রকাশিত সংস্করণ হালনাগাদ করুন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            story.sources.forEach { source ->
                Panel {
                    Kicker(source.publication)
                    Text(source.headline, fontWeight = FontWeight.SemiBold)
                    Text(source.date, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    TextButton(onClick = {
                        runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(source.url))) }
                    }) {
                        Icon(Icons.Default.OpenInNew, null, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(5.dp))
                        Text("মূল প্রতিবেদন খুলুন")
                    }
                }
            }
        }
        RowLink("উৎসের রেকর্ড", "প্রকাশক, অনুমতি ও মূল URL", Icons.Default.Link) { nav.navigate(AppDestination.SourceDetail.route) }
    }
}

@Composable internal fun ReadingHistory(nav: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val recent = remember { mutableStateListOf<String>().apply { addAll(LocalLearningStore.getRecentArticles(context)) } }
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                recent.clear()
                recent.addAll(LocalLearningStore.getRecentArticles(context))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    Scroll {
        Intro("পড়ার ইতিহাস", "সম্প্রতি খোলা পাঠে ফিরে যান।")
        if (recent.isEmpty()) {
            Panel { Text("এখনো কোনো পড়ার ইতিহাস নেই", fontWeight = FontWeight.Bold); Text("কোনো নিবন্ধ খুললে সেটি এখানে দেখা যাবে।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else {
            recent.forEach { id ->
                val story = EditionFeedRepository.story(context, id)
                Panel {
                    Kicker("সম্প্রতি পড়া")
                    Text(story?.headline ?: if (id == "sample-international-justice") "গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন" else id,
                        fontWeight = FontWeight.SemiBold)
                    if (story != null || id == "sample-international-justice") {
                        TextButton(onClick = {
                            EditionFeedRepository.selectStory(context, id)
                            nav.navigate(AppDestination.Article.route)
                        }) { Text("নিবন্ধটি খুলুন") }
                    } else {
                        Text("এই পাঠের বিস্তারিত এখন আর উপলভ্য নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                }
            }
            TextButton(onClick = { LocalLearningStore.clearReadingHistory(context); recent.clear() }) { Text("পড়ার ইতিহাস মুছুন") }
        }
        Text("ইতিহাস এই ডিভাইসে সংরক্ষিত হয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
    }
}

@Composable internal fun SavedCollections(nav: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val articles = remember { mutableStateListOf<String>().apply { addAll(LocalLearningStore.getSavedArticles(context).sorted()) } }
    val words = remember { mutableStateListOf<String>().apply { addAll(LocalLearningStore.getBookmarkedWords(context).sorted()) } }

    // This destination can remain on the back stack while bookmarks change elsewhere.
    // Refresh on return so the collection never presents a stale snapshot.
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                articles.clear()
                articles.addAll(LocalLearningStore.getSavedArticles(context).sorted())
                words.clear()
                words.addAll(LocalLearningStore.getBookmarkedWords(context).sorted())
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scroll {
        Intro("সংরক্ষিত সংগ্রহ", "সংবাদ ও শব্দের বুকমার্ক এখানে একসঙ্গে গুছিয়ে রাখা হয়েছে।")
        Text("সংরক্ষিত নিবন্ধ • ${articles.size}", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        if (articles.isEmpty()) Panel { Text("কোনো নিবন্ধ সংরক্ষিত নেই", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        articles.forEach { id ->
            val story = EditionFeedRepository.story(context, id)
            Panel {
                Kicker("সংরক্ষিত নিবন্ধ")
                Text(story?.headline ?: if (id == "sample-international-justice") "গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন" else id,
                    fontWeight = FontWeight.SemiBold)
                if (story != null || id == "sample-international-justice") {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TextButton(
                            onClick = {
                                EditionFeedRepository.selectStory(context, id)
                                nav.navigate(AppDestination.Article.route)
                            },
                            modifier = Modifier.fillMaxWidth().testTag("saved-article-open")
                        ) { Text("নিবন্ধটি খুলুন") }
                        TextButton(
                            onClick = {
                                LocalLearningStore.setArticleSaved(context, id, false)
                                articles.remove(id)
                            },
                            modifier = Modifier.fillMaxWidth().testTag("saved-article-remove")
                        ) { Text("সংরক্ষণ সরান") }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("এই নিবন্ধের বিস্তারিত এখনো উপলভ্য নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        TextButton(onClick = {
                            LocalLearningStore.setArticleSaved(context, id, false)
                            articles.remove(id)
                        }) { Text("সংরক্ষণ সরান") }
                    }
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Text("বুকমার্ক করা শব্দ • ${words.size}", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        if (words.isEmpty()) Panel { Text("কোনো শব্দ বুকমার্ক করা নেই", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        words.forEach { word ->
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(word, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                TextButton(onClick = {
                    nav.currentBackStackEntry?.savedStateHandle?.set("selected_word", word)
                    nav.navigate(AppDestination.WordDetail.route)
                }) { Text("শব্দটি খুলুন") }
                TextButton(onClick = {
                    LocalLearningStore.setWordBookmarked(context, word, false)
                    words.remove(word)
                }) { Text("সরান") }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
        TextButton(onClick = { nav.navigate(AppDestination.Vocabulary.route) }) { Text("Vocabulary Lab খুলুন") }
        Text("সংগ্রহগুলো এই ডিভাইসে সংরক্ষিত। কাস্টম ফোল্ডার ও ক্লাউড সিঙ্ক এখনো যুক্ত হয়নি।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
    }
}

@Composable internal fun SourceDetail() {
    Scroll {
        Intro("উৎসের পরিচিতি", "প্রকাশক ও মূল নথির নির্ভরযোগ্যতা যাচাইয়ের তথ্য।")
        Panel { Text("উৎসের রেকর্ড অপেক্ষমাণ", fontWeight = FontWeight.Bold); Text("প্রকাশকের নাম, মূল URL, প্রকাশকাল, প্রবেশযোগ্যতা, ব্যবহারের অনুমতি এবং সংশ্লিষ্ট দাবির তালিকা প্রকৃত উৎস-রেকর্ড থেকে আসবে।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable internal fun IndicatorDetail() {
    Scroll {
        Intro("সূচকের বিস্তারিত", "সংখ্যা বোঝার আগে সংজ্ঞা, একক ও সময়কাল যাচাই করুন।")
        Panel { Text("ডেটা সংযুক্ত নয়", fontWeight = FontWeight.Bold); Text("সূচকের মান, সময়সীমা, একক, পদ্ধতি, সংশোধনের ইতিহাস এবং মূল প্রকাশনা যুক্ত হলে এখানে দেখানো হবে।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Text("কোনো চার্ট বা মান দেখানো হচ্ছে না, কারণ যাচাইকৃত ডেটা এখনো সংযুক্ত নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

 
@Preview(name = "Home / Light", showBackground = true, widthDp = 393, heightDp = 852)
@Composable private fun HomeLightPreview() {
    SarangshoTheme(darkTheme = false) { Home(rememberNavController()) }
}

@Preview(name = "Home / Dark", showBackground = true, widthDp = 393, heightDp = 852)
@Composable private fun HomeDarkPreview() {
    SarangshoTheme(darkTheme = true) { Home(rememberNavController()) }
}

@Preview(name = "Article / Light", showBackground = true, widthDp = 393, heightDp = 852)
@Composable private fun ArticleLightPreview() {
    SarangshoTheme(darkTheme = false) { Article(rememberNavController()) }
}
