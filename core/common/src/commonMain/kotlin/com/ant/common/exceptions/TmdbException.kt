package com.ant.common.exceptions

/** Application-specific exception for TMDb API or domain-level errors. */
class TmdbException(override val message: String, errorCause: Throwable? = null) : RuntimeException(message, errorCause)