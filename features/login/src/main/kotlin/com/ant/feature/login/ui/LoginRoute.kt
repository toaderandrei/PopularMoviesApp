package com.ant.feature.login.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.feature.login.LoginViewModel
import com.ant.feature.login.state.LoginState

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val loginState by viewModel.loginStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onLoginSuccess()
        }
    }

    LoginScreen(
        loginState = loginState,
        onLogin = viewModel::login,
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}
