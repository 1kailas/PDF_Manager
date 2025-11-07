package com.example.chat.z

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile
import com.example.chat.z.data.PdfFile
import com.example.chat.z.data.PdfRepository
import com.example.chat.z.ui.EnhancedPdfViewerScreen
import com.example.chat.z.ui.PdfManagerHomeScreen
import com.example.chat.z.ui.theme.ChatZTheme
import com.example.chat.z.util.FileUtils

class MainActivity : ComponentActivity() {
    private lateinit var repository: PdfRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable 120fps support by configuring the window
        window?.let {
            // Request highest refresh rate available
            val params = it.attributes
            params.preferredDisplayModeId = 0 // Let system choose best mode
            it.attributes = params
        }

        repository = PdfRepository(this)

        // Handle PDF opened from external app
        val intentUri = handleIntent(intent)
        
        // Load dark mode preference
        val sharedPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val initialDarkMode = sharedPrefs.getBoolean("dark_mode", false)

        setContent {
            var darkMode by remember { mutableStateOf(initialDarkMode) }

            ChatZTheme(darkTheme = darkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PdfViewerApp(
                        initialUri = intentUri,
                        repository = repository,
                        darkMode = darkMode,
                        onToggleDarkMode = { 
                            darkMode = !darkMode
                            sharedPrefs.edit().putBoolean("dark_mode", darkMode).apply()
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Handle new PDF intent when app is already running
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent): Uri? {
        return when (intent.action) {
            Intent.ACTION_VIEW -> {
                intent.data?.also { uri ->
                    try {
                        // Grant persistent read permission if possible
                        contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    } catch (e: Exception) {
                        // Permission might not be available, but we can still read
                        e.printStackTrace()
                    }
                }
            }
            else -> null
        }
    }
}

@Composable
fun PdfViewerApp(
    initialUri: Uri? = null,
    repository: PdfRepository,
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val context = LocalContext.current
    var currentPdfUri by remember { mutableStateOf(initialUri) }
    var currentPdfFile by remember { mutableStateOf<PdfFile?>(null) }
    var showHomeScreen by remember { mutableStateOf(initialUri == null) }

    // Update when new intent arrives
    val activity = context as? MainActivity
    LaunchedEffect(activity) {
        activity?.intent?.data?.let { uri ->
            currentPdfUri = uri
            showHomeScreen = false
        }
    }


    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                try {
                    // Grant persistent read permission
                    context.contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                currentPdfUri = it
                showHomeScreen = false

                // Add to repository
                val docFile = DocumentFile.fromSingleUri(context, it)
                val pdfFile = PdfFile(
                    uriString = it.toString(),
                    name = docFile?.name ?: "Unknown.pdf",
                    size = docFile?.length() ?: 0L,
                    pageCount = 0, // Will be updated when loaded
                    folderId = null // New files have no folder
                )
                currentPdfFile = pdfFile
                repository.addRecentFile(pdfFile)
            }
        }
    )

    AnimatedContent(
        targetState = showHomeScreen,
        transitionSpec = {
            // Smooth slide animations for 120fps
            // When going to PDF viewer (forward): slide in from right
            // When going back to home: slide in from left
            val isForward = !targetState // targetState is showHomeScreen, so !targetState means going to PDF

            if (isForward) {
                // Opening PDF - slide in from right
                (slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
                    ),
                    initialAlpha = 0.3f
                )) togetherWith (slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
                    ),
                    targetAlpha = 0.3f
                ))
            } else {
                // Going back to home - slide in from left
                (slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
                    ),
                    initialAlpha = 0.3f
                )) togetherWith (slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
                    ),
                    targetAlpha = 0.3f
                ))
            } using SizeTransform { _, _ ->
                tween(durationMillis = 0)
            }
        },
        label = "screenTransition"
    ) { isHomeScreen ->
        if (isHomeScreen) {
            PdfManagerHomeScreen(
                repository = repository,
                onOpenFile = {
                    filePickerLauncher.launch(arrayOf("application/pdf"))
                },
                onFileClick = { file ->
                    currentPdfUri = file.uri
                    currentPdfFile = file
                    showHomeScreen = false
                    repository.addRecentFile(file)
                },
                darkMode = darkMode,
                onToggleDarkMode = onToggleDarkMode
            )
        } else if (currentPdfUri != null) {
            EnhancedPdfViewerScreen(
                pdfUri = currentPdfUri,
                pdfName = currentPdfFile?.name ?: FileUtils.getFileName(context, currentPdfUri!!),
                onClose = {
                    currentPdfUri = null
                    currentPdfFile = null
                    showHomeScreen = true
                }
            )
        }
    }
}