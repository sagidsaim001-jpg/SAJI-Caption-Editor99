package com.saji.captioneditor.ui.screens.features

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.saji.captioneditor.ui.AppViewModel
import com.saji.captioneditor.ui.components.PremiumButton
import com.saji.captioneditor.ui.components.PremiumTopBar
import com.saji.captioneditor.ui.components.SelectionChip
import com.saji.captioneditor.ui.theme.SajiColors

@Composable
fun FeatureScreen(title: String, vm: AppViewModel, onBack: () -> Unit) {
    var selected by remember(title) { mutableStateOf("Default") }
    var textValue by remember(title) { mutableStateOf("") }
    val options = when (title) {
        "Auto Captions" -> listOf("English", "Urdu", "Hindi", "Arabic", "Auto Detect")
        "Text Styles", "Caption Style" -> listOf("Bold", "Minimal", "Neon", "Classic", "Karaoke", "Gradient")
        "Fonts" -> listOf("Default", "Serif", "Sans", "Rounded", "Mono")
        "Animation" -> listOf("Fade", "Pop", "Slide", "Typewriter", "Bounce")
        "Shadow" -> listOf("Soft", "Hard", "Glow", "Outline", "None")
        "Effects" -> listOf("VHS", "Blur", "Glow", "Vintage", "Film", "Grain")
        "Music" -> listOf("My Audio", "Soundtracks", "Ambient", "Cinematic", "Beats")
        "Aspect Ratio" -> listOf("9:16", "16:9", "1:1", "4:5", "Custom")
        "Templates" -> listOf("Reels", "Lyrics", "Vlog", "Quotes", "Minimal")
        "Export" -> listOf("720p", "1080p", "2K", "4K")
        "Language" -> listOf("English", "Urdu", "Hindi", "Arabic")
        else -> listOf("Default", "Standard", "Custom")
    }
    Column(Modifier.fillMaxSize().background(SajiColors.ScreenGradient)) {
        PremiumTopBar(title = title, onBack = onBack)
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                when (title) {
                    "Auto Captions" -> "Speech-to-text setup"
                    "Export" -> "Choose output quality"
                    "Music" -> "Choose an audio source"
                    else -> "Customize $title"
                },
                style = MaterialTheme.typography.titleLarge,
                color = SajiColors.TextPrimary
            )
            Text(
                when (title) {
                    "Auto Captions" -> "Caption generation engine is not connected yet. Choose your preferred language for the next implementation phase."
                    "Export" -> "Export settings are ready to configure. Actual video rendering will be connected after the editor timeline and media pipeline."
                    else -> "Select an option to preview your choice. Changes are currently stored for this screen session."
                },
                color = SajiColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
            if (title == "Auto Captions") {
                OutlinedTextField(
                    value = textValue, onValueChange = { textValue = it },
                    modifier = Modifier.fillMaxWidth(), label = { Text("Caption text (manual preview)") },
                    minLines = 3
                )
            }
            Text("Options", color = SajiColors.TextPrimary, style = MaterialTheme.typography.titleMedium)
            options.chunked(2).forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { option ->
                        SelectionChip(option, selected == option, { selected = option }, Modifier.weight(1f))
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
            if (title == "Export") {
                Text("Frame rate", color = SajiColors.TextPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("24 fps", "30 fps", "60 fps").forEach { SelectionChip(it, false, { selected = it }) }
                }
            }
            if (title == "Music") {
                OutlinedTextField(value = textValue, onValueChange = { textValue = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Search audio") })
            }
            Spacer(Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(SajiColors.Card).padding(16.dp)) {
                Column {
                    Text("Selected", color = SajiColors.TextSecondary)
                    Text(selected, color = SajiColors.Pink, style = MaterialTheme.typography.titleLarge)
                }
            }
            PremiumButton(
                when (title) {
                    "Export" -> "Save export settings"
                    "Auto Captions" -> "Apply language"
                    else -> "Apply selection"
                },
                onClick = { vm.showToast("$title: $selected selected") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
