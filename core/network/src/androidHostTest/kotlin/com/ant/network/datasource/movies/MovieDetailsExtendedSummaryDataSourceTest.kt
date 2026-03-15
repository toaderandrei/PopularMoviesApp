package com.ant.network.datasource.movies

import com.ant.models.entities.MovieData
import com.ant.models.entities.MovieDetails
import com.ant.models.request.MovieAppendToResponseItem
import com.ant.models.request.RequestType
import com.ant.network.dto.MovieDto
import com.ant.network.mappers.movies.MovieDetailsMapper
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

class MovieDetailsExtendedSummaryDataSourceTest {

    private val movieDetailsMapper = mockk<MovieDetailsMapper>()
    private val movieDetails = MovieDetails(movieData = MovieData(id = 550, name = "Fight Club"))

    private val json = Json { ignoreUnknownKeys = true }

    private fun createClient(responseJson: String): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = responseJson,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) { json(json) }
            install(Resources)
        }
    }

    @Test
    fun `Should build append string from items`() = runTest {
        val movieDto = MovieDto(id = 550, title = "Fight Club")
        val client = createClient(json.encodeToString(MovieDto.serializer(), movieDto))
        val dataSource = MovieDetailsExtendedSummaryDataSource(client, movieDetailsMapper)
        coEvery { movieDetailsMapper.map(movieDto) } returns movieDetails

        val params = RequestType.MovieRequestDetails(
            tmdbMovieId = 550,
            appendToResponseItems = listOf(
                MovieAppendToResponseItem.CREDITS,
                MovieAppendToResponseItem.VIDEOS,
            ),
        )

        val result = dataSource(params)

        assertEquals(movieDetails, result)
    }

    @Test
    fun `Should pass null when no append items`() = runTest {
        val movieDto = MovieDto(id = 42)
        val client = createClient(json.encodeToString(MovieDto.serializer(), movieDto))
        val dataSource = MovieDetailsExtendedSummaryDataSource(client, movieDetailsMapper)
        coEvery { movieDetailsMapper.map(movieDto) } returns movieDetails

        val params = RequestType.MovieRequestDetails(
            tmdbMovieId = 42,
            appendToResponseItems = emptyList(),
        )

        val result = dataSource(params)

        assertEquals(movieDetails, result)
    }

    @Test
    fun `Should include all append to response items`() = runTest {
        val movieDto = MovieDto(id = 1)
        val client = createClient(json.encodeToString(MovieDto.serializer(), movieDto))
        val dataSource = MovieDetailsExtendedSummaryDataSource(client, movieDetailsMapper)
        coEvery { movieDetailsMapper.map(movieDto) } returns movieDetails

        val params = RequestType.MovieRequestDetails(
            tmdbMovieId = 1,
            appendToResponseItems = listOf(
                MovieAppendToResponseItem.CREDITS,
                MovieAppendToResponseItem.REVIEWS,
                MovieAppendToResponseItem.VIDEOS,
                MovieAppendToResponseItem.MOVIE_CREDITS,
            ),
        )

        val result = dataSource(params)

        assertEquals(movieDetails, result)
    }
}
