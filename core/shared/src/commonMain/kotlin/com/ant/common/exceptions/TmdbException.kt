package com.ant.shared.exceptions

/** Application-specific exception for TMDb API or domain-level errors. */
class TmdbException(override val message: String, errorCause: Throwable? = null) : RuntimeException(message, errorCause)