package com.sarangsho.plus.features.reading
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.sarangsho.plus.data.LocalLearningStore
import androidx.navigation.compose.rememberNavController
import com.sarangsho.plus.ui.theme.SarangshoTheme
import androidx.navigation.NavHostController
import com.sarangsho.plus.ui.theme.SarangshoPalette


@Composable internal fun Welcome(nav: NavHostController) {
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(25.dp), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(8.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(26.dp), modifier = Modifier.size(104.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.MenuBook, null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(52.dp)) }
            }
            Text("Sarangsho+", fontSize = 32.sp, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
            Text("সঠিক তথ্য। গভীর উপলব্ধি। প্রস্তুতিতে এগিয়ে যান।", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
            Text("বাংলাদেশ ও বিশ্বের গুরুত্বপূর্ণ ঘটনা বুঝুন, প্রেক্ষাপট জানুন এবং BCS ও Bank পরীক্ষার জন্য শিখুন।", color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 25.sp)
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Primary("শুরু করুন") { nav.navigate("onboarding-goals") }
            OutlinedButton(onClick = { nav.navigate("signin") }, modifier = Modifier.fillMaxWidth()) { Text("সাইন ইন") }
            Text("নমুনা সংস্করণ • অ্যাকাউন্ট ছাড়াও দেখা যাবে", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }
    }
}

