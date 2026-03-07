package com.ant.models.model

/**
 * Represents the currently authenticated user's profile data.
 */
data class UserData(
    val username: String? = null,
    val sessionId: String? = null,
)