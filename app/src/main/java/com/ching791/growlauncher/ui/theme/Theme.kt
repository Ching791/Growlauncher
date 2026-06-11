package com.ching791.growlauncher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

enum class ThemeAccent {
    PURPLE,
    BLUE,
    GREEN
}

@Composable
fun GrowlauncherTheme(
    darkTheme: Boolean,
    accent: ThemeAccent,
    content: @Composable () -> Unit
) {
    val primary = when (accent) {
        ThemeAccent.PURPLE -> PurpleLight
        ThemeAccent.BLUE -> BlueLight
        ThemeAccent.GREEN -> GreenLight
    }

    val colorScheme = if (darkTheme) {
        darkColorScheme(primary = primary)
    } else {
        lightColorScheme(primary = primary)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
