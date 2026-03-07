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

class DeleteMovieDetailsUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val movieRepository = mockk<MovieRepository>(relaxed = true)
    private val sessionManager = mockk<SessionManager>()
    private val favoriteRepository = mockk<FavoriteRepository>(relaxed = true)
    private val useCase = DeleteMovieDetailsUseCase(
        movieRepository, sessionManager, favoriteRepository, testDispatcher
    )

    private val movieDetails = MovieDetails(
        movieData = MovieData(id = 7, name = "Movie To Delete"),
    )

    @Test
    fun `Should emit Loading then Success when deleting locally`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns null

        useCase(movieDetails).test {
            assertIs<Result.Loading>(awaitItem())
            assertIs<Result.Success<Unit>>(awaitItem())
            awaitComplete()
        }

        coVerify { movieRepository.deleteMovieDetails(movieDetails) }
    }

    @Test
    fun `Should skip remote sync when no session exists`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns null

        useCase(movieDetails).test {
            assertIs<Result.Loading>(awaitItem())
            assertIs<Result.Success<Unit>>(awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 0) { favoriteRepository.syncFavoriteToRemote(any()) }
    }

    @Test
    fun `Should sync deletion to remote when session exists`() = runTest(testDispatcher) {
        coEvery { sessionManager.getSessionId() } returns "session456"
        coEvery { favoriteRepository.syncFavoriteToRemote(any()) } returns true

        useCase(movieDetails).test {
            assertIs<Result.Loading>(awaitItem())
            assertIs<Result.Success<Unit>>(awaitItem())
            awaitComplete()
        }

        coVerify {
            favoriteRepository.syncFavoriteToRemote(
                RequestType.FavoriteRequest(
                    sessionId = "session456",
                    favorite = false,
                    favoriteId = 7,
                    mediaType = FavoriteType.MOVIE,
                )
            )
        }
    }

    @Test
    fun `Should emit error when local delete throws`() = runTest(testDispatcher) {
        coEvery { movieRepository.deleteMovieDetails(movieDetails) } throws RuntimeException("DB error")
        coEvery { sessionManager.getSessionId() } returns null

        useCase(movieDetails).test {
            assertIs<Result.Loading>(awaitItem())
            val error = awaitItem()
            assertIs<Result.Error<Unit>>(error)
            awaitComplete()
        }
    }
}
