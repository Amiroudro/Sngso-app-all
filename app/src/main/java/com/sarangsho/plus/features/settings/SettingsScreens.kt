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
        RowLink("গোপনীয়তা", "ডেটা ও অ্যাকাউন্ট নিয়ন্ত্রণ", Icons.Default.PrivacyTip) { nav.navigate("help") }
        RowLink("সহায়তা ও মতামত", "সমস্যা জানান বা প্রশ্ন করুন", Icons.Default.SupportAgent) { nav.navigate("help") }
        RowLink("অ্যাপ সম্পর্কে", "সংস্করণ ও প্রকল্পের তথ্য", Icons.Default.Info) { nav.navigate("about") }
    }
}

@Composable internal fun ReadingSettings() {
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

@Composable internal fun Downloads() {
    Scroll {
        Intro("অফলাইন লাইব্রেরি", "ডাউনলোড করা সংস্করণ নেটওয়ার্ক ছাড়াও পড়ুন।")
        Panel { Icon(Icons.Default.CloudOff, null, tint = SarangshoPalette.Teal, modifier = Modifier.size(30.dp)); Text("কোনো সংস্করণ ডাউনলোড করা নেই", fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("ডাউনলোড সুবিধা যুক্ত হলে সংরক্ষিত সংস্করণ, ফাইলের আকার, শেষ সিঙ্ক ও স্টোরেজ ব্যবহার এখানে দেখানো হবে।", color = SarangshoPalette.Muted) }
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
                if (question in expanded) Text(answer, color = SarangshoPalette.Muted, lineHeight = 22.sp)
            }
        }
    }
}

@Composable internal fun Feedback() {
    var message by remember { mutableStateOf("") }; var category by remember { mutableStateOf("সাধারণ মতামত") }
    Scroll {
        Intro("আপনার মতামত", "কোন জায়গাটি কাজ করছে না বা কী উন্নত করা দরকার, লিখে জানান।")
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { listOf("সাধারণ মতামত", "সমস্যা", "ভুল তথ্য").forEach { FilterChip(selected = category == it, onClick = { category = it }, label = { Text(it) }) } }
        OutlinedTextField(message, { message = it }, Modifier.fillMaxWidth().height(180.dp), label = { Text("বিস্তারিত লিখুন") }, placeholder = { Text("কী ঘটেছে, কোন স্ক্রিনে, কী আশা করেছিলেন...") })
        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("পাঠানো শিগগিরই চালু হবে") }
        Text("মতামত পাঠানোর সার্ভিস এখনো সংযুক্ত নয়; এই খসড়া কোথাও পাঠানো হবে না।", color = SarangshoPalette.Muted, fontSize = 12.sp)
    }
}
