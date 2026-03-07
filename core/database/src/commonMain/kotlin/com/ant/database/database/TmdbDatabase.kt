package com.ant.database.database

import com.ant.database.dao.MovieCastsDao
import com.ant.database.dao.MovieCrewDao
import com.ant.database.dao.MovieReviewsDao
import com.ant.database.dao.MovieVideosDao
import com.ant.database.dao.MoviesDao
import com.ant.database.dao.TvSeriesDao

/** Contract for accessing all TMDb-related DAOs. Implemented by [MoviesDb]. */
interface TmdbDatabase {
    /** Provides access to movie data operations. */
    fun moviesDao(): MoviesDao

    /** Provides access to TV series data operations. */
    fun tvSeriesDao(): TvSeriesDao

    /** Provides access to movie review operations. */
    fun movieReviewsDao(): MovieReviewsDao

    /** Provides access to movie crew operations. */
    fun movieCrewDao(): MovieCrewDao

    /** Provides access to movie cast operations. */
    fun movieCastDao(): MovieCastsDao

    /** Provides access to movie video operations. */
    fun movieVideosDao(): MovieVideosDao
}
