package com.ant.data.repositories.search

import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import com.ant.network.datasource.search.SearchTvShowDataSource


/**
 * Searches TMDb for TV shows matching a query string.
 */
class SearchTvShowRepository constructor(
    private val searchTvShowDataSource: SearchTvShowDataSource,
) {
    /** Executes the TV show search and returns matching results. */
    suspend fun performRequest(params: RequestType.SearchTvShowRequest): List<TvShow> {
        return searchTvShowDataSource(params)
    }
}
