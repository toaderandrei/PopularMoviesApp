package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.database.entity.MovieCrewEntity
import com.ant.database.entity.MovieCrewsRelation

@Dao
abstract class MovieCrewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg movie: MovieCrewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: MovieCrewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieData: List<MovieCrewEntity>)

    @Query("SELECT * FROM MovieCrew WHERE  movie_id = :movieId")
    abstract suspend fun loadMovieCrews(movieId: Long): MovieCrewsRelation
}