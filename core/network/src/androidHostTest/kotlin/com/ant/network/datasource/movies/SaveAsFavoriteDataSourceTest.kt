package com.ant.network.datasource.movies

import com.ant.shared.logger.Logger
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.network.api.TmdbAuthApi
import com.ant.network.dto.AccountDto
import com.ant.network.dto.StatusResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SaveAsFavoriteDataSourceTest {

    private val authApi = mockk<TmdbAuthApi>()
    private val logger = mockk<Logger>(relaxed = true)
    private val dataSource = SaveAsFavoriteDataSource(authApi, logger)

    @Test
    fun `Should return true when status code is positive`() = runTest {
        val params = RequestType.FavoriteRequest(
            sessionId = "session1",
            favorite = true,
            favoriteId = 42,
            mediaType = FavoriteType.MOVIE,
        )
        coEvery { authApi.getAccountDetails("session1") } returns AccountDto(id = 1)
        coEvery {
            authApi.markAsFavorite(
                accountId = 1,
                sessionId = "session1",
                mediaType = "movie",
                mediaId = 42,
                favorite = true,
            )
        } returns StatusResponseDto(statusCode = 1, statusMessage = "Success")

        val result = dataSource.invoke(params)

        assertTrue(result)
    }

    @Test
    fun `Should return false when status code is zero`() = runTest {
        val params = RequestType.FavoriteRequest(
            sessionId = "session1",
            favorite = true,
            favoriteId = 42,
            mediaType = FavoriteType.MOVIE,
        )
        coEvery { authApi.getAccountDetails("session1") } returns AccountDto(id = 1)
        coEvery {
            authApi.markAsFavorite(any(), any(), any(), any(), any())
        } returns StatusResponseDto(statusCode = 0)

        val result = dataSource.invoke(params)

        assertFalse(result)
    }

    @Test
    fun `Should return false when status code is null`() = runTest {
        val params = RequestType.FavoriteRequest(
            sessionId = "session1",
            favorite = true,
            favoriteId = 42,
            mediaType = FavoriteType.MOVIE,
        )
        coEvery { authApi.getAccountDetails("session1") } returns AccountDto(id = 1)
        coEvery {
            authApi.markAsFavorite(any(), any(), any(), any(), any())
        } returns StatusResponseDto(statusCode = null)

        val result = dataSource.invoke(params)

        assertFalse(result)
    }

    @Test
    fun `Should return false when account id is null`() = runTest {
        val params = RequestType.FavoriteRequest(
            sessionId = "session1",
            favorite = true,
            favoriteId = 42,
            mediaType = FavoriteType.MOVIE,
        )
        coEvery { authApi.getAccountDetails("session1") } returns AccountDto(id = null)

        val result = dataSource.invoke(params)

        assertFalse(result)
        coVerify(exactly = 0) { authApi.markAsFavorite(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `Should use correct media type string for TV`() = runTest {
        val params = RequestType.FavoriteRequest(
            sessionId = "s1",
            favorite = true,
            favoriteId = 10,
            mediaType = FavoriteType.TV,
        )
        coEvery { authApi.getAccountDetails("s1") } returns AccountDto(id = 5)
        coEvery {
            authApi.markAsFavorite(any(), any(), any(), any(), any())
        } returns StatusResponseDto(statusCode = 12)

        dataSource.invoke(params)

        coVerify {
            authApi.markAsFavorite(
                accountId = 5,
                sessionId = "s1",
                mediaType = "tv",
                mediaId = 10,
                favorite = true,
            )
        }
    }

    @Test
    fun `Should use correct media type string for PERSON`() = runTest {
        val params = RequestType.FavoriteRequest(
            sessionId = "s1",
            favorite = false,
            favoriteId = 99,
            mediaType = FavoriteType.PERSON,
        )
        coEvery { authApi.getAccountDetails("s1") } returns AccountDto(id = 5)
        coEvery {
            authApi.markAsFavorite(any(), any(), any(), any(), any())
        } returns StatusResponseDto(statusCode = 1)

        dataSource.invoke(params)

        coVerify {
            authApi.markAsFavorite(
                accountId = 5,
                sessionId = "s1",
                mediaType = "person",
                mediaId = 99,
                favorite = false,
            )
        }
    }
}
