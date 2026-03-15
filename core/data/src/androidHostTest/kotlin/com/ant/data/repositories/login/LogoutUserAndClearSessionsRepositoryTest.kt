package com.ant.data.repositories.login

import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.network.dto.StatusResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertTrue

class LogoutUserAndClearSessionsRepositoryTest {

    private val sessionManager = mockk<SessionManager>(relaxed = true)
    private val json = Json { ignoreUnknownKeys = true }

    private val logoutRequest = RequestType.LoginSessionRequest.Logout(username = "testuser")

    private fun createClient(respondWithError: Boolean = false): HttpClient {
        val mockEngine = MockEngine { _ ->
            if (respondWithError) {
                respondError(HttpStatusCode.InternalServerError)
            } else {
                respond(
                    content = json.encodeToString(
                        StatusResponseDto.serializer(),
                        StatusResponseDto(statusCode = 1, statusMessage = "Success"),
                    ),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                )
            }
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) { json(json) }
            install(Resources)
        }
    }

    @Test
    fun `Should delete remote session and clear local when session exists`() = runTest {
        coEvery { sessionManager.getSessionId() } returns "active_session"
        val client = createClient()
        val repository = LogoutUserAndClearSessionsRepository(client, sessionManager)

        val result = repository.performRequest(logoutRequest)

        assertTrue(result)
        coVerify { sessionManager.clearSessionAndSignOut() }
    }

    @Test
    fun `Should skip remote deletion when no session exists`() = runTest {
        coEvery { sessionManager.getSessionId() } returns null
        val client = createClient()
        val repository = LogoutUserAndClearSessionsRepository(client, sessionManager)

        val result = repository.performRequest(logoutRequest)

        assertTrue(result)
        coVerify { sessionManager.clearSessionAndSignOut() }
    }

    @Test
    fun `Should skip remote deletion when session is empty`() = runTest {
        coEvery { sessionManager.getSessionId() } returns ""
        val client = createClient()
        val repository = LogoutUserAndClearSessionsRepository(client, sessionManager)

        val result = repository.performRequest(logoutRequest)

        assertTrue(result)
        coVerify { sessionManager.clearSessionAndSignOut() }
    }

    @Test
    fun `Should still clear local session when remote deletion throws`() = runTest {
        coEvery { sessionManager.getSessionId() } returns "active_session"
        val client = createClient(respondWithError = true)
        val repository = LogoutUserAndClearSessionsRepository(client, sessionManager)

        val result = repository.performRequest(logoutRequest)

        assertTrue(result)
        coVerify { sessionManager.clearSessionAndSignOut() }
    }

    @Test
    fun `Should always return true regardless of remote outcome`() = runTest {
        coEvery { sessionManager.getSessionId() } returns "session"
        val client = createClient(respondWithError = true)
        val repository = LogoutUserAndClearSessionsRepository(client, sessionManager)

        val result = repository.performRequest(logoutRequest)

        assertTrue(result)
    }
}
