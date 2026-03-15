package com.ant.network.datasource.movies

import com.ant.models.entities.MovieData
import com.ant.models.model.PaginatedResult
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.MovieDto
import com.ant.network.dto.MovieResultsPageDto
import com.ant.network.mappers.movies.MoviesListMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieListDataSourceTest {

    private val moviesListMapper = mockk<MoviesListMapper>()
    private val json = Json { ignoreUnknownKeys = true }

    private val pageDto = MovieResultsPageDto(page = 1, results = listOf(MovieDto(id = 1)))
    private val genres = GenreResultsDto(genres = emptyList())
    private val mappedResult = PaginatedResult(
        items = listOf(MovieData(id = 1)),
        page = 1,
        totalPages = 1,
    )

    private var requestCount = 0

    private fun createClient(): HttpClient {
        requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            val path = request.url.encodedPath
            val content = when {
                path.contains("genre") -> json.encodeToString(GenreResultsDto.serializer(), genres)
                else -> json.encodeToString(MovieResultsPageDto.serializer(), pageDto)
            }
            respond(
                content = content,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) { json(json) }
            install(Resources)
        }
    }

    @Test
    fun `Should call getPopular for POPULAR type`() = runTest {
        val client = createClient()
        val dataSource = MovieListDataSource(client, moviesListMapper)
        coEvery { moviesListMapper.map(any()) } returns mappedResult

        val result = dataSource(RequestType.MovieRequest(movieType = MovieType.POPULAR, page = 1))

        assertEquals(mappedResult, result)
    }

    @Test
    fun `Should call getTopRated for TOP_RATED type`() = runTest {
        val client = createClient()
        val dataSource = MovieListDataSource(client, moviesListMapper)
        coEvery { moviesListMapper.map(any()) } returns mappedResult

        val result = dataSource(RequestType.MovieRequest(movieType = MovieType.TOP_RATED, page = 2))

        assertEquals(mappedResult, result)
    }

    @Test
    fun `Should call getNowPlaying for NOW_PLAYING type`() = runTest {
        val client = createClient()
        val dataSource = MovieListDataSource(client, moviesListMapper)
        coEvery { moviesListMapper.map(any()) } returns mappedResult

        val result = dataSource(RequestType.MovieRequest(movieType = MovieType.NOW_PLAYING, page = 1))

        assertEquals(mappedResult, result)
    }

    @Test
    fun `Should call getUpcoming for UPCOMING type`() = runTest {
        val client = createClient()
        val dataSource = MovieListDataSource(client, moviesListMapper)
        coEvery { moviesListMapper.map(any()) } returns mappedResult

        val result = dataSource(RequestType.MovieRequest(movieType = MovieType.UPCOMING, page = 1))

        assertEquals(mappedResult, result)
    }

    @Test
    fun `Should fetch genres and pass to mapper`() = runTest {
        val client = createClient()
        val dataSource = MovieListDataSource(client, moviesListMapper)
        coEvery { moviesListMapper.map(Pair(pageDto, genres)) } returns mappedResult

        val result = dataSource(RequestType.MovieRequest(movieType = MovieType.POPULAR, page = 1))

        assertEquals(mappedResult, result)
    }
}
