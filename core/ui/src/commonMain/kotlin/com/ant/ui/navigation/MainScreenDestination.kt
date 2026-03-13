package com.ant.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.ui.graphics.vector.ImageVector

object Graph {
    const val ROOT = "root"
    const val AUTHENTICATION = "account"
    const val MAIN  = "main"
}

enum class MainScreenDestination(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    MOVIES(
        route = "movies",
        title = "Movies",
        selectedIcon = Icons.Filled.Movie,
        unselectedIcon = Icons.Outlined.Movie
    ),
    TV_SHOW(
        route = "tvshow",
        title = "TV Shows",
        selectedIcon = Icons.Filled.Tv,
        unselectedIcon = Icons.Outlined.Tv
    ),
    FAVORITES(
        route = "favorites",
        title = "Favorites",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.Favorite
    ),
    SETTINGS(
        route = "settings",
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    );
}

enum class LoginScreenDestination(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    LOGIN(
        route = "login",
        title = "Login",
        selectedIcon = Icons.AutoMirrored.Filled.Login,
        unselectedIcon = Icons.AutoMirrored.Outlined.Login
    );
}