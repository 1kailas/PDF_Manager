package com.example.chat.z.data

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PdfRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("pdf_manager", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Recent files
    fun getRecentFiles(): List<PdfFile> {
        val json = prefs.getString("recent_files", "[]") ?: "[]"
        val type = object : TypeToken<List<PdfFile>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addRecentFile(file: PdfFile) {
        val recent = getRecentFiles().toMutableList()
        recent.removeAll { it.uriString == file.uriString }
        recent.add(0, file.copy(lastOpened = System.currentTimeMillis()))
        if (recent.size > 50) recent.removeAt(recent.size - 1)
        saveRecentFiles(recent)
    }

    private fun saveRecentFiles(files: List<PdfFile>) {
        prefs.edit().putString("recent_files", gson.toJson(files)).apply()
    }

    fun removeRecentFile(fileId: String) {
        val recent = getRecentFiles().filter { it.id != fileId }
        saveRecentFiles(recent)
    }

    fun clearRecentFiles() {
        saveRecentFiles(emptyList())
    }

    // Folders
    fun getFolders(): List<PdfFolder> {
        val json = prefs.getString("folders", "[]") ?: "[]"
        val type = object : TypeToken<List<PdfFolder>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addFolder(folder: PdfFolder) {
        val folders = getFolders().toMutableList()
        folders.add(folder)
        saveFolders(folders)
    }

    fun updateFolder(folder: PdfFolder) {
        val folders = getFolders().map { if (it.id == folder.id) folder else it }
        saveFolders(folders)
    }

    fun deleteFolder(folderId: String) {
        val folders = getFolders().filter { it.id != folderId }
        saveFolders(folders)
        // Remove folder association from files
        val files = getRecentFiles().map {
            if (it.folderId == folderId) it.copy(folderId = null) else it
        }
        saveRecentFiles(files)
    }

    private fun saveFolders(folders: List<PdfFolder>) {
        prefs.edit().putString("folders", gson.toJson(folders)).apply()
    }

    // File operations
    fun getFilesInFolder(folderId: String?): List<PdfFile> {
        return getRecentFiles().filter { it.folderId == folderId }
    }

    fun moveFileToFolder(fileId: String, folderId: String?) {
        val files = getRecentFiles().map {
            if (it.id == fileId) it.copy(folderId = folderId) else it
        }
        saveRecentFiles(files)
        updateFolderCounts()
    }

    fun toggleFavorite(fileId: String) {
        val files = getRecentFiles().map {
            if (it.id == fileId) it.copy(isFavorite = !it.isFavorite) else it
        }
        saveRecentFiles(files)
    }

    fun getFavorites(): List<PdfFile> {
        return getRecentFiles().filter { it.isFavorite }
    }

    private fun updateFolderCounts() {
        val files = getRecentFiles()
        val folders = getFolders().map { folder ->
            folder.copy(fileCount = files.count { it.folderId == folder.id })
        }
        saveFolders(folders)
    }

    // Sort
    fun sortFiles(files: List<PdfFile>, option: SortOption): List<PdfFile> {
        return when (option) {
            SortOption.NAME_ASC -> files.sortedBy { it.name.lowercase() }
            SortOption.NAME_DESC -> files.sortedByDescending { it.name.lowercase() }
            SortOption.DATE_ASC -> files.sortedBy { it.lastOpened }
            SortOption.DATE_DESC -> files.sortedByDescending { it.lastOpened }
            SortOption.SIZE_ASC -> files.sortedBy { it.size }
            SortOption.SIZE_DESC -> files.sortedByDescending { it.size }
        }
    }
}
