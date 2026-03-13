package com.ant.domain.repositories

import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType

/**
 * Repository for managing favorites synchronization between local storage and the TMDb API.
 */
interface FavoriteRepository {
    /**
     * Syncs a favorite item to the remote TMDb account.
     * @return true if the remote sync succeeded.
     */
    suspend fun syncFavoriteToRemote(params: RequestType.FavoriteRequest): Boolean

    /** Updates the local sync status flag for a favorited item. */
    suspend fun updateSyncStatus(id: Long, mediaType: FavoriteType, synced: Boolean)

    /**
     * Fetches favorites from the remote TMDb account and syncs with local database.
     * This performs a bidirectional sync:
     * - Adds remote favorites that aren't in local DB
     * - Marks items that are in both as synced
     * - Keeps local-only favorites (to be synced later)
     */
    suspend fun syncFavoritesFromRemote()
}
