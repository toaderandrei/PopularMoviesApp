package com.ant.data.repositories.favorites

import com.ant.database.database.MoviesDb
import com.ant.models.request.FavoriteType


/**
 * Updates the remote-sync status flag for a favorited item in the local database.
 */
class UpdateFavoriteSyncStatusRepository constructor(
    private val moviesDb: MoviesDb,
) {
    /**
     * Sets the sync status for the given media item.
     *
     * @param id database ID of the movie or TV series.
     * @param mediaType whether the item is a movie, TV show, or person.
     * @param synced `true` if the favorite has been synced to TMDb.
     */
    suspend fun updateSyncStatus(id: Long, mediaType: FavoriteType, synced: Boolean) {
        when (mediaType) {
            FavoriteType.MOVIE -> moviesDb.moviesDao().updateSyncStatus(id, synced)
            FavoriteType.TV -> moviesDb.tvSeriesDao().updateSyncStatus(id, synced)
            FavoriteType.PERSON -> { /* Not supported */ }
        }
    }
}
