package com.ant.data.repositories.search

import com.ant.models.entities.MovieData
import com.ant.models.request.RequestType
import com.ant.network.datasource.search.SearchMovieDataSource


/**
 * Searches TMDb for movies matching a query string.
 */
class SearchMovieRepository constructor(
    private val searchMovieDataSource: SearchMovieDataSource,
) {
    /** Executes the movie search and returns matching results. */
    suspend fun performRequest(params: RequestType.SearchMovieRequest): List<MovieData> {
        return searchMovieDataSource(params)
    }
}
