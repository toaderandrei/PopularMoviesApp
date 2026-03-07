package com.ant.shared.listeners

/** Callback for authentication actions on the login screen. */
interface LoginCallback {
    /** Initiates the login flow. */
    fun login()

    /** Initiates the logout flow. */
    fun logout()

    /** Navigates to the sign-up flow. */
    fun signUp()
}