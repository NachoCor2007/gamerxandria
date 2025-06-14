package com.austral.gamerxandria.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark color scheme with our custom colors
private val DarkColorScheme = darkColorScheme(
    primary = BackgroundDark,
    secondary = CardBackgroundDark,
    tertiary = ActiveTabColorDark,
    background = BackgroundDark,
    surface = CardBackgroundDark,
    onPrimary = TextWhiteDark,
    onSecondary = TextGrayDark,
    onTertiary = TextWhiteDark,
    onBackground = TextWhiteDark,
    onSurface = TextWhiteDark,
    error = ErrorTextDark
)

// Light color scheme with our custom colors
private val LightColorScheme = lightColorScheme(
    primary = ButtonRedLight,
    secondary = AccentPurpleLight,
    tertiary = ActiveTabColorLight,
    background = BackgroundLight,
    surface = CardBackgroundLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextDarkLight,
    onSurface = TextDarkLight,
    error = ErrorTextLight
)

@Composable
fun GamerxandriaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}