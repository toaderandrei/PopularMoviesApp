package com.ant.data.datasource

import com.ant.database.database.MoviesDb
import com.ant.database.entity.MovieDataEntity
import com.ant.database.mapper.toDomain
import com.ant.models.entities.MovieDetails

/**
 * Assembles [MovieDetails] from the local database for a given movie entity.
 *
 * Loads associated videos and cast from their respective DAOs and combines them
 * with the provided [MovieDataEntity].
 */
class MovieDetailsLocalDbDataSource(
    private val movieDataEntity: MovieDataEntity,
    private val moviesDb: MoviesDb,
) {
    /**
     * Loads videos and cast for the movie and returns a complete [MovieDetails].
     */
    suspend operator fun invoke(): MovieDetails {
        val movieVideos = moviesDb.movieVideosDao().loadVideosForMovieId(movieDataEntity.id)
            .map { it.toDomain() }
        val movieCasts = moviesDb.movieCastDao().loadMovieCastsForMovieId(movieDataEntity.id)
            .map { it.toDomain() }

        return MovieDetails(
            movieData = movieDataEntity.toDomain(),
            videos = movieVideos,
            movieCasts = movieCasts,
            movieCrewList = null,
            reviews = null
        )
    }
}
