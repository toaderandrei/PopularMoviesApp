package com.ant.models.extensions

/**
 * iOS implementation: returns a generic error message.
 */
actual fun Throwable.toReadableError(): String {
    return "Unknown error"
}
