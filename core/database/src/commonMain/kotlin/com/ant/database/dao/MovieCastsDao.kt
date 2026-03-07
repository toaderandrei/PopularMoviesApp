package com.ant.database.dao

import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Dao
import com.ant.database.entity.MovieCastEntity

/** Data access object for [MovieCastEntity] operations. */
@Dao
abstract class MovieCastsDao {

    /** Inserts one or more cast entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg movie: MovieCastEntity)

    /** Inserts a single cast entity, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(movie: MovieCastEntity)

    /** Inserts one or more cast entities from varargs, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(vararg movieCasts: MovieCastEntity)

    /** Inserts a list of cast entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieCasts: List<MovieCastEntity>)

    /** Loads all cast entries associated with the given [movieId]. */
    @Query("SELECT * FROM MovieCast WHERE  movie_id = :movieId")
    abstract suspend fun loadMovieCastsForMovieId(movieId: Long): List<MovieCastEntity>

    /** Deletes all cast entries associated with the given [movieId]. */
    @Query("DELETE FROM MovieCast WHERE  movie_id = :movieId")
    abstract suspend fun deleteMovieCastsById(movieId: Long)
}