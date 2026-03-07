package com.ant.data.repositories.tvseries

import com.ant.models.entities.TvShowDetails
import com.ant.database.database.MoviesDb


/**
 * Deletes a TV series and its associated cast and videos from the local database.
 */
class DeleteTvSeriesDetailsRepository constructor(
    private val moviesDb: MoviesDb,
) {
    /** Removes the TV series, its cast entries, and video entries by series ID. */
    suspend fun performRequest(params: TvShowDetails) {
        moviesDb.tvSeriesDao().deleteTvSeriesById(params.tvSeriesData.id)
            .also {
                params.tvSeriesCasts?.let {
                    moviesDb.movieCastDao().deleteMovieCastsById(params.tvSeriesData.id)
                }
            }.also {
                params.videos?.let {
                    moviesDb.movieVideosDao().deleteMovieVideosById(params.tvSeriesData.id)
                }
            }
    }
}