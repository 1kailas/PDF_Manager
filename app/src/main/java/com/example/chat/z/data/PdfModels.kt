package com.example.chat.z.data

import android.net.Uri

data class PdfFile(
    val id: String = java.util.UUID.randomUUID().toString(),
    val uriString: String, // Store as String instead of Uri for Gson
    val name: String,
    val size: Long,
    val pageCount: Int = 0,
    val lastOpened: Long = System.currentTimeMillis(),
    val dateAdded: Long = System.currentTimeMillis(),
    val folderId: String? = null,
    val isFavorite: Boolean = false,
    val thumbnail: String? = null
) {
    val uri: Uri
        get() = Uri.parse(uriString)
}

data class PdfFolder(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val color: Int,
    val icon: String = "📁",
    val dateCreated: Long = System.currentTimeMillis(),
    val fileCount: Int = 0
)

enum class SortOption {
    NAME_ASC,
    NAME_DESC,
    DATE_ASC,
    DATE_DESC,
    SIZE_ASC,
    SIZE_DESC
}

enum class ViewMode {
    GRID,
    LIST
}

