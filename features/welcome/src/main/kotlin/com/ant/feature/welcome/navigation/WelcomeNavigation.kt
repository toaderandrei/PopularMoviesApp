package com.ant.feature.welcome.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ant.feature.welcome.ui.WelcomeRoute

const val WELCOME_ROUTE = "welcome"

fun NavController.navigateToWelcome(navOptions: NavOptions? = null) {
    navigate(WELCOME_ROUTE, navOptions)
}

fun NavGraphBuilder.welcomeScreen(
    onNavigateToLogin: () -> Unit,
    onGuestModeActivated: () -> Unit,
) {
    composable(route = WELCOME_ROUTE) {
        WelcomeRoute(
            onNavigateToLogin = onNavigateToLogin,
            onGuestModeActivated = onGuestModeActivated,
        )
    }
}
