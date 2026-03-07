package com.ant.data.datasource

import com.ant.database.database.MoviesDb
import com.ant.database.entity.TvShowEntity
import com.ant.database.mapper.toDomain
import com.ant.models.entities.TvShowDetails

/**
 * Assembles [TvShowDetails] from the local database for a given TV series entity.
 *
 * Loads associated videos and cast from their respective DAOs and combines them
 * with the provided [TvShowEntity].
 */
class TvSeriesDetailsLocalDbDataSource(
    private val tvShowEntity: TvShowEntity,
    private val moviesDb: MoviesDb,
) {
    /**
     * Loads videos and cast for the TV series and returns a complete [TvShowDetails].
     */
    suspend operator fun invoke(): TvShowDetails {
        val movieVideos = moviesDb.movieVideosDao().loadVideosForMovieId(tvShowEntity.id)
            .map { it.toDomain() }
        val movieCasts = moviesDb.movieCastDao().loadMovieCastsForMovieId(tvShowEntity.id)
            .map { it.toDomain() }

        return TvShowDetails(
            tvSeriesData = tvShowEntity.toDomain(),
            videos = movieVideos,
            tvSeriesCasts = movieCasts,
            movieCrewList = null,
        )
    }
}
