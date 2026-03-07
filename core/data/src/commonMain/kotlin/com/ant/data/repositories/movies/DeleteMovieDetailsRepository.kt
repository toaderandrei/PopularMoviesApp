package com.ant.data.repositories.movies

import com.ant.database.database.MoviesDb
import com.ant.models.entities.MovieDetails


/**
 * Deletes a movie and its associated cast and videos from the local database.
 */
class DeleteMovieDetailsRepository constructor(
    private val moviesDb: MoviesDb,
) {
    /** Removes the movie, its cast entries, and video entries by movie ID. */
    suspend fun performRequest(params: MovieDetails) {
        moviesDb.moviesDao().deleteMovieById(params.movieData.id)
            .also {
                params.movieCasts?.let {
                    moviesDb.movieCastDao().deleteMovieCastsById(params.movieData.id)
                }
            }.also {
                params.videos?.let {
                    moviesDb.movieVideosDao().deleteMovieVideosById(params.movieData.id)
                }
            }
    }
}