package com.ant.domain.usecases.login

import app.cash.turbine.test
import com.ant.models.model.Result
import com.ant.models.model.UserData
import com.ant.models.session.SessionManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class LoadAccountProfileUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val sessionManager = mockk<SessionManager>()
    private val useCase = LoadAccountProfileUseCase(testDispatcher, sessionManager)

    @Test
    fun `Should emit Loading then Success with user data`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns "session_abc"
        coEvery { sessionManager.getUsername() } returns "john_doe"

        useCase(Unit).test {
            assertIs<Result.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<Result.Success<UserData>>(success)
            assertEquals("john_doe", success.data.username)
            assertEquals("session_abc", success.data.sessionId)
            awaitComplete()
        }
    }

    @Test
    fun `Should return null values when no session exists`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns null
        coEvery { sessionManager.getUsername() } returns null

        useCase(Unit).test {
            assertIs<Result.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<Result.Success<UserData>>(success)
            assertNull(success.data.username)
            assertNull(success.data.sessionId)
            awaitComplete()
        }
    }

    @Test
    fun `Should return username without session when partially logged out`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns null
        coEvery { sessionManager.getUsername() } returns "cached_user"

        useCase(Unit).test {
            assertIs<Result.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<Result.Success<UserData>>(success)
            assertEquals("cached_user", success.data.username)
            assertNull(success.data.sessionId)
            awaitComplete()
        }
    }

    @Test
    fun `Should emit error when session manager throws`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } throws RuntimeException("DataStore error")

        useCase(Unit).test {
            assertIs<Result.Loading>(awaitItem())
            val error = awaitItem()
            assertIs<Result.Error<UserData>>(error)
            assertEquals("DataStore error", error.throwable.message)
            awaitComplete()
        }
    }
}
