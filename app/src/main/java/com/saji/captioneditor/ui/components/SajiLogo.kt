package com.saji.captioneditor.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saji.captioneditor.ui.theme.SajiColors

/** The "Cc" app logo with a small gold crown. */
@Composable
fun SajiLogo(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(18.dp)
    Box(modifier = modifier.size(68.dp)) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(60.dp)
                .clip(shape)
                .background(
                    Brush.linearGradient(listOf(Color(0xFF3B1D8F), Color(0xFF7C3AED), Color(0xFF2A1070))),
                    shape
                )
                .border(BorderStroke(2.dp, SajiColors.BorderGradient), shape),
            contentAlignment = Alignment.Center
        ) {
            Text("Cc", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        }
        Icon(
            Icons.Filled.WorkspacePremium,
            contentDescription = null,
            tint = SajiColors.Gold,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 2.dp, y = (-2).dp)
                .size(24.dp)
                .rotate(14f)
        )
    }
}
