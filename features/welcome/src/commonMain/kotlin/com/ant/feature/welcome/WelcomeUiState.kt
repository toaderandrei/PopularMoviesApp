package com.ant.feature.welcome

data class WelcomeUiState(
    val backdropUrl: String? = null,
    val movieTitle: String? = null,
    val isLoading: Boolean = true,
    val guestModeActivated: Boolean = false,
)
