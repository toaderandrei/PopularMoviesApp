package com.ant.common.exceptions

sealed class NetworkError(
    message: String? = null,
    cause: Throwable? = null,
) : Throwable(message, cause) {

    data class Unauthorized(
        override val cause: Throwable? = null,
        override val message: String? = "Authentication required",
    ) : NetworkError(message, cause)

    data class ServerError(
        override val cause: Throwable? = null,
        override val message: String? = "Server error, please try again later",
    ) : NetworkError(message, cause)

    data class Serialization(
        override val cause: Throwable? = null,
    ) : NetworkError(cause?.message, cause)

    data class Unknown(
        override val cause: Throwable? = null,
        override val message: String? = "An unexpected error occurred",
    ) : NetworkError(message, cause)
}
