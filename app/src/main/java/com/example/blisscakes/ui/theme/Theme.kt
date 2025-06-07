package com.example.blisscakes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PinkPrimary,
    background = BackgroundLight,
    onPrimary = TextLight,
    surface = White,
    onSurface = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = PinkDark,
    background = BackgroundDark,
    onBackground = TextLight,
    onPrimary = TextDark,
    surface = DarkBackground,
    onSurface = White
)

@Composable
fun BlissCakesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
