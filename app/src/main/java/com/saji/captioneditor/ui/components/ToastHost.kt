package com.saji.captioneditor.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.saji.captioneditor.ui.theme.SajiColors
import kotlinx.coroutines.delay

/** Small pill that slides up to give feedback (Duplicate, Delete, Apply...). Auto-dismisses. */
@Composable
fun ToastHost(
    message: String?,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    var last by remember { mutableStateOf("") }
    LaunchedEffect(message) {
        if (message != null) {
            last = message
            delay(1800)
            onDone()
        }
    }
    AnimatedVisibility(
        visible = message != null,
        modifier = modifier,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        val shape = RoundedCornerShape(50)
        Text(
            text = last,
            style = MaterialTheme.typography.labelLarge,
            color = SajiColors.TextPrimary,
            modifier = Modifier
                .clip(shape)
                .background(SajiColors.CardLight, shape)
                .border(BorderStroke(1.dp, SajiColors.Violet), shape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        )
    }
}
