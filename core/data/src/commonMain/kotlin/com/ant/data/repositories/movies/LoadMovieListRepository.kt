package com.ant.data.repositories.movies

import com.ant.models.entities.MovieData
import com.ant.models.model.PaginatedResult
import com.ant.models.request.RequestType
import com.ant.network.datasource.movies.MovieListDataSource


/**
 * Fetches a paginated list of movies from the TMDb network API.
 */
class LoadMovieListRepository constructor(
    private val movieListDataSource: MovieListDataSource,
) {
    /** Loads movies for the requested category and page. */
    suspend fun performRequest(params: RequestType.MovieRequest): PaginatedResult<MovieData> {
        return movieListDataSource.invoke(params)
    }
}