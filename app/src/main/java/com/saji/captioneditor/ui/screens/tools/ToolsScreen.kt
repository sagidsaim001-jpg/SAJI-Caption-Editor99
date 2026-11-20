package com.saji.captioneditor.ui.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.saji.captioneditor.navigation.Routes
import com.saji.captioneditor.ui.components.ToolCard
import com.saji.captioneditor.ui.theme.SajiColors

private data class ToolEntry(val icon: ImageVector, val label: String, val route: String)

private val toolSections: List<Pair<String, List<ToolEntry>>> = listOf(
    "Captions & Text" to listOf(
        ToolEntry(Icons.Filled.ClosedCaption, "Auto Captions", Routes.AutoCaptions),
        ToolEntry(Icons.Filled.TextFields, "Text Styles", Routes.TextStyles),
        ToolEntry(Icons.Filled.FontDownload, "Fonts", Routes.Fonts),
        ToolEntry(Icons.Filled.Animation, "Animation", Routes.TextAnimation),
        ToolEntry(Icons.Filled.Layers, "Shadow", Routes.TextShadow),
        ToolEntry(Icons.Filled.TextFields, "Text Editor", Routes.TextEditor)
    ),
    "Creative" to listOf(
        ToolEntry(Icons.Filled.VideoLibrary, "Templates", Routes.Templates),
        ToolEntry(Icons.Filled.AutoAwesome, "Effects", Routes.Effects),
        ToolEntry(Icons.Filled.LibraryMusic, "Music", Routes.Music)
    ),
    "Canvas" to listOf(
        ToolEntry(Icons.Filled.Crop, "Aspect Ratio", Routes.AspectRatio)
    )
)

@Composable
fun ToolsScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SajiColors.ScreenGradient)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 130.dp)
    ) {
        Text("Tools", style = MaterialTheme.typography.headlineMedium, color = SajiColors.TextPrimary)
        Text(
            "Everything you need to style your videos",
            style = MaterialTheme.typography.bodyMedium,
            color = SajiColors.TextSecondary
        )
        toolSections.forEach { (title, entries) ->
            Spacer(Modifier.height(22.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, color = SajiColors.TextPrimary)
            Spacer(Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                entries.chunked(3).forEach { rowEntries ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        rowEntries.forEach { entry ->
                            ToolCard(
                                icon = entry.icon,
                                label = entry.label,
                                onClick = { onNavigate(entry.route) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(92.dp)
                            )
                        }
                        repeat(3 - rowEntries.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
        }
    }
}
