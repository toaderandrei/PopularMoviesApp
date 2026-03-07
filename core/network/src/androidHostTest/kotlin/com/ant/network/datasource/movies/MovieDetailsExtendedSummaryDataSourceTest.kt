package com.ant.network.datasource.movies

import com.ant.models.entities.MovieData
import com.ant.models.entities.MovieDetails
import com.ant.models.request.MovieAppendToResponseItem
import com.ant.models.request.RequestType
import com.ant.network.api.TmdbMoviesApi
import com.ant.network.dto.MovieDto
import com.ant.network.mappers.movies.MovieDetailsMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieDetailsExtendedSummaryDataSourceTest {

    private val moviesApi = mockk<TmdbMoviesApi>()
    private val movieDetailsMapper = mockk<MovieDetailsMapper>()
    private val dataSource = MovieDetailsExtendedSummaryDataSource(moviesApi, movieDetailsMapper)

    private val movieDto = MovieDto(id = 550, title = "Fight Club")
    private val movieDetails = MovieDetails(movieData = MovieData(id = 550, name = "Fight Club"))

    @Test
    fun `Should build append string from items`() = runTest {
        val params = RequestType.MovieRequestDetails(
            tmdbMovieId = 550,
            appendToResponseItems = listOf(
                MovieAppendToResponseItem.CREDITS,
                MovieAppendToResponseItem.VIDEOS,
            ),
        )
        coEvery { moviesApi.getDetails(550, "credits,videos") } returns movieDto
        coEvery { movieDetailsMapper.map(movieDto) } returns movieDetails

        val result = dataSource(params)

        assertEquals(movieDetails, result)
        coVerify { moviesApi.getDetails(550, "credits,videos") }
    }

    @Test
    fun `Should pass null when no append items`() = runTest {
        val params = RequestType.MovieRequestDetails(
            tmdbMovieId = 42,
            appendToResponseItems = emptyList(),
        )
        coEvery { moviesApi.getDetails(42, null) } returns movieDto
        coEvery { movieDetailsMapper.map(movieDto) } returns movieDetails

        dataSource(params)

        coVerify { moviesApi.getDetails(42, null) }
    }

    @Test
    fun `Should include all append to response items`() = runTest {
        val params = RequestType.MovieRequestDetails(
            tmdbMovieId = 1,
            appendToResponseItems = listOf(
                MovieAppendToResponseItem.CREDITS,
                MovieAppendToResponseItem.REVIEWS,
                MovieAppendToResponseItem.VIDEOS,
                MovieAppendToResponseItem.MOVIE_CREDITS,
            ),
        )
        coEvery { moviesApi.getDetails(1, "credits,reviews,videos,movie_credits") } returns movieDto
        coEvery { movieDetailsMapper.map(movieDto) } returns movieDetails

        dataSource(params)

        coVerify { moviesApi.getDetails(1, "credits,reviews,videos,movie_credits") }
    }
}
