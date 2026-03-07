package com.ant.data.repositories.login

import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.network.api.TmdbAuthApi


/**
 * Logs the user out by deleting the remote TMDb session (best-effort)
 * and clearing all local session data.
 */
class LogoutUserAndClearSessionsRepository constructor(
    private val authApi: TmdbAuthApi,
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
                authApi.deleteSession(sessionId)
            } catch (_: Exception) {
                // Best-effort remote session cleanup
            }
        }
        sessionManager.clearSessionAndSignOut()
        return true
    }
}
