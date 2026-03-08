package com.ant.feature.welcome

import app.cash.turbine.test
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.models.entities.MovieData
import com.ant.models.model.PaginatedResult
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var movieListUseCase: MovieListUseCase
    private lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        movieListUseCase = mockk()
        sessionManager = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): WelcomeViewModel {
        return WelcomeViewModel(movieListUseCase, sessionManager)
    }

    @Test
    fun `Should emit loading state initially`() = runTest {
        every { movieListUseCase(any<RequestType.MovieRequest>()) } returns flowOf(Result.Loading)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isLoading)
            assertNull(state.backdropUrl)
            assertNull(state.movieTitle)
            assertFalse(state.guestModeActivated)
        }
    }

    @Test
    fun `Should load backdrop image on init`() = runTest {
        val movies = listOf(
            MovieData(id = 1, name = "Test Movie", backDropPath = "/test.jpg"),
        )
        val paginatedResult = PaginatedResult(items = movies, page = 1, totalPages = 1)
        every { movieListUseCase(any<RequestType.MovieRequest>()) } returns flowOf(
            Result.Loading,
            Result.Success(paginatedResult),
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("https://image.tmdb.org/t/p/w1280/test.jpg", state.backdropUrl)
            assertEquals("Test Movie", state.movieTitle)
        }
    }

    @Test
    fun `Should set backdrop URL from popular movie with backdrop path`() = runTest {
        val movies = listOf(
            MovieData(id = 1, name = "No Backdrop", backDropPath = null),
            MovieData(id = 2, name = "Has Backdrop", backDropPath = "/backdrop.jpg"),
        )
        val paginatedResult = PaginatedResult(items = movies, page = 1, totalPages = 1)
        every { movieListUseCase(any<RequestType.MovieRequest>()) } returns flowOf(
            Result.Success(paginatedResult),
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("https://image.tmdb.org/t/p/w1280/backdrop.jpg", state.backdropUrl)
            assertEquals("Has Backdrop", state.movieTitle)
        }
    }

    @Test
    fun `Should set movie title from selected movie`() = runTest {
        val movies = listOf(
            MovieData(id = 1, name = "The Best Movie", backDropPath = "/best.jpg"),
        )
        val paginatedResult = PaginatedResult(items = movies, page = 1, totalPages = 1)
        every { movieListUseCase(any<RequestType.MovieRequest>()) } returns flowOf(
            Result.Success(paginatedResult),
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("The Best Movie", state.movieTitle)
        }
    }

    @Test
    fun `Should handle empty movie list gracefully`() = runTest {
        val paginatedResult = PaginatedResult(items = emptyList<MovieData>(), page = 1, totalPages = 1)
        every { movieListUseCase(any<RequestType.MovieRequest>()) } returns flowOf(
            Result.Success(paginatedResult),
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNull(state.backdropUrl)
            assertNull(state.movieTitle)
        }
    }

    @Test
    fun `Should handle all movies without backdrop path`() = runTest {
        val movies = listOf(
            MovieData(id = 1, name = "Movie 1", backDropPath = null),
            MovieData(id = 2, name = "Movie 2", backDropPath = ""),
            MovieData(id = 3, name = "Movie 3", backDropPath = "   "),
        )
        val paginatedResult = PaginatedResult(items = movies, page = 1, totalPages = 1)
        every { movieListUseCase(any<RequestType.MovieRequest>()) } returns flowOf(
            Result.Success(paginatedResult),
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNull(state.backdropUrl)
            assertNull(state.movieTitle)
        }
    }

    @Test
    fun `Should handle error result gracefully`() = runTest {
        every { movieListUseCase(any<RequestType.MovieRequest>()) } returns flowOf(
            Result.Loading,
            Result.Error(RuntimeException("Network error")),
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNull(state.backdropUrl)
            assertNull(state.movieTitle)
        }
    }

    @Test
    fun `Should activate guest mode when continueAsGuest called`() = runTest {
        every { movieListUseCase(any<RequestType.MovieRequest>()) } returns flowOf(Result.Loading)
        coEvery { sessionManager.setGuestMode(true) } returns true

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.continueAsGuest()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.guestModeActivated)
        }
    }

    @Test
    fun `Should call sessionManager setGuestMode when continuing as guest`() = runTest {
        every { movieListUseCase(any<RequestType.MovieRequest>()) } returns flowOf(Result.Loading)
        coEvery { sessionManager.setGuestMode(true) } returns true

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.continueAsGuest()
        advanceUntilIdle()

        coVerify(exactly = 1) { sessionManager.setGuestMode(true) }
    }
}
