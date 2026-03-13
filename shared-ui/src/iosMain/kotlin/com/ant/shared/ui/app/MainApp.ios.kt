package com.ant.shared.ui.app

import androidx.compose.runtime.Composable
import com.ant.common.logger.Logger
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
actual fun provideMainActivityViewModel(): MainActivityViewModel {
    val logger = koinInject<Logger>()
    val sessionManager = koinInject<SessionManager>()
    return remember { MainActivityViewModelImpl(logger, sessionManager) }
}

private class MainActivityViewModelImpl(
    logger: Logger,
    sessionManager: SessionManager,
) : MainActivityViewModel {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    override val isUserLoggedIn: StateFlow<Boolean?> = _isUserLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            logger.d("Checking authentication status.")
            sessionManager.canSkipAuthentication()
                .filterNotNull()
                .collect { status ->
                    logger.d("User login status: $status")
                    _isUserLoggedIn.value = status
                }
        }
    }
}

@Composable
private fun <T> remember(calculation: () -> T): T {
    return androidx.compose.runtime.remember { calculation() }
}
