package com.ant.data.repositories.tvseries

import com.ant.models.entities.TvShow
import com.ant.models.model.PaginatedResult
import com.ant.models.request.RequestType
import com.ant.network.datasource.tvseries.TvSeriesListDataSource


/**
 * Fetches a paginated list of TV series from the TMDb network API.
 */
class LoadTvSeriesListRepository constructor(
    private val tvSeriesListDataSource: TvSeriesListDataSource,
) {
    /** Loads TV series for the requested category and page. */
    suspend fun performRequest(params: RequestType.TvShowRequest): PaginatedResult<TvShow> {
        return tvSeriesListDataSource.invoke(params)
    }
}
