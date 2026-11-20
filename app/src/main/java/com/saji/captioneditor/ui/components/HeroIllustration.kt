package com.saji.captioneditor.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Vector-drawn hero: glowing clapperboard, neon ribbon and the script tagline. */
@Composable
fun HeroIllustration(modifier: Modifier = Modifier) {
    Box(modifier) {
        Canvas(Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            drawRect(
                Brush.radialGradient(
                    listOf(Color(0x668B2BE2), Color.Transparent),
                    center = Offset(w * 0.3f, h * 0.55f),
                    radius = w * 0.65f
                )
            )
            drawRect(
                Brush.radialGradient(
                    listOf(Color(0x44E879F9), Color.Transparent),
                    center = Offset(w * 0.88f, h * 0.25f),
                    radius = w * 0.5f
                )
            )

            val ribbon = Path().apply {
                moveTo(-10f, h * 0.88f)
                cubicTo(w * 0.18f, h * 1.02f, w * 0.48f, h * 0.92f, w * 0.60f, h * 0.62f)
                cubicTo(w * 0.66f, h * 0.45f, w * 0.42f, h * 0.50f, w * 0.35f, h * 0.66f)
            }
            drawPath(
                ribbon,
                Brush.linearGradient(
                    listOf(Color(0xFF5B21B6), Color(0xFFE879F9), Color(0xFFA855F7)),
                    start = Offset(0f, h),
                    end = Offset(w * 0.7f, h * 0.4f)
                ),
                style = Stroke(width = h * 0.12f, cap = StrokeCap.Round)
            )

            val bw = w * 0.36f
            val bh = h * 0.56f
            val left = w * 0.12f
            val top = h * 0.30f
            val bar = bh * 0.2f
            rotate(-10f, pivot = Offset(left + bw / 2, top + bh / 2)) {
                drawRoundRect(Color(0xFF1A1330), Offset(left, top + bar), Size(bw, bh - bar), CornerRadius(18f))
                drawRoundRect(
                    Color(0xFF8B5CF6), Offset(left, top + bar), Size(bw, bh - bar),
                    CornerRadius(18f), style = Stroke(3f)
                )
                drawRoundRect(Color(0xFF241A45), Offset(left, top), Size(bw, bar), CornerRadius(12f))
                val stripeW = bw / 6f
                val skew = 14f
                for (i in 0 until 6 step 2) {
                    val x = left + i * stripeW
                    val stripe = Path().apply {
                        moveTo(x + skew, top)
                        lineTo(x + stripeW + skew, top)
                        lineTo(x + stripeW, top + bar)
                        lineTo(x, top + bar)
                        close()
                    }
                    drawPath(stripe, Color.White.copy(alpha = 0.92f))
                }
                val cx = left + bw / 2
                val cy = top + bar + (bh - bar) / 2
                val s = bh * 0.2f
                val play = Path().apply {
                    moveTo(cx - s * 0.6f, cy - s)
                    lineTo(cx + s, cy)
                    lineTo(cx - s * 0.6f, cy + s)
                    close()
                }
                drawPath(
                    play,
                    Brush.linearGradient(
                        listOf(Color(0xFFE879F9), Color(0xFF8B5CF6)),
                        start = Offset(cx - s, cy - s),
                        end = Offset(cx + s, cy + s)
                    )
                )
            }
        }
        Text(
            text = "Create\nStunning\nVideos",
            color = Color(0xFFD9B8FF),
            fontFamily = FontFamily.Cursive,
            fontStyle = FontStyle.Italic,
            fontSize = 32.sp,
            lineHeight = 34.sp,
            textAlign = TextAlign.End,
            style = androidx.compose.ui.text.TextStyle(
                shadow = Shadow(color = Color(0xFFA855F7), blurRadius = 18f)
            ),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 22.dp)
        )
    }
}
