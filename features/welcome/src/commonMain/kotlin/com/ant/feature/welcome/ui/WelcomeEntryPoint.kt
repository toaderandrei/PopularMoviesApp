package com.ant.feature.welcome.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ant.feature.welcome.WelcomeViewModel
import org.koin.compose.viewmodel.koinViewModel


/**
 * Entry point composable for the Welcome feature.
 * Handles ViewModel injection and state management.
 */
@Composable
fun WelcomeEntryPoint(
    onNavigateToLogin: () -> Unit,
    onGuestModeActivated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.guestModeActivated) {
        if (uiState.guestModeActivated) {
            onGuestModeActivated()
        }
    }

    WelcomeScreen(
        uiState = uiState,
        onLoginClick = onNavigateToLogin,
        onContinueAsGuestClick = viewModel::continueAsGuest,
        modifier = modifier,
    )
}
