package com.example.gamebrowser.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NeonBlue,
    onPrimary = Color.Black,
    primaryContainer = NeonBlueDark,
    onPrimaryContainer = Color.White,

    secondary = ElectricPurple,
    onSecondary = Color.White,
    secondaryContainer = ElectricPurpleDark,
    onSecondaryContainer = Color.White,

    tertiary = NeonPink,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF8B0048),
    onTertiaryContainer = Color.White,

    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFF8B0000),
    onErrorContainer = Color.White,

    background = DarkBackground,
    onBackground = TextPrimaryDark,

    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,

    surfaceTint = NeonBlue,
    inverseSurface = LightSurface,
    inverseOnSurface = TextPrimaryLight,

    outline = Color(0xFF4A5079),
    outlineVariant = Color(0xFF2A2F4F),

    scrim = Color.Black.copy(alpha = 0.5f),
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricPurpleDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE9D5FF),
    onPrimaryContainer = Color(0xFF3B0764),

    secondary = NeonBlueDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCFF6FF),
    onSecondaryContainer = Color(0xFF003D4D),

    tertiary = Color(0xFFDB0059),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD9E4),
    onTertiaryContainer = Color(0xFF5C0028),

    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFEDED),
    onErrorContainer = Color(0xFF8B0000),

    background = LightBackground,
    onBackground = TextPrimaryLight,

    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,

    surfaceTint = ElectricPurpleDark,
    inverseSurface = DarkSurface,
    inverseOnSurface = TextPrimaryDark,

    outline = Color(0xFFD1D5DB),
    outlineVariant = Color(0xFFE5E7EB),

    scrim = Color.Black.copy(alpha = 0.3f),
)

@Composable
fun GameBrowserTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // Set to false by default for consistent gaming theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}