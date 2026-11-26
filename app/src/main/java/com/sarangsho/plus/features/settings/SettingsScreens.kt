package com.sarangsho.plus.features.settings

import com.sarangsho.plus.navigation.AppDestination
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
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sarangsho.plus.ui.theme.SarangshoPalette
import com.sarangsho.plus.data.LocalLearningStore
import com.sarangsho.plus.data.ReaderPreferencesRepository
import com.sarangsho.plus.data.ReaderSettings
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics


@Composable internal fun Settings(nav: NavHostController) {
    Scroll {
        Intro("সেটিংস", "অ্যাপের ব্যবহার, পড়া ও ব্যক্তিগত তথ্য নিয়ন্ত্রণ করুন।")
        RowLink("পড়ার পছন্দ", "ফন্ট, আকার, লাইন স্পেসিং ও থিম", Icons.Default.TextFields) { nav.navigate(AppDestination.ReadingSettings.route) }
        RowLink("অফলাইন লাইব্রেরি", "ডাউনলোড ও স্টোরেজ", Icons.Default.Download) { nav.navigate(AppDestination.Downloads.route) }
        RowLink("নোটিফিকেশন", "কোন আপডেট পাবেন", Icons.Default.Notifications) { nav.navigate(AppDestination.Notifications.route) }
        RowLink("অ্যাপের ভাষা", "বাংলা, English অথবা মিশ্র ভাষা", Icons.Default.Translate) { nav.navigate(AppDestination.Language.route) }
        RowLink("গোপনীয়তা", "ডেটা ও অ্যাকাউন্ট নিয়ন্ত্রণ", Icons.Default.PrivacyTip) { nav.navigate(AppDestination.Privacy.route) }
        RowLink("সহায়তা ও মতামত", "সমস্যা জানান বা প্রশ্ন করুন", Icons.Default.SupportAgent) { nav.navigate(AppDestination.Feedback.route) }
        RowLink("অ্যাপ সম্পর্কে", "সংস্করণ ও প্রকল্পের তথ্য", Icons.Default.Info) { nav.navigate(AppDestination.About.route) }
    }
}

