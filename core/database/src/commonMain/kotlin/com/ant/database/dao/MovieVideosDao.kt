package com.ant.database.dao

import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Dao
import com.ant.database.entity.MovieVideoEntity

/** Data access object for [MovieVideoEntity] operations. */
@Dao
abstract class MovieVideosDao {

    /** Inserts one or more video entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg movie: MovieVideoEntity)

    /** Inserts a single video entity, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(movie: MovieVideoEntity)

    /** Inserts one or more video entities from varargs, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(vararg movieVideos: MovieVideoEntity)

    /** Inserts a list of video entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieVideos: List<MovieVideoEntity>)

    /** Loads all video entries associated with the given [movieId]. */
    @Query("SELECT * FROM MovieVideo WHERE  movie_id = :movieId")
    abstract suspend fun loadVideosForMovieId(movieId: Long): List<MovieVideoEntity>

    /** Deletes all video entries associated with the given [movieId]. */
    @Query("DELETE FROM MovieVideo WHERE  movie_id = :movieId")
    abstract suspend fun deleteMovieVideosById(movieId: Long)
}