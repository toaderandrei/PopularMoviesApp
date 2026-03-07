package com.ant.domain.repositories

import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType

/**
 * Repository for searching movies and TV shows via the TMDb API.
 */
interface SearchRepository {
    /** Searches for movies matching the query in the request. */
    suspend fun searchMovies(params: RequestType.SearchMovieRequest): List<MovieData>

    /** Searches for TV shows matching the query in the request. */
    suspend fun searchTvShows(params: RequestType.SearchTvShowRequest): List<TvShow>
}
