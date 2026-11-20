package com.saji.captioneditor.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object SajiColors {
    val Bg = Color(0xFF07050F)
    val Surface = Color(0xFF110C24)
    val Card = Color(0xFF181033)
    val CardLight = Color(0xFF221848)
    val Violet = Color(0xFF8B5CF6)
    val VioletDeep = Color(0xFF5B21B6)
    val Purple = Color(0xFF7C3AED)
    val Pink = Color(0xFFE879F9)
    val Border = Color(0xFF6D4FD6)
    val BorderFaint = Color(0x33A78BFA)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFB9AEDD)
    val Gold = Color(0xFFFFC53D)
    val Danger = Color(0xFFFF5C7A)
    val Blue = Color(0xFF2F7BFF)
    val Green = Color(0xFF34D399)

    val PrimaryGradient: Brush = Brush.horizontalGradient(
        listOf(Color(0xFF6D28D9), Color(0xFFA855F7), Color(0xFFE879F9))
    )
    val ExportGradient: Brush = Brush.horizontalGradient(
        listOf(Color(0xFF5B3DF5), Color(0xFF8B5CF6))
    )
    val CardGradient: Brush = Brush.verticalGradient(
        listOf(Color(0xFF1D1440), Color(0xFF120C28))
    )
    val BorderGradient: Brush = Brush.linearGradient(
        listOf(Color(0xFFA78BFA), Color(0xFF6D28D9), Color(0xFFE879F9))
    )
    val ScreenGradient: Brush = Brush.verticalGradient(
        listOf(Color(0xFF140A33), Color(0xFF07050F), Color(0xFF07050F))
    )
    val GoldGradient: Brush = Brush.verticalGradient(
        listOf(Color(0xFFFFD86B), Color(0xFFE09A12))
    )
}
