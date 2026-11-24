package com.sarangsho.plus.features.learning
import com.sarangsho.plus.data.LocalLearningStore
import com.sarangsho.plus.ui.components.*

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sarangsho.plus.ui.theme.SarangshoPalette


private data class VocabularyEntry(
    val term: String,
    val partOfSpeech: String,
    val pronunciation: String,
    val banglaMeaning: String,
    val definition: String,
    val synonyms: String,
    val antonym: String,
    val example: String
)

private val vocabularyEntries = listOf(
    VocabularyEntry(
        "Resilience", "Noun", "/rɪˈzɪliəns/",
        "প্রতিকূলতা কাটিয়ে ওঠার সক্ষমতা",
        "The ability to recover or adapt after difficulty.",
        "adaptability, toughness", "fragility",
        "Communities need resilience after a natural disaster."
    ),
    VocabularyEntry(
        "Accountability", "Noun", "/əˌkaʊntəˈbɪləti/",
        "জবাবদিহিতা; নিজের কাজের দায় নেওয়া",
        "The duty to explain actions and accept responsibility.",
        "responsibility, answerability", "irresponsibility",
        "Public accountability helps strengthen democratic institutions."
    ),
    VocabularyEntry(
        "Sustainable", "Adjective", "/səˈsteɪnəbl/",
        "টেকসই; দীর্ঘদিন বজায় রাখা সম্ভব",
        "Able to continue over time without exhausting resources.",
        "viable, maintainable", "unsustainable",
        "A sustainable policy considers both people and the environment."
    ),
    VocabularyEntry(
        "Mitigate", "Verb", "/ˈmɪtɪɡeɪt/",
        "প্রশমিত করা; ক্ষতি বা তীব্রতা কমানো",
        "To make a harmful effect less severe.",
        "reduce, alleviate", "aggravate",
        "Early warnings can mitigate the impact of floods."
    ),
    VocabularyEntry(
        "Consensus", "Noun", "/kənˈsensəs/",
        "ঐকমত্য; সম্মিলিত মত",
        "A general agreement reached by a group.",
        "agreement, accord", "disagreement",
        "The committee reached a consensus after discussion."
    )
)

internal fun searchVocabulary(query: String): List<Pair<String, String>> {
    if (query.isBlank()) return emptyList()
    return vocabularyEntries.filter { entry ->
        entry.term.contains(query, ignoreCase = true) ||
            entry.banglaMeaning.contains(query, ignoreCase = true) ||
            entry.definition.contains(query, ignoreCase = true)
    }.map { it.term to it.banglaMeaning }
}

