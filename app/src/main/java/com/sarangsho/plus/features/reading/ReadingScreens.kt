package com.sarangsho.plus.features.reading

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
import androidx.navigation.compose.rememberNavController
import com.sarangsho.plus.ui.theme.SarangshoTheme
import androidx.navigation.NavHostController
import com.sarangsho.plus.ui.theme.SarangshoPalette


@Composable internal fun Welcome(nav: NavHostController) {
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

@Composable internal fun SignIn(nav: NavHostController) {
    var email by rememberSaveable { mutableStateOf("") }; var password by rememberSaveable { mutableStateOf("") }
    Scroll {
        Intro("আপনার পড়াশোনা সঙ্গে রাখুন", "অ্যাকাউন্টে প্রবেশ করুন অথবা আপাতত স্থানীয়ভাবে চালিয়ে যান।")
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("ইমেইল বা ফোন নম্বর") }, singleLine = true)
        OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(), label = { Text("পাসওয়ার্ড") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
        Primary("সাইন ইন") { nav.navigate("home") }
        Text("ডেমো UI: প্রকৃত authentication এখনো সংযুক্ত নয়।", color = SarangshoPalette.Muted, fontSize = 12.sp)
        OutlinedButton(onClick = { nav.navigate("home") }, modifier = Modifier.fillMaxWidth()) { Text("অ্যাকাউন্ট ছাড়া চালিয়ে যান") }
        Text("নতুন অ্যাকাউন্ট তৈরি করুন", Modifier.clickable { nav.navigate("signup") }, color = SarangshoPalette.Teal)
        Text("পাসওয়ার্ড ভুলে গেছেন?", Modifier.clickable { nav.navigate("password-reset") }, color = SarangshoPalette.Teal)
    }
}

@Composable internal fun Home(nav: NavHostController) {
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
                Text("অন্য শব্দ লিখুন অথবা ফিল্টার বদলান।", color = SarangshoPalette.Muted)
            }
        } else {
            filtered.forEach { Story(it.first, it.second, it.third, nav) }
        }
        Text("এগুলো UI পরীক্ষার নমুনা শিরোনাম, লাইভ সংবাদ নয়।", color = SarangshoPalette.Muted, fontSize = 11.sp)
    }
}

@Composable internal fun Article(nav: NavHostController) {
    Scroll {
        Kicker("আন্তর্জাতিক আইন • নমুনা নিবন্ধ")
        Text("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন", fontSize = 25.sp, fontWeight = FontWeight.Bold, lineHeight = 34.sp)
        Text("প্রকাশকাল ও হালনাগাদ সময়: উৎস-ডেটা সংযুক্ত হলে এখানে দেখানো হবে", color = SarangshoPalette.Muted, fontSize = 12.sp)
        Panel { Text("যাচাইয়ের অবস্থা", fontWeight = FontWeight.Bold); Text("প্রকৃত সংস্করণে প্রতিটি গুরুত্বপূর্ণ দাবির পাশে উৎস, তারিখ এবং যাচাইয়ের অবস্থা থাকবে।"); AssistChip(onClick = {}, enabled = false, label = { Text("প্রাথমিক উৎস") }) }
        RowLink("তিন-স্তরের সারাংশ", "দ্রুত ধারণা থেকে বিস্তারিত ব্যাখ্যা", Icons.Default.Subject) { nav.navigate("summary") }
        RowLink("ঘটনার টাইমলাইন", "তারিখ অনুযায়ী ঘটনাপ্রবাহ", Icons.Default.Timeline) { nav.navigate("timeline") }
        RowLink("পত্রিকার দৃষ্টিভঙ্গি", "শুধু অনুমোদিত ও পাওয়া উৎস", Icons.Default.CompareArrows) { nav.navigate("perspectives") }
        RowLink("জ্ঞান মানচিত্র", "প্রতিষ্ঠান ও ধারণার সম্পর্ক", Icons.Default.Hub) { nav.navigate("graph") }
        RowLink("উৎস ও যাচাই", "মূল নথি ও প্রকাশনার তথ্য", Icons.Default.Source) { nav.navigate("sources") }
        RowLink("তথ্য সংশোধনের অনুরোধ", "ভুল বা অস্পষ্ট তথ্য জানান", Icons.Default.Flag) { nav.navigate("report-story") }
        OutlinedButton(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.BookmarkBorder, null); Text("  সংরক্ষণ সুবিধা আসছে") }
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
        Panel { Kicker(labels[level]); Text(texts[level], fontSize = 16.sp, lineHeight = 28.sp); Text("নমুনা লেখা • যাচাইকৃত সংবাদ সারাংশ নয়", color = SarangshoPalette.Muted, fontSize = 11.sp) }
        RowLink("টাইমলাইন", "ঘটনার পূর্বাপর", Icons.Default.Timeline) { nav.navigate("timeline") }
    }
}

