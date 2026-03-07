package com.ant.data.repositories.tvseries

import com.ant.database.database.MoviesDb
import com.ant.database.mapper.toDomain
import com.ant.models.entities.TvShow


/**
 * Loads TV series from the local database filtered by their favored status.
 */
class LoadFavoredTvSeriesListRepository constructor(
    private val moviesDb: MoviesDb,
) {
    /**
     * Queries the local database for TV series matching the favored flag.
     *
     * @param params `true` to load favored TV series, `false` for non-favored.
     */
    suspend fun performRequest(params: Boolean): List<TvShow> {
        return moviesDb.tvSeriesDao().loadFavoredTvSeriesData(params).map { it.toDomain() }
    }
}