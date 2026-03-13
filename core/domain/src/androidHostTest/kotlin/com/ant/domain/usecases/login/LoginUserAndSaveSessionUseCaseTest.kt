package com.ant.domain.usecases.login

import app.cash.turbine.test
import com.ant.shared.logger.Logger
import com.ant.models.model.Result
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LoginUserAndSaveSessionUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val logger = mockk<Logger>(relaxed = true)
    private val sessionManager = mockk<SessionManager>(relaxed = true)
    private val loginUserToTmDbUseCase = mockk<LoginUserToTmDbUseCase>()
    private val useCase = LoginUserAndSaveSessionUseCase(
        logger, loginUserToTmDbUseCase, sessionManager, testDispatcher
    )

    private val credentials = RequestType.LoginSessionRequest.WithCredentials(
        username = "testuser",
        password = "password123",
    )

    @Test
    fun `Should return immediately when session already exists`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns "existing_session"

        useCase(credentials).test {
            assertIs<Result.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<Result.Success<UserData>>(success)
            assertEquals("testuser", success.data.username)
            awaitComplete()
        }

        coVerify(exactly = 0) { loginUserToTmDbUseCase(any()) }
    }

    @Test
    fun `Should authenticate and save session when no session exists`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns null
        val userData = UserData(username = "testuser", sessionId = "new_session")
        every { loginUserToTmDbUseCase(credentials) } returns flowOf(
            Result.Loading,
            Result.Success(userData),
        )

        useCase(credentials).test {
            assertIs<Result.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<Result.Success<UserData>>(success)
            assertEquals("testuser", success.data.username)
            assertEquals("new_session", success.data.sessionId)
            awaitComplete()
        }

        coVerify { sessionManager.saveSessionId("new_session") }
        coVerify { sessionManager.saveUsername("testuser") }
    }

    @Test
    fun `Should authenticate when session is empty string`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns ""
        val userData = UserData(username = "testuser", sessionId = "fresh_session")
        every { loginUserToTmDbUseCase(credentials) } returns flowOf(
            Result.Loading,
            Result.Success(userData),
        )

        useCase(credentials).test {
            assertIs<Result.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<Result.Success<UserData>>(success)
            assertEquals("fresh_session", success.data.sessionId)
            awaitComplete()
        }
    }

    @Test
    fun `Should emit error when authentication fails`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns null
        val authError = RuntimeException("Invalid credentials")
        every { loginUserToTmDbUseCase(credentials) } returns flowOf(
            Result.Loading,
            Result.Error(authError),
        )

        useCase(credentials).test {
            assertIs<Result.Loading>(awaitItem())
            val error = awaitItem()
            assertIs<Result.Error<UserData>>(error)
            assertEquals("Invalid credentials", error.throwable.message)
            awaitComplete()
        }
    }

    @Test
    fun `Should not save session when session ID is null in response`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns null
        val userData = UserData(username = "testuser", sessionId = null)
        every { loginUserToTmDbUseCase(credentials) } returns flowOf(
            Result.Loading,
            Result.Success(userData),
        )

        useCase(credentials).test {
            assertIs<Result.Loading>(awaitItem())
            assertIs<Result.Success<UserData>>(awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 0) { sessionManager.saveSessionId(any()) }
        coVerify(exactly = 0) { sessionManager.saveUsername(any()) }
    }
}
