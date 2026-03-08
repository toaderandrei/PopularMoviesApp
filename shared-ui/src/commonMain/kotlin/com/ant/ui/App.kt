package com.ant.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.ant.feature.favorites.navigation.favoritesScreen
import com.ant.feature.login.navigation.LOGIN_ROUTE
import com.ant.feature.login.navigation.loginScreen
import com.ant.feature.login.navigation.navigateToLogin
import com.ant.feature.movies.navigation.movieCategoryScreen
import com.ant.feature.movies.navigation.movieDetailsScreen
import com.ant.feature.movies.navigation.moviesScreen
import com.ant.feature.movies.navigation.navigateToMovieCategory
import com.ant.feature.movies.navigation.navigateToMovieDetails
import com.ant.feature.search.navigation.searchScreen
import com.ant.feature.tvshow.navigation.navigateToTvShowCategory
import com.ant.feature.tvshow.navigation.navigateToTvShowDetails
import com.ant.feature.tvshow.navigation.tvShowCategoryScreen
import com.ant.feature.tvshow.navigation.tvShowDetailsScreen
import com.ant.feature.tvshow.navigation.tvShowScreen
import com.ant.feature.welcome.navigation.WELCOME_ROUTE
import com.ant.feature.welcome.navigation.welcomeScreen
import com.ant.ui.navigation.Graph
import com.ant.ui.navigation.MainScreenDestination

/**
 * Main application composable for KMP.
 * This is the entry point for both Android and iOS.
 */
@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { paddingValues ->
            // Skip authentication for now - directly show main content
            NavHost(
                navController = navController,
                startDestination = Graph.MAIN,
                route = Graph.ROOT,
            ) {
                // Nested graph for authentication (for future use)
                authGraph(navController)

                // Nested graph for main flow
                composable(Graph.MAIN) {
                    MainContent(
                        outerPadding = paddingValues,
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MainContent(
    outerPadding: PaddingValues,
    mainContentState: MainContentState = rememberMainContentState(),
) {
    val mainNavController: NavHostController = mainContentState.navController

    Scaffold { paddingValues ->
        NavHost(
            navController = mainNavController,
            startDestination = MainScreenDestination.MOVIES.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Movies feature
            moviesScreen(
                onNavigateToDetails = { mainNavController.navigateToMovieDetails(it) },
                onNavigateToCategory = { mainNavController.navigateToMovieCategory(it) },
            )
            movieDetailsScreen(
                onNavigateBack = { mainNavController.popBackStack() },
            )
            movieCategoryScreen(
                onNavigateToDetails = { mainNavController.navigateToMovieDetails(it) },
                onNavigateBack = { mainNavController.popBackStack() },
            )

            // TV Show feature
            tvShowScreen(
                onNavigateToDetails = { mainNavController.navigateToTvShowDetails(it) },
                onNavigateToCategory = { mainNavController.navigateToTvShowCategory(it) },
            )
            tvShowDetailsScreen(
                onNavigateBack = { mainNavController.popBackStack() },
            )
            tvShowCategoryScreen(
                onNavigateToDetails = { mainNavController.navigateToTvShowDetails(it) },
                onNavigateBack = { mainNavController.popBackStack() },
            )

            // Favorites feature
            favoritesScreen(
                onNavigateToMovieDetails = { mainNavController.navigateToMovieDetails(it) },
                onNavigateToTvShowDetails = { mainNavController.navigateToTvShowDetails(it) },
            )

            // Search feature
            searchScreen(
                onNavigateToMovieDetails = { mainNavController.navigateToMovieDetails(it) },
                onNavigateToTvShowDetails = { mainNavController.navigateToTvShowDetails(it) },
                onNavigateBack = { mainNavController.popBackStack() },
            )

            // Login screen (for re-authentication)
            loginScreen(
                onLoginSuccess = { mainNavController.popBackStack() },
                onNavigateBack = { mainNavController.popBackStack() },
            )

            // Settings screen placeholder
            composable(MainScreenDestination.SETTINGS.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Settings Screen - Coming Soon")
                }
            }
        }
    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = WELCOME_ROUTE,
        route = Graph.AUTHENTICATION,
    ) {
        welcomeScreen(
            onNavigateToLogin = {
                navController.navigateToLogin()
            },
            onGuestModeActivated = {
                navController.navigate(Graph.MAIN) {
                    popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                }
            },
        )
        loginScreen(
            onLoginSuccess = {
                navController.navigate(Graph.MAIN) {
                    popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                }
            },
            onNavigateBack = {
                navController.popBackStack()
            },
        )
    }
}
