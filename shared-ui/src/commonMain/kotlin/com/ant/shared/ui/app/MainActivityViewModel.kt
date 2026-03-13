package com.ant.shared.ui.app

import kotlinx.coroutines.flow.StateFlow

interface MainActivityViewModel {
    val isUserLoggedIn: StateFlow<Boolean?>
}
