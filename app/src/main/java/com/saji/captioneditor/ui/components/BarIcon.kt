package com.saji.captioneditor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.saji.captioneditor.ui.theme.SajiColors

/** Round 40dp icon button used in top bars and toolbars. */
@Composable
fun BarIcon(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = SajiColors.TextPrimary,
    active: Boolean = false
) {
    Box(
        modifier = modifier
            .pressScale(onClick = onClick)
            .size(40.dp)
            .clip(CircleShape)
            .background(if (active) SajiColors.Purple else SajiColors.Card),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = description, tint = tint, modifier = Modifier.size(22.dp))
    }
}
