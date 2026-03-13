package com.ant.feature.welcome.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ant.feature.welcome.ui.WelcomeEntryPoint

/**
 * Navigation setup for the Welcome feature.
 *
 * Supports both Navigation 2.x (string-based) and Navigation 3 (type-safe).
 * The Navigation 3 route is defined in WelcomeRoute.kt as a @Serializable data object.
 */

// Navigation 2.x - String-based route (for backwards compatibility)
const val WELCOME_ROUTE = "welcome"

fun NavController.navigateToWelcome(navOptions: NavOptions? = null) {
    navigate(WELCOME_ROUTE, navOptions)
}

fun NavGraphBuilder.welcomeScreen(
    onNavigateToLogin: () -> Unit,
    onGuestModeActivated: () -> Unit,
) {
    composable(route = WELCOME_ROUTE) {
        WelcomeEntryPoint(
            onNavigateToLogin = onNavigateToLogin,
            onGuestModeActivated = onGuestModeActivated,
        )
    }
}
