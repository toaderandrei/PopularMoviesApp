package com.ant.shared.exceptions

/**
 * Represents an error response from a network or domain operation.
 *
 * @param statusCode HTTP status code, or 0 if not applicable.
 * @param e The underlying throwable that caused the error.
 * @param statusMessage Optional human-readable error message from the API.
 */
data class Error(
    val statusCode: Int = 0,
    val e: Throwable,
    val statusMessage: String? = null
)