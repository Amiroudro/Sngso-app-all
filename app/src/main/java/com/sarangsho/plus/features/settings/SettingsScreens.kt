package com.sarangsho.plus.features.settings

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
import androidx.navigation.NavHostController
import com.sarangsho.plus.ui.theme.SarangshoPalette


@Composable internal fun Settings(nav: NavHostController) {
    Scroll {
        Intro("সেটিংস", "অ্যাপের ব্যবহার, পড়া ও ব্যক্তিগত তথ্য নিয়ন্ত্রণ করুন।")
        RowLink("পড়ার পছন্দ", "ফন্ট, আকার, লাইন স্পেসিং ও থিম", Icons.Default.TextFields) { nav.navigate("reading-settings") }
        RowLink("অফলাইন লাইব্রেরি", "ডাউনলোড ও স্টোরেজ", Icons.Default.Download) { nav.navigate("downloads") }
        RowLink("নোটিফিকেশন", "কোন আপডেট পাবেন", Icons.Default.Notifications) { nav.navigate("notifications") }
        RowLink("গোপনীয়তা", "ডেটা ও অ্যাকাউন্ট নিয়ন্ত্রণ", Icons.Default.PrivacyTip) { nav.navigate("privacy") }
        RowLink("সহায়তা ও মতামত", "সমস্যা জানান বা প্রশ্ন করুন", Icons.Default.SupportAgent) { nav.navigate("feedback") }
        RowLink("অ্যাপ সম্পর্কে", "সংস্করণ ও প্রকল্পের তথ্য", Icons.Default.Info) { nav.navigate("about") }
    }
}

@Composable internal fun ReadingSettings() {
    var fontSize by rememberSaveable { mutableFloatStateOf(18f) }; var lineSpacing by rememberSaveable { mutableFloatStateOf(1.5f) }
    var sepia by rememberSaveable { mutableStateOf(false) }
    Scroll {
        Intro("পড়ার পছন্দ", "দীর্ঘ লেখা আরাম করে পড়ার জন্য নিজের মতো সাজান।")
        Panel {
            Text("লেখার আকার: " + fontSize.toInt().toString() + " sp", fontWeight = FontWeight.SemiBold)
            Slider(value = fontSize, onValueChange = { fontSize = it }, valueRange = 16f..26f)
            Text("লাইন স্পেসিং: " + "%.1f".format(lineSpacing), fontWeight = FontWeight.SemiBold)
            Slider(value = lineSpacing, onValueChange = { lineSpacing = it }, valueRange = 1.2f..2.0f)
            Row(verticalAlignment = Alignment.CenterVertically) { Text("উষ্ণ কাগজের নমুনা", Modifier.weight(1f)); Switch(checked = sepia, onCheckedChange = { sepia = it }) }
        }
        Surface(color = if (sepia) SarangshoPalette.Paper else MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("নমুনা পাঠ", fontSize = fontSize.sp, lineHeight = (fontSize * lineSpacing).sp, color = if (sepia) SarangshoPalette.Ink else MaterialTheme.colorScheme.onSurface)
                Text("বাংলা ও ইংরেজি একসঙ্গে পড়ার সময় অক্ষরের গঠন এবং লাইনের ফাঁক যেন পরিষ্কার থাকে।", fontSize = fontSize.sp, lineHeight = (fontSize * lineSpacing).sp, color = SarangshoPalette.Ink)
            }
        }
        Text("এই নিয়ন্ত্রণগুলো শুধু নমুনা পাঠে প্রযোজ্য; স্থায়ীভাবে সংরক্ষিত হয় না।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable internal fun Downloads() {
    Scroll {
        Intro("অফলাইন লাইব্রেরি", "ডাউনলোড করা সংস্করণ নেটওয়ার্ক ছাড়াও পড়ুন।")
        Panel { Icon(Icons.Default.CloudOff, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp)); Text("কোনো সংস্করণ ডাউনলোড করা নেই", fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("ডাউনলোড সুবিধা যুক্ত হলে সংরক্ষিত সংস্করণ, ফাইলের আকার, শেষ সিঙ্ক ও স্টোরেজ ব্যবহার এখানে দেখানো হবে।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
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
    var message by rememberSaveable { mutableStateOf("") }; var category by rememberSaveable { mutableStateOf("সাধারণ মতামত") }
    Scroll {
        Intro("আপনার মতামত", "কোন জায়গাটি কাজ করছে না বা কী উন্নত করা দরকার, লিখে জানান।")
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { listOf("সাধারণ মতামত", "সমস্যা", "ভুল তথ্য").forEach { FilterChip(selected = category == it, onClick = { category = it }, label = { Text(it) }) } }
        OutlinedTextField(message, { message = it }, Modifier.fillMaxWidth().height(180.dp), label = { Text("বিস্তারিত লিখুন") }, placeholder = { Text("কী ঘটেছে, কোন স্ক্রিনে, কী আশা করেছিলেন...") })
        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("পাঠানো শিগগিরই চালু হবে") }
        Text("মতামত পাঠানোর সার্ভিস এখনো সংযুক্ত নয়; এই খসড়া কোথাও পাঠানো হবে না।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
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
    var language by rememberSaveable { mutableStateOf("বাংলা") }
    Scroll {
        Intro("অ্যাপের ভাষা", "প্রধান ব্যাখ্যার ভাষা বেছে নিন। ইংরেজি পরিভাষা প্রয়োজন অনুযায়ী পাশে থাকবে।")
        listOf("বাংলা", "English", "বাংলা + English").forEach {
            Row(Modifier.fillMaxWidth().clickable { language = it }.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = language == it, onClick = { language = it }); Text(it)
            }
        }
        Text("নির্বাচন: " + language + " • এই পছন্দ এখনো স্থায়ীভাবে সংরক্ষিত নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
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
        Text("সাইন ইন পেজে ফিরুন", Modifier.clickable { nav.navigate("signin") }, color = MaterialTheme.colorScheme.primary)
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
    var details by rememberSaveable { mutableStateOf("") }
    Scroll {
        Intro("তথ্য সংশোধনের অনুরোধ", "ভুল তারিখ, সংখ্যা, উদ্ধৃতি বা প্রসঙ্গ চোখে পড়লে নির্দিষ্ট করে জানান।")
        OutlinedTextField(details, { details = it }, Modifier.fillMaxWidth().height(180.dp), label = { Text("কী ভুল হয়েছে?") }, placeholder = { Text("দাবি, সংশোধনের প্রস্তাব এবং প্রাসঙ্গিক উৎসের লিংক লিখুন।") })
        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("অনুরোধ পাঠানো শিগগিরই চালু হবে") }
        Text("রিপোর্টিং সার্ভিস সংযুক্ত না হওয়া পর্যন্ত এই খসড়া পাঠানো হবে না।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}
