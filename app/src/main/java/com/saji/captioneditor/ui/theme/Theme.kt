package com.saji.captioneditor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SajiDarkScheme = darkColorScheme(
    primary = SajiColors.Violet,
    onPrimary = SajiColors.TextPrimary,
    secondary = SajiColors.Pink,
    background = SajiColors.Bg,
    onBackground = SajiColors.TextPrimary,
    surface = SajiColors.Surface,
    onSurface = SajiColors.TextPrimary,
    surfaceVariant = SajiColors.Card,
    onSurfaceVariant = SajiColors.TextSecondary,
    error = SajiColors.Danger
)

@Composable
fun SajiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SajiDarkScheme,
        typography = SajiTypography,
        content = content
    )
}
