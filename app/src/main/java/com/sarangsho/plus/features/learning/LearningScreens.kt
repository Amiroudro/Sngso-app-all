package com.sarangsho.plus.features.learning
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
import androidx.navigation.NavHostController
import com.sarangsho.plus.ui.theme.SarangshoPalette


@Composable internal fun Vocabulary(nav: NavHostController) {
    var reveal by rememberSaveable { mutableStateOf(false) }
    var bookmarked by rememberSaveable { mutableStateOf(false) }
    Scroll {
        Intro("আজকের শব্দ", "শব্দের অর্থ, ব্যবহার ও পরীক্ষায় প্রয়োগ শিখুন।")
        Panel {
            Kicker("NOUN"); Text("Resilience", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface); Text("/rɪˈzɪliəns/ • উচ্চারণ", color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (reveal) {
                Text("বাংলা অর্থ: প্রতিকূলতা কাটিয়ে ওঠার সক্ষমতা", fontWeight = FontWeight.SemiBold)
                Text("Definition: The ability to recover quickly from difficulty.")
                Text("Synonym: toughness • Antonym: fragility", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                Text("Example: Communities need resilience after a natural disaster.")
            } else Text("অর্থ ও উদাহরণ দেখতে নিচের বোতাম চাপুন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Primary(if (reveal) "আবার লুকান" else "অর্থ দেখুন") { reveal = !reveal }
        }
        OutlinedButton(
            onClick = { bookmarked = !bookmarked },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                if (bookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = null
            )
            Text(if (bookmarked) "  এই সেশনে সংরক্ষিত" else "  সাময়িকভাবে সংরক্ষণ করুন")
        }
        Text(
            if (bookmarked) "শব্দটি এই স্ক্রিনের সেশনে চিহ্নিত হয়েছে। স্থায়ী বুকমার্ক ও সিঙ্ক এখনো যুক্ত হয়নি।"
            else "এটি সাময়িক বুকমার্ক; অ্যাপ বন্ধ করলে সংরক্ষণ থাকবে না।",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
        )
        listOf("Accountability", "Sustainable", "Mitigate", "Consensus").forEach { word ->
            Text(word, Modifier.fillMaxWidth().clickable { nav.navigate("word-detail") }.padding(vertical = 6.dp), fontWeight = FontWeight.Medium)
        }
    }
}

@Composable internal fun Challenge(nav: NavHostController) {
    var selected by rememberSaveable { mutableIntStateOf(-1) }
    Scroll {
        Kicker("BCS & BANK • নমুনা প্রশ্ন"); Intro("বাংলাদেশের সংবিধান", "প্রশ্ন ১ / ১ • নমুনা প্রশ্ন।")
        Panel {
            Text("বাংলাদেশের সংবিধান কবে গণপরিষদে গৃহীত হয়?", fontSize = 19.sp, fontWeight = FontWeight.Bold, lineHeight = 28.sp)
            listOf("৪ নভেম্বর ১৯৭২", "১৬ ডিসেম্বর ১৯৭২", "২৬ মার্চ ১৯৭১", "১০ জানুয়ারি ১৯৭২").forEachIndexed { i, option ->
                Surface(color = if (selected == i) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(10.dp), border = BorderStroke(1.dp, if (selected == i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant), modifier = Modifier.fillMaxWidth().clickable { selected = i }) {
                    Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) { RadioButton(selected == i, { selected = i }); Text("Option " + (i + 1) + "  " + option) }
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
                nav.navigate("quiz-results")
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("সেশন শেষ করুন") }
        OutlinedButton(onClick = { nav.navigate("revision") }, modifier = Modifier.fillMaxWidth()) { Text("রিভিশনে যোগ করুন") }
    }
}

@Composable internal fun Writing() {
    var draft by rememberSaveable { mutableStateOf("") }
    Scroll {
        Intro("লিখিত অনুশীলন", "নিজের ভাষায় উত্তর লিখুন। খসড়া সংরক্ষণ এখনো সংযুক্ত নয়।")
        Panel { Kicker("বিশ্লেষণী প্রশ্ন"); Text("জলবায়ু পরিবর্তন বাংলাদেশের উন্নয়ন পরিকল্পনায় কী ধরনের চ্যালেঞ্জ তৈরি করে? উদাহরণসহ আলোচনা করুন।", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 28.sp) }
        OutlinedTextField(draft, { draft = it }, Modifier.fillMaxWidth().height(220.dp), label = { Text("আপনার উত্তর লিখুন") }, placeholder = { Text("ভূমিকা, মূল আলোচনা, উদাহরণ ও উপসংহার...") })
        Text(draft.length.toString() + " অক্ষর", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("খসড়া সংরক্ষণ শিগগিরই চালু হবে") }
    }
}

@Composable internal fun Revision() {
    var flipped by rememberSaveable { mutableStateOf(false) }
    Scroll {
        Intro("রিভিশন ডেক", "সক্রিয় স্মরণ দিয়ে শেখা বিষয় মনে রাখুন।")
        LinearProgressIndicator(progress = { 0.35f }, modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary, trackColor = MaterialTheme.colorScheme.outlineVariant)
        Text("কার্ড ৭ / ২০ • নমুনা সেশন", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        Panel {
            Text(if (flipped) "উত্তর" else "প্রশ্ন", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(if (flipped) "জাতিসংঘ প্রতিষ্ঠিত হয় ১৯৪৫ সালের ২৪ অক্টোবর।" else "জাতিসংঘ কবে প্রতিষ্ঠিত হয়?", fontSize = 21.sp, fontWeight = FontWeight.Bold, lineHeight = 31.sp)
            Text(if (flipped) "২৪ অক্টোবর, United Nations Day." else "উত্তর দেখতে কার্ডে চাপ দিন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedButton(onClick = { flipped = !flipped }, modifier = Modifier.fillMaxWidth()) { Text(if (flipped) "প্রশ্ন দেখুন" else "উত্তর দেখুন") }
        }
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
                Text("এটি বর্তমানে একটি নমুনা প্রশ্নের ফলাফল; পূর্ণ প্রশ্নব্যাংক বা স্থায়ী ইতিহাস নয়।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Text("এই সেশনের ফল পাওয়া যায়নি", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("প্রথমে অনুশীলনের প্রশ্নের উত্তর জমা দিন।", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Primary("আবার অনুশীলন করুন") { nav.navigate("quiz-setup") }
        OutlinedButton(onClick = { nav.navigate("revision") }, modifier = Modifier.fillMaxWidth()) { Text("রিভিশন ডেক খুলুন") }
    }
}

@Composable internal fun WordDetail() {
    Scroll {
        Intro("শব্দের ব্যবহার", "শব্দের অর্থের পাশাপাশি বাক্যে ব্যবহার ও পরীক্ষায় প্রয়োগ বুঝুন।")
        Panel {
            Text("Resilience", fontSize = 28.sp, fontWeight = FontWeight.Bold); Text("/rɪˈzɪliəns/ • noun", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("বাংলা অর্থ: প্রতিকূলতা কাটিয়ে ওঠার সক্ষমতা")
            Text("Definition: The ability to recover quickly from difficulty.")
            Text("Synonyms: toughness, adaptability"); Text("Antonym: fragility")
            Text("Collocation: community resilience, economic resilience")
            Text("Example: Resilience helps communities recover after disasters.")
        }
        Text("এই শব্দের ডেটা নমুনা; অভিধান-উৎস যুক্ত হলে উৎসের তথ্য দেখানো হবে।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable internal fun StudyPlan() {
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
            Text(done.size.toString() + " / " + tasks.size + " সম্পন্ন", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("ডেমো পরিকল্পনা: অগ্রগতি এই স্ক্রিনে থাকে, স্থায়ীভাবে সংরক্ষিত হয় না।", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}


@Composable internal fun PracticeHistory() {
    Scroll {
        Intro("অনুশীলনের ইতিহাস", "আগের সেশন, বিষয়ভিত্তিক দুর্বলতা ও পুনরায় চেষ্টা করার সুযোগ।")
        Panel { Text("কোনো সেশন সংরক্ষিত নেই", fontWeight = FontWeight.Bold); Text("প্রশ্নব্যাংক, স্কোরিং ও স্থানীয় সংরক্ষণ যুক্ত হলে প্রতিটি প্রচেষ্টার তারিখ, ফল এবং ভুলের ব্যাখ্যা এখানে থাকবে।", color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}