@Composable internal fun LearningHub(nav: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var savedWordCount by remember { mutableIntStateOf(LocalLearningStore.getBookmarkedWords(context).size) }
    var completedTaskCount by remember { mutableIntStateOf(LocalLearningStore.getCompletedStudyTasks(context).size) }
    var practiceSessionCount by remember { mutableIntStateOf(LocalLearningStore.getPracticeHistory(context).size) }
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                savedWordCount = LocalLearningStore.getBookmarkedWords(context).size
                completedTaskCount = LocalLearningStore.getCompletedStudyTasks(context).size
                practiceSessionCount = LocalLearningStore.getPracticeHistory(context).size
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    Scroll {
        Intro("শিখুন", "সংবাদ থেকে পাওয়া জ্ঞানকে শব্দভান্ডার, অনুশীলন ও রিভিশনের মাধ্যমে স্থায়ী করুন।")
        Panel {
            Kicker("আপনার শেখার হিসাব • এই ডিভাইস")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(Modifier.weight(1f).heightIn(min = 64.dp).clickable(role = Role.Button) { nav.navigate("saved") }.padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(savedWordCount.toString(), fontSize = 23.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("সংরক্ষিত শব্দ", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(Modifier.weight(1f).heightIn(min = 64.dp).clickable(role = Role.Button) { nav.navigate("study-plan") }.padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(completedTaskCount.toString(), fontSize = 23.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("সম্পন্ন পড়ার কাজ", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(Modifier.weight(1f).heightIn(min = 64.dp).clickable(role = Role.Button) { nav.navigate("practice-history") }.padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(practiceSessionCount.toString(), fontSize = 23.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("অনুশীলন সেশন", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text(
                "এগুলো শুধু এই ডিভাইসে সংরক্ষিত কার্যকলাপের হিসাব; কোনো ক্লাউড বা পরীক্ষার ফল নয়।",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
        Panel {
            Kicker("VOCABULARY LAB")
            Text("শব্দভান্ডার গড়ে তুলুন", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("বাংলা অর্থ, ইংরেজি সংজ্ঞা, সমার্থক শব্দ, collocation এবং বাস্তব বাক্যে ব্যবহার শিখুন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Primary("শব্দ শেখা শুরু করুন") { nav.navigate("vocabulary") }
        }
        Panel {
            Kicker("BCS • BANK")
            Text("পরীক্ষার অনুশীলন", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("বিষয় ও সময় বেছে MCQ অনুশীলন করুন। প্রকৃত প্রশ্নব্যাংক সংযুক্ত না হওয়া পর্যন্ত নমুনা প্রশ্ন স্পষ্টভাবে চিহ্নিত থাকবে।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Primary("অনুশীলন সাজান") { nav.navigate("quiz-setup") }
        }
        Panel {
            Kicker("WRITTEN PRACTICE")
            Text("লিখিত উত্তরের চর্চা", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Text("বিশ্লেষণী প্রশ্নে নিজের ভাষায় উত্তর লিখুন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedButton(onClick = { nav.navigate("writing") }, modifier = Modifier.fillMaxWidth()) { Text("লিখিত অনুশীলন খুলুন") }
        }
        Panel {
            Kicker("ACTIVE RECALL")
            Text("রিভিশন ডেক", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Text("ফ্ল্যাশকার্ডের মাধ্যমে শেখা তথ্য মনে করার অনুশীলন করুন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedButton(onClick = { nav.navigate("revision") }, modifier = Modifier.fillMaxWidth()) { Text("রিভিশন শুরু করুন") }
        }
        Panel {
            Kicker("STUDY PLAN")
            Text("পড়ার পরিকল্পনা", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Text("আজকের পড়া ও অনুশীলন নিজের সুবিধামতো সাজান।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedButton(onClick = { nav.navigate("study-plan") }, modifier = Modifier.fillMaxWidth()) { Text("পরিকল্পনা দেখুন") }
        }
    }
}

@Composable internal fun Vocabulary(nav: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val bookmarkedWords = remember {
        mutableStateListOf<String>().apply {
            addAll(LocalLearningStore.getBookmarkedWords(context))
        }
    }
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                bookmarkedWords.clear()
                bookmarkedWords.addAll(LocalLearningStore.getBookmarkedWords(context))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    var query by rememberSaveable { mutableStateOf("") }
    val filteredWords = vocabularyEntries.filter { entry ->
        query.isBlank() ||
            entry.term.contains(query, ignoreCase = true) ||
            entry.banglaMeaning.contains(query, ignoreCase = true)
    }
    Scroll {
        Intro("Vocabulary Lab", "অর্থ, ব্যবহার ও পরীক্ষায় প্রয়োগ শিখুন। শব্দ বুকমার্ক করলে Saved-এ পাবেন।")
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("শব্দ বা বাংলা অর্থ খুঁজুন") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )
        Text("${filteredWords.size}টি শব্দ", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        if (filteredWords.isEmpty()) {
            Panel {
                Text("কোনো শব্দ পাওয়া যায়নি", fontWeight = FontWeight.SemiBold)
                Text("অন্য বানান বা বাংলা অর্থ দিয়ে খুঁজুন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        filteredWords.forEach { entry ->
            var reveal by rememberSaveable(entry.term) { mutableStateOf(false) }
            val bookmarked = entry.term in bookmarkedWords
            Panel {
                Kicker(entry.partOfSpeech.uppercase())
                Text(entry.term, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(entry.pronunciation, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                if (reveal) {
                    Text("বাংলা অর্থ: ${entry.banglaMeaning}", fontWeight = FontWeight.SemiBold)
                    Text("Definition: ${entry.definition}")
                    Text("Synonyms: ${entry.synonyms}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    Text("Antonym: ${entry.antonym}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    Text("Example: ${entry.example}")
                } else {
                    Text(entry.banglaMeaning, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = { reveal = !reveal }, modifier = Modifier.weight(1f)) {
                        Text(if (reveal) "সংক্ষিপ্ত করুন" else "বিস্তারিত দেখুন")
                    }
                    OutlinedButton(onClick = {
                        val shouldBookmark = !bookmarked
                        LocalLearningStore.setWordBookmarked(context, entry.term, shouldBookmark)
                        if (shouldBookmark) bookmarkedWords.add(entry.term)
                        else bookmarkedWords.remove(entry.term)
                    }) {
                        Icon(
                            if (bookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (bookmarked) "বুকমার্ক করা" else "বুকমার্ক করুন"
                        )
                        Text(if (bookmarked) "সংরক্ষিত" else "সংরক্ষণ")
                    }
                }
                OutlinedButton(
                    onClick = {
                        nav.currentBackStackEntry?.savedStateHandle?.set("selected_word", entry.term)
                        nav.navigate("word-detail")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("পূর্ণাঙ্গ পাঠ খুলুন")
                }
            }
        }
        Text(
            "শব্দগুলো প্রাথমিক শেখার নমুনা। প্রতিটি শব্দের জন্য অভিধান-উৎস ও পরীক্ষার রেফারেন্স যুক্ত করা এখনো বাকি।",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}

@Composable internal fun Challenge(nav: NavHostController) {
    var selected by rememberSaveable { mutableIntStateOf(-1) }
    val options = listOf("৪ নভেম্বর ১৯৭২", "১৬ ডিসেম্বর ১৯৭২", "২৬ মার্চ ১৯৭১", "১০ জানুয়ারি ১৯৭২")
    Scroll {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Kicker("BCS • BANK CHALLENGE")
            AssistChip(onClick = {}, enabled = false, label = { Text("নমুনা প্রশ্ন") })
        }
        Text("বাংলাদেশের সংবিধান", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(9.dp)) { Text("প্রশ্ন ১ / ১", Modifier.padding(9.dp), color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.SemiBold, fontSize = 11.sp) }
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(9.dp)) { Text("সময় নির্ধারিত নয়", Modifier.padding(9.dp), fontSize = 11.sp) }
        }
        Panel {
            Text("বাংলাদেশের সংবিধান কবে গণপরিষদে গৃহীত হয়?", fontSize = 19.sp, fontWeight = FontWeight.Bold, lineHeight = 29.sp)
            options.forEachIndexed { i, option ->
                Surface(
                    color = if (selected == i) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (selected == i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth().clickable { selected = i }
                ) {
                    Row(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = if (selected == i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(50.dp)) {
                            Text(('A' + i).toString(), Modifier.padding(horizontal = 9.dp, vertical = 5.dp), color = if (selected == i) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(option, Modifier.weight(1f), fontWeight = if (selected == i) FontWeight.SemiBold else FontWeight.Normal)
                        RadioButton(selected == i, { selected = i })
                    }
                }
            }
            Primary("উত্তর জমা দিন") {
                if (selected >= 0) {
                    nav.currentBackStackEntry?.savedStateHandle?.set("selectedAnswer", selected)
                    nav.navigate("answer")
                }
            }
            Text("নমুনা প্রশ্ন; প্রকৃত পরীক্ষার প্রশ্ন বা পূর্বাভাস নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }
    }
}

@Composable internal fun Answer(nav: NavHostController) {
    val selectedAnswer = nav.previousBackStackEntry?.savedStateHandle?.get<Int>("selectedAnswer") ?: -1
    val correctAnswer = 0
    val options = listOf("৪ নভেম্বর ১৯৭২", "১৬ ডিসেম্বর ১৯৭২", "২৬ মার্চ ১৯৭১", "১০ জানুয়ারি ১৯৭২")
    val isCorrect = selectedAnswer == correctAnswer
    Scroll {
        Panel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Info,
                    null,
                    tint = if (isCorrect) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text("উত্তর ব্যাখ্যা", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                if (isCorrect) "আপনার উত্তর সঠিক" else "আপনার উত্তর সঠিক নয়",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCorrect) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
            )
            if (selectedAnswer in options.indices) {
                Text("আপনার উত্তর: ${options[selectedAnswer]}")
            } else {
                Text("আপনার উত্তর পাওয়া যায়নি।")
            }
            Text("সঠিক উত্তর: ${options[correctAnswer]}", fontWeight = FontWeight.SemiBold)
            Text("গণপরিষদে বাংলাদেশের সংবিধান ১৯৭২ সালের ৪ নভেম্বর গৃহীত হয় এবং ১৬ ডিসেম্বর ১৯৭২ কার্যকর হয়। গৃহীত হওয়ার তারিখ ও কার্যকর হওয়ার তারিখ আলাদা।")
            Text("পরীক্ষার কৌশল: ‘গৃহীত’, ‘স্বাক্ষরিত’ ও ‘কার্যকর’ শব্দের পার্থক্য খেয়াল করুন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Primary("আরও অনুশীলন") { nav.navigate("challenge") }
        OutlinedButton(
            onClick = {
                nav.currentBackStackEntry?.savedStateHandle?.set("correctCount", if (isCorrect) 1 else 0)
                nav.currentBackStackEntry?.savedStateHandle?.set("questionCount", 1)
                // Reset the guard for every attempt, including a reused answer destination.
                nav.currentBackStackEntry?.savedStateHandle?.set("practiceAttemptRecorded", false)
                nav.navigate("quiz-results")
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("সেশন শেষ করুন") }
        OutlinedButton(onClick = { nav.navigate("revision") }, modifier = Modifier.fillMaxWidth()) { Text("রিভিশনে যোগ করুন") }
    }
}

@Composable internal fun Writing() {
    val context = LocalContext.current
    var draft by rememberSaveable { mutableStateOf(LocalLearningStore.getWritingDraft(context)) }
    var saved by rememberSaveable { mutableStateOf(true) }
    Scroll {
        Intro("লিখিত অনুশীলন", "নিজের ভাষায় উত্তর লিখুন। খসড়া এই ডিভাইসে সংরক্ষণ করা যাবে।")
        Panel {
            Kicker("বিশ্লেষণী প্রশ্ন • নমুনা")
            Text(
                "জলবায়ু পরিবর্তন বাংলাদেশের উন্নয়ন পরিকল্পনায় কী ধরনের চ্যালেঞ্জ তৈরি করে? উদাহরণসহ আলোচনা করুন।",
                fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 28.sp
            )
            Text("উত্তরের কাঠামো: ভূমিকা, প্রভাব, বাংলাদেশের প্রেক্ষাপট, নীতিগত করণীয় ও উপসংহার।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 19.sp)
        }
        OutlinedTextField(
            value = draft,
            onValueChange = { draft = it; saved = false },
            modifier = Modifier.fillMaxWidth().height(250.dp),
            label = { Text("আপনার উত্তর লিখুন") },
            placeholder = { Text("ভূমিকা দিয়ে শুরু করুন...") },
            supportingText = { Text("${draft.length} অক্ষর") }
        )
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                if (saved) "এই ডিভাইসে সংরক্ষিত" else "পরিবর্তন সংরক্ষণ করা হয়নি",
                color = if (saved) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
            TextButton(onClick = { draft = ""; LocalLearningStore.setWritingDraft(context, ""); saved = true }) { Text("মুছে ফেলুন") }
        }
        Button(
            onClick = {
                LocalLearningStore.setWritingDraft(context, draft)
                saved = true
            },
            enabled = !saved && draft.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Save, null)
            Spacer(Modifier.width(8.dp))
            Text("খসড়া সংরক্ষণ করুন")
        }
        Text("খসড়াটি শুধু এই ডিভাইসে থাকে। অ্যাকাউন্ট বা ক্লাউডে সিঙ্ক হয় না এবং স্বয়ংক্রিয় মূল্যায়নও চালু নেই।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, lineHeight = 17.sp)
    }
}


@Composable internal fun Revision() {
    var flipped by rememberSaveable { mutableStateOf(false) }
    Scroll {
        Intro("রিভিশন ডেক", "সক্রিয় স্মরণ দিয়ে শেখা বিষয় মনে রাখুন।")
        Panel {
            Kicker("নমুনা কার্ড")
            Text("এই ডেমো সেশনে ব্যক্তিগত অগ্রগতি গণনা বা সংরক্ষণ করা হচ্ছে না।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 19.sp)
        }
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(if (flipped) "উত্তর" else "প্রশ্ন", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(
                    if (flipped) "জাতিসংঘ প্রতিষ্ঠিত হয় ১৯৪৫ সালের ২৪ অক্টোবর।" else "জাতিসংঘ কবে প্রতিষ্ঠিত হয়?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )
                Text(
                    if (flipped) "২৪ অক্টোবর, United Nations Day." else "মনে করার চেষ্টা করুন, তারপর উত্তর দেখুন।",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
                OutlinedButton(onClick = { flipped = !flipped }, modifier = Modifier.fillMaxWidth()) {
                    Icon(if (flipped) Icons.Default.Refresh else Icons.Default.Visibility, null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (flipped) "আবার প্রশ্ন দেখুন" else "উত্তর দেখুন")
                }
            }
        }
        Text(
            "সম্পূর্ণ রিভিশন সেশন, কার্ডের সংখ্যা ও শেখার অগ্রগতি ব্যক্তিগত স্টাডি ডেটা সংযুক্ত হলে দেখানো হবে।",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp,
            lineHeight = 17.sp
        )
    }
}

@Composable internal fun QuizSetup(nav: NavHostController) {
    var exam by rememberSaveable { mutableStateOf("BCS") }; var duration by rememberSaveable { mutableStateOf("১৫ মিনিট") }
    Scroll {
        Intro("অনুশীলন সাজান", "আপনার প্রস্তুতির ধরন অনুযায়ী প্রশ্ন বেছে নিন।")
        Text("পরীক্ষা", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("BCS", "Bank", "Mixed").forEach { FilterChip(selected = exam == it, onClick = { exam = it }, label = { Text(it) }) } }
        Text("সময়", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("৫ মিনিট", "১৫ মিনিট", "৩০ মিনিট").forEach { FilterChip(selected = duration == it, onClick = { duration = it }, label = { Text(it) }) } }
        Panel { Text("আপনার নির্বাচন", fontWeight = FontWeight.Bold); Text(exam + " • " + duration); Text("এখনকার ডেমোতে পরীক্ষা ও সময়ের নির্বাচন অনুশীলনে প্রয়োগ হয় না। একটি নির্দিষ্ট নমুনা প্রশ্ন দেখানো হবে।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Primary("নমুনা প্রশ্ন দেখুন") { nav.navigate("challenge") }
    }
}

@Composable internal fun QuizResults(nav: NavHostController) {
    val resultState = nav.previousBackStackEntry?.savedStateHandle
    val correctCount = resultState?.get<Int>("correctCount")
    val questionCount = resultState?.get<Int>("questionCount")
    val hasResult = correctCount != null && questionCount != null && questionCount > 0
    val percentage = if (hasResult) (correctCount!! * 100) / questionCount!! else 0
    val context = LocalContext.current
    // The result route can be recomposed or revisited. Store a marker on the
    // originating answer entry so one completed session is recorded only once.
    val alreadyRecorded = resultState?.get<Boolean>("practiceAttemptRecorded") == true
    LaunchedEffect(correctCount, questionCount, alreadyRecorded) {
        if (hasResult && !alreadyRecorded) {
            LocalLearningStore.recordPracticeAttempt(context, correctCount!!, questionCount!!)
            resultState?.set("practiceAttemptRecorded", true)
        }
    }
    Scroll {
        Intro("অনুশীলন সম্পন্ন", "এই সেশনের উত্তর যাচাইয়ের ফলাফল।")
        Panel {
            if (hasResult) {
                Text("আপনার স্কোর", fontWeight = FontWeight.Bold)
                Text("$correctCount / $questionCount", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("$percentage% সঠিক", fontWeight = FontWeight.SemiBold)
                LinearProgressIndicator(
                    progress = { percentage / 100f },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outlineVariant
                )
                Text(
                    if (correctCount == questionCount) "দারুণ! নমুনা প্রশ্নটির উত্তর সঠিক হয়েছে।"
                    else "ভুল উত্তরটি আবার দেখে নিন এবং ব্যাখ্যাটি মনে রাখুন।"
                )
                Text("এটি একটি নমুনা প্রশ্নের ফলাফল। সেশনটি এই ডিভাইসের অনুশীলনের ইতিহাসে সংরক্ষিত হবে।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Text("এই সেশনের ফল পাওয়া যায়নি", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("প্রথমে অনুশীলনের প্রশ্নের উত্তর জমা দিন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Primary("আবার অনুশীলন করুন") { nav.navigate("quiz-setup") }
        OutlinedButton(onClick = { nav.navigate("practice-history") }, modifier = Modifier.fillMaxWidth()) { Text("অনুশীলনের ইতিহাস দেখুন") }
        OutlinedButton(onClick = { nav.navigate("revision") }, modifier = Modifier.fillMaxWidth()) { Text("রিভিশন ডেক খুলুন") }
    }
}

@Composable internal @Composable internal fun WordDetail(nav: NavHostController) {
    val selectedTerm = nav.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("selected_word")
    val entry = vocabularyEntries.firstOrNull { it.term == selectedTerm }
    Scroll {
        Intro("শব্দের বিস্তারিত", "অর্থ, সংজ্ঞা, সমার্থক শব্দ ও বাস্তব বাক্যে প্রয়োগ এক জায়গায় দেখুন।")
        if (entry == null) {
            Panel {
                Text("কোনো শব্দ নির্বাচন করা হয়নি", fontWeight = FontWeight.SemiBold)
                Text("Vocabulary Lab থেকে একটি শব্দ বেছে পূর্ণাঙ্গ পাঠ খুলুন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedButton(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Vocabulary Lab-এ ফিরুন")
                }
            }
        } else {
            Panel {
                Kicker(entry.partOfSpeech.uppercase())
                Text(entry.term, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(entry.pronunciation, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("বাংলা অর্থ: ${entry.banglaMeaning}", fontWeight = FontWeight.SemiBold)
                Text("Definition: ${entry.definition}")
                Text("Synonyms: ${entry.synonyms}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Antonym: ${entry.antonym}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Example: ${entry.example}")
            }
            Text("শব্দটি প্রাথমিক শেখার নমুনা; অভিধান-উৎস ও পরীক্ষার রেফারেন্স যুক্ত করা এখনো বাকি।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable internal fun StudyPlan() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val tasks = listOf("আজকের ব্রিফিং পড়া", "১০টি MCQ অনুশীলন", "৫টি নতুন শব্দ শেখা", "আগের ভুলগুলো রিভিশন")
    val done = remember { mutableStateListOf<String>().apply {
        addAll(LocalLearningStore.getCompletedStudyTasks(context).filter { it in tasks })
    } }
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                done.clear()
                done.addAll(LocalLearningStore.getCompletedStudyTasks(context).filter { it in tasks })
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    Scroll {
        Intro("আপনার পড়ার পরিকল্পনা", "ছোট, বাস্তবসম্মত সেশন দিয়ে ধারাবাহিকতা তৈরি করুন।")
        Panel {
            Text("আজকের কাজ", fontWeight = FontWeight.Bold)
            tasks.forEach { task ->
                Row(Modifier.fillMaxWidth().clickable {
                    val checked = task !in done
                    if (checked) done.add(task) else done.remove(task)
                    LocalLearningStore.setStudyTaskCompleted(context, task, checked)
                }.padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = task in done, onCheckedChange = { checked ->
                        if (checked && task !in done) done.add(task)
                        if (!checked) done.remove(task)
                        LocalLearningStore.setStudyTaskCompleted(context, task, checked)
                    }); Text(task)
                }
            }
            Text(done.size.toString() + " / " + tasks.size + " সম্পন্ন", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("কাজের অগ্রগতি এই ডিভাইসে সংরক্ষিত থাকে এবং নতুন স্থানীয় তারিখে তালিকা স্বয়ংক্রিয়ভাবে রিসেট হয়। অ্যাকাউন্ট সিঙ্ক এখনো নেই।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}


@Composable internal fun PracticeHistory() {
    val context = LocalContext.current
    val attempts = remember { mutableStateListOf<LocalLearningStore.PracticeAttempt>().apply {
        addAll(LocalLearningStore.getPracticeHistory(context))
    } }
    Scroll {
        Intro("অনুশীলনের ইতিহাস", "এই ডিভাইসে সংরক্ষিত নমুনা অনুশীলনের ফলাফল।")
        if (attempts.isEmpty()) {
            Panel {
                Text("এখনো কোনো সেশন সংরক্ষিত নেই", fontWeight = FontWeight.Bold)
                Text("নমুনা প্রশ্নের উত্তর জমা দিয়ে ফলাফল দেখলে সেটি এখানে যুক্ত হবে।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            Text("সাম্প্রতিক সেশন • ${attempts.size}", fontWeight = FontWeight.Bold)
            attempts.forEach { attempt ->
                Panel {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("${attempt.correct} / ${attempt.total} সঠিক", fontWeight = FontWeight.Bold)
                            Text("${(attempt.correct * 100) / attempt.total}% স্কোর", color = MaterialTheme.colorScheme.primary)
                        }
                        Text(java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.MEDIUM, java.text.DateFormat.SHORT).format(java.util.Date(attempt.timestamp)), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    }
                }
            }
            TextButton(onClick = { LocalLearningStore.clearPracticeHistory(context); attempts.clear() }) { Text("ইতিহাস মুছুন") }
        }
        Text("এটি নমুনা প্রশ্নের স্থানীয় ইতিহাস। পূর্ণ প্রশ্নব্যাংক, বিষয়ভিত্তিক বিশ্লেষণ ও অ্যাকাউন্ট সিঙ্ক এখনো নেই।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
    }
}
