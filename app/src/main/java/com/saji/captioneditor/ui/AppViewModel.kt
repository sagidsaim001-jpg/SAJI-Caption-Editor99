package com.saji.captioneditor.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.saji.captioneditor.model.MediaSample
import com.saji.captioneditor.model.Project
import com.saji.captioneditor.model.SampleData

/** Shared in-memory UI state. No persistence in this phase. */
class AppViewModel : ViewModel() {
    val projects = mutableStateListOf<Project>().apply { addAll(SampleData.projects()) }
    val selectedMedia = mutableStateListOf<Int>()
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
    }

    fun toggleFavorite(id: Int) {
        val i = projects.indexOfFirst { it.id == id }
        if (i >= 0) projects[i] = projects[i].copy(favorite = !projects[i].favorite)
    }

    fun createProject(name: String): Project {
        val items = selectedMedia.mapNotNull { MediaSample.byId(it) }
        val first = items.firstOrNull()
        val total = items.sumOf { if (it.isVideo) it.durationSec else 5 }.coerceAtLeast(5)
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
        selectedMedia.clear()
        return project
    }
}
