package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = TokTokPink,
    onPrimary = Color.White,
    primaryContainer = TokTokPink.copy(alpha = 0.2f),
    onPrimaryContainer = TokTokPink,
    secondary = TokTokCyan,
    onSecondary = Color.Black,
    secondaryContainer = TokTokCyan.copy(alpha = 0.2f),
    onSecondaryContainer = TokTokCyan,
    tertiary = PurpleAccent,
    background = TokTokDarkBg,
    onBackground = Color.White,
    surface = TokTokSurfaceDark,
    onSurface = Color.White,
    surfaceVariant = TokTokCardDark,
    onSurfaceVariant = Color(0xFFC0C1CD),
    outline = TokTokBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = TokTokPink,
    onPrimary = Color.White,
    primaryContainer = TokTokPink.copy(alpha = 0.12f),
    onPrimaryContainer = TokTokPink,
    secondary = TokTokCyan,
    onSecondary = Color.Black,
    secondaryContainer = TokTokCyan.copy(alpha = 0.12f),
    onSecondaryContainer = Color(0xFF007572),
    tertiary = PurpleAccent,
    background = TokTokLightBg,
    onBackground = TokTokTextPrimaryLight,
    surface = TokTokSurfaceLight,
    onSurface = TokTokTextPrimaryLight,
    surfaceVariant = TokTokCardLight,
    onSurfaceVariant = TokTokTextSecondaryLight,
    outline = TokTokBorderLight
)

@Composable
fun TokTokTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