@Composable internal fun Timeline() {
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

@Composable internal fun Perspectives() {
    Scroll {
        Intro("একাধিক প্রকাশনার দৃষ্টিভঙ্গি", "প্রতিবেদনের ভাষা, গুরুত্ব, তথ্য ও অনুপস্থিত প্রসঙ্গ তুলনা করুন।")
        Panel { Icon(Icons.Default.Info, null, tint = SarangshoPalette.Gold); Text("এই নমুনা সংস্করণে কোনো সংবাদপত্রের নিবন্ধ সংগ্রহ বা তুলনা করা হয়নি।", fontWeight = FontWeight.SemiBold); Text("উৎসের অনুমতি, প্রবেশযোগ্যতা এবং মেটাডেটা যাচাই না হওয়া পর্যন্ত তুলনা ‘চালানো হয়নি’ হিসেবে থাকবে।", color = SarangshoPalette.Muted) }
        listOf("শিরোনাম ও ফ্রেমিং", "যাচাইযোগ্য তথ্য", "উদ্ধৃত বক্তব্য", "অনুপস্থিত বা অনিশ্চিত বিষয়").forEach { Panel { Text(it, fontWeight = FontWeight.SemiBold); Text("উৎস-নির্ভর তথ্য যুক্ত হলে এই অংশ পূরণ হবে।", color = SarangshoPalette.Muted, fontSize = 13.sp) } }
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
                    Surface(color = SarangshoPalette.Deep, shape = RoundedCornerShape(18.dp)) { Text("মূল বিষয়", Modifier.padding(14.dp), color = Color.White, fontWeight = FontWeight.Bold) }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("প্রতিষ্ঠান", "আইন", "রাষ্ট্র").forEach { Surface(color = SarangshoPalette.Soft, shape = RoundedCornerShape(10.dp)) { Text(it, Modifier.padding(10.dp), fontSize = 12.sp) } } }
                    Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(18.dp)) { Text("প্রভাব ও প্রেক্ষাপট", Modifier.padding(12.dp)) }
                }
            }
            Text("চিত্রটি ইন্টারঅ্যাকটিভ গ্রাফের নমুনা; সম্পর্কের ডেটা এখনো যুক্ত হয়নি।", color = SarangshoPalette.Muted, fontSize = 11.sp)
        }
        Text("সম্পর্ক: কারণ • আইনি এখতিয়ার • সদস্যপদ • প্রভাব • সময়গত সম্পর্ক", color = SarangshoPalette.Muted)
    }
}

@Composable internal fun DataExplorer() {
    Scroll {
        Intro("তথ্য ও পরিসংখ্যান", "সংখ্যার সঙ্গে একক, সময়কাল, সংজ্ঞা ও মূল উৎস দেখুন।")
        Panel { Kicker("ইন্ডিকেটর কার্ড • নমুনা"); Text("মূল্যস্ফীতি", fontSize = 20.sp, fontWeight = FontWeight.Bold); Text("সর্বশেষ মান: লাইভ ডেটা সংযুক্ত নয়", color = SarangshoPalette.Muted); HorizontalDivider(color = SarangshoPalette.Line); Text("সময়কাল: উল্লেখ করতে হবে"); Text("একক: শতাংশ"); Text("উৎস: সংশ্লিষ্ট সরকারি পরিসংখ্যান সংস্থা") }
        Panel {
            RowLink("সূচকের বিস্তারিত", "সংজ্ঞা, সময়কাল ও সংশোধন", Icons.Default.Insights) { nav.navigate("indicator-detail") }
        Text("তথ্য পড়ার নিয়ম", fontWeight = FontWeight.Bold)
            listOf("স্টক ও প্রবাহ আলাদা করুন", "নামমাত্র ও প্রকৃত মান গুলিয়ে ফেলবেন না", "পূর্বাভাস ও পর্যবেক্ষণ আলাদা রাখুন", "প্রাথমিক ও সংশোধিত উপাত্ত চিহ্নিত করুন").forEach { Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Check, null, tint = SarangshoPalette.Green); Spacer(Modifier.width(8.dp)); Text(it) } }
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
        else { Text("জনপ্রিয় অনুসন্ধান", fontWeight = FontWeight.Bold); listOf("বাংলাদেশ ব্যাংক", "সংবিধান", "জাতিসংঘ", "জলবায়ু অর্থায়ন").forEach { Text(it, Modifier.fillMaxWidth().clickable { query = it }.padding(vertical = 8.dp), color = SarangshoPalette.Teal) } }
        Text("প্রতিটি ফলাফলে প্রকাশক, মূল লিংক, প্রকাশের সময় ও প্রবেশযোগ্যতার অবস্থা দেখানো হবে।", color = SarangshoPalette.Muted)
    }
}

@Composable internal fun Saved(nav: NavHostController) {
    Scroll {
        Intro("আপনার সংরক্ষিত বিষয়", "পরে পড়ার জন্য রাখা সংবাদ, শব্দ ও রিভিশন কার্ড।")
        Panel { Icon(Icons.Default.Bookmarks, null, tint = SarangshoPalette.Teal, modifier = Modifier.size(30.dp)); Text("এখনো কিছু সংরক্ষণ করা হয়নি", fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("সংবাদ বা শব্দের পাশে বুকমার্ক আইকনে চাপ দিলে এখানে পাওয়া যাবে।", color = SarangshoPalette.Muted); Primary("সংবাদ দেখুন") { nav.navigate("feed") } }
        RowLink("সংরক্ষিত সংগ্রহ", "ফোল্ডার ও বিষয়ভিত্তিক তালিকা", Icons.Default.Folder) { nav.navigate("saved-collections") }
    }
}

@Composable internal fun Profile(nav: NavHostController) {
    Scroll {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Surface(color = SarangshoPalette.Soft, shape = RoundedCornerShape(50.dp)) { Icon(Icons.Default.Person, null, tint = SarangshoPalette.Teal, modifier = Modifier.padding(16.dp).size(34.dp)) }
            Column { Text("পাঠক প্রোফাইল", fontSize = 20.sp, fontWeight = FontWeight.Bold); Text("স্থানীয় ডেমো • সাইন ইন করা হয়নি", color = SarangshoPalette.Muted, fontSize = 12.sp) }
        }
        Panel { Text("শেখার অগ্রগতি", fontWeight = FontWeight.Bold); Text("প্রকৃত ব্যবহার-ডেটা যুক্ত না হওয়া পর্যন্ত কোনো স্কোর বা ধারাবাহিকতার সংখ্যা দেখানো হবে না।", color = SarangshoPalette.Muted) }
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
        Text("পছন্দ পরে সেটিংস থেকে বদলানো যাবে।", color = SarangshoPalette.Muted, fontSize = 12.sp)
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
        Text("এই পছন্দগুলো শুধু ডেমো সেশনে থাকে; অ্যাকাউন্টে সিঙ্ক হয় না।", color = SarangshoPalette.Muted, fontSize = 12.sp)
    }
}

@Composable internal fun Archive(nav: NavHostController) {
    Scroll {
        Intro("দৈনিক সংস্করণ", "আগের দিনের ব্রিফিং খুঁজুন এবং পরে পড়ার জন্য খুলুন।")
        Panel { Text("সাম্প্রতিক সংস্করণ", fontWeight = FontWeight.Bold); Text("প্রকাশিত সংস্করণের তালিকা ইঞ্জিন থেকে যুক্ত হলে এখানে তারিখ, ভাষা ও অবস্থা দেখানো হবে।", color = SarangshoPalette.Muted) }
        RowLink("আজকের সংস্করণ", "লাইভ কনটেন্ট সংযুক্ত নয়", Icons.Default.Today) { nav.navigate("edition") }
        RowLink("গতকালের সংস্করণ", "আর্কাইভ ডেটা অপেক্ষমাণ", Icons.Default.CalendarMonth) { nav.navigate("edition") }
    }
}

