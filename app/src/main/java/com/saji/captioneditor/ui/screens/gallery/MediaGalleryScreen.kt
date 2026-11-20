package com.saji.captioneditor.ui.screens.gallery

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightimport androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.saji.captioneditor.model.MediaItem
import com.saji.captioneditor.model.MediaSample
import com.saji.captioneditor.ui.AppViewModel
import com.saji.captioneditor.ui.components.BarIcon
import com.saji.captioneditor.ui.components.EmptyState
import com.saji.captioneditor.ui.components.GlassCard
import com.saji.captioneditor.ui.components.PremiumButton
import com.saji.captioneditor.ui.components.PremiumTopBar
import com.saji.captioneditor.ui.components.SajiBottomSheet
import com.saji.captioneditor.ui.components.SearchField
import com.saji.captioneditor.ui.components.SelectionChip
import com.saji.captioneditor.ui.components.SettingsItem
import com.saji.captioneditor.ui.theme.SajiColors

private val GalleryTabs = listOf("All", "Videos", "Photos", "Albums")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaGalleryScreen(vm: AppViewModel, onBack: () -> Unit, onNext: () -> Unit) {
    var tab by remember { mutableIntStateOf(0) }
    var openAlbum by remember { mutableStateOf<String?>(null) }
    var searching by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var moreOpen by remember { mutableStateOf(false) }
    var newestFirst by remember { mutableStateOf(true) }
    var preview by remember { mutableStateOf<MediaItem?>(null) }

    val filtered = remember(tab, openAlbum, query, newestFirst) {
        MediaSample.items
            .filter { openAlbum == null || it.album == openAlbum }
            .filter {
                when (tab) {
                    1 -> it.isVideo
                    2 -> !it.isVideo
                    else -> true
                }
            }
            .filter { it.name.contains(query, ignoreCase = true) }
            .let { if (newestFirst) it else it.reversed() }
    }
    val selection = vm.selectedMedia

    fun toggle(id: Int) {
        if (id in selection) selection.remove(id) else selection.add(id)
    }

    fun leave() {
        selection.clear()
        onBack()
    }

    BackHandler(enabled = openAlbum != null) { openAlbum = null }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SajiColors.ScreenGradient)
    ) {
        Column(Modifier.fillMaxSize()) {
            PremiumTopBar(
                title = openAlbum ?: "Media Gallery",
                onBack = { if (openAlbum != null) openAlbum = null else leave() },
                actions = {
                    BarIcon(Icons.Filled.Search, "Search", onClick = {
                        searching = !searching
                        if (!searching) query = ""
                    }, active = searching)
                    BarIcon(Icons.Filled.MoreVert, "More options", onClick = { moreOpen = true })
                }
            )
            AnimatedVisibility(searching) {
                SearchField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = "Search media",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GalleryTabs.forEachIndexed { i, label ->
                    SelectionChip(label, selected = tab == i, onClick = { tab = i; openAlbum = null })
                }
            }
            AnimatedVisibility(selection.isNotEmpty()) {
                Text(
                    "${selection.size} selected",
                    style = MaterialTheme.typography.labelLarge,
                    color = SajiColors.Pink,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            Box(Modifier.weight(1f)) {
                AnimatedContent(
                    targetState = if (tab == 3 && openAlbum == null) "albums" else "grid",
                    transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
                    label = "galleryContent"
                ) { state ->
                    if (state == "albums") {
                        AlbumsGrid(onOpen = { openAlbum = it })
                    } else if (filtered.isEmpty()) {
                        EmptyState(
                            icon = Icons.Filled.Image,
                            title = "No media found",
                            subtitle = "Try a different search or tab.",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        MediaGrid(
                            media = filtered,
                            showRecent = tab == 0 && openAlbum == null && query.isBlank(),
                            selection = selection,
                            onToggle = { toggle(it) },
                            onPreview = { preview = it }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = selection.isNotEmpty(),
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SajiColors.Surface)
                    .navigationBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PremiumButton("Cancel", onClick = { selection.clear() }, filled = false, modifier = Modifier.weight(1f))
                PremiumButton("Next (${selection.size})", onClick = onNext, modifier = Modifier.weight(1.4f))
            }
        }
    }

    if (moreOpen) {
        SajiBottomSheet(onDismiss = { moreOpen = false }) {
            Text("Gallery options", style = MaterialTheme.typography.titleMedium, color = SajiColors.TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
            SettingsItem(Icons.Filled.SelectAll, "Select all shown", onClick = {
                filtered.forEach { if (it.id !in selection) selection.add(it.id) }
                moreOpen = false
            })
            SettingsItem(Icons.Filled.ClearAll, "Clear selection", onClick = {
                selection.clear()
                moreOpen = false
            })
            SettingsItem(
                Icons.Filled.SwapVert,
                if (newestFirst) "Sort: Newest first" else "Sort: Oldest first",
                subtitle = "Tap to switch",
                onClick = { newestFirst = !newestFirst; moreOpen = false }
            )
            Spacer(Modifier.height(12.dp))
        }
    }

    preview?.let { item ->
        MediaPreviewDialog(
            item = item,
            selected = item.id in selection,
            onToggle = { toggle(item.id) },
            onDismiss = { preview = null }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaGrid(
    media: List<MediaItem>,
    showRecent: Boolean,
    selection: List<Int>,
    onToggle: (Int) -> Unit,
    onPreview: (MediaItem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 110.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (showRecent) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(Modifier.padding(bottom = 8.dp)) {
                    Text("Recent", style = MaterialTheme.typography.titleMedium, color = SajiColors.TextPrimary, modifier = Modifier.padding(vertical = 6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(media.take(8), key = { it.id }) { m ->
                            MediaThumb(
                                item = m,
                                order = selection.indexOf(m.id) + 1,
                                onToggle = { onToggle(m.id) },
                                onPreview = { onPreview(m) },
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }
                    Text("All media", style = MaterialTheme.typography.titleMedium, color = SajiColors.TextPrimary, modifier = Modifier.padding(top = 14.dp))
                }
            }
        }
        items(media, key = { it.id }) { m ->
            MediaThumb(
                item = m,
                order = selection.indexOf(m.id) + 1,
                onToggle = { onToggle(m.id) },
                onPreview = { onPreview(m) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaThumb(
    item: MediaItem,
    order: Int,
    onToggle: () -> Unit,
    onPreview: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = order > 0
    val scale by animateFloatAsState(if (selected) 0.92f else 1f, label = "thumbScale")
    val borderColor by animateColorAsState(if (selected) SajiColors.Pink else Color.Transparent, label = "thumbBorder")
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .clip(shape)
            .background(Brush.linearGradient(listOf(item.c1, item.c2)))
            .border(3.dp, borderColor, shape)
            .combinedClickable(onClick = onToggle, onLongClick = onPreview)
    ) {
        Icon(
            if (item.isVideo) Icons.Filled.PlayArrow else Icons.Filled.Image,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.75f),
            modifier = Modifier
                .align(Alignment.Center)
                .size(28.dp)
        )
        if (selected) {
            Box(
                Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.25f))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(SajiColors.Purple),
                contentAlignment = Alignment.Center
            ) {
                Text("$order", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(22.dp)
                    .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape)
            )
        }
        if (item.isVideo) {
            Text(
                item.durationLabel,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 5.dp, vertical = 1.dp)
            )
        }
    }
}

@Composable
private fun AlbumsGrid(onOpen: (String) -> Unit) {
    val albums = remember { MediaSample.items.groupBy { it.album }.toList() }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 110.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(albums, key = { it.first }) { (name, list) ->
            GlassCard(Modifier.fillMaxWidth(), onClick = { onOpen(name) }) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.2f)
                            .background(Brush.linearGradient(listOf(list[0].c1, list[0].c2))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Folder, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(40.dp))
                    }
                    Column(Modifier.padding(10.dp)) {
                        Text(name, style = MaterialTheme.typography.labelLarge, color = SajiColors.TextPrimary)
                        Text("${list.size} items", style = MaterialTheme.typography.bodySmall, color = SajiColors.TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaPreviewDialog(
    item: MediaItem,
    selected: Boolean,
    onToggle: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.94f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    BarIcon(Icons.Filled.Close, "Close preview", onClick = onDismiss)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 12.dp)
                        .aspectRatio(0.56f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.verticalGradient(listOf(item.c1, item.c2))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (item.isVideo) Icons.Filled.PlayArrow else Icons.Filled.Image,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(72.dp)
                    )
                }
                Text(item.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(
                    if (item.isVideo) "Video - ${item.durationLabel} - ${item.album}" else "Photo - ${item.album}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SajiColors.TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PremiumButton("Close", onClick = onDismiss, filled = false, modifier = Modifier.weight(1f))
                    PremiumButton(
                        if (selected) "Deselect" else "Select",
                        onClick = onToggle,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
