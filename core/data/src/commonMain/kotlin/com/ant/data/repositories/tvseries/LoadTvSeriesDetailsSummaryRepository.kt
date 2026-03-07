package com.ant.data.repositories.tvseries

import com.ant.database.database.MoviesDb
import com.ant.models.entities.TvShowDetails
import com.ant.models.request.RequestType
import com.ant.data.datasource.TvSeriesDetailsLocalDbDataSource
import com.ant.network.datasource.tvseries.TvSeriesDetailsExtendedSummaryDataSource


/**
 * Loads TV series details with a local-first strategy: returns data from the local
 * database when available, otherwise fetches from the TMDb network API.
 */
class LoadTvSeriesDetailsSummaryRepository constructor(
    private val moviesDb: MoviesDb,
    private val tvSeriesDetailsExtendedSummaryDataSource: TvSeriesDetailsExtendedSummaryDataSource,
) {
    /** Returns [TvShowDetails] from the local DB if the series exists, otherwise from the network. */
    suspend fun performRequest(params: RequestType.TvSeriesRequestDetails): TvShowDetails {
        val movieData = moviesDb.tvSeriesDao().findTvSeriesById(params.tmdbTvSeriesId.toInt())
        return if (movieData != null) {
            TvSeriesDetailsLocalDbDataSource(
                movieData,
                moviesDb
            ).invoke()
        } else {
            tvSeriesDetailsExtendedSummaryDataSource.invoke(params)
        }
    }
}
