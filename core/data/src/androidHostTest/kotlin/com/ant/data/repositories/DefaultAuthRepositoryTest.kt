package com.ant.data.repositories

import com.ant.data.repositories.login.LoginUserTmDbRepository
import com.ant.data.repositories.login.LogoutUserAndClearSessionsRepository
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultAuthRepositoryTest {

    private val loginUserTmDbRepository = mockk<LoginUserTmDbRepository>()
    private val logoutUserAndClearSessionsRepository = mockk<LogoutUserAndClearSessionsRepository>()
    private val repository = DefaultAuthRepository(
        loginUserTmDbRepository,
        logoutUserAndClearSessionsRepository,
    )

    @Test
    fun `Should delegate login to LoginUserTmDbRepository`() = runTest {
        val credentials = RequestType.LoginSessionRequest.WithCredentials(
            username = "user",
            password = "pass",
        )
        val expected = UserData(username = "user", sessionId = "session_1")
        coEvery { loginUserTmDbRepository.performRequest(credentials) } returns expected

        val result = repository.login(credentials)

        assertEquals(expected, result)
        coVerify { loginUserTmDbRepository.performRequest(credentials) }
    }

    @Test
    fun `Should delegate logout to LogoutUserAndClearSessionsRepository`() = runTest {
        val logoutRequest = RequestType.LoginSessionRequest.Logout(username = "user")
        coEvery { logoutUserAndClearSessionsRepository.performRequest(logoutRequest) } returns true

        val result = repository.logout(logoutRequest)

        assertTrue(result)
        coVerify { logoutUserAndClearSessionsRepository.performRequest(logoutRequest) }
    }
}
