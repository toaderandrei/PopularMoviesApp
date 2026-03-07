package com.ant.domain.usecases.movies

import app.cash.turbine.test
import com.ant.domain.repositories.FavoriteRepository
import com.ant.domain.repositories.MovieRepository
import com.ant.models.entities.MovieData
import com.ant.models.entities.MovieDetails
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
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SaveMovieDetailsUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val movieRepository = mockk<MovieRepository>()
    private val favoriteRepository = mockk<FavoriteRepository>(relaxed = true)
    private val sessionManager = mockk<SessionManager>()
    private val useCase = SaveMovieDetailsUseCase(
        movieRepository, favoriteRepository, sessionManager, testDispatcher
    )

    private val movieDetails = MovieDetails(
        movieData = MovieData(id = 42, name = "Test Movie"),
    )

    @Test
    fun `Should emit Loading then Success when saving locally`() = runTest(testDispatcher) {
        coEvery { movieRepository.saveMovieDetails(movieDetails) } returns true
        coEvery { sessionManager.getSessionId() } returns null

        useCase(movieDetails).test {
            assertIs<Result.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<Result.Success<Boolean>>(success)
            assertTrue(success.data)
            awaitComplete()
        }
    }

    @Test
    fun `Should skip remote sync when no session exists`() = runTest(testDispatcher) {
        coEvery { movieRepository.saveMovieDetails(movieDetails) } returns true
        coEvery { sessionManager.getSessionId() } returns null

        useCase(movieDetails).test {
            assertIs<Result.Loading>(awaitItem())
            assertIs<Result.Success<Boolean>>(awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 0) { favoriteRepository.syncFavoriteToRemote(any()) }
    }

    @Test
    fun `Should sync to remote and update status when session exists and sync succeeds`() = runTest(testDispatcher) {
        coEvery { movieRepository.saveMovieDetails(movieDetails) } returns true
        coEvery { sessionManager.getSessionId() } returns "session123"
        coEvery { favoriteRepository.syncFavoriteToRemote(any()) } returns true
        coEvery { favoriteRepository.updateSyncStatus(any(), any(), any()) } returns Unit

        useCase(movieDetails).test {
            assertIs<Result.Loading>(awaitItem())
            assertIs<Result.Success<Boolean>>(awaitItem())
            awaitComplete()
        }

        coVerify {
            favoriteRepository.syncFavoriteToRemote(
                RequestType.FavoriteRequest(
                    sessionId = "session123",
                    favorite = true,
                    favoriteId = 42,
                    mediaType = FavoriteType.MOVIE,
                )
            )
        }
        coVerify {
            favoriteRepository.updateSyncStatus(
                id = 42,
                mediaType = FavoriteType.MOVIE,
                synced = true,
            )
        }
    }

    @Test
    fun `Should not update sync status when remote sync returns false`() = runTest(testDispatcher) {
        coEvery { movieRepository.saveMovieDetails(movieDetails) } returns true
        coEvery { sessionManager.getSessionId() } returns "session123"
        coEvery { favoriteRepository.syncFavoriteToRemote(any()) } returns false

        useCase(movieDetails).test {
            assertIs<Result.Loading>(awaitItem())
            assertIs<Result.Success<Boolean>>(awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 0) { favoriteRepository.updateSyncStatus(any(), any(), any()) }
    }

    @Test
    fun `Should still succeed locally when remote sync throws exception`() = runTest(testDispatcher) {
        coEvery { movieRepository.saveMovieDetails(movieDetails) } returns true
        coEvery { sessionManager.getSessionId() } returns "session123"
        coEvery { favoriteRepository.syncFavoriteToRemote(any()) } throws RuntimeException("Remote error")

        useCase(movieDetails).test {
            assertIs<Result.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<Result.Success<Boolean>>(success)
            assertTrue(success.data)
            awaitComplete()
        }
    }

    @Test
    fun `Should emit error when local save throws`() = runTest(testDispatcher) {
        coEvery { movieRepository.saveMovieDetails(movieDetails) } throws RuntimeException("DB error")
        coEvery { sessionManager.getSessionId() } returns null

        useCase(movieDetails).test {
            assertIs<Result.Loading>(awaitItem())
            val error = awaitItem()
            assertIs<Result.Error<Boolean>>(error)
            awaitComplete()
        }
    }
}
