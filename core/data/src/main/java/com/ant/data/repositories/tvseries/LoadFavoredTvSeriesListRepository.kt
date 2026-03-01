package com.ant.data.repositories.tvseries

import com.ant.database.database.MoviesDb
import com.ant.database.mapper.toDomain
import com.ant.models.entities.TvShow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadFavoredTvSeriesListRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) {
    suspend fun performRequest(params: Boolean): List<TvShow> {
        return moviesDb.tvSeriesDao().loadFavoredTvSeriesData(params).map { it.toDomain() }
    }
}