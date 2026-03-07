package com.ant.data.repositories.tvseries

import com.ant.database.database.MoviesDb
import com.ant.database.mapper.toEntity
import com.ant.models.entities.TvShowDetails


/**
 * Persists TV series details to the local database as a favorite,
 * marking it as not yet synced to TMDb.
 */
class SaveTvSeriesDetailsRepository constructor(
    private val moviesDb: MoviesDb,
) {
    /** Inserts the TV series into the local database with favored status. */
    suspend fun performRequest(params: TvShowDetails) {
        moviesDb.tvSeriesDao().insert(
            params.tvSeriesData.toEntity().copy(favored = true, syncedToRemote = false)
        )
    }
}