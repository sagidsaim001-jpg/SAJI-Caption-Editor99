package com.saji.captioneditor.ui.screens.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.saji.captioneditor.model.MediaSample
import com.saji.captioneditor.ui.AppViewModel
import com.saji.captioneditor.ui.components.EmptyState
import com.saji.captioneditor.ui.components.PremiumButton
import com.saji.captioneditor.ui.components.PremiumTopBar
import com.saji.captioneditor.ui.components.SelectionChip
import com.saji.captioneditor.ui.components.pressScale
import com.saji.captioneditor.ui.components.sajiTextFieldColors
import com.saji.captioneditor.ui.theme.SajiColors

/** Step after the gallery: review selected media, name the project, pick a ratio, create it. */
@Composable
fun NewProjectScreen(vm: AppViewModel, onBack: () -> Unit, onCreated: (Int) -> Unit) {
    val media = vm.selectedMedia.mapNotNull { MediaSample.byId(it) }
    var name by remember { mutableStateOf("My Caption Video") }
    var ratio by remember { mutableStateOf("9:16") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SajiColors.ScreenGradient)
            .imePadding()
    ) {
        PremiumTopBar(title = "New Project", onBack = onBack)

        if (media.isEmpty() && vm.importedMedia.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.PhotoLibrary,
                title = "No media selected",
                subtitle = "Go back and pick at least one video or photo.",
                modifier = Modifier.weight(1f),
                actionText = "Back to Gallery",
                onAction = onBack
            )
        } else {
            // Keep duration arithmetic explicitly Long to resolve sumOf overload selection.
            val total = media.sumOf { if (it.isVideo) it.durationSec.toLong() else 5L } +
                vm.importedMedia.sumOf { if (it.isVideo) 30L else 5L }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val first = media.firstOrNull()
                Box(
                    modifier = Modifier
                        .height(260.dp)
                        .aspectRatio(0.56f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.verticalGradient(listOf(first?.c1 ?: SajiColors.Purple, first?.c2 ?: SajiColors.Bg))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.size(60.dp))
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    "${media.size} item(s) - total %02d:%02d".format(total / 60, total % 60),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SajiColors.TextSecondary
                )

                if (vm.importedMedia.isNotEmpty()) {
                    Spacer(Modifier.height(14.dp))
                    Text(
                        "Device imports: ${vm.importedMedia.size}",
                        style = MaterialTheme.typography.titleMedium,
                        color = SajiColors.Pink,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "Photos and videos are attached to this project using their device media URIs.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SajiColors.TextSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(18.dp))
                Text(
                    "Selected media",
                    style = MaterialTheme.typography.titleMedium,
                    color = SajiColors.TextPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(media, key = { it.id }) { m ->
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.linearGradient(listOf(m.c1, m.c2)))
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(3.dp)
                                    .pressScale(onClick = { vm.selectedMedia.remove(m.id) })
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.65f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Project name") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = sajiTextFieldColors()
                )

                Spacer(Modifier.height(18.dp))
                Text(
                    "Aspect ratio",
                    style = MaterialTheme.typography.titleMedium,
                    color = SajiColors.TextPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("9:16", "16:9", "1:1", "4:5").forEach { r ->
                        SelectionChip(r, selected = ratio == r, onClick = { ratio = r })
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SajiColors.Surface)
                    .navigationBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PremiumButton("Cancel", onClick = onBack, filled = false, modifier = Modifier.weight(1f))
                PremiumButton(
                    "Create Project",
                    onClick = { onCreated(vm.createProject(name).id) },
                    modifier = Modifier.weight(1.4f)
                )
            }
        }
    }
}