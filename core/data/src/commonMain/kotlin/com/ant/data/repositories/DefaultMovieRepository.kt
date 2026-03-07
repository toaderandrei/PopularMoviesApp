package com.ant.data.repositories

import com.ant.data.repositories.movies.DeleteMovieDetailsRepository
import com.ant.data.repositories.movies.LoadFavoredMovieListRepository
import com.ant.data.repositories.movies.LoadMovieDetailsSummaryRepository
import com.ant.data.repositories.movies.LoadMovieListRepository
import com.ant.data.repositories.movies.SaveMovieDetailsToLocalRepository
import com.ant.domain.repositories.MovieRepository
import com.ant.models.entities.MovieData
import com.ant.models.entities.MovieDetails
import com.ant.models.model.PaginatedResult
import com.ant.models.request.RequestType


/**
 * Default [MovieRepository] implementation that delegates to single-responsibility
 * repositories for listing, details, saving, and deleting movies.
 */
class DefaultMovieRepository constructor(
    private val loadMovieListRepository: LoadMovieListRepository,
    private val loadMovieDetailsSummaryRepository: LoadMovieDetailsSummaryRepository,
    private val saveMovieDetailsToLocalRepository: SaveMovieDetailsToLocalRepository,
    private val deleteMovieDetailsRepository: DeleteMovieDetailsRepository,
    private val loadFavoredMovieListRepository: LoadFavoredMovieListRepository,
) : MovieRepository {

    /** Fetches a paginated list of movies from TMDb by category. */
    override suspend fun getMovieList(params: RequestType.MovieRequest): PaginatedResult<MovieData> {
        return loadMovieListRepository.performRequest(params)
    }

    /** Loads movie details from the local database if available, otherwise from the network. */
    override suspend fun getMovieDetails(params: RequestType.MovieRequestDetails): MovieDetails {
        return loadMovieDetailsSummaryRepository.performRequest(params)
    }

    /** Saves movie details to the local database as a favorite. */
    override suspend fun saveMovieDetails(params: MovieDetails): Boolean {
        return saveMovieDetailsToLocalRepository.performRequest(params)
    }

    /** Deletes movie details and associated data from the local database. */
    override suspend fun deleteMovieDetails(params: MovieDetails) {
        deleteMovieDetailsRepository.performRequest(params)
    }

    /** Loads movies from the local database filtered by their favored status. */
    override suspend fun getFavoredMovies(favored: Boolean): List<MovieData> {
        return loadFavoredMovieListRepository.performRequest(favored)
    }
}
