package com.ant.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** TMDb request token response. */
@Serializable
data class RequestTokenDto(
    val success: Boolean? = null,
    @SerialName("expires_at") val expiresAt: String? = null,
    @SerialName("request_token") val requestToken: String? = null,
)

/** TMDb session creation response. */
@Serializable
data class SessionDto(
    val success: Boolean? = null,
    @SerialName("session_id") val sessionId: String? = null,
)

/** TMDb user account details response. */
@Serializable
data class AccountDto(
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
)

/** Request body for marking/unmarking a media item as favorite. */
@Serializable
data class FavoriteRequestBody(
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
    val favorite: Boolean,
)

/** Generic TMDb status response containing a code and message. */
@Serializable
data class StatusResponseDto(
    @SerialName("status_code") val statusCode: Int? = null,
    @SerialName("status_message") val statusMessage: String? = null,
)

/** Request body for deleting (logging out) a session. */
@Serializable
data class DeleteSessionBody(
    @SerialName("session_id") val sessionId: String,
)

/** Request body for validating a request token with user credentials. */
@Serializable
data class ValidateTokenBody(
    val username: String,
    val password: String,
    @SerialName("request_token") val requestToken: String,
)

/** Request body for creating a session from a validated request token. */
@Serializable
data class CreateSessionBody(
    @SerialName("request_token") val requestToken: String,
)
