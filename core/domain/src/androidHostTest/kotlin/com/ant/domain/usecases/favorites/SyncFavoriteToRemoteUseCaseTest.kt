package com.ant.domain.usecases.favorites

import app.cash.turbine.test
import com.ant.domain.repositories.FavoriteRepository
import com.ant.models.model.Result
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SyncFavoriteToRemoteUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val favoriteRepository = mockk<FavoriteRepository>(relaxed = true)
    private val sessionManager = mockk<SessionManager>()
    private val useCase = SyncFavoriteToRemoteUseCase(
        favoriteRepository, sessionManager, testDispatcher
    )

    @Test
    fun `Should emit error with IllegalStateException when no session exists`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns null

        useCase(1L, FavoriteType.MOVIE).test {
            assertIs<Result.Loading>(awaitItem())
            val error = awaitItem()
            assertIs<Result.Error<Boolean>>(error)
            assertIs<IllegalStateException>(error.throwable)
            assertEquals("Login required to sync favorites to TMDb", error.throwable.message)
            awaitComplete()
        }
    }

    @Test
    fun `Should sync movie and update status when session exists and sync succeeds`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns "session789"
        coEvery { favoriteRepository.syncFavoriteToRemote(any()) } returns true

        useCase(10L, FavoriteType.MOVIE).test {
            assertIs<Result.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<Result.Success<Boolean>>(success)
            assertTrue(success.data)
            awaitComplete()
        }

        coVerify {
            favoriteRepository.syncFavoriteToRemote(
                RequestType.FavoriteRequest(
                    sessionId = "session789",
                    favorite = true,
                    favoriteId = 10,
                    mediaType = FavoriteType.MOVIE,
                )
            )
        }
        coVerify {
            favoriteRepository.updateSyncStatus(
                id = 10L,
                mediaType = FavoriteType.MOVIE,
                synced = true,
            )
        }
    }

    @Test
    fun `Should not update sync status when remote sync returns false`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns "session789"
        coEvery { favoriteRepository.syncFavoriteToRemote(any()) } returns false

        useCase(5L, FavoriteType.TV).test {
            assertIs<Result.Loading>(awaitItem())
            assertIs<Result.Success<Boolean>>(awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 0) { favoriteRepository.updateSyncStatus(any(), any(), any()) }
    }

    @Test
    fun `Should pass correct media type for TV favorites`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns "session789"
        coEvery { favoriteRepository.syncFavoriteToRemote(any()) } returns true

        useCase(20L, FavoriteType.TV).test {
            assertIs<Result.Loading>(awaitItem())
            assertIs<Result.Success<Boolean>>(awaitItem())
            awaitComplete()
        }

        coVerify {
            favoriteRepository.syncFavoriteToRemote(
                RequestType.FavoriteRequest(
                    sessionId = "session789",
                    favorite = true,
                    favoriteId = 20,
                    mediaType = FavoriteType.TV,
                )
            )
        }
        coVerify {
            favoriteRepository.updateSyncStatus(
                id = 20L,
                mediaType = FavoriteType.TV,
                synced = true,
            )
        }
    }

    @Test
    fun `Should emit error when sync throws exception`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns "session789"
        coEvery { favoriteRepository.syncFavoriteToRemote(any()) } throws RuntimeException("API error")

        useCase(1L, FavoriteType.MOVIE).test {
            assertIs<Result.Loading>(awaitItem())
            val error = awaitItem()
            assertIs<Result.Error<Boolean>>(error)
            assertEquals("API error", error.throwable.message)
            awaitComplete()
        }
    }
}
