package com.ant.data.repositories.movies

import com.ant.database.database.MoviesDb
import com.ant.database.mapper.toEntity
import com.ant.models.entities.MovieDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveMovieDetailsToLocalRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) {
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