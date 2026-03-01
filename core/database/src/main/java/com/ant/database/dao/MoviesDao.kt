package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.database.entity.MovieDataEntity

@Dao
abstract class MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg movie: MovieDataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: MovieDataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieData: List<MovieDataEntity>)

    @Query("SELECT * FROM MovieData WHERE id = :id")
    abstract fun findMovieById(id: Int?): MovieDataEntity?

    @Query("DELETE FROM movieData where id =:id")
    abstract fun deleteMovieById(id: Long)

    @Query("SELECT * from MovieData where favored=:favored")
    abstract fun loadFavoredMovies(favored: Boolean): List<MovieDataEntity>

    @Query("UPDATE moviedata SET synced_to_remote = :synced WHERE id = :id")
    abstract suspend fun updateSyncStatus(id: Long, synced: Boolean)
}