package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.database.entity.MovieDataEntity

/** Data access object for [MovieDataEntity] operations. */
@Dao
abstract class MoviesDao {

    /** Inserts one or more movie entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg movie: MovieDataEntity)

    /** Inserts a single movie entity, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(movie: MovieDataEntity)

    /** Inserts a list of movie entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieData: List<MovieDataEntity>)

    /** Finds a movie by its [id], or returns null if not found. */
    @Query("SELECT * FROM moviedata WHERE id = :id")
    abstract suspend fun findMovieById(id: Int?): MovieDataEntity?

    /** Deletes the movie with the given [id]. */
    @Query("DELETE FROM moviedata where id =:id")
    abstract suspend fun deleteMovieById(id: Long)

    /** Loads all movies matching the given [favored] status. */
    @Query("SELECT * from moviedata where favored=:favored")
    abstract suspend fun loadFavoredMovies(favored: Boolean): List<MovieDataEntity>

    /** Updates the remote sync status for the movie with the given [id]. */
    @Query("UPDATE moviedata SET synced_to_remote = :synced WHERE id = :id")
    abstract suspend fun updateSyncStatus(id: Long, synced: Boolean)
}