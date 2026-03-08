package com.ant.ui

import androidx.compose.ui.window.ComposeUIViewController
import com.ant.shared.ui.app.MainApp
import com.ant.ui.di.initKoinIos
import platform.UIKit.UIViewController

/**
 * Flag to ensure Koin is only initialized once.
 */
private var koinInitialized = false

/**
 * Creates the main UIViewController for iOS that displays the Compose Multiplatform app.
 * This is called from Swift code in the iOS app.
 *
 * IMPORTANT: Initializes Koin DI framework on first call before rendering Compose UI.
 */
fun MainViewController(): UIViewController {
    // Initialize Koin only once (idempotent)
    if (!koinInitialized) {
        initKoinIos()
        koinInitialized = true
    }

    return ComposeUIViewController {
        MainApp()
    }
}
