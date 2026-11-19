package com.sarangsho.plus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object SarangshoPalette {
    val Paper = Color(0xFFF7F5EF)
    val Surface = Color(0xFFFFFFFF)
    val Ink = Color(0xFF172B32)
    val Muted = Color(0xFF637279)
    val Teal = Color(0xFF0D5960)
    val Deep = Color(0xFF123B43)
    val Gold = Color(0xFFC58A36)
    val Line = Color(0xFFE5E2D9)
    val Soft = Color(0xFFF0EEE7)
    val Green = Color(0xFF287A59)
    val Error = Color(0xFFB3261E)

    val DarkPaper = Color(0xFF11191C)
    val DarkSurface = Color(0xFF1A2529)
    val DarkInk = Color(0xFFE9EFED)
    val DarkMuted = Color(0xFFB3C0BC)
    val DarkLine = Color(0xFF354348)
    val DarkSoft = Color(0xFF263438)
}

@Composable
fun SarangshoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = SarangshoPalette.Teal,
            onPrimary = Color.White,
            secondary = SarangshoPalette.Gold,
            onSecondary = SarangshoPalette.Ink,
            background = SarangshoPalette.DarkPaper,
            onBackground = SarangshoPalette.DarkInk,
            surface = SarangshoPalette.DarkSurface,
            onSurface = SarangshoPalette.DarkInk,
            outline = SarangshoPalette.DarkLine,
            error = Color(0xFFFFB4AB)
        )
    } else {
        lightColorScheme(
            primary = SarangshoPalette.Teal,
            onPrimary = Color.White,
            secondary = SarangshoPalette.Gold,
            onSecondary = SarangshoPalette.Ink,
            background = SarangshoPalette.Paper,
            onBackground = SarangshoPalette.Ink,
            surface = SarangshoPalette.Surface,
            onSurface = SarangshoPalette.Ink,
            outline = SarangshoPalette.Line,
            error = SarangshoPalette.Error
        )
    }

    MaterialTheme(colorScheme = colors, content = content)
}
