package com.ant.data.repositories.login

import com.ant.common.exceptions.NetworkError
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.network.dto.StatusResponseDto
import com.ant.network.ktx.safeResourceDelete
import com.ant.network.resources.AuthResources
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


/**
 * Logs the user out by deleting the remote TMDb session (best-effort)
 * and clearing all local session data.
 */
class LogoutUserAndClearSessionsRepository constructor(
    private val client: HttpClient,
    private val sessionManager: SessionManager,
) {
    /**
     * Performs logout: attempts remote session deletion, then clears local session.
     *
     * @return `true` when local session has been cleared (always succeeds).
     */
    suspend fun performRequest(params: RequestType.LoginSessionRequest.Logout): Boolean {
        val sessionId = sessionManager.getSessionId()
        if (!sessionId.isNullOrEmpty()) {
            try {
                client.safeResourceDelete<StatusResponseDto, AuthResources.DeleteSession>(
                    resource = AuthResources.DeleteSession(),
                    maxAttempts = 1,
                ) {
                    contentType(ContentType.Application.Json)
                    setBody(mapOf("session_id" to sessionId))
                }
            } catch (_: NetworkError) {
                // Best-effort remote session cleanup — network errors are expected
            } catch (_: Exception) {
                // Best-effort remote session cleanup — other exceptions
            }
        }
        sessionManager.clearSessionAndSignOut()
        return true
    }
}
