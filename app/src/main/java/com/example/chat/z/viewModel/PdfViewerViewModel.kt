package com.example.chat.z.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.z.data.PdfDocument
import com.example.chat.z.util.PdfRendererManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PdfViewerState(
    val isLoading: Boolean = false,
    val currentDocument: PdfDocument? = null,
    val currentPageIndex: Int = 0,
    val currentPageBitmap: Bitmap? = null,
    val error: String? = null,
    val scale: Float = 1f,
    val recentDocuments: List<PdfDocument> = emptyList()
)

class PdfViewerViewModel(application: Application) : AndroidViewModel(application) {

    private val pdfRenderer = PdfRendererManager(application.applicationContext)

    private val _state = MutableStateFlow(PdfViewerState())
    val state: StateFlow<PdfViewerState> = _state.asStateFlow()

    fun openPdfDocument(uri: Uri, name: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val pageCount = pdfRenderer.openPdfDocument(uri)

            if (pageCount != null && pageCount > 0) {
                val document = PdfDocument(
                    uri = uri,
                    name = name,
                    pageCount = pageCount
                )

                _state.value = _state.value.copy(
                    currentDocument = document,
                    currentPageIndex = 0,
                    isLoading = false
                )

                // Add to recent documents
                addToRecent(document)

                // Load first page
                loadPage(0)
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to open PDF document"
                )
            }
        }
    }

    fun loadPage(pageIndex: Int) {
        val document = _state.value.currentDocument ?: return

        if (pageIndex < 0 || pageIndex >= document.pageCount) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val bitmap = pdfRenderer.renderPage(pageIndex)

            _state.value = _state.value.copy(
                currentPageIndex = pageIndex,
                currentPageBitmap = bitmap,
                isLoading = false,
                scale = 1f // Reset scale when changing pages
            )
        }
    }

    fun nextPage() {
        val currentIndex = _state.value.currentPageIndex
        val document = _state.value.currentDocument ?: return

        if (currentIndex < document.pageCount - 1) {
            loadPage(currentIndex + 1)
        }
    }

    fun previousPage() {
        val currentIndex = _state.value.currentPageIndex

        if (currentIndex > 0) {
            loadPage(currentIndex - 1)
        }
    }

    fun goToPage(pageIndex: Int) {
        loadPage(pageIndex)
    }

    fun updateScale(scale: Float) {
        _state.value = _state.value.copy(scale = scale)
    }

    fun closeDocument() {
        pdfRenderer.close()
        _state.value = PdfViewerState(recentDocuments = _state.value.recentDocuments)
    }

    private fun addToRecent(document: PdfDocument) {
        val recent = _state.value.recentDocuments.toMutableList()

        // Remove if already exists
        recent.removeAll { it.uri == document.uri }

        // Add to beginning
        recent.add(0, document)

        // Keep only last 10
        if (recent.size > 10) {
            recent.removeAt(recent.size - 1)
        }

        _state.value = _state.value.copy(recentDocuments = recent)
    }

    override fun onCleared() {
        super.onCleared()
        pdfRenderer.close()
    }
}

