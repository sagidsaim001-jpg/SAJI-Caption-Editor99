package com.saji.captioneditor.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.saji.captioneditor.ui.theme.SajiColors

/** Rounded selectable chip used for category tabs, filters and option groups. */
@Composable
fun SelectionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(50)
    val bg by animateColorAsState(
        if (selected) SajiColors.Purple else SajiColors.Card, label = "chipBg"
    )
    val border by animateColorAsState(
        if (selected) SajiColors.Pink.copy(alpha = 0.8f) else SajiColors.BorderFaint, label = "chipBorder"
    )
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = SajiColors.TextPrimary,
        modifier = modifier
            .clip(shape)
            .background(bg, shape)
            .border(BorderStroke(1.dp, border), shape)
            .pressScale(pressedScale = 0.94f, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
