package com.saji.captioneditor.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saji.captioneditor.model.Project
import com.saji.captioneditor.ui.AppViewModel
import com.saji.captioneditor.ui.theme.SajiColors

private enum class ProjectDialogMode { Menu, Rename, Details, Delete }

/**
 * Three-dot menu for a project: bottom sheet with Open / Rename / Duplicate / Favorite /
 * Details / Delete, plus the dialogs those actions need. Pass null to show nothing.
 */
@Composable
fun ProjectMenuHost(
    project: Project?,
    onDismiss: () -> Unit,
    vm: AppViewModel,
    onOpen: (Project) -> Unit
) {
    if (project == null) return
    var mode by remember(project.id) { mutableStateOf(ProjectDialogMode.Menu) }

    when (mode) {
        ProjectDialogMode.Menu -> SajiBottomSheet(onDismiss = onDismiss) {
            Text(
                project.name,
                style = MaterialTheme.typography.titleMedium,
                color = SajiColors.TextPrimary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                "${project.durationLabel} - ${project.lastEdited}",
                style = MaterialTheme.typography.bodySmall,
                color = SajiColors.TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SettingsItem(Icons.Filled.PlayArrow, "Open Project", onClick = { onDismiss(); onOpen(project) })
            SettingsItem(Icons.Filled.Edit, "Rename Project", onClick = { mode = ProjectDialogMode.Rename })
            SettingsItem(Icons.Filled.ContentCopy, "Duplicate Project", onClick = {
                vm.duplicate(project.id)
                vm.showToast("Project duplicated")
                onDismiss()
            })
            SettingsItem(
                if (project.favorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                if (project.favorite) "Remove from Favorites" else "Add to Favorites",
                onClick = {
                    vm.toggleFavorite(project.id)
                    vm.showToast(if (project.favorite) "Removed from favorites" else "Added to favorites")
                    onDismiss()
                }
            )
            SettingsItem(Icons.Filled.Info, "Project Details", onClick = { mode = ProjectDialogMode.Details })
            SettingsItem(
                Icons.Filled.Delete, "Delete Project",
                tint = SajiColors.Danger,
                onClick = { mode = ProjectDialogMode.Delete }
            )
            Spacer(Modifier.height(12.dp))
        }

        ProjectDialogMode.Rename -> TextInputDialog(
            title = "Rename Project",
            initial = project.name,
            confirmText = "Save",
            onConfirm = {
                vm.rename(project.id, it)
                vm.showToast("Project renamed")
                onDismiss()
            },
            onDismiss = onDismiss
        )

        ProjectDialogMode.Details -> CustomDialog(
            title = "Project Details",
            message = "Name: ${project.name}\nDuration: ${project.durationLabel}\n" +
                "Resolution: 1080 x 1920\nFrame rate: 30 FPS\nSize: about ${project.durationSec * 3} MB\n${project.lastEdited}",
            confirmText = "Open",
            dismissText = "Close",
            onConfirm = { onDismiss(); onOpen(project) },
            onDismiss = onDismiss
        )

        ProjectDialogMode.Delete -> CustomDialog(
            title = "Delete project?",
            message = "\"${project.name}\" will be permanently removed. This cannot be undone.",
            confirmText = "Delete",
            destructive = true,
            onConfirm = {
                vm.delete(project.id)
                vm.showToast("Project deleted")
                onDismiss()
            },
            onDismiss = onDismiss
        )
    }
}
