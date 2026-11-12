package com.example.bloom.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import id.ac.binus.bloomapp.ui.theme.AppTypography

private val BloomColorScheme = lightColorScheme(
    // Brand
    primary = Primary,
    onPrimary = Background,

    secondary = Secondary,
    onSecondary = Background,

    // Background
    background = Background,
    onBackground = TextPrimary,

    // Surface
    surface = Surface,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,

    // Other
    outline = Outline,
    error = Error,
    onError = Background
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