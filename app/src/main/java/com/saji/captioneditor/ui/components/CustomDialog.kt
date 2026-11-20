package com.saji.captioneditor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/** Premium confirmation dialog. [destructive] colours the confirm button red. */
@Composable
fun CustomDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String = "Cancel",
    destructive: Boolean = false
) {
    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(title, style = MaterialTheme.typography.titleLarge, color = com.saji.captioneditor.ui.theme.SajiColors.TextPrimary, textAlign = TextAlign.Center)
                Spacer(Modifier.height(10.dp))
                Text(message, style = MaterialTheme.typography.bodyMedium, color = com.saji.captioneditor.ui.theme.SajiColors.TextSecondary, textAlign = TextAlign.Center)
                Spacer(Modifier.height(22.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PremiumButton(
                        text = dismissText,
                        onClick = onDismiss,
                        filled = false,
                        modifier = Modifier.weight(1f)
                    )
                    PremiumButton(
                        text = confirmText,
                        onClick = onConfirm,
                        danger = destructive,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
