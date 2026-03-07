package com.ant.common.listeners

/** Callback for account-level login and logout actions. */
interface AccountLoginCallback {
    /** Initiates the login flow. */
    fun login()

    /** Initiates the logout flow. */
    fun logout()
}