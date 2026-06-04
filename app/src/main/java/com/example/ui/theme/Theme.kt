package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    secondary = ChampagneSecondary,
    tertiary = AntiqueBronze,
    background = ObsidianDarkBackground,
    surface = ObsidianDarkSurface,
    onPrimary = Color(0xFF100F14),
    onSecondary = Color(0xFF100F14),
    onTertiary = Color.White,
    onBackground = WarmWhiteText,
    onSurface = WarmWhiteText,
    surfaceVariant = Color(0xFF272530),
    onSurfaceVariant = WarmMutedText
)

private val LightColorScheme = lightColorScheme(
    primary = AntiqueBronze,
    secondary = GoldPrimary,
    tertiary = ChampagneSecondary,
    background = Color(0xFFFAF9F6), // Warm soft Alabaster
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color(0xFF100F14),
    onTertiary = Color(0xFF100F14),
    onBackground = Color(0xFF1B1A22),
    onSurface = Color(0xFF1B1A22),
    surfaceVariant = Color(0xFFF0EEE9),
    onSurfaceVariant = Color(0xFF6B6673)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Force consistent branding
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
