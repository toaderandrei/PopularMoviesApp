package com.ant.data.repositories.tvseries

import com.ant.database.database.MoviesDb
import com.ant.database.mapper.toEntity
import com.ant.models.entities.TvShowDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveTvSeriesDetailsRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) {
    suspend fun performRequest(params: TvShowDetails) {
        moviesDb.tvSeriesDao().insert(
            params.tvSeriesData.toEntity().copy(favored = true, syncedToRemote = false)
        )
    }
}