package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val FitPulseDarkColorScheme = darkColorScheme(
    primary = EmeraldBright,
    onPrimary = Color(0xFF00291A),
    primaryContainer = EmeraldContainerDark,
    onPrimaryContainer = Color(0xFFA7F9D7),
    secondary = Color(0xFF7DD3FC),
    onSecondary = Color(0xFF002636),
    secondaryContainer = SlateContainerDark,
    onSecondaryContainer = Color(0xFFD4EAF3),
    tertiary = CoralAccent,
    onTertiary = Color.White,
    tertiaryContainer = CoralContainerDark,
    onTertiaryContainer = Color(0xFFFFDAD0),
    background = ObsidianBgDark,
    onBackground = TextInkDark,
    surface = ObsidianSurfaceDark,
    onSurface = TextInkDark,
    surfaceVariant = ObsidianSurfaceVariantDark,
    onSurfaceVariant = TextMutedDark,
    outline = Color(0xFF33474D),
    outlineVariant = Color(0xFF223034)
)

private val FitPulseLightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EmeraldContainerLight,
    onPrimaryContainer = Color(0xFF003824),
    secondary = SlateSecondary,
    onSecondary = Color.White,
    secondaryContainer = SlateContainerLight,
    onSecondaryContainer = TextInkLight,
    tertiary = CoralAccent,
    onTertiary = Color.White,
    tertiaryContainer = CoralContainerLight,
    onTertiaryContainer = Color(0xFF4D1A08),
    background = AlabasterBgLight,
    onBackground = TextInkLight,
    surface = CrispSurfaceLight,
    onSurface = TextInkLight,
    surfaceVariant = MistSurfaceVariantLight,
    onSurfaceVariant = TextMutedLight,
    outline = Color(0xFFCBD8D5),
    outlineVariant = Color(0xFFE1EAE7)
)

val FitPulseShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun FitPulseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) FitPulseDarkColorScheme else FitPulseLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FitPulseTypography,
        shapes = FitPulseShapes,
        content = content
    )
}
