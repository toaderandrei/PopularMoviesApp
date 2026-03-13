package com.ant.data.repositories

import com.ant.data.repositories.favorites.FavoriteDetailsToRemoteRepository
import com.ant.data.repositories.favorites.SyncFavoritesFromRemoteRepository
import com.ant.data.repositories.favorites.UpdateFavoriteSyncStatusRepository
import com.ant.domain.repositories.FavoriteRepository
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType


/**
 * Default [FavoriteRepository] implementation that coordinates syncing favorites
 * to/from TMDb and updating their local sync status.
 */
class DefaultFavoriteRepository constructor(
    private val favoriteDetailsToRemoteRepository: FavoriteDetailsToRemoteRepository,
    private val updateFavoriteSyncStatusRepository: UpdateFavoriteSyncStatusRepository,
    private val syncFavoritesFromRemoteRepository: SyncFavoritesFromRemoteRepository,
) : FavoriteRepository {

    /** Sends the favorite state to TMDb's remote API. */
    override suspend fun syncFavoriteToRemote(params: RequestType.FavoriteRequest): Boolean {
        return favoriteDetailsToRemoteRepository.performRequest(params)
    }

    /** Updates the local database sync flag for a favorited item. */
    override suspend fun updateSyncStatus(id: Long, mediaType: FavoriteType, synced: Boolean) {
        updateFavoriteSyncStatusRepository.updateSyncStatus(id, mediaType, synced)
    }

    /** Fetches favorites from TMDb backend and syncs with local database. */
    override suspend fun syncFavoritesFromRemote() {
        syncFavoritesFromRemoteRepository.syncFavorites()
    }
}
