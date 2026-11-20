package com.saji.captioneditor.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.saji.captioneditor.ui.theme.SajiColors

/**
 * Premium pill button. [filled] = gradient background, otherwise a violet outlined button.
 * [danger] switches to a red palette (used for destructive confirmations).
 */
@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    filled: Boolean = true,
    danger: Boolean = false
) {
    val shape = RoundedCornerShape(50)
    val bg: Brush = when {
        danger && filled -> Brush.horizontalGradient(listOf(Color(0xFFE11D48), SajiColors.Danger))
        filled -> SajiColors.PrimaryGradient
        else -> Brush.horizontalGradient(listOf(SajiColors.Card, SajiColors.Card))
    }
    val borderColor = if (danger) SajiColors.Danger else SajiColors.Violet

    Row(
        modifier = modifier
            .heightIn(min = 48.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(shape)
            .background(bg, shape)
            .then(if (!filled) Modifier.border(BorderStroke(1.5.dp, borderColor), shape) else Modifier)
            .pressScale(enabled = enabled, onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = SajiColors.TextPrimary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (!filled && danger) SajiColors.Danger else SajiColors.TextPrimary
        )
    }
}
