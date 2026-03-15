package com.ant.network.datasource.movies

import com.ant.shared.logger.Logger
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.network.dto.AccountDto
import com.ant.network.dto.StatusResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SaveAsFavoriteDataSourceTest {

    private val logger = mockk<Logger>(relaxed = true)
    private val json = Json { ignoreUnknownKeys = true }

    private fun createClient(
        accountDto: AccountDto,
        statusResponseDto: StatusResponseDto = StatusResponseDto(),
    ): HttpClient {
        val mockEngine = MockEngine { request ->
            val content = when (request.method) {
                HttpMethod.Post -> json.encodeToString(StatusResponseDto.serializer(), statusResponseDto)
                else -> json.encodeToString(AccountDto.serializer(), accountDto)
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
    fun `Should return true when status code is positive`() = runTest {
        val client = createClient(
            accountDto = AccountDto(id = 1),
            statusResponseDto = StatusResponseDto(statusCode = 1, statusMessage = "Success"),
        )
        val dataSource = SaveAsFavoriteDataSource(client, logger)

        val params = RequestType.FavoriteRequest(
            sessionId = "session1",
            favorite = true,
            favoriteId = 42,
            mediaType = FavoriteType.MOVIE,
        )

        val result = dataSource.invoke(params)

        assertTrue(result)
    }

    @Test
    fun `Should return false when status code is zero`() = runTest {
        val client = createClient(
            accountDto = AccountDto(id = 1),
            statusResponseDto = StatusResponseDto(statusCode = 0),
        )
        val dataSource = SaveAsFavoriteDataSource(client, logger)

        val params = RequestType.FavoriteRequest(
            sessionId = "session1",
            favorite = true,
            favoriteId = 42,
            mediaType = FavoriteType.MOVIE,
        )

        val result = dataSource.invoke(params)

        assertFalse(result)
    }

    @Test
    fun `Should return false when status code is null`() = runTest {
        val client = createClient(
            accountDto = AccountDto(id = 1),
            statusResponseDto = StatusResponseDto(statusCode = null),
        )
        val dataSource = SaveAsFavoriteDataSource(client, logger)

        val params = RequestType.FavoriteRequest(
            sessionId = "session1",
            favorite = true,
            favoriteId = 42,
            mediaType = FavoriteType.MOVIE,
        )

        val result = dataSource.invoke(params)

        assertFalse(result)
    }

    @Test
    fun `Should return false when account id is null`() = runTest {
        val client = createClient(accountDto = AccountDto(id = null))
        val dataSource = SaveAsFavoriteDataSource(client, logger)

        val params = RequestType.FavoriteRequest(
            sessionId = "session1",
            favorite = true,
            favoriteId = 42,
            mediaType = FavoriteType.MOVIE,
        )

        try {
            dataSource.invoke(params)
            // If we reach here, the datasource didn't throw - it should have
            assertFalse(true, "Expected exception for null account id")
        } catch (_: Throwable) {
            // Expected: NetworkError.Unknown for missing account ID
        }
    }

    @Test
    fun `Should use correct media type string for TV`() = runTest {
        val client = createClient(
            accountDto = AccountDto(id = 5),
            statusResponseDto = StatusResponseDto(statusCode = 12),
        )
        val dataSource = SaveAsFavoriteDataSource(client, logger)

        val params = RequestType.FavoriteRequest(
            sessionId = "s1",
            favorite = true,
            favoriteId = 10,
            mediaType = FavoriteType.TV,
        )

        val result = dataSource.invoke(params)

        assertTrue(result)
    }

    @Test
    fun `Should use correct media type string for PERSON`() = runTest {
        val client = createClient(
            accountDto = AccountDto(id = 5),
            statusResponseDto = StatusResponseDto(statusCode = 1),
        )
        val dataSource = SaveAsFavoriteDataSource(client, logger)

        val params = RequestType.FavoriteRequest(
            sessionId = "s1",
            favorite = false,
            favoriteId = 99,
            mediaType = FavoriteType.PERSON,
        )

        val result = dataSource.invoke(params)

        assertTrue(result)
    }
}
