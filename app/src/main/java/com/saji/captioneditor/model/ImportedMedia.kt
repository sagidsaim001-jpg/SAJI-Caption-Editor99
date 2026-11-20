package com.saji.captioneditor.model

/** A user-selected item returned by Android's system Photo Picker. */
data class ImportedMedia(
    val uri: String,
    val mimeType: String?
) {
    val isVideo: Boolean get() = mimeType?.startsWith("video/") == true
}
