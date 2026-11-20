package com.saji.captioneditor.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.saji.captioneditor.model.ImportedMedia
import com.saji.captioneditor.model.MediaSample
import com.saji.captioneditor.model.Project
import com.saji.captioneditor.model.SampleData

/** Shared in-memory UI state. Project media and captions are retained while the app process lives. */
class AppViewModel : ViewModel() {
    val projects = mutableStateListOf<Project>().apply { addAll(SampleData.projects()) }
    val selectedMedia = mutableStateListOf<Int>()
    val importedMedia = mutableStateListOf<ImportedMedia>()
    private val projectMedia = mutableStateMapOf<Int, List<ImportedMedia>>()
    private val captions = mutableStateMapOf<Int, String>()

    fun addImportedMedia(items: List<ImportedMedia>) {
        items.forEach { item ->
            if (importedMedia.none { it.uri == item.uri }) importedMedia.add(item)
        }
        showToast("${importedMedia.size} device item(s) selected")
    }

    fun removeImportedMedia(uri: String) {
        importedMedia.removeAll { it.uri == uri }
    }

    fun mediaForProject(projectId: Int): List<ImportedMedia> = projectMedia[projectId].orEmpty()

    fun captionFor(projectId: Int): String = captions[projectId] ?: "Your story begins here..."

    fun saveCaption(projectId: Int, value: String) {
        captions[projectId] = value
        showToast("Caption saved")
    }

    var toast by mutableStateOf<String?>(null)
    private var nextId = 100

    fun showToast(message: String) {
        toast = message
    }

    fun rename(id: Int, name: String) {
        val i = projects.indexOfFirst { it.id == id }
        if (i >= 0) projects[i] = projects[i].copy(name = name.trim(), lastEdited = "Edited just now")
    }

    fun duplicate(id: Int) {
        val i = projects.indexOfFirst { it.id == id }
        if (i >= 0) {
            val p = projects[i]
            projects.add(i + 1, p.copy(id = nextId++, name = p.name + " (Copy)", lastEdited = "Edited just now", favorite = false))
        }
    }

    fun delete(id: Int) {
        projects.removeAll { it.id == id }
        projectMedia.remove(id)
        captions.remove(id)
    }

    fun toggleFavorite(id: Int) {
        val i = projects.indexOfFirst { it.id == id }
        if (i >= 0) projects[i] = projects[i].copy(favorite = !projects[i].favorite)
    }

    fun createProject(name: String): Project {
        val items = selectedMedia.mapNotNull { MediaSample.byId(it) }
        val imported = importedMedia.toList()
        val first = items.firstOrNull()
        val total = (items.sumOf { if (it.isVideo) it.durationSec else 5 } +
            imported.sumOf { if (it.isVideo) 30 else 5 }).coerceAtLeast(5)
        val project = Project(
            id = nextId++,
            name = name.trim().ifBlank { "Untitled" },
            durationSec = total,
            lastEdited = "Edited just now",
            draft = true,
            skyTop = first?.c1 ?: Color(0xFF8B5CF6),
            skyBottom = first?.c2 ?: Color(0xFF1B1245)
        )
        projects.add(0, project)
        projectMedia[project.id] = imported
        selectedMedia.clear()
        importedMedia.clear()
        return project
    }
}
