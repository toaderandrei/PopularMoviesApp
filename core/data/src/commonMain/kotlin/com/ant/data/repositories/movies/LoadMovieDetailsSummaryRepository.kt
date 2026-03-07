package com.ant.data.repositories.movies

import com.ant.models.entities.MovieDetails
import com.ant.models.request.RequestType
import com.ant.database.database.MoviesDb
import com.ant.data.datasource.MovieDetailsLocalDbDataSource
import com.ant.network.datasource.movies.MovieDetailsExtendedSummaryDataSource


/**
 * Loads movie details with a local-first strategy: returns data from the local
 * database when available, otherwise fetches from the TMDb network API.
 */
class LoadMovieDetailsSummaryRepository constructor(
    private val moviesDb: MoviesDb,
    private val movieDetailsExtendedSummaryDataSource: MovieDetailsExtendedSummaryDataSource,
) {
    /** Returns [MovieDetails] from the local DB if the movie exists, otherwise from the network. */
    suspend fun performRequest(params: RequestType.MovieRequestDetails): MovieDetails {
        val movieData = moviesDb.moviesDao().findMovieById(params.tmdbMovieId.toInt())
        return if (movieData != null) {
            MovieDetailsLocalDbDataSource(
                movieData,
                moviesDb
            ).invoke()
        } else {
            movieDetailsExtendedSummaryDataSource.invoke(params)
        }
    }
}
