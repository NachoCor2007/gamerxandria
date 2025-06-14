package com.austral.gamerxandria.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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

// Custom color provider to access current theme colors throughout the app
data class GamerxandriaColors(
    val background: Color,
    val cardBackground: Color,
    val accentPurple: Color,
    val buttonRed: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val activeTabColor: Color,
    val inactiveTabColor: Color,
    val statusWrong: Color,
    val statusNear: Color,
    val statusCorrect: Color,
    val errorBackground: Color,
    val errorText: Color
)

val LocalGamerxandriaColors = staticCompositionLocalOf {
    GamerxandriaColors(
        background = BackgroundDark,
        cardBackground = CardBackgroundDark,
        accentPurple = AccentPurpleDark,
        buttonRed = ButtonRedDark,
        textPrimary = TextWhiteDark,
        textSecondary = TextGrayDark,
        activeTabColor = ActiveTabColorDark,
        inactiveTabColor = InactiveTabColorDark,
        statusWrong = StatusWrongDark,
        statusNear = StatusNearDark,
        statusCorrect = StatusCorrectDark,
        errorBackground = ErrorBackgroundDark,
        errorText = ErrorTextDark
    )
}

@Composable
fun GamerxandriaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Set app-specific colors based on theme
    val gamerxandriaColors = if (darkTheme) {
        GamerxandriaColors(
            background = BackgroundDark,
            cardBackground = CardBackgroundDark,
            accentPurple = AccentPurpleDark,
            buttonRed = ButtonRedDark,
            textPrimary = TextWhiteDark,
            textSecondary = TextGrayDark,
            activeTabColor = ActiveTabColorDark,
            inactiveTabColor = InactiveTabColorDark,
            statusWrong = StatusWrongDark,
            statusNear = StatusNearDark,
            statusCorrect = StatusCorrectDark,
            errorBackground = ErrorBackgroundDark,
            errorText = ErrorTextDark
        )
    } else {
        GamerxandriaColors(
            background = BackgroundLight,
            cardBackground = CardBackgroundLight,
            accentPurple = AccentPurpleLight,
            buttonRed = ButtonRedLight,
            textPrimary = TextDarkLight,
            textSecondary = TextGrayLight,
            activeTabColor = ActiveTabColorLight,
            inactiveTabColor = InactiveTabColorLight,
            statusWrong = StatusWrongLight,
            statusNear = StatusNearLight,
            statusCorrect = StatusCorrectLight,
            errorBackground = ErrorBackgroundLight,
            errorText = ErrorTextLight
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}