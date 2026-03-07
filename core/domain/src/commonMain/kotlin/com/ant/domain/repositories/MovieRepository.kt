package com.ant.domain.repositories

import com.ant.models.entities.MovieData
import com.ant.models.entities.MovieDetails
import com.ant.models.model.PaginatedResult
import com.ant.models.request.RequestType

/**
 * Repository for movie data operations including listing, details, and local favorites persistence.
 */
interface MovieRepository {
    /** Fetches a paginated list of movies for the given category/page request. */
    suspend fun getMovieList(params: RequestType.MovieRequest): PaginatedResult<MovieData>

    /** Fetches extended movie details (credits, reviews, videos) for a specific movie. */
    suspend fun getMovieDetails(params: RequestType.MovieRequestDetails): MovieDetails

    /** Saves movie details to local storage as a favorite. */
    suspend fun saveMovieDetails(params: MovieDetails): Boolean

    /** Deletes movie details from local favorites storage. */
    suspend fun deleteMovieDetails(params: MovieDetails)

    /**
     * Retrieves locally stored movies filtered by favorite status.
     * @param favored true to return only favored movies, false for non-favored.
     */
    suspend fun getFavoredMovies(favored: Boolean): List<MovieData>
}
