package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.database.entity.MovieCrewEntity
import com.ant.database.entity.MovieCrewsRelation

/** Data access object for [MovieCrewEntity] operations. */
@Dao
abstract class MovieCrewDao {

    /** Inserts one or more crew entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg movie: MovieCrewEntity)

    /** Inserts a single crew entity, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(movie: MovieCrewEntity)

    /** Inserts a list of crew entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieData: List<MovieCrewEntity>)

    /** Loads the movie with its crew members for the given [movieId]. */
    @Query("SELECT * FROM MovieCrew WHERE  movie_id = :movieId")
    abstract suspend fun loadMovieCrews(movieId: Long): MovieCrewsRelation
}