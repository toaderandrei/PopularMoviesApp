package com.ant.shared.listeners

/** Callback for retrying a failed operation. */
interface RetryCallback {
    /** Retries the last failed operation. */
    fun retry()
}