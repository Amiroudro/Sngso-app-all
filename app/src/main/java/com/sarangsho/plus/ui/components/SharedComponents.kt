package com.sarangsho.plus.ui.components

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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sarangsho.plus.ui.theme.SarangshoPalette


@Composable
internal fun Scroll(content: @Composable ColumnScope.() -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Column(verticalArrangement = Arrangement.spacedBy(14.dp), content = content) }
    }
}

@Composable internal fun Intro(title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(title, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = SarangshoPalette.Ink)
        Text(body, fontSize = 14.sp, color = SarangshoPalette.Muted, lineHeight = 22.sp)
    }
}

@Composable internal fun Kicker(text: String) { Text(text.uppercase(), fontSize = 10.sp, color = SarangshoPalette.Teal, fontWeight = FontWeight.Bold, letterSpacing = 1.sp) }

@Composable internal fun Panel(content: @Composable ColumnScope.() -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(15.dp), border = BorderStroke(1.dp, SarangshoPalette.Line), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}

@Composable internal fun Primary(text: String, action: () -> Unit) {
    Button(action, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = SarangshoPalette.Teal), contentPadding = PaddingValues(14.dp)) { Text(text, fontWeight = FontWeight.SemiBold) }
}

@Composable internal fun RowLink(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector, action: () -> Unit) {
    Panel {
        Row(Modifier.clickable(role = Role.Button, onClick = action), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(color = SarangshoPalette.Soft, shape = RoundedCornerShape(10.dp)) { Icon(icon, null, tint = SarangshoPalette.Teal, modifier = Modifier.padding(10.dp).size(22.dp)) }
            Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.SemiBold); Text(detail, color = SarangshoPalette.Muted, fontSize = 12.sp) }
            Icon(Icons.Default.ChevronRight, null, tint = SarangshoPalette.Muted)
        }
    }
}

@Composable internal fun Story(title: String, category: String, detail: String, nav: NavHostController) {
    Panel {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Kicker(category); Text("নমুনা", color = SarangshoPalette.Gold, fontSize = 10.sp) }
        Text(title, fontSize = 18.sp, lineHeight = 27.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(detail, color = SarangshoPalette.Muted, fontSize = 11.sp, modifier = Modifier.weight(1f))
            Text("পড়ুন →", color = SarangshoPalette.Teal, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(role = Role.Button) { nav.navigate("article") })
        }
    }
}
