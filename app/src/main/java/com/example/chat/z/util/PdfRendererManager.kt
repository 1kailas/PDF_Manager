package com.example.chat.z.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PdfRendererManager(private val context: Context) {

    private var pdfRenderer: PdfRenderer? = null
    private var parcelFileDescriptor: ParcelFileDescriptor? = null
    private var currentFile: File? = null

    suspend fun openPdfDocument(uri: Uri): Int? = withContext(Dispatchers.IO) {
        try {
            close()

            // Copy URI content to a temporary file (required for PdfRenderer)
            val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext null
            val file = File(context.cacheDir, "temp_pdf_${System.currentTimeMillis()}.pdf")
            currentFile = file

            FileOutputStream(file).use { output ->
                inputStream.copyTo(output)
            }
            inputStream.close()

            parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(parcelFileDescriptor!!)

            pdfRenderer?.pageCount
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun renderPage(pageIndex: Int, width: Int = 1080): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val renderer = pdfRenderer ?: return@withContext null
            if (pageIndex < 0 || pageIndex >= renderer.pageCount) return@withContext null

            val page = renderer.openPage(pageIndex)

            // Calculate height maintaining aspect ratio
            val aspectRatio = page.height.toFloat() / page.width.toFloat()
            val height = (width * aspectRatio).toInt()

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()

            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getPageCount(): Int = pdfRenderer?.pageCount ?: 0

    fun close() {
        pdfRenderer?.close()
        parcelFileDescriptor?.close()
        currentFile?.delete()

        pdfRenderer = null
        parcelFileDescriptor = null
        currentFile = null
    }
}