@Composable internal fun Edition(nav: NavHostController) {
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

@Composable internal fun Sources() {
    Scroll {
        Intro("উৎস ও যাচাই", "প্রতিটি তথ্য কোথা থেকে এসেছে এবং কীভাবে যাচাই হয়েছে তা দেখুন।")
        Panel { Text("উৎস-তথ্য", fontWeight = FontWeight.Bold); Text("প্রকৃত সংস্করণ যুক্ত হলে প্রকাশক, শিরোনাম, মূল URL, প্রকাশকাল, হালনাগাদ সময়, প্রবেশযোগ্যতা ও যাচাইয়ের অবস্থা দেখানো হবে।") }
        RowLink("উৎসের রেকর্ড", "প্রকাশক, অনুমতি ও মূল URL", Icons.Default.Link) { nav.navigate("source-detail") }
        listOf("প্রাথমিক সরকারি নথি", "আদালত ও আইনগত নথি", "আন্তর্জাতিক সংস্থা", "সংবাদ প্রকাশনা").forEach {
            Panel { Text(it, fontWeight = FontWeight.SemiBold); Text("উৎস-রেজিস্ট্রি থেকে তথ্য আসবে", color = SarangshoPalette.Muted, fontSize = 12.sp) }
        }
    }
}

@Composable internal fun Notifications() {
    Scroll {
        Intro("নোটিফিকেশন", "নতুন সংস্করণ, ডাউনলোড ও রিভিশনের আপডেট এখানে দেখা যাবে।")
        Panel { Icon(Icons.Default.NotificationsNone, null, tint = SarangshoPalette.Teal, modifier = Modifier.size(30.dp)); Text("এখনো কোনো নোটিফিকেশন নেই", fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("নতুন আপডেট এলে এখানে তারিখ ও প্রয়োজনীয় পদক্ষেপসহ দেখানো হবে।", color = SarangshoPalette.Muted) }
    }
}

@Composable internal fun About() {
    Scroll {
        Intro("Sarangsho Plus", "সংবাদকে বোঝা, শেখা ও মনে রাখার একটি বাংলা-প্রথম প্ল্যাটফর্ম।")
        Panel { Text("সংস্করণ 0.1.0 • প্রোটোটাইপ", fontWeight = FontWeight.Bold); Text("এই অ্যাপের লক্ষ্য হলো যাচাইকৃত বর্তমান ঘটনাকে প্রেক্ষাপট, ধারণা, তথ্যসূত্র ও পরীক্ষার অনুশীলনের সঙ্গে যুক্ত করা।") }
        Text("স্বাধীনভাবে যাচাই না করা পর্যন্ত কোনো সংবাদ, সংখ্যা বা উৎসকে লাইভ বলে উপস্থাপন করা হবে না।", color = SarangshoPalette.Muted)
    }
}


@Composable internal fun ReadingHistory() {
    Scroll {
        Intro("পড়ার ইতিহাস", "সম্প্রতি খোলা পাঠে ফিরে যান।")
        Panel { Text("এখনো কোনো পড়ার ইতিহাস নেই", fontWeight = FontWeight.Bold); Text("পড়া শুরু করলে আপনার সাম্প্রতিক পাঠ এখানে দেখা যাবে। এই প্রোটোটাইপে ইতিহাস সংরক্ষণ চালু হয়নি।", color = SarangshoPalette.Muted) }
    }
}

@Composable internal fun SavedCollections() {
    Scroll {
        Intro("সংরক্ষিত সংগ্রহ", "সংবাদ, শব্দ ও প্রশ্ন নিজের মতো গুছিয়ে রাখুন।")
        Panel { Text("কোনো সংগ্রহ তৈরি হয়নি", fontWeight = FontWeight.Bold); Text("সংগ্রহ ও ফোল্ডার তৈরি করার সুবিধা স্থানীয় ডেটাবেস যুক্ত হলে চালু হবে।", color = SarangshoPalette.Muted) }
    }
}

@Composable internal fun SourceDetail() {
    Scroll {
        Intro("উৎসের পরিচিতি", "প্রকাশক ও মূল নথির নির্ভরযোগ্যতা যাচাইয়ের তথ্য।")
        Panel { Text("উৎসের রেকর্ড অপেক্ষমাণ", fontWeight = FontWeight.Bold); Text("প্রকাশকের নাম, মূল URL, প্রকাশকাল, প্রবেশযোগ্যতা, ব্যবহারের অনুমতি এবং সংশ্লিষ্ট দাবির তালিকা প্রকৃত উৎস-রেকর্ড থেকে আসবে।", color = SarangshoPalette.Muted) }
    }
}

@Composable internal fun IndicatorDetail() {
    Scroll {
        Intro("সূচকের বিস্তারিত", "সংখ্যা বোঝার আগে সংজ্ঞা, একক ও সময়কাল যাচাই করুন।")
        Panel { Text("ডেটা সংযুক্ত নয়", fontWeight = FontWeight.Bold); Text("সূচকের মান, সময়সীমা, একক, পদ্ধতি, সংশোধনের ইতিহাস এবং মূল প্রকাশনা যুক্ত হলে এখানে দেখানো হবে।", color = SarangshoPalette.Muted) }
        Text("কোনো চার্ট বা মান দেখানো হচ্ছে না, কারণ যাচাইকৃত ডেটা এখনো সংযুক্ত নয়।", color = SarangshoPalette.Muted)
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
