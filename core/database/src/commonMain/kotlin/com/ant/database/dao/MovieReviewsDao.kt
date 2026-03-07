package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.database.entity.MovieReviewEntity
import com.ant.database.entity.MovieReviewsRelation

/** Data access object for [MovieReviewEntity] operations. */
@Dao
abstract class MovieReviewsDao {

    /** Inserts one or more review entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg movie: MovieReviewEntity)

    /** Inserts a single review entity, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(movie: MovieReviewEntity)

    /** Inserts a list of review entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieData: List<MovieReviewEntity>)

    /** Loads the movie with its reviews for the given [movieId]. */
    @Query("SELECT * FROM MovieReview WHERE  movie_id = :movieId")
    abstract suspend fun loadMovieReviews(movieId: Long): MovieReviewsRelation
}