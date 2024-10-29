package com.example.tp1_epicerie.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFECECEC),
    primaryContainer = Color(0xFFEAEAEA),
    onPrimary = Color(0xFF383838),

    secondary = Color(0xFFDBF3D8),
    secondaryContainer = Color(0xFF000000),
    tertiary = Color(0xFFF6D8CE),
    background = Color(0xFF1C1C1C)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF000000),
    primaryContainer = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFD5D5D5),
    secondary = Color(0xFFE3F2E1),
    secondaryContainer = Color(0xFF1C1C1C),
    tertiary = Color(0xFFF6D8CE),
    background = Color(0xFFEFEFEF)

    /* Other default colors to override
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TP1EpicerieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}