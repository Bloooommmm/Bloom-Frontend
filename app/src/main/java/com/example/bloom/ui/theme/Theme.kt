package com.example.bloom.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val BloomColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,

    secondary = PrimaryShadow,
    onSecondary = OnPrimary,

    background = AppBackground,
    onBackground = TextPrimary,

    surface = SurfaceWhite,
    onSurface = TextPrimary,

    outline = BorderLight,
    error = Error
)

@Composable
fun BloomTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BloomColorScheme,
        typography = AppTypography,
        content = content
    )
}