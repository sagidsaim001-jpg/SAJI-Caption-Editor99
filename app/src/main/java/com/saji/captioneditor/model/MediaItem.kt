package com.saji.captioneditor.model

import androidx.compose.ui.graphics.Color

data class MediaItem(
    val id: Int,
    val name: String,
    val isVideo: Boolean,
    val durationSec: Int,
    val album: String,
    val c1: Color,
    val c2: Color
) {
    val durationLabel: String
        get() = "%02d:%02d".format(durationSec / 60, durationSec % 60)
}

/** Fake media used only for the UI phase. Real device media comes later. */
object MediaSample {
    private val palette = listOf(
        Color(0xFFFF8A5B) to Color(0xFF3B2A6B),
        Color(0xFFB3123A) to Color(0xFF1F3B2A),
        Color(0xFF2F7BFF) to Color(0xFF1B1245),
        Color(0xFFE879F9) to Color(0xFF2A0B5B),
        Color(0xFFFFC53D) to Color(0xFF5B21B6),
        Color(0xFF34D399) to Color(0xFF0F2A3B),
        Color(0xFFFF5C7A) to Color(0xFF2B1B6B)
    )
    private val albums = listOf("Camera", "Downloads", "WhatsApp", "Screenshots", "Favorites")

    val items: List<MediaItem> = (1..36).map { i ->
        val (a, b) = palette[i % palette.size]
        val video = i % 3 != 0
        MediaItem(
            id = i,
            name = (if (video) "VID_%04d" else "IMG_%04d").format(i),
            isVideo = video,
            durationSec = 5 + (i * 7) % 55,
            album = albums[i % albums.size],
            c1 = a,
            c2 = b
        )
    }

    fun byId(id: Int): MediaItem? = items.firstOrNull { it.id == id }
}