@Composable internal fun SignIn(nav: NavHostController) {
    var email by rememberSaveable { mutableStateOf("") }; var password by rememberSaveable { mutableStateOf("") }
    Scroll {
        Intro("আপনার পড়াশোনা সঙ্গে রাখুন", "অ্যাকাউন্টে প্রবেশ করুন অথবা আপাতত স্থানীয়ভাবে চালিয়ে যান।")
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("ইমেইল বা ফোন নম্বর") }, singleLine = true)
        OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(), label = { Text("পাসওয়ার্ড") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("সাইন ইন শিগগিরই চালু হবে") }
        Text("ডেমো UI: প্রকৃত authentication এখনো সংযুক্ত নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        OutlinedButton(onClick = { nav.navigate("home") }, modifier = Modifier.fillMaxWidth()) { Text("অ্যাকাউন্ট ছাড়া চালিয়ে যান") }
        Text("নতুন অ্যাকাউন্ট তৈরি করুন", Modifier.clickable { nav.navigate("signup") }, color = MaterialTheme.colorScheme.primary)
        Text("পাসওয়ার্ড ভুলে গেছেন?", Modifier.clickable { nav.navigate("password-reset") }, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable internal fun Home(nav: NavHostController) {
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
                Text(
                    "DAILY BRIEFING",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Intro("আজকের ব্রিফিং", "গুরুত্বপূর্ণ ঘটনা থেকে প্রেক্ষাপট, ধারণা ও পরীক্ষার প্রস্তুতি।")
        Surface(
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Kicker("আপনার প্রস্তুতি")
                Text("আজকের পড়ার লক্ষ্য ঠিক করুন", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    "লক্ষ্য নির্ধারণ করলে আপনার দৈনিক পড়ার পরিকল্পনা এখানে দেখা যাবে।",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.82f),
                    fontSize = 12.sp,
                    lineHeight = 19.sp
                )
                Button(
                    onClick = { nav.navigate("onboarding-goals") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("লক্ষ্য নির্ধারণ করুন")
                    Spacer(Modifier.width(6.dp))
                    Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(16.dp))
                }
            }
        }
        Panel {
            Kicker("প্রধান প্রতিবেদন • নমুনা পাঠ")
            Text(
                "গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন",
                fontSize = 23.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "আন্তর্জাতিক আদালতের এখতিয়ার, রাষ্ট্রের দায়বদ্ধতা এবং রোহিঙ্গা সংকটের আইনি প্রেক্ষাপট।",
                fontSize = 14.sp,
                lineHeight = 23.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, enabled = false, label = { Text("আন্তর্জাতিক আইন") })
                AssistChip(onClick = {}, enabled = false, label = { Text("নমুনা কনটেন্ট") })
            }
            Primary("প্রতিবেদনটি পড়ুন") { nav.navigate("article") }
        }
        RowLink("সব প্রতিবেদন দেখুন", "বিষয় অনুযায়ী সংবাদ ও ব্যাখ্যা", Icons.Default.ArrowForward) {
            nav.navigate("feed")
        }
        Text("সংবাদ থেকে শেখা", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Text("একটি ঘটনার পেছনের তথ্য, সময়রেখা ও উৎস আলাদা করে দেখুন.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        RowLink("তিন-স্তরের সারাংশ", "৩০ সেকেন্ড, ২ মিনিট অথবা বিস্তারিত", Icons.Default.Subject) { nav.navigate("summary") }
        RowLink("ঘটনার টাইমলাইন", "তারিখ ও প্রেক্ষাপট আলাদা করে দেখুন", Icons.Default.Timeline) { nav.navigate("timeline") }
        RowLink("পত্রিকার দৃষ্টিভঙ্গি", "উপলভ্য ও অনুমোদিত উৎস তুলনা", Icons.Default.CompareArrows) { nav.navigate("perspectives") }
        RowLink("তথ্য ও পরিসংখ্যান", "সংজ্ঞা, একক, সময়কাল ও মূল উৎস", Icons.Default.BarChart) { nav.navigate("data") }
        Text("প্রস্তুতি চালিয়ে যান", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Text("পড়া বিষয়কে শব্দভান্ডার ও পরীক্ষার অনুশীলনের সঙ্গে যুক্ত করুন.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        RowLink("Vocabulary Lab", "শব্দ, অর্থ, collocation ও ব্যবহার", Icons.Default.Translate) { nav.navigate("vocabulary") }
        RowLink("BCS ও Bank অনুশীলন", "প্রশ্নের ধরন বেছে প্রস্তুতি নিন", Icons.Default.EditNote) { nav.navigate("quiz-setup") }
        RowLink("পড়ার পরিকল্পনা", "নিজের সময় অনুযায়ী প্রস্তুতি", Icons.Default.CalendarToday) { nav.navigate("study-plan") }
        Text(
            "এই হোম স্ক্রিনে প্রদর্শিত প্রতিবেদন ও বিষয়বস্তু নমুনা। লাইভ সংবাদ, ব্যক্তিগত অগ্রগতি বা পরীক্ষার ফল হিসেবে এগুলো ব্যবহার করবেন না।",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp,
            lineHeight = 17.sp
        )
    }
}
@Composable internal fun Feed(nav: NavHostController) {
    var filter by rememberSaveable { mutableStateOf("সব") }
    var query by rememberSaveable { mutableStateOf("") }
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
                Text("অন্য শব্দ লিখুন অথবা ফিল্টার বদলান।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            filtered.forEach { Story(it.first, it.second, it.third, nav) }
        }
        Text("এগুলো UI পরীক্ষার নমুনা শিরোনাম, লাইভ সংবাদ নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
    }
}

@Composable internal fun Article(nav: NavHostController) {
    Scroll {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Kicker("বিশ্লেষণ • নমুনা নিবন্ধ")
            AssistChip(onClick = {}, enabled = false, label = { Text("নমুনা", fontSize = 10.sp) })
        }
        Text("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন", fontSize = 25.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Text("আন্তর্জাতিক আইন • রোহিঙ্গা সংকট • বিচারিক প্রক্রিয়া", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        Panel {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.VerifiedUser, null, tint = MaterialTheme.colorScheme.primary)
                Column(Modifier.weight(1f)) {
                    Text("উৎস যাচাইয়ের অবস্থা", fontWeight = FontWeight.SemiBold)
                    Text("এই স্ক্রিনে প্রকৃত উৎস-নথি সংযুক্ত নেই", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                }
                AssistChip(onClick = {}, enabled = false, label = { Text("যাচাই হয়নি") })
            }
        }
        Text("প্রথমে বুঝুন", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Panel {
            Kicker("মূল বিষয়")
            Text("রাষ্ট্রের বিরুদ্ধে আন্তর্জাতিক আদালতে দায়বদ্ধতার প্রশ্ন কীভাবে উত্থাপিত হয়, তা বুঝতে মামলার পক্ষ, আদালতের এখতিয়ার এবং প্রযোজ্য আন্তর্জাতিক আইন আলাদা করে দেখতে হয়।", fontSize = 15.sp, lineHeight = 25.sp)
            Text("নমুনা ব্যাখ্যা • যাচাইকৃত সংবাদ প্রতিবেদন নয়", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }
        Text("এই প্রতিবেদনের পাঠ-সরঞ্জাম", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        RowLink("তিন-স্তরের সারাংশ", "৩০ সেকেন্ড, ২ মিনিট ও বিস্তারিত পাঠ", Icons.Default.Subject) { nav.navigate("summary") }
        RowLink("ঘটনার টাইমলাইন", "তারিখ ও ঘটনার সম্পর্ক যাচাই করে পড়ুন", Icons.Default.Timeline) { nav.navigate("timeline") }
        RowLink("পত্রিকার দৃষ্টিভঙ্গি", "উৎস পাওয়া গেলে তুলনা করুন", Icons.Default.CompareArrows) { nav.navigate("perspectives") }
        RowLink("জ্ঞান মানচিত্র", "প্রতিষ্ঠান, আইন ও ধারণার সম্পর্ক", Icons.Default.Hub) { nav.navigate("graph") }
        RowLink("উৎস ও যাচাই", "মূল নথি ও প্রকাশনার তথ্য", Icons.Default.Source) { nav.navigate("sources") }
        RowLink("তথ্য সংশোধনের অনুরোধ", "ভুল বা অস্পষ্ট তথ্য জানান", Icons.Default.Flag) { nav.navigate("report-story") }
    }
}

@Composable @OptIn(ExperimentalMaterial3Api::class)
internal fun Summary(nav: NavHostController) {
    var level by rememberSaveable { mutableIntStateOf(0) }
    val labels = listOf("৩০ সেকেন্ড", "২ মিনিট", "বিস্তারিত")
    val texts = listOf("ঘটনাটির মূল বিষয় এক বাক্যে বুঝুন।", "মূল সিদ্ধান্ত, সংশ্লিষ্ট প্রতিষ্ঠান এবং বাংলাদেশের জন্য সম্ভাব্য তাৎপর্য সংক্ষেপে পড়ুন।", "বিস্তারিত সংস্করণে পটভূমি, প্রক্রিয়া, মূল তথ্য, সংশ্লিষ্ট পক্ষের অবস্থান, অনিশ্চয়তা এবং পরীক্ষায় প্রাসঙ্গিক ধারণা থাকবে।")
    Scroll {
        Intro("একই ঘটনা, তিনভাবে বুঝুন", "আপনার হাতে যত সময় আছে, সেই অনুযায়ী পড়ুন।")
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) { labels.forEachIndexed { i, label -> SegmentedButton(level == i, { level = i }, SegmentedButtonDefaults.itemShape(i, labels.size)) { Text(label, fontSize = 11.sp) } } }
        Panel { Kicker(labels[level]); Text(texts[level], fontSize = 16.sp, lineHeight = 28.sp); Text("নমুনা লেখা • যাচাইকৃত সংবাদ সারাংশ নয়", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp) }
        RowLink("টাইমলাইন", "ঘটনার পূর্বাপর", Icons.Default.Timeline) { nav.navigate("timeline") }
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
                Icon(Icons.Default.CompareArrows, null, tint = MaterialTheme.colorScheme.primary)
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
    val list = if (world) listOf("জাতিসংঘ ও আন্তর্জাতিক সংস্থা", "আন্তর্জাতিক আইন ও চুক্তি", "বিশ্ব অর্থনীতি", "জলবায়ু ও পরিবেশ", "দক্ষিণ এশিয়া", "ভূরাজনীতি") else listOf("সংবিধান ও সরকারব্যবস্থা", "অর্থনীতি ও ব্যাংকিং", "সমাজ ও উন্নয়ন", "পরিবেশ ও জলবায়ু", "শিক্ষা ও স্বাস্থ্য", "আইন ও বিচার")
    Scroll {
        Intro(if (world) "বিশ্ব বিষয়াবলি" else "বাংলাদেশ বিষয়াবলি", "বর্তমান ঘটনা, মৌলিক ধারণা ও পরীক্ষার প্রস্তুতি একসঙ্গে দেখুন।")
        list.forEach { RowLink(it, "বিষয়ভিত্তিক নোট, সাম্প্রতিক ঘটনা ও অনুশীলন", Icons.Default.Public) { nav.navigate("topic-detail") } }
    }
}

@Composable internal fun Graph() {
    Scroll {
        Intro("জ্ঞান মানচিত্র", "একটি ঘটনার সঙ্গে যুক্ত প্রতিষ্ঠান, ব্যক্তি, আইন ও ধারণার সম্পর্ক দেখুন।")
        Panel {
            Box(Modifier.fillMaxWidth().height(190.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(18.dp)) { Text("মূল বিষয়", Modifier.padding(14.dp), color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold) }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("প্রতিষ্ঠান", "আইন", "রাষ্ট্র").forEach { Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(10.dp)) { Text(it, Modifier.padding(10.dp), fontSize = 12.sp) } } }
                    Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(18.dp)) { Text("প্রভাব ও প্রেক্ষাপট", Modifier.padding(12.dp)) }
                }
            }
            Text("চিত্রটি ইন্টারঅ্যাকটিভ গ্রাফের নমুনা; সম্পর্কের ডেটা এখনো যুক্ত হয়নি।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }
        Text("সম্পর্ক: কারণ • আইনি এখতিয়ার • সদস্যপদ • প্রভাব • সময়গত সম্পর্ক", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable internal fun DataExplorer(nav: NavHostController) {
    Scroll {
        Intro("তথ্য ও পরিসংখ্যান", "সংখ্যার সঙ্গে একক, সময়কাল, সংজ্ঞা ও মূল উৎস দেখুন।")
        Panel { Kicker("ইন্ডিকেটর কার্ড • নমুনা"); Text("মূল্যস্ফীতি", fontSize = 20.sp, fontWeight = FontWeight.Bold); Text("সর্বশেষ মান: লাইভ ডেটা সংযুক্ত নয়", color = MaterialTheme.colorScheme.onSurfaceVariant); HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant); Text("সময়কাল: উল্লেখ করতে হবে"); Text("একক: শতাংশ"); Text("উৎস: সংশ্লিষ্ট সরকারি পরিসংখ্যান সংস্থা") }
        Panel {
            RowLink("সূচকের বিস্তারিত", "সংজ্ঞা, সময়কাল ও সংশোধন", Icons.Default.Insights) { nav.navigate("indicator-detail") }
        Text("তথ্য পড়ার নিয়ম", fontWeight = FontWeight.Bold)
            listOf("স্টক ও প্রবাহ আলাদা করুন", "নামমাত্র ও প্রকৃত মান গুলিয়ে ফেলবেন না", "পূর্বাভাস ও পর্যবেক্ষণ আলাদা রাখুন", "প্রাথমিক ও সংশোধিত উপাত্ত চিহ্নিত করুন").forEach { Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.tertiary); Spacer(Modifier.width(8.dp)); Text(it) } }
        }
    }
}

@Composable internal fun Search(nav: NavHostController) {
    var query by rememberSaveable { mutableStateOf("") }; var searched by rememberSaveable { mutableStateOf(false) }
    Scroll {
        Intro("এক জায়গায় খুঁজুন", "সংবাদ, বিষয়, শব্দ, প্রতিষ্ঠান ও প্রকাশনার নাম লিখুন।")
        OutlinedTextField(query, { query = it; searched = false }, Modifier.fillMaxWidth(), placeholder = { Text("যেমন: মূল্যস্ফীতি, ICJ, resilience") }, leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true)
        Primary("খুঁজুন") { searched = query.isNotBlank() }
        if (searched) { Text("অনুসন্ধানের নমুনা ফলাফল", fontWeight = FontWeight.Bold); RowLink("বিষয়ভিত্তিক নোট", "প্রকৃত সূচি যুক্ত হলে ফলাফল আসবে", Icons.Default.Article) { nav.navigate("feed") } }
        else { Text("জনপ্রিয় অনুসন্ধান", fontWeight = FontWeight.Bold); listOf("বাংলাদেশ ব্যাংক", "সংবিধান", "জাতিসংঘ", "জলবায়ু অর্থায়ন").forEach { Text(it, Modifier.fillMaxWidth().clickable { query = it }.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.primary) } }
        Text("প্রতিটি ফলাফলে প্রকাশক, মূল লিংক, প্রকাশের সময় ও প্রবেশযোগ্যতার অবস্থা দেখানো হবে।", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable internal fun Saved(nav: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val bookmarkedWords = remember {
        mutableStateListOf<String>().apply {
            addAll(LocalLearningStore.getBookmarkedWords(context).sorted())
        }
    }
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                bookmarkedWords.clear()
                bookmarkedWords.addAll(LocalLearningStore.getBookmarkedWords(context).sorted())
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    Scroll {
        Intro("আপনার সংরক্ষিত বিষয়", "আপনার বুকমার্ক করা শব্দ এই ডিভাইসেই সংরক্ষিত থাকে।")
        if (bookmarkedWords.isEmpty()) {
            Panel {
                Icon(Icons.Default.Bookmarks, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp))
                Text("এখনো কোনো শব্দ বুকমার্ক করা হয়নি", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Vocabulary Lab-এ গিয়ে কোনো শব্দ বুকমার্ক করলে সেটি এখানে দেখা যাবে। সংবাদ ও রিভিশন কার্ড সংরক্ষণ এখনো চালু হয়নি।", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Primary("Vocabulary Lab খুলুন") { nav.navigate("vocabulary") }
            }
        } else {
            Text("সংরক্ষিত শব্দ • ${bookmarkedWords.size}", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            bookmarkedWords.toList().forEach { word ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f).padding(vertical = 9.dp)) {
                        Text(word, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text("Vocabulary Lab • এই ডিভাইসে সংরক্ষিত", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    }
                    TextButton(onClick = {
                        LocalLearningStore.setWordBookmarked(context, word, false)
                        bookmarkedWords.remove(word)
                    }) { Text("সরান") }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
        RowLink("Vocabulary Lab", "আরও শব্দ শিখুন ও বুকমার্ক করুন", Icons.Default.Translate) { nav.navigate("vocabulary") }
        RowLink("সংরক্ষিত সংগ্রহ", "ফোল্ডার ও বিষয়ভিত্তিক তালিকা", Icons.Default.Folder) { nav.navigate("saved-collections") }
        Text("বর্তমানে শুধু Vocabulary Lab-এর বুকমার্ক এই তালিকায় আসে। অ্যাকাউন্ট বা অন্য ডিভাইসে সিঙ্ক হয় না।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
    }
}

@Composable internal fun Profile(nav: NavHostController) {
    Scroll {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(50.dp)) { Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(16.dp).size(34.dp)) }
            Column { Text("পাঠক প্রোফাইল", fontSize = 20.sp, fontWeight = FontWeight.Bold); Text("স্থানীয় ডেমো • সাইন ইন করা হয়নি", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp) }
        }
        Panel { Text("শেখার অগ্রগতি", fontWeight = FontWeight.Bold); Text("প্রকৃত ব্যবহার-ডেটা যুক্ত না হওয়া পর্যন্ত কোনো স্কোর বা ধারাবাহিকতার সংখ্যা দেখানো হবে না।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        RowLink("আমার শব্দভান্ডার", "সংরক্ষিত শব্দ ও পুনরাবৃত্তি", Icons.Default.Translate) { nav.navigate("vocabulary") }
        RowLink("রিভিশন ডেক", "মনে রাখার অনুশীলন", Icons.Default.Style) { nav.navigate("revision") }
        RowLink("লিখিত অনুশীলন", "আপনার উত্তর ও খসড়া", Icons.Default.EditNote) { nav.navigate("writing") }
        RowLink("অনুশীলনের ইতিহাস", "আগের সেশন ও ভুলের তালিকা", Icons.Default.History) { nav.navigate("practice-history") }
        RowLink("পড়ার ইতিহাস", "সম্প্রতি খোলা পাঠ", Icons.Default.MenuBook) { nav.navigate("reading-history") }
        RowLink("সাইন ইন", "অ্যাকাউন্ট ও সিঙ্ক", Icons.Default.Login) { nav.navigate("signin") }
        RowLink("নোটিফিকেশন", "আপডেট ও রিভিশন স্মরণ", Icons.Default.Notifications) { nav.navigate("notifications") }
        RowLink("সেটিংস ও পছন্দ", "ভাষা, ফন্ট, ডাউনলোড ও গোপনীয়তা", Icons.Default.Settings) { nav.navigate("settings") }
        RowLink("সহায়তা", "প্রশ্ন, সমস্যা ও মতামত", Icons.Default.HelpOutline) { nav.navigate("help") }
    }
}

@Composable internal fun OnboardingGoals(nav: NavHostController) {
    var goal by rememberSaveable { mutableStateOf("BCS প্রস্তুতি") }
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
        Text("পছন্দ পরে সেটিংস থেকে বদলানো যাবে।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable internal fun OnboardingInterests(nav: NavHostController) {
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
        Text("এই পছন্দগুলো শুধু ডেমো সেশনে থাকে; অ্যাকাউন্টে সিঙ্ক হয় না।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable internal fun Archive(nav: NavHostController) {
    Scroll {
        Intro("দৈনিক সংস্করণ", "আগের দিনের ব্রিফিং খুঁজুন এবং পরে পড়ার জন্য খুলুন।")
        Panel { Text("সাম্প্রতিক সংস্করণ", fontWeight = FontWeight.Bold); Text("প্রকাশিত সংস্করণের তালিকা ইঞ্জিন থেকে যুক্ত হলে এখানে তারিখ, ভাষা ও অবস্থা দেখানো হবে।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        RowLink("আজকের সংস্করণ", "লাইভ কনটেন্ট সংযুক্ত নয়", Icons.Default.Today) { nav.navigate("edition") }
        RowLink("গতকালের সংস্করণ", "আর্কাইভ ডেটা অপেক্ষমাণ", Icons.Default.CalendarMonth) { nav.navigate("edition") }
    }
}

@Composable internal fun Edition(nav: NavHostController) {
    Scroll {
        Intro("দৈনিক ব্রিফিং", "একটি সংস্করণের সব পাঠ, অনুশীলন ও তথ্যসূত্র এক জায়গায়।")
        Panel { Text("সংস্করণের অবস্থা", fontWeight = FontWeight.Bold); Text("এই প্রোটোটাইপে কোনো প্রকাশিত সংস্করণ লোড হয়নি।", color = MaterialTheme.colorScheme.onSurfaceVariant); AssistChip(onClick = {}, enabled = false, label = { Text("কনটেন্ট অপেক্ষমাণ") }) }
        RowLink("প্রধান সংবাদ", "সংস্করণ থেকে যাচাইকৃত গল্প", Icons.Default.Article) { nav.navigate("article") }
        RowLink("বাংলাদেশ বিষয়াবলি", "দেশের গুরুত্বপূর্ণ ঘটনা", Icons.Default.Flag) { nav.navigate("bangladesh") }
        RowLink("বিশ্ব বিষয়াবলি", "আন্তর্জাতিক প্রেক্ষাপট", Icons.Default.Public) { nav.navigate("world") }
        RowLink("আজকের অনুশীলন", "BCS ও Bank প্রশ্ন", Icons.Default.Quiz) { nav.navigate("quiz-setup") }
        RowLink("তথ্যসূত্র ও যাচাই", "উৎস, সময় ও প্রবেশযোগ্যতা", Icons.Default.Source) { nav.navigate("sources") }
    }
}

@Composable internal fun TopicDetail(nav: NavHostController) {
    Scroll {
        Intro("বিষয়ভিত্তিক পাঠ", "একটি বিষয়ের মৌলিক ধারণা, সাম্প্রতিক ঘটনা ও পরীক্ষার সংযোগ একত্রে।")
        Panel { Text("মূল ধারণা", fontWeight = FontWeight.Bold); Text("বিষয়ভিত্তিক ব্যাখ্যা ও সংজ্ঞা যাচাইকৃত পাঠ্যসূত্র থেকে যুক্ত হবে।") }
        RowLink("সংশ্লিষ্ট সংবাদ", "এই বিষয়ের সাম্প্রতিক ঘটনা", Icons.Default.Article) { nav.navigate("feed") }
        RowLink("জ্ঞান মানচিত্র", "প্রতিষ্ঠান ও ধারণার সম্পর্ক", Icons.Default.Hub) { nav.navigate("graph") }
        RowLink("তথ্য ও পরিসংখ্যান", "উৎসসহ প্রাসঙ্গিক সূচক", Icons.Default.BarChart) { nav.navigate("data") }
        RowLink("অনুশীলন", "বিষয়ভিত্তিক প্রশ্ন", Icons.Default.EditNote) { nav.navigate("quiz-setup") }
    }
}

@Composable internal fun Sources(nav: NavHostController) {
    Scroll {
        Intro("উৎস ও যাচাই", "প্রতিটি তথ্য কোথা থেকে এসেছে এবং কীভাবে যাচাই হয়েছে তা দেখুন।")
        Panel { Text("উৎস-তথ্য", fontWeight = FontWeight.Bold); Text("প্রকৃত সংস্করণ যুক্ত হলে প্রকাশক, শিরোনাম, মূল URL, প্রকাশকাল, হালনাগাদ সময়, প্রবেশযোগ্যতা ও যাচাইয়ের অবস্থা দেখানো হবে।") }
        RowLink("উৎসের রেকর্ড", "প্রকাশক, অনুমতি ও মূল URL", Icons.Default.Link) { nav.navigate("source-detail") }
        listOf("প্রাথমিক সরকারি নথি", "আদালত ও আইনগত নথি", "আন্তর্জাতিক সংস্থা", "সংবাদ প্রকাশনা").forEach {
            Panel { Text(it, fontWeight = FontWeight.SemiBold); Text("উৎস-রেজিস্ট্রি থেকে তথ্য আসবে", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp) }
        }
    }
}

@Composable internal fun Notifications() {
    Scroll {
        Intro("নোটিফিকেশন", "নতুন সংস্করণ, ডাউনলোড ও রিভিশনের আপডেট এখানে দেখা যাবে।")
        Panel { Icon(Icons.Default.NotificationsNone, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp)); Text("এখনো কোনো নোটিফিকেশন নেই", fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("নতুন আপডেট এলে এখানে তারিখ ও প্রয়োজনীয় পদক্ষেপসহ দেখানো হবে।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable internal fun About() {
    Scroll {
        Intro("Sarangsho Plus", "সংবাদকে বোঝা, শেখা ও মনে রাখার একটি বাংলা-প্রথম প্ল্যাটফর্ম।")
        Panel { Text("সংস্করণ 0.1.0 • প্রোটোটাইপ", fontWeight = FontWeight.Bold); Text("এই অ্যাপের লক্ষ্য হলো যাচাইকৃত বর্তমান ঘটনাকে প্রেক্ষাপট, ধারণা, তথ্যসূত্র ও পরীক্ষার অনুশীলনের সঙ্গে যুক্ত করা।") }
        Text("স্বাধীনভাবে যাচাই না করা পর্যন্ত কোনো সংবাদ, সংখ্যা বা উৎসকে লাইভ বলে উপস্থাপন করা হবে না।", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}


@Composable internal fun ReadingHistory() {
    Scroll {
        Intro("পড়ার ইতিহাস", "সম্প্রতি খোলা পাঠে ফিরে যান।")
        Panel { Text("এখনো কোনো পড়ার ইতিহাস নেই", fontWeight = FontWeight.Bold); Text("পড়া শুরু করলে আপনার সাম্প্রতিক পাঠ এখানে দেখা যাবে। এই প্রোটোটাইপে ইতিহাস সংরক্ষণ চালু হয়নি।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable internal fun SavedCollections() {
    Scroll {
        Intro("সংরক্ষিত সংগ্রহ", "সংবাদ, শব্দ ও প্রশ্ন নিজের মতো গুছিয়ে রাখুন।")
        Panel { Text("কোনো সংগ্রহ তৈরি হয়নি", fontWeight = FontWeight.Bold); Text("সংগ্রহ ও ফোল্ডার তৈরি করার সুবিধা স্থানীয় ডেটাবেস যুক্ত হলে চালু হবে।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
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
