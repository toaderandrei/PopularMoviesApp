package com.ant.domain.usecases.movies

import app.cash.turbine.test
import com.ant.domain.repositories.MovieRepository
import com.ant.models.entities.MovieData
import com.ant.models.model.PaginatedResult
import com.ant.models.model.Result
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class MovieListUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val movieRepository = mockk<MovieRepository>()
    private val useCase = MovieListUseCase(movieRepository, testDispatcher)

    private val request = RequestType.MovieRequest(movieType = MovieType.POPULAR, page = 1)

    @Test
    fun `Should emit Loading then Success with movie list`() = runTest(testDispatcher) {
        val movies = listOf(
            MovieData(id = 1, name = "Movie 1"),
            MovieData(id = 2, name = "Movie 2"),
        )
        val expected = PaginatedResult(items = movies, page = 1, totalPages = 5)
        coEvery { movieRepository.getMovieList(request) } returns expected

        useCase(request).test {
            assertIs<Result.Loading>(awaitItem())
            val success = awaitItem()
            assertIs<Result.Success<PaginatedResult<MovieData>>>(success)
            assertEquals(2, success.data.items.size)
            assertEquals("Movie 1", success.data.items[0].name)
            assertEquals(5, success.data.totalPages)
            awaitComplete()
        }
    }

    @Test
    fun `Should emit Loading then Error when repository throws`() = runTest(testDispatcher) {
        coEvery { movieRepository.getMovieList(request) } throws RuntimeException("Network error")

        useCase(request).test {
            assertIs<Result.Loading>(awaitItem())
            val error = awaitItem()
            assertIs<Result.Error<PaginatedResult<MovieData>>>(error)
            assertEquals("Network error", error.throwable.message)
            awaitComplete()
        }
    }

    @Test
    fun `Should pass correct request parameters to repository`() = runTest(testDispatcher) {
        val specificRequest = RequestType.MovieRequest(movieType = MovieType.TOP_RATED, page = 3)
        val expected = PaginatedResult(items = emptyList<MovieData>(), page = 3, totalPages = 10)
        coEvery { movieRepository.getMovieList(specificRequest) } returns expected

        useCase(specificRequest).test {
            assertIs<Result.Loading>(awaitItem())
            assertIs<Result.Success<PaginatedResult<MovieData>>>(awaitItem())
            awaitComplete()
        }

        coVerify { movieRepository.getMovieList(specificRequest) }
    }
}
