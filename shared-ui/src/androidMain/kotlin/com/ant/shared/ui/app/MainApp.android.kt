package com.ant.shared.ui.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.common.logger.Logger
import com.ant.models.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun provideMainActivityViewModel(): MainActivityViewModel {
    return koinViewModel<MainActivityViewModelImpl>()
}

class MainActivityViewModelImpl(
    logger: Logger,
    sessionManager: SessionManager,
) : ViewModel(), MainActivityViewModel {

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
