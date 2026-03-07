package com.ant.data.repositories.login

import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.network.api.TmdbAuthApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class LogoutUserAndClearSessionsRepositoryTest {

    private val authApi = mockk<TmdbAuthApi>(relaxed = true)
    private val sessionManager = mockk<SessionManager>(relaxed = true)
    private val repository = LogoutUserAndClearSessionsRepository(authApi, sessionManager)

    private val logoutRequest = RequestType.LoginSessionRequest.Logout(username = "testuser")

    @Test
    fun `Should delete remote session and clear local when session exists`() = runTest {
        coEvery { sessionManager.getSessionId() } returns "active_session"

        val result = repository.performRequest(logoutRequest)

        assertTrue(result)
        coVerify { authApi.deleteSession("active_session") }
        coVerify { sessionManager.clearSessionAndSignOut() }
    }

    @Test
    fun `Should skip remote deletion when no session exists`() = runTest {
        coEvery { sessionManager.getSessionId() } returns null

        val result = repository.performRequest(logoutRequest)

        assertTrue(result)
        coVerify(exactly = 0) { authApi.deleteSession(any()) }
        coVerify { sessionManager.clearSessionAndSignOut() }
    }

    @Test
    fun `Should skip remote deletion when session is empty`() = runTest {
        coEvery { sessionManager.getSessionId() } returns ""

        val result = repository.performRequest(logoutRequest)

        assertTrue(result)
        coVerify(exactly = 0) { authApi.deleteSession(any()) }
        coVerify { sessionManager.clearSessionAndSignOut() }
    }

    @Test
    fun `Should still clear local session when remote deletion throws`() = runTest {
        coEvery { sessionManager.getSessionId() } returns "active_session"
        coEvery { authApi.deleteSession("active_session") } throws RuntimeException("Network error")

        val result = repository.performRequest(logoutRequest)

        assertTrue(result)
        coVerify { sessionManager.clearSessionAndSignOut() }
    }

    @Test
    fun `Should always return true regardless of remote outcome`() = runTest {
        coEvery { sessionManager.getSessionId() } returns "session"
        coEvery { authApi.deleteSession(any()) } throws RuntimeException("Timeout")

        val result = repository.performRequest(logoutRequest)

        assertTrue(result)
    }
}