@Composable internal fun ReadingSettings() {
    val context = LocalContext.current
    val readerSettings by ReaderPreferencesRepository.observe(context).collectAsState(initial = ReaderSettings())
    val scope = rememberCoroutineScope()
    val fontSize = readerSettings.fontSize
    val lineSpacing = readerSettings.lineSpacing
    val sepia = readerSettings.sepia
    Scroll {
        Intro("পড়ার পছন্দ", "দীর্ঘ লেখা আরাম করে পড়ার জন্য নিজের মতো সাজান।")
        Panel {
            Text("লেখার আকার: ${fontSize.toInt()} sp", fontWeight = FontWeight.SemiBold)
            Slider(value = fontSize, modifier = Modifier.testTag("reader-font-size-slider").semantics { contentDescription = "লেখার আকার" }, onValueChange = {
                scope.launch { ReaderPreferencesRepository.setFontSize(context, it) }
            }, valueRange = 16f..26f)
            Text("লাইন স্পেসিং: " + "%.1f".format(lineSpacing), fontWeight = FontWeight.SemiBold)
            Slider(value = lineSpacing, modifier = Modifier.testTag("reader-line-spacing-slider").semantics { contentDescription = "লাইন স্পেসিং" }, onValueChange = {
                scope.launch { ReaderPreferencesRepository.setLineSpacing(context, it) }
            }, valueRange = 1.2f..2.0f)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("উষ্ণ কাগজের নমুনা", Modifier.weight(1f))
                Switch(checked = sepia, modifier = Modifier.testTag("reader-sepia-switch").semantics { contentDescription = "উষ্ণ কাগজের নমুনা" }, onCheckedChange = {
                    scope.launch { ReaderPreferencesRepository.setSepia(context, it) }
                })
            }
        }
        Surface(color = if (sepia) SarangshoPalette.Paper else MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("নমুনা পাঠ", fontSize = fontSize.sp, lineHeight = (fontSize * lineSpacing).sp, color = if (sepia) SarangshoPalette.Ink else MaterialTheme.colorScheme.onSurface)
                Text("বাংলা ও ইংরেজি একসঙ্গে পড়ার সময় অক্ষরের গঠন এবং লাইনের ফাঁক যেন পরিষ্কার থাকে।", fontSize = fontSize.sp, lineHeight = (fontSize * lineSpacing).sp, color = if (sepia) SarangshoPalette.Ink else MaterialTheme.colorScheme.onSurface)
            }
        }
        Text("এই পছন্দগুলো এই ডিভাইসে সংরক্ষিত থাকে এবং নমুনা নিবন্ধ ও তিন-স্তরের সারাংশে প্রয়োগ হয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable internal fun Downloads() {
    Scroll {
        Intro("অফলাইন লাইব্রেরি", "ডাউনলোড করা সংস্করণ নেটওয়ার্ক ছাড়াও পড়ুন।")
        Panel { Icon(Icons.Default.CloudOff, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp)); Text("কোনো সংস্করণ ডাউনলোড করা নেই", fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("ডাউনলোড সুবিধা যুক্ত হলে সংরক্ষিত সংস্করণ, ফাইলের আকার, শেষ সিঙ্ক ও স্টোরেজ ব্যবহার এখানে দেখানো হবে।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable internal fun Notifications() {
    val context = LocalContext.current
    var briefing by rememberSaveable { mutableStateOf(LocalLearningStore.getDailyBriefingNotifications(context)) }
    var words by rememberSaveable { mutableStateOf(LocalLearningStore.getWordNotifications(context)) }
    var exams by rememberSaveable { mutableStateOf(LocalLearningStore.getExamNotifications(context)) }
    Scroll {
        Intro("নোটিফিকেশন", "কোন ধরনের আপডেট পেতে চান, তা বেছে নিন।")
        Panel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) { Text("দৈনিক ব্রিফিং", fontWeight = FontWeight.SemiBold); Text("নতুন দিনের প্রধান খবর ও সারাংশ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                Switch(checked = briefing, onCheckedChange = { briefing = it; LocalLearningStore.setDailyBriefingNotifications(context, it) })
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) { Text("শব্দভান্ডার", fontWeight = FontWeight.SemiBold); Text("নতুন শব্দ ও পুনরাবৃত্তির মনে করানো", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                Switch(checked = words, onCheckedChange = { words = it; LocalLearningStore.setWordNotifications(context, it) })
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) { Text("BCS ও Bank প্রস্তুতি", fontWeight = FontWeight.SemiBold); Text("অনুশীলন ও পড়ার পরিকল্পনার মনে করানো", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                Switch(checked = exams, onCheckedChange = { exams = it; LocalLearningStore.setExamNotifications(context, it) })
            }
        }
        Text("এই পছন্দগুলো এই ডিভাইসে সংরক্ষিত হয়। বাস্তব push notification পাঠানোর ব্যবস্থা এখনো সংযুক্ত নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable internal fun About() {
    Scroll {
        Intro("Sarangsho Plus", "বাংলাদেশ ও বিশ্বের খবর বুঝে পড়া এবং BCS ও Bank প্রস্তুতির জন্য একটি সম্পাদকীয়-শিক্ষণ প্ল্যাটফর্ম।")
        Panel {
            Text("আমাদের নীতি", fontWeight = FontWeight.Bold)
            Text("তথ্য, বিশ্লেষণ ও মতামত আলাদা রাখা; উৎস ও তারিখ স্পষ্ট করা; নমুনা কনটেন্টকে যাচাইকৃত সংবাদ হিসেবে না দেখানো।", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Panel {
            Text("বর্তমান সংস্করণ", fontWeight = FontWeight.SemiBold)
            Text("Android UI prototype • Development build")
            Text("এই সংস্করণে কিছু ফিচার স্থানীয়ভাবে কাজ করে; authentication, live news feed, cloud sync ও push notifications এখনো সংযুক্ত নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        Text("Designed for careful reading and consistent preparation.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable internal fun Help() {
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
                if (question in expanded) Text(answer, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 22.sp)
            }
        }
    }
}

@Composable internal fun Feedback() {
    val context = LocalContext.current
    var message by rememberSaveable { mutableStateOf(LocalLearningStore.getFeedbackDraft(context)) }
    var category by rememberSaveable { mutableStateOf("সাধারণ মতামত") }
    var saved by rememberSaveable { mutableStateOf(false) }
    Scroll {
        Intro("আপনার মতামত", "কোন জায়গাটি কাজ করছে না বা কী উন্নত করা দরকার, লিখে জানান।")
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { listOf("সাধারণ মতামত", "সমস্যা", "ভুল তথ্য").forEach { FilterChip(selected = category == it, onClick = { category = it; saved = false }, label = { Text(it) }) } }
        OutlinedTextField(
            value = message,
            onValueChange = { message = it; saved = false },
            modifier = Modifier.fillMaxWidth().height(180.dp).testTag("report-story-details"),
            label = { Text("বিস্তারিত লিখুন") },
            placeholder = { Text("কী ঘটেছে, কোন স্ক্রিনে, কী আশা করেছিলেন...") }
        )
        Button(
            onClick = {
                LocalLearningStore.setFeedbackDraft(context, "[$category] $message")
                saved = true
            },
            enabled = message.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) { Text("খসড়া এই ডিভাইসে সংরক্ষণ করুন") }
        if (saved) Text("খসড়া সংরক্ষিত হয়েছে। এটি সার্ভারে পাঠানো হয়নি।", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
        else Text("মতামত পাঠানোর সার্ভিস এখনো সংযুক্ত নয়। আপনার লেখা শুধু সংরক্ষণ করলে এই ডিভাইসে থাকবে।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}


@Composable internal fun Privacy() {
    Scroll {
        Intro("গোপনীয়তা ও ডেটা", "আপনার পড়ার তথ্য কীভাবে ব্যবহৃত হবে, তা পরিষ্কারভাবে দেখুন।")
        Panel { Text("বর্তমান অবস্থা", fontWeight = FontWeight.Bold); Text("এই প্রোটোটাইপে কোনো অ্যাকাউন্ট, ক্লাউড প্রোফাইল বা আচরণগত বিশ্লেষণ সংযুক্ত নেই।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Panel { Text("ডেটা নিয়ন্ত্রণ", fontWeight = FontWeight.SemiBold); Text("অ্যাকাউন্ট ও সিঙ্ক চালু হলে ডেটা রপ্তানি, মুছে ফেলা এবং অনুমতি নিয়ন্ত্রণের অপশন এখানে যুক্ত হবে।") }
    }
}

@Composable internal fun LanguageSettings() {
    val context = LocalContext.current
    var language by rememberSaveable { mutableStateOf(LocalLearningStore.getAppLanguage(context)) }
    Scroll {
        Intro("অ্যাপের ভাষা", "প্রধান ব্যাখ্যার ভাষা বেছে নিন। ইংরেজি পরিভাষা প্রয়োজন অনুযায়ী পাশে থাকবে।")
        listOf("বাংলা", "English", "বাংলা + English").forEach {
            Row(Modifier.fillMaxWidth().clickable {
                language = it
                LocalLearningStore.setAppLanguage(context, it)
            }.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = language == it, onClick = {
                    language = it
                    LocalLearningStore.setAppLanguage(context, it)
                })
                Text(it)
            }
        }
        Text("নির্বাচন: $language • এই পছন্দ এই ডিভাইসে সংরক্ষিত। সব স্ক্রিনের ভাষা পরিবর্তনের সংযোগ এখনো বাকি।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable internal fun SignUp(nav: NavHostController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Scroll {
        Intro("নতুন অ্যাকাউন্ট", "আপনার পড়ার অগ্রগতি একাধিক ডিভাইসে রাখতে অ্যাকাউন্ট তৈরি করুন।")
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("ইমেইল") }, singleLine = true)
        OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(), label = { Text("পাসওয়ার্ড") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("অ্যাকাউন্ট তৈরি শিগগিরই চালু হবে") }
        Text("নিরাপদ authentication ও account storage এখনো সংযুক্ত নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        Text("সাইন ইন পেজে ফিরুন", Modifier.clickable { nav.navigate(AppDestination.Signin.route) }, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable internal fun PasswordReset() {
    var email by rememberSaveable { mutableStateOf("") }
    Scroll {
        Intro("পাসওয়ার্ড পুনরুদ্ধার", "অ্যাকাউন্ট যাচাই ও ইমেইল পাঠানোর ব্যবস্থা সংযুক্ত হলে এখানে চালু হবে।")
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("অ্যাকাউন্টের ইমেইল") }, singleLine = true)
        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("পুনরুদ্ধার লিংক পাঠানো শিগগিরই চালু হবে") }
    }
}

@Composable internal fun ReportStory() {
    val context = LocalContext.current
    var details by rememberSaveable { mutableStateOf(LocalLearningStore.getReportStoryDraft(context)) }
    var saved by rememberSaveable { mutableStateOf(false) }
    Scroll {
        Intro("তথ্য সংশোধনের অনুরোধ", "ভুল তারিখ, সংখ্যা, উদ্ধৃতি বা প্রসঙ্গ চোখে পড়লে নির্দিষ্ট করে জানান।")
        OutlinedTextField(
            details,
            { details = it; saved = false },
            Modifier.fillMaxWidth().height(180.dp).testTag("report-story-details"),
            label = { Text("কী ভুল হয়েছে?") },
            placeholder = { Text("দাবি, সংশোধনের প্রস্তাব এবং প্রাসঙ্গিক উৎসের লিংক লিখুন।") }
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { LocalLearningStore.setReportStoryDraft(context, details); saved = true },
                enabled = details.isNotBlank(),
                modifier = Modifier.weight(1f).testTag("report-story-save")
            ) { Text("খসড়া সংরক্ষণ") }
            TextButton(
                onClick = {
                    LocalLearningStore.clearReportStoryDraft(context)
                    details = ""
                    saved = false
                },
                enabled = details.isNotBlank(),
                modifier = Modifier.testTag("report-story-clear")
            ) { Text("খসড়া মুছুন") }
        }
        if (saved) Text("খসড়া সংরক্ষিত হয়েছে। এটি সার্ভারে পাঠানো হয়নি।", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
        Text("খসড়া এই ডিভাইসেই থাকবে। প্রতিবেদন পাঠানোর সার্ভিস এখনো সংযুক্ত নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}
