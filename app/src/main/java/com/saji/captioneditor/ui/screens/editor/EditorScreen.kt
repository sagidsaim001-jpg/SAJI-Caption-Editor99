package com.saji.captioneditor.ui.screens.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saji.captioneditor.model.Project
import com.saji.captioneditor.ui.AppViewModel
import com.saji.captioneditor.ui.components.PremiumButton
import com.saji.captioneditor.ui.components.PremiumTopBar
import com.saji.captioneditor.ui.components.SelectionChip
import com.saji.captioneditor.ui.theme.SajiColors

@Composable
fun EditorScreen(
    vm: AppViewModel,
    projectId: Int,
    onBack: () -> Unit,
    onOpenTool: (String) -> Unit
) {
    val project = vm.projects.firstOrNull { it.id == projectId }
    var caption by remember(projectId) { mutableStateOf("Your story begins here...") }
    var selectedTab by remember { mutableStateOf("Text") }
    var selectedStyle by remember { mutableStateOf("Bold Glow") }
    var fontSize by remember { mutableFloatStateOf(30f) }
    var position by remember { mutableFloatStateOf(65f) }
    var playing by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().background(SajiColors.Bg)) {
        PremiumTopBar(
            title = project?.name ?: "Caption Editor",
            onBack = onBack,
            actions = {
                TextButton(onClick = { vm.showToast("Project saved in this session") }) {
                    Icon(Icons.Filled.Save, null, tint = SajiColors.Pink)
                    Spacer(Modifier.width(4.dp))
                    Text("Save", color = SajiColors.Pink)
                }
            }
        )
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Preview canvas
            Box(
                Modifier.fillMaxWidth().padding(top = 6.dp).heightIn(min = 280.dp, max = 390.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(project?.skyTop ?: Color(0xFF6D28D9), project?.skyBottom ?: Color(0xFF100A28))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(Modifier.fillMaxSize().padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("PREVIEW", color = Color.White.copy(.75f), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        Icon(Icons.Filled.Fullscreen, null, tint = Color.White.copy(.8f))
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        caption.ifBlank { "Tap to add your caption" },
                        color = Color.White,
                        fontSize = (fontSize / 2).sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        lineHeight = (fontSize / 1.8f).sp,
                        modifier = Modifier.fillMaxWidth()
                            .background(if (selectedStyle == "Box") Color.Black.copy(.55f) else Color.Transparent, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Text("00:00 / ${project?.durationLabel ?: "00:15"}", color = Color.White.copy(.8f), fontSize = 11.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            // Playback / timeline controls
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(SajiColors.Card).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { playing = !playing }) {
                    Icon(if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow, null, tint = SajiColors.Pink)
                }
                Column(Modifier.weight(1f)) {
                    Text("Timeline", color = SajiColors.TextPrimary, fontWeight = FontWeight.SemiBold)
                    Text("00:00     ━━━━━━━━━━━━━     ${project?.durationLabel ?: "00:15"}", color = SajiColors.TextSecondary, fontSize = 11.sp)
                }
                IconButton(onClick = { vm.showToast("More timeline controls coming next") }) {
                    Icon(Icons.Filled.Tune, null, tint = SajiColors.TextSecondary)
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Text", "Style", "Font", "Animation", "Shadow", "Timing", "Music", "Canvas").forEach { tab ->
                    SelectionChip(tab, selectedTab == tab, { selectedTab = tab })
                }
            }
            Spacer(Modifier.height(14.dp))
            when (selectedTab) {
                "Text" -> {
                    Text("Caption text", color = SajiColors.TextPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = caption, onValueChange = { caption = it },
                        modifier = Modifier.fillMaxWidth(), minLines = 2, maxLines = 4,
                        placeholder = { Text("Type your caption...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = SajiColors.Pink, unfocusedBorderColor = SajiColors.BorderFaint,
                            focusedLabelColor = SajiColors.Pink, cursorColor = SajiColors.Pink
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PremiumButton("Add Caption", { if (caption.isBlank()) vm.showToast("Type a caption first") else vm.showToast("Caption added to preview") }, Modifier.weight(1f))
                        PremiumButton("Clear", { caption = "" }, Modifier.weight(1f), filled = false)
                    }
                }
                "Style" -> {
                    Text("Caption styles", color = SajiColors.TextPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    listOf("Bold Glow", "Classic", "Box", "Minimal", "Neon", "Karaoke").chunked(3).forEach { row ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { s -> SelectionChip(s, selectedStyle == s, { selectedStyle = s }, Modifier.weight(1f)) }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
                "Font" -> {
                    Text("Font size", color = SajiColors.TextPrimary, fontWeight = FontWeight.Bold)
                    Slider(value = fontSize, onValueChange = { fontSize = it }, valueRange = 22f..56f, colors = SliderDefaults.colors(thumbColor = SajiColors.Pink, activeTrackColor = SajiColors.Violet))
                    Text("${fontSize.toInt()} sp", color = SajiColors.TextSecondary)
                    Spacer(Modifier.height(8.dp))
                    Text("Font family", color = SajiColors.TextPrimary, fontWeight = FontWeight.Bold)
                    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Default", "Serif", "Mono", "Rounded").forEach { SelectionChip(it, false, { vm.showToast("$it font selected for preview") }) }
                    }
                }
                "Animation" -> ToolOptions(listOf("Fade", "Pop", "Slide Up", "Typewriter", "Bounce"), vm)
                "Shadow" -> ToolOptions(listOf("None", "Soft", "Hard", "Glow", "Outline"), vm)
                "Timing" -> {
                    Text("Caption vertical position", color = SajiColors.TextPrimary, fontWeight = FontWeight.Bold)
                    Slider(value = position, onValueChange = { position = it }, valueRange = 10f..90f)
                    Text("${position.toInt()}%", color = SajiColors.TextSecondary)
                    PremiumButton("Set caption timing", { vm.showToast("Timing controls selected") }, Modifier.fillMaxWidth())
                }
                "Music" -> {
                    Text("Audio tools", color = SajiColors.TextPrimary, fontWeight = FontWeight.Bold)
                    PremiumButton("Open Music Library", { onOpenTool("music") }, Modifier.fillMaxWidth())
                    PremiumButton("Audio Settings", { onOpenTool("audio_settings") }, Modifier.fillMaxWidth(), filled = false)
                }
                else -> {
                    Text("Canvas & layout", color = SajiColors.TextPrimary, fontWeight = FontWeight.Bold)
                    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("9:16", "16:9", "1:1", "4:5").forEach { SelectionChip(it, false, { vm.showToast("Canvas ratio: $it") }) }
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PremiumButton("Auto Captions", { onOpenTool("auto_captions") }, Modifier.weight(1f), filled = false)
                PremiumButton("Export", { onOpenTool("export") }, Modifier.weight(1f))
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ToolOptions(options: List<String>, vm: AppViewModel) {
    Text("Choose an effect", color = SajiColors.TextPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(8.dp))
    options.chunked(3).forEach { row ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            row.forEach { option -> SelectionChip(option, false, { vm.showToast("$option selected") }, Modifier.weight(1f)) }
        }
        Spacer(Modifier.height(8.dp))
    }
}
