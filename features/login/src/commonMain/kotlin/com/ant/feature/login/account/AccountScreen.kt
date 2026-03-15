package com.ant.feature.login.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.collectAsState
import com.ant.resources.Res
import com.ant.resources.account
import com.ant.resources.account_is_logged_in_title
import com.ant.resources.image_login
import com.ant.resources.login
import com.ant.resources.login_title
import com.ant.resources.logout
import com.ant.resources.navigate_back
import com.ant.resources.user_login_avatar
import com.ant.resources.username_account
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AccountRoute(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.logoutSuccess) {
        if (uiState.logoutSuccess) {
            onNavigateBack()
        }
    }

    AccountScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToLogin = onNavigateToLogin,
        onLogout = viewModel::logout,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    uiState: AccountUiState,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.account)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.navigate_back),
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.isLoggedIn -> {
                    LoggedInContent(
                        username = uiState.username,
                        isLoggingOut = uiState.isLoggingOut,
                        onLogout = onLogout,
                    )
                }

                else -> {
                    NotLoggedInContent(
                        onNavigateToLogin = onNavigateToLogin,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoggedInContent(
    username: String?,
    isLoggingOut: Boolean,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.user_login_avatar),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.username_account, username ?: "User"),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.account_is_logged_in_title),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoggingOut,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
            ),
        ) {
            if (isLoggingOut) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onError,
                )
            } else {
                Text(stringResource(Res.string.logout))
            }
        }
    }
}

@Composable
private fun NotLoggedInContent(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.image_login),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.login_title),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(stringResource(Res.string.login))
        }
    }
}

@Preview
@Composable
private fun AccountScreenLoggedInPreview() {
    MaterialTheme {
        AccountScreen(
            uiState = AccountUiState(
                isLoading = false,
                isLoggedIn = true,
                username = "john_doe",
                sessionId = "abc123",
            ),
            onNavigateBack = {},
            onNavigateToLogin = {},
            onLogout = {},
        )
    }
}

@Preview
@Composable
private fun AccountScreenNotLoggedInPreview() {
    MaterialTheme {
        AccountScreen(
            uiState = AccountUiState(
                isLoading = false,
                isLoggedIn = false,
            ),
            onNavigateBack = {},
            onNavigateToLogin = {},
            onLogout = {},
        )
    }
}

@Preview
@Composable
private fun AccountScreenLoadingPreview() {
    MaterialTheme {
        AccountScreen(
            uiState = AccountUiState(isLoading = true),
            onNavigateBack = {},
            onNavigateToLogin = {},
            onLogout = {},
        )
    }
}
