package com.ant.data.repositories.movies

import com.ant.database.database.MoviesDb
import com.ant.database.mapper.toEntity
import com.ant.models.entities.MovieDetails


/**
 * Persists movie details (movie data, cast, and videos) to the local database
 * as a favorite, marking it as not yet synced to TMDb.
 */
class SaveMovieDetailsToLocalRepository constructor(
    private val moviesDb: MoviesDb,
) {
    /**
     * Inserts the movie, its cast, and videos into the local database.
     *
     * @return `true` when the save completes successfully.
     */
    suspend fun performRequest(params: MovieDetails): Boolean {
        moviesDb.moviesDao().insert(
            params.movieData.toEntity().copy(favored = true, syncedToRemote = false)
        )
        params.movieCasts?.let {
            val movieCasts = it.map { movieCast ->
                movieCast.copy(movieId = params.movieData.id).toEntity()
            }
            moviesDb.movieCastDao().insertAll(movieCasts)
        }
        params.videos?.let {
            val videosToInsert = it.map { video ->
                video.copy(movieId = params.movieData.id).toEntity()
            }
            moviesDb.movieVideosDao().insertAll(videosToInsert)
        }

        return true
    }
}