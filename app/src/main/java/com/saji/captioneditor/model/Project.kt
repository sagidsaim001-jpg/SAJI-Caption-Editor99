package com.saji.captioneditor.model

import androidx.compose.ui.graphics.Color

data class Project(
    val id: Int,
    val name: String,
    val durationSec: Int,
    val lastEdited: String,
    val favorite: Boolean = false,
    val draft: Boolean = false,
    val skyTop: Color,
    val skyBottom: Color
) {
    val durationLabel: String
        get() = "%02d:%02d".format(durationSec / 60, durationSec % 60)
}

object SampleData {
    fun projects(): List<Project> = listOf(
        Project(1, "Sunset Feelings", 15, "Edited 2 hours ago", favorite = true,
            skyTop = Color(0xFFFF8A5B), skyBottom = Color(0xFF3B2A6B)),
        Project(2, "Red Rose", 20, "Edited yesterday",
            skyTop = Color(0xFFB3123A), skyBottom = Color(0xFF1F3B2A)),
        Project(3, "Ishq Hai", 30, "Edited 3 days ago", favorite = true,
            skyTop = Color(0xFFFF9F6B), skyBottom = Color(0xFF4B2A7A)),
        Project(4, "Night Lyrics", 42, "Edited last week", draft = true,
            skyTop = Color(0xFF2B1B6B), skyBottom = Color(0xFF0B0720)),
        Project(5, "Travel Vlog", 58, "Edited 2 weeks ago",
            skyTop = Color(0xFF2F7BFF), skyBottom = Color(0xFF1B1245)),
        Project(6, "Neon Quotes", 12, "Edited last month", draft = true,
            skyTop = Color(0xFFE879F9), skyBottom = Color(0xFF2A0B5B))
    )
}
