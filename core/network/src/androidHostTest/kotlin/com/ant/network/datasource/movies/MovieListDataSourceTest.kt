package com.ant.network.datasource.movies

import com.ant.models.entities.MovieData
import com.ant.models.model.PaginatedResult
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import com.ant.network.api.TmdbGenreApi
import com.ant.network.api.TmdbMoviesApi
import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.MovieDto
import com.ant.network.dto.MovieResultsPageDto
import com.ant.network.mappers.movies.MoviesListMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieListDataSourceTest {

    private val moviesApi = mockk<TmdbMoviesApi>()
    private val genreApi = mockk<TmdbGenreApi>()
    private val moviesListMapper = mockk<MoviesListMapper>()
    private val dataSource = MovieListDataSource(moviesApi, genreApi, moviesListMapper)

    private val genres = GenreResultsDto(genres = emptyList())
    private val pageDto = MovieResultsPageDto(page = 1, results = listOf(MovieDto(id = 1)))
    private val mappedResult = PaginatedResult(
        items = listOf(MovieData(id = 1)),
        page = 1,
        totalPages = 1,
    )

    @Test
    fun `Should call getPopular for POPULAR type`() = runTest {
        coEvery { moviesApi.getPopular(1) } returns pageDto
        coEvery { genreApi.getMovieGenres() } returns genres
        coEvery { moviesListMapper.map(any()) } returns mappedResult

        dataSource(RequestType.MovieRequest(movieType = MovieType.POPULAR, page = 1))

        coVerify { moviesApi.getPopular(1) }
    }

    @Test
    fun `Should call getTopRated for TOP_RATED type`() = runTest {
        coEvery { moviesApi.getTopRated(2) } returns pageDto
        coEvery { genreApi.getMovieGenres() } returns genres
        coEvery { moviesListMapper.map(any()) } returns mappedResult

        dataSource(RequestType.MovieRequest(movieType = MovieType.TOP_RATED, page = 2))

        coVerify { moviesApi.getTopRated(2) }
    }

    @Test
    fun `Should call getNowPlaying for NOW_PLAYING type`() = runTest {
        coEvery { moviesApi.getNowPlaying(1) } returns pageDto
        coEvery { genreApi.getMovieGenres() } returns genres
        coEvery { moviesListMapper.map(any()) } returns mappedResult

        dataSource(RequestType.MovieRequest(movieType = MovieType.NOW_PLAYING, page = 1))

        coVerify { moviesApi.getNowPlaying(1) }
    }

    @Test
    fun `Should call getUpcoming for UPCOMING type`() = runTest {
        coEvery { moviesApi.getUpcoming(1) } returns pageDto
        coEvery { genreApi.getMovieGenres() } returns genres
        coEvery { moviesListMapper.map(any()) } returns mappedResult

        dataSource(RequestType.MovieRequest(movieType = MovieType.UPCOMING, page = 1))

        coVerify { moviesApi.getUpcoming(1) }
    }

    @Test
    fun `Should fetch genres and pass to mapper`() = runTest {
        coEvery { moviesApi.getPopular(1) } returns pageDto
        coEvery { genreApi.getMovieGenres() } returns genres
        coEvery { moviesListMapper.map(Pair(pageDto, genres)) } returns mappedResult

        val result = dataSource(RequestType.MovieRequest(movieType = MovieType.POPULAR, page = 1))

        assertEquals(mappedResult, result)
        coVerify { genreApi.getMovieGenres() }
        coVerify { moviesListMapper.map(Pair(pageDto, genres)) }
    }
}
