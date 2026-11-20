package com.saji.captioneditor.ui.screens.projects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saji.captioneditor.model.Project
import com.saji.captioneditor.navigation.Routes
import com.saji.captioneditor.ui.AppViewModel
import com.saji.captioneditor.ui.components.BarIcon
import com.saji.captioneditor.ui.components.CustomDialog
import com.saji.captioneditor.ui.components.EmptyState
import com.saji.captioneditor.ui.components.GlassCard
import com.saji.captioneditor.ui.components.PremiumButton
import com.saji.captioneditor.ui.components.ProjectCard
import com.saji.captioneditor.ui.components.ProjectMenuHost
import com.saji.captioneditor.ui.components.SajiBottomSheet
import com.saji.captioneditor.ui.components.SearchField
import com.saji.captioneditor.ui.components.SelectionChip
import com.saji.captioneditor.ui.components.SettingsItem
import com.saji.captioneditor.ui.components.pressScale
import com.saji.captioneditor.ui.theme.SajiColors

private enum class ProjectFilter(val label: String) {
    Recent("Recent"), All("All"), Favorites("Favorites"), Drafts("Drafts")
}

private enum class SortMode(val label: String) {
    Newest("Newest first"), Name("Name (A-Z)"), Duration("Longest first")
}

@Composable
fun ProjectsScreen(vm: AppViewModel, onNavigate: (String) -> Unit) {
    var filter by remember { mutableStateOf(ProjectFilter.Recent) }
    var sort by remember { mutableStateOf(SortMode.Newest) }
    var grid by remember { mutableStateOf(true) }
    var searching by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var selectMode by remember { mutableStateOf(false) }
    val selected = remember { mutableStateListOf<Int>() }
    var sortSheet by remember { mutableStateOf(false) }
    var menuProject by remember { mutableStateOf<Project?>(null) }
    var confirmDelete by remember { mutableStateOf(false) }

    val base = when (filter) {
        ProjectFilter.Recent -> vm.projects.take(4)
        ProjectFilter.All -> vm.projects.toList()
        ProjectFilter.Favorites -> vm.projects.filter { it.favorite }
        ProjectFilter.Drafts -> vm.projects.filter { it.draft }
    }
    val visible = base
        .filter { it.name.contains(query, ignoreCase = true) }
        .let { list ->
            when (sort) {
                SortMode.Newest -> list
                SortMode.Name -> list.sortedBy { it.name.lowercase() }
                SortMode.Duration -> list.sortedByDescending { it.durationSec }
            }
        }

    fun open(p: Project) = onNavigate(Routes.editor(p.id))
    fun toggle(p: Project) {
        if (p.id in selected) selected.remove(p.id) else selected.add(p.id)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SajiColors.ScreenGradient)
    ) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 12.dp, top = 12.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (selectMode) "${selected.size} selected" else "Projects",
                    style = MaterialTheme.typography.headlineMedium,
                    color = SajiColors.TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                BarIcon(Icons.Filled.Search, "Search", onClick = {
                    searching = !searching
                    if (!searching) query = ""
                }, active = searching)
                Spacer(Modifier.width(6.dp))
                BarIcon(Icons.AutoMirrored.Filled.Sort, "Sort", onClick = { sortSheet = true })
                Spacer(Modifier.width(6.dp))
                BarIcon(
                    if (grid) Icons.AutoMirrored.Filled.ViewList else Icons.Filled.GridView,
                    if (grid) "List view" else "Grid view",
                    onClick = { grid = !grid }
                )
                Spacer(Modifier.width(6.dp))
                BarIcon(Icons.Filled.Add, "New project", onClick = { onNavigate(Routes.Gallery) })
            }

            AnimatedVisibility(searching) {
                SearchField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = "Search projects",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProjectFilter.values().forEach { f ->
                    SelectionChip(f.label, selected = filter == f, onClick = { filter = f })
                }
                SelectionChip(
                    if (selectMode) "Cancel" else "Select",
                    selected = selectMode,
                    onClick = {
                        selectMode = !selectMode
                        selected.clear()
                    }
                )
            }

            Box(Modifier.weight(1f)) {
                if (visible.isEmpty()) {
                    val none = vm.projects.isEmpty()
                    EmptyState(
                        icon = Icons.Filled.VideoLibrary,
                        title = when {
                            none -> "No projects yet"
                            query.isNotBlank() -> "No results"
                            else -> "Nothing in ${filter.label}"
                        },
                        subtitle = when {
                            none -> "Create your first caption video."
                            query.isNotBlank() -> "Try a different search."
                            filter == ProjectFilter.Favorites -> "Add favorites from a project's menu."
                            filter == ProjectFilter.Drafts -> "Unfinished projects show up here."
                            else -> "Try another tab."
                        },
                        modifier = Modifier.align(Alignment.Center),
                        actionText = if (none) "Add Project" else null,
                        onAction = if (none) ({ onNavigate(Routes.Gallery) }) else null
                    )
                } else if (grid) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 140.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(visible, key = { it.id }) { p ->
                            ProjectCard(
                                project = p,
                                showMeta = true,
                                selectionMode = selectMode,
                                selected = p.id in selected,
                                onClick = { if (selectMode) toggle(p) else open(p) },
                                onMenuClick = { menuProject = p }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 140.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(visible, key = { it.id }) { p ->
                            ProjectRow(
                                project = p,
                                selectionMode = selectMode,
                                selected = p.id in selected,
                                onClick = { if (selectMode) toggle(p) else open(p) },
                                onMenuClick = { menuProject = p }
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = selectMode && selected.isNotEmpty(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 112.dp, start = 16.dp, end = 16.dp)
        ) {
            GlassCard(Modifier.fillMaxWidth(), shape = RoundedCornerShape(50)) {
                Row(
                    modifier = Modifier.padding(start = 20.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${selected.size} selected", color = SajiColors.TextPrimary, modifier = Modifier.weight(1f))
                    PremiumButton(
                        text = "Delete",
                        icon = Icons.Filled.Delete,
                        danger = true,
                        onClick = { confirmDelete = true }
                    )
                }
            }
        }
    }

    if (sortSheet) {
        SajiBottomSheet(onDismiss = { sortSheet = false }) {
            Text("Sort by", style = MaterialTheme.typography.titleMedium, color = SajiColors.TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
            SortMode.values().forEach { mode ->
                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.Sort,
                    title = mode.label,
                    onClick = { sort = mode; sortSheet = false },
                    trailing = if (sort == mode) ({
                        Icon(Icons.Filled.Check, contentDescription = "Selected", tint = SajiColors.Pink)
                    }) else null
                )
            }
            Spacer(Modifier.size(12.dp))
        }
    }

    if (confirmDelete) {
        CustomDialog(
            title = "Delete ${selected.size} project(s)?",
            message = "The selected projects will be permanently removed. This cannot be undone.",
            confirmText = "Delete",
            destructive = true,
            onConfirm = {
                val ids = selected.toList()
                vm.projects.removeAll { it.id in ids }
                selected.clear()
                selectMode = false
                confirmDelete = false
                vm.showToast("Projects deleted")
            },
            onDismiss = { confirmDelete = false }
        )
    }

    ProjectMenuHost(
        project = menuProject,
        onDismiss = { menuProject = null },
        vm = vm,
        onOpen = { open(it) }
    )
}

@Composable
private fun ProjectRow(
    project: Project,
    selectionMode: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    GlassCard(Modifier.fillMaxWidth(), selected = selected, onClick = onClick) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 84.dp, height = 64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.verticalGradient(listOf(project.skyTop, project.skyBottom)))
            ) {
                Text(
                    project.durationLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    project.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = SajiColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(project.lastEdited, style = MaterialTheme.typography.bodySmall, color = SajiColors.TextSecondary)
                if (project.draft) {
                    Text("Draft", style = MaterialTheme.typography.labelSmall, color = SajiColors.Gold)
                }
            }
            if (selectionMode) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (selected) SajiColors.Purple else SajiColors.CardLight),
                    contentAlignment = Alignment.Center
                ) {
                    if (selected) Icon(Icons.Filled.Check, contentDescription = "Selected", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            } else {
                Box(
                    modifier = Modifier
                        .pressScale(onClick = onMenuClick)
                        .size(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Project menu", tint = SajiColors.TextSecondary)
                }
            }
        }
    }
}
