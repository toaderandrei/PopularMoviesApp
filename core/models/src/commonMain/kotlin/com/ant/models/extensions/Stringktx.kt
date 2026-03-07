package com.ant.models.extensions

/**
 * Converts this [Throwable] into a user-friendly error message.
 * Platform-specific implementations may provide more detailed messages for known error types.
 */
expect fun Throwable.toReadableError(): String
