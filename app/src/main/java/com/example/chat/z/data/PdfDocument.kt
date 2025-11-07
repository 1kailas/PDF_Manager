package com.example.chat.z.data

import android.net.Uri

data class PdfDocument(
    val uri: Uri,
    val name: String,
    val pageCount: Int,
    val lastOpenedTime: Long = System.currentTimeMillis()
)

data class PdfPage(
    val pageNumber: Int,
    val bitmap: android.graphics.Bitmap?
)

