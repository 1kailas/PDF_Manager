package com.example.chat.z.ui

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.LastPage
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedPdfViewerScreen(
    pdfUri: Uri?,
    pdfName: String,
    onClose: () -> Unit
) {
    var currentPage by remember { mutableStateOf(0) }
    var totalPages by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var pdfView by remember { mutableStateOf<PDFView?>(null) }
    var nightMode by remember { mutableStateOf(false) }
    var showControls by remember { mutableStateOf(true) }
    var showPageJumper by remember { mutableStateOf(false) }
    var pageInput by remember { mutableStateOf("") }
    var showBookmarks by remember { mutableStateOf(false) }
    var bookmarks by remember { mutableStateOf(setOf<Int>()) }
    var controlsKey by remember { mutableStateOf(0) }
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showSettings by remember { mutableStateOf(false) }
    var scrollMode by remember { mutableStateOf("continuous") } // continuous or page-by-page

    // Auto-hide controls after 4 seconds when they're shown
    LaunchedEffect(controlsKey) {
        if (showControls && pdfUri != null) {
            delay(4000)
            showControls = false
        }
    }

    // Apply night mode immediately when toggled
    LaunchedEffect(nightMode) {
        pdfView?.setNightMode(nightMode)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .graphicsLayer { }
    ) {
        // PDF Content - no home screen here anymore
        if (pdfUri != null) {
            PDFViewerContent(
                pdfUri = pdfUri,
                nightMode = nightMode,
                scrollMode = scrollMode,
                isLoading = isLoading,
                onPdfViewCreated = { view -> pdfView = view },
                onPageChanged = { page -> currentPage = page },
                onLoadComplete = { pages ->
                    totalPages = pages
                    isLoading = false
                },
                onTap = {
                    showControls = !showControls
                    controlsKey++
                }
            )

            // Page Jumper Dialog
            if (showPageJumper) {
                PageJumperDialog(
                    totalPages = totalPages,
                    pageInput = pageInput,
                    onPageInputChange = { pageInput = it },
                    onDismiss = {
                        showPageJumper = false
                        pageInput = ""
                    },
                    onJump = { page ->
                        pdfView?.jumpTo(page - 1)
                        showPageJumper = false
                        pageInput = ""
                    }
                )
            }

            // Bookmarks Panel
            if (showBookmarks && bookmarks.isNotEmpty()) {
                BookmarksPanel(
                    bookmarks = bookmarks,
                    onBookmarkClick = { page ->
                        pdfView?.jumpTo(page)
                        showBookmarks = false
                    },
                    onDismiss = { showBookmarks = false }
                )
            }

            // Search Bar
            AnimatedVisibility(
                visible = showSearch,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(300, easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f))
                ) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(200, easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f))
                ) + fadeOut(animationSpec = tween(200)),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 72.dp)
                    .zIndex(11f)
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onClose = { showSearch = false; searchQuery = "" }
                )
            }

            // Settings Dialog
            if (showSettings) {
                SettingsDialog(
                    scrollMode = scrollMode,
                    onScrollModeChange = { scrollMode = it },
                    onDismiss = { showSettings = false }
                )
            }
        }

        // Overlay Controls - Top Bar
        AnimatedVisibility(
            visible = showControls && pdfUri != null,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(300, easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f))
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(200, easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f))
            ) + fadeOut(animationSpec = tween(200)),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .zIndex(10f)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                shadowElevation = 8.dp
            ) {
                TopAppBar(
                    title = {
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = pdfName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (totalPages > 0) {
                                Text(
                                    text = "Page ${currentPage + 1} of $totalPages",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onClose) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    actions = {
                        // Search
                        IconButton(
                            onClick = { showSearch = !showSearch }
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = if (showSearch) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Bookmark current page with animation
                        val bookmarkScale by animateFloatAsState(
                            targetValue = if (currentPage in bookmarks) 1.2f else 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessVeryLow
                            ),
                            label = "bookmarkScale"
                        )

                        IconButton(
                            onClick = {
                                bookmarks = if (currentPage in bookmarks) {
                                    bookmarks - currentPage
                                } else {
                                    bookmarks + currentPage
                                }
                            }
                        ) {
                            Icon(
                                if (currentPage in bookmarks) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark Page",
                                tint = if (currentPage in bookmarks)
                                    Color(0xFFFFD700)
                                else
                                    MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = bookmarkScale
                                    scaleY = bookmarkScale
                                }
                            )
                        }

                        // Show bookmarks with badge
                        if (bookmarks.isNotEmpty()) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            bookmarks.size.toString(),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            ) {
                                IconButton(onClick = { showBookmarks = !showBookmarks }) {
                                    Icon(
                                        Icons.Default.Bookmarks,
                                        contentDescription = "View Bookmarks",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Night mode toggle with rotation animation
                        val nightModeRotation by animateFloatAsState(
                            targetValue = if (nightMode) 180f else 0f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            ),
                            label = "nightModeRotation"
                        )

                        IconButton(
                            onClick = {
                                nightMode = !nightMode
                            }
                        ) {
                            Icon(
                                if (nightMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Night Mode",
                                tint = if (nightMode) Color(0xFFFFA500) else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.graphicsLayer {
                                    rotationZ = nightModeRotation
                                }
                            )
                        }

                        // Settings menu
                        IconButton(onClick = { showSettings = true }) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            }
        }

        // Overlay Controls - Bottom Bar
        AnimatedVisibility(
            visible = showControls && pdfUri != null && totalPages > 0,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(300, easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f))
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(200, easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f))
            ) + fadeOut(animationSpec = tween(200)),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .zIndex(10f)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // First page
                    IconButton(
                        onClick = { pdfView?.jumpTo(0) },
                        enabled = currentPage > 0
                    ) {
                        Icon(
                            Icons.Default.FirstPage,
                            "First Page",
                            tint = if (currentPage > 0)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }

                    // Previous page
                    IconButton(
                        onClick = {
                            if (currentPage > 0) {
                                pdfView?.jumpTo(currentPage - 1)
                            }
                        },
                        enabled = currentPage > 0
                    ) {
                        Icon(
                            Icons.Default.ChevronLeft,
                            "Previous",
                            tint = if (currentPage > 0)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }

                    // Page counter (clickable)
                    Surface(
                        onClick = { showPageJumper = true },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = "${currentPage + 1} / $totalPages",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Next page
                    IconButton(
                        onClick = {
                            if (currentPage < totalPages - 1) {
                                pdfView?.jumpTo(currentPage + 1)
                            }
                        },
                        enabled = currentPage < totalPages - 1
                    ) {
                        Icon(
                            Icons.Default.ChevronRight,
                            "Next",
                            tint = if (currentPage < totalPages - 1)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }

                    // Last page
                    IconButton(
                        onClick = { pdfView?.jumpTo(totalPages - 1) },
                        enabled = currentPage < totalPages - 1
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.LastPage,
                            "Last Page",
                            tint = if (currentPage < totalPages - 1)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }
                }
            }
        }
    }
}

// Separate composable components for better organization

// HomeScreenContent moved to PdfManagerHome.kt for better organization

@Composable
private fun PDFViewerContent(
    pdfUri: Uri,
    scrollMode: String,
    nightMode: Boolean,
    isLoading: Boolean,
    onPdfViewCreated: (PDFView) -> Unit,
    onPageChanged: (Int) -> Unit,
    onLoadComplete: (Int) -> Unit,
    onTap: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Key on nightMode AND scrollMode to recreate view when either changes
        key(nightMode, scrollMode) {
            AndroidView(
                factory = { context ->
                    PDFView(context, null).apply {
                        onPdfViewCreated(this)

                        fromUri(pdfUri)
                            .defaultPage(0)
                            .onLoad { nbPages ->
                                onLoadComplete(nbPages)
                            }
                            .onPageChange { page, _ ->
                                onPageChanged(page)
                            }
                            .onTap {
                                onTap()
                                true
                            }
                            .enableSwipe(true)
                            .swipeHorizontal(scrollMode == "page-by-page")
                            .enableDoubletap(true)
                            .enableAnnotationRendering(false)
                            .scrollHandle(null)
                            .spacing(if (scrollMode == "page-by-page") 10 else 0)
                            .nightMode(nightMode)
                            .pageSnap(false) // NEVER snap - let velocity control scroll distance!
                            .pageFling(true) // Enable velocity-based scrolling
                            .autoSpacing(false)
                            .pageFitPolicy(
                                if (scrollMode == "page-by-page")
                                    com.github.barteksc.pdfviewer.util.FitPolicy.BOTH // Fit both to fill screen
                                else
                                    com.github.barteksc.pdfviewer.util.FitPolicy.WIDTH // Fit width for continuous
                            )
                            .enableAntialiasing(true)
                            .password(null)
                            .load()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (nightMode) Color(0xFF121212) else Color(0xFFF5F5F5))
            )
        }

        // Loading indicator - minimal overlay
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PageJumperDialog(
    totalPages: Int,
    pageInput: String,
    onPageInputChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onJump: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Jump to Page",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column {
                Text(
                    "Enter page number (1-$totalPages)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = pageInput,
                    onValueChange = {
                        if (it.isEmpty() || (it.all { char -> char.isDigit() } && it.toIntOrNull() != null)) {
                            onPageInputChange(it)
                        }
                    },
                    label = { Text("Page Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val page = pageInput.toIntOrNull()
                    if (page != null && page in 1..totalPages) {
                        onJump(page)
                    }
                },
                enabled = pageInput.toIntOrNull()?.let { it in 1..totalPages } == true,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Jump")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun BookmarksPanel(
    bookmarks: Set<Int>,
    onBookmarkClick: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clickable(enabled = false) { },
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bookmarks",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            "Close",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                bookmarks.sorted().forEach { page ->
                    Surface(
                        onClick = { onBookmarkClick(page) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Page ${page + 1}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search in PDF...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier.weight(1f)
            )
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, "Clear")
                }
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, "Close")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDialog(
    scrollMode: String,
    onScrollModeChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "PDF Settings",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        text = {
            Column {
                Text(
                    "Scroll Mode",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    onClick = { onScrollModeChange("continuous") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = if (scrollMode == "continuous")
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = scrollMode == "continuous",
                            onClick = { onScrollModeChange("continuous") }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Continuous Scroll",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                            Text(
                                "Smooth scrolling through pages",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    onClick = { onScrollModeChange("page-by-page") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = if (scrollMode == "page-by-page")
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = scrollMode == "page-by-page",
                            onClick = { onScrollModeChange("page-by-page") }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Page by Page",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                            Text(
                                "Swipe to change pages",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Done")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}
