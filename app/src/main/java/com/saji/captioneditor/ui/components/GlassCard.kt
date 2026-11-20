package com.saji.captioneditor.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.saji.captioneditor.ui.theme.SajiColors

/** Dark glassmorphism card with a faint violet border. Gets a glowing border when [selected]. */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val borderBrush: Brush =
        if (selected) SajiColors.BorderGradient
        else Brush.linearGradient(listOf(SajiColors.BorderFaint, SajiColors.BorderFaint))
    val borderWidth = if (selected) 2.dp else 1.dp

    val base = modifier
        .clip(shape)
        .background(SajiColors.CardGradient, shape)
        .border(BorderStroke(borderWidth, borderBrush), shape)

    val finalModifier = if (onClick != null) base.pressScale(onClick = onClick) else base
    Box(modifier = finalModifier, content = content)
}

/** Large gradient card (used for the Add Project hero button and premium banners). */
@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp),
    brush: Brush = SajiColors.PrimaryGradient,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val base = modifier
        .clip(shape)
        .background(brush, shape)
        .border(BorderStroke(1.5.dp, Brush.linearGradient(listOf(SajiColors.TextPrimary.copy(alpha = 0.55f), SajiColors.Violet.copy(alpha = 0.2f)))), shape)
    val finalModifier = if (onClick != null) base.pressScale(pressedScale = 0.97f, onClick = onClick) else base
    Box(modifier = finalModifier, content = content)
}
