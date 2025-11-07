# PDF Manager - Advanced PDF Viewer for Android

A modern, feature-rich PDF manager and viewer for Android built with Jetpack Compose and Material 3. Optimized for 120Hz displays with smooth animations and professional UI/UX.

## ✨ Key Features

### 📄 Advanced PDF Viewing
- **High-quality rendering** using Android's native PdfRenderer API
- **Smooth page navigation** with gesture support
- **Night mode** for comfortable reading in low light
- **Page counter** with current page and total pages display
- **Auto-hide controls** for immersive reading experience

### 📚 PDF Management
- **Folder organization** - Create and manage custom folders
- **Recent files** - Quick access to recently opened documents
- **Favorites** - Star important PDFs for easy access
- **File metadata** - View file size, page count, and last opened date
- **Sorting options** - Sort by name (A-Z, Z-A) or date (newest/oldest)

### 🎨 Modern UI/UX
- **Material 3 Design** with smooth, professional animations
- **120fps animations** - Optimized for high refresh rate displays
- **Dark/Light theme** support with instant switching
- **Slide transitions** - Smooth screen navigation animations
- **Hardware accelerated** - Buttery smooth performance
- **Tab-based navigation** - Files, All Files, and Favorites

### 🎯 Enhanced Controls
- **Page navigation** - First, previous, next, last page buttons
- **Page jumper** - Quick jump to any page number
- **Bookmarks** - Save and manage bookmarks for quick access
- **Search functionality** - Find text within PDFs (UI ready)
- **Settings panel** - Customize scroll mode and viewing preferences

### 🔄 Smooth Animations
- **Slide in/out** transitions for screen navigation
- **Fade animations** for controls and dialogs
- **Spring physics** for natural button interactions
- **120Hz optimized** - Professional Material Design 3 easing curves
- **No lag** - Hardware accelerated rendering

## 🏗️ Technical Architecture

### Built With
- **Jetpack Compose** - Modern declarative UI framework
- **Material 3** - Latest Material Design components
- **Kotlin** - 100% Kotlin codebase
- **Coroutines & Flow** - Asynchronous operations
- **AndroidX PdfRenderer** - Native PDF rendering
- **SharedPreferences** - Local data persistence

### Architecture Pattern
- **MVVM** - Model-View-ViewModel architecture
- **Repository pattern** - Data management layer
- **State management** - Compose state and ViewModel
- **Lifecycle aware** - Proper lifecycle management

### Performance Optimizations
- **Hardware acceleration** with `graphicsLayer`
- **Efficient memory usage** with bitmap recycling
- **Background rendering** for smooth UI
- **120Hz display support** with optimized animations
- **Lazy loading** for large file lists
- **State persistence** across configuration changes

## 📱 System Requirements
- **Android 7.0 (API 24)** or higher
- **2GB RAM** minimum (4GB recommended)
- **Storage permissions** for file access
- **120Hz display** recommended for optimal animations

## 🚀 Getting Started

### Opening a PDF
1. Launch the app
2. Tap the **floating action button (+)**
3. Select a PDF from your device
4. Start reading immediately

### Managing PDFs
- **Create folders** using the "New Folder" button
- **Long press files** to access options (move, delete, favorite)
- **Swipe between tabs** to view different file categories
- **Sort files** using the sort menu in the top bar

### Reading Experience
- **Tap screen** to show/hide controls
- **Swipe pages** for continuous scrolling
- **Toggle night mode** for comfortable night reading
- **Set bookmarks** to remember important pages
- **Jump to page** for quick navigation

## 🎨 Animation Details

### Screen Transitions
- **Opening PDF**: Slide in from right (400ms)
- **Closing PDF**: Slide out to right (400ms)
- **Emphasized easing**: Professional Material Design curves

### Control Animations  
- **Top/Bottom bars**: Slide in/out with fade (300ms)
- **Night mode icon**: Smooth 180° rotation with bounce
- **Buttons**: Spring physics for natural feel

### Performance
- **48 frames** per transition at 120fps
- **No diagonal sweep bug** - Instant size transforms
- **Smooth scrolling** - Hardware accelerated lists

## 🔮 Planned Features
- [ ] Full text search implementation
- [ ] PDF annotations and highlighting
- [ ] Page thumbnails in viewer
- [ ] Export and share options
- [ ] Cloud storage integration
- [ ] Reading statistics and history

## 📄 License
This project is open source and available under the MIT License.

## 🤝 Contributing
Contributions, issues, and feature requests are welcome!

## 📧 Contact
For questions or feedback, please open an issue on GitHub.
- Night mode with inverted colors for PDFs
- Text selection and copying
- Annotation support
- Password-protected PDF support
- Print functionality

## License
This project is open source and available for personal and commercial use.

## Credits
Built with ❤️ using Kotlin and Jetpack Compose

