
package com.example.bloom.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.bloom.R

val Nunito = FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_medium, FontWeight.Medium),
    Font(R.font.nunito_semibold, FontWeight.SemiBold),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_extrabold, FontWeight.ExtraBold)
)

private val defaultTypography = Typography()

val AppTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = Nunito),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = Nunito),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = Nunito),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = Nunito),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = Nunito),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = Nunito),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = Nunito),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = Nunito),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = Nunito),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = Nunito),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = Nunito),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = Nunito),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = Nunito),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = Nunito),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = Nunito)
)