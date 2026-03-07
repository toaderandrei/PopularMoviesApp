package com.ant.models.extensions

import java.net.UnknownHostException

/**
 * Android implementation: maps known exceptions (e.g., [UnknownHostException]) to user-friendly messages.
 */
actual fun Throwable.toReadableError(): String {
    if (this is UnknownHostException) {
        return "No internet connection or host is down"
    }
    return "Unknown error"
}
