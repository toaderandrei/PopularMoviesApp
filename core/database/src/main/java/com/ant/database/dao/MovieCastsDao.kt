package com.ant.database.dao

import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Dao
import com.ant.database.entity.MovieCastEntity

@Dao
abstract class MovieCastsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg movie: MovieCastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: MovieCastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(vararg movieCasts: MovieCastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieCasts: List<MovieCastEntity>)

    @Query("SELECT * FROM MovieCast WHERE  movie_id = :movieId")
    abstract suspend fun loadMovieCastsForMovieId(movieId: Long): List<MovieCastEntity>

    @Query("DELETE FROM MovieCast WHERE  movie_id = :movieId")
    abstract suspend fun deleteMovieCastsById(movieId: Long)
}