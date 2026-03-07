package com.ant.data.repositories

import com.ant.data.repositories.favorites.FavoriteDetailsToRemoteRepository
import com.ant.data.repositories.favorites.UpdateFavoriteSyncStatusRepository
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultFavoriteRepositoryTest {

    private val favoriteDetailsToRemoteRepository = mockk<FavoriteDetailsToRemoteRepository>()
    private val updateFavoriteSyncStatusRepository = mockk<UpdateFavoriteSyncStatusRepository>(relaxed = true)
    private val repository = DefaultFavoriteRepository(
        favoriteDetailsToRemoteRepository,
        updateFavoriteSyncStatusRepository,
    )

    @Test
    fun `Should delegate syncFavoriteToRemote to FavoriteDetailsToRemoteRepository`() = runTest {
        val request = RequestType.FavoriteRequest(
            sessionId = "session",
            favorite = true,
            favoriteId = 1,
            mediaType = FavoriteType.MOVIE,
        )
        coEvery { favoriteDetailsToRemoteRepository.performRequest(request) } returns true

        val result = repository.syncFavoriteToRemote(request)

        assertTrue(result)
        coVerify { favoriteDetailsToRemoteRepository.performRequest(request) }
    }

    @Test
    fun `Should return false when remote sync returns false`() = runTest {
        val request = RequestType.FavoriteRequest(
            sessionId = "session",
            favorite = true,
            favoriteId = 2,
            mediaType = FavoriteType.TV,
        )
        coEvery { favoriteDetailsToRemoteRepository.performRequest(request) } returns false

        val result = repository.syncFavoriteToRemote(request)

        assertFalse(result)
    }

    @Test
    fun `Should delegate updateSyncStatus to UpdateFavoriteSyncStatusRepository`() = runTest {
        repository.updateSyncStatus(42L, FavoriteType.MOVIE, true)

        coVerify {
            updateFavoriteSyncStatusRepository.updateSyncStatus(42L, FavoriteType.MOVIE, true)
        }
    }
}
