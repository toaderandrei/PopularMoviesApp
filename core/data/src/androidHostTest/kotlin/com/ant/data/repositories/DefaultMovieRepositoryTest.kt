package com.ant.data.repositories

import com.ant.data.repositories.movies.DeleteMovieDetailsRepository
import com.ant.data.repositories.movies.LoadFavoredMovieListRepository
import com.ant.data.repositories.movies.LoadMovieDetailsSummaryRepository
import com.ant.data.repositories.movies.LoadMovieListRepository
import com.ant.data.repositories.movies.SaveMovieDetailsToLocalRepository
import com.ant.models.entities.MovieData
import com.ant.models.entities.MovieDetails
import com.ant.models.model.PaginatedResult
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultMovieRepositoryTest {

    private val loadMovieListRepository = mockk<LoadMovieListRepository>()
    private val loadMovieDetailsSummaryRepository = mockk<LoadMovieDetailsSummaryRepository>()
    private val saveMovieDetailsToLocalRepository = mockk<SaveMovieDetailsToLocalRepository>()
    private val deleteMovieDetailsRepository = mockk<DeleteMovieDetailsRepository>(relaxed = true)
    private val loadFavoredMovieListRepository = mockk<LoadFavoredMovieListRepository>()

    private val repository = DefaultMovieRepository(
        loadMovieListRepository,
        loadMovieDetailsSummaryRepository,
        saveMovieDetailsToLocalRepository,
        deleteMovieDetailsRepository,
        loadFavoredMovieListRepository,
    )

    @Test
    fun `Should delegate getMovieList to LoadMovieListRepository`() = runTest {
        val request = RequestType.MovieRequest(movieType = MovieType.POPULAR, page = 2)
        val expected = PaginatedResult(
            items = listOf(MovieData(id = 1, name = "Movie")),
            page = 2,
            totalPages = 5,
        )
        coEvery { loadMovieListRepository.performRequest(request) } returns expected

        val result = repository.getMovieList(request)

        assertEquals(expected, result)
        coVerify { loadMovieListRepository.performRequest(request) }
    }

    @Test
    fun `Should delegate getMovieDetails to LoadMovieDetailsSummaryRepository`() = runTest {
        val request = RequestType.MovieRequestDetails(tmdbMovieId = 42)
        val expected = MovieDetails(movieData = MovieData(id = 42, name = "Details"))
        coEvery { loadMovieDetailsSummaryRepository.performRequest(request) } returns expected

        val result = repository.getMovieDetails(request)

        assertEquals(expected, result)
        coVerify { loadMovieDetailsSummaryRepository.performRequest(request) }
    }

    @Test
    fun `Should delegate saveMovieDetails to SaveMovieDetailsToLocalRepository`() = runTest {
        val details = MovieDetails(movieData = MovieData(id = 1, name = "Save Me"))
        coEvery { saveMovieDetailsToLocalRepository.performRequest(details) } returns true

        val result = repository.saveMovieDetails(details)

        assertTrue(result)
        coVerify { saveMovieDetailsToLocalRepository.performRequest(details) }
    }

    @Test
    fun `Should delegate deleteMovieDetails to DeleteMovieDetailsRepository`() = runTest {
        val details = MovieDetails(movieData = MovieData(id = 1, name = "Delete Me"))

        repository.deleteMovieDetails(details)

        coVerify { deleteMovieDetailsRepository.performRequest(details) }
    }

    @Test
    fun `Should delegate getFavoredMovies to LoadFavoredMovieListRepository`() = runTest {
        val expected = listOf(
            MovieData(id = 1, name = "Fav 1"),
            MovieData(id = 2, name = "Fav 2"),
        )
        coEvery { loadFavoredMovieListRepository.performRequest(true) } returns expected

        val result = repository.getFavoredMovies(true)

        assertEquals(2, result.size)
        assertEquals("Fav 1", result[0].name)
        coVerify { loadFavoredMovieListRepository.performRequest(true) }
    }
}
