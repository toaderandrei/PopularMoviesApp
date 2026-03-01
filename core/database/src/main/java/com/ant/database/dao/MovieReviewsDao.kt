package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.database.entity.MovieReviewEntity
import com.ant.database.entity.MovieReviewsRelation

@Dao
abstract class MovieReviewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg movie: MovieReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: MovieReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieData: List<MovieReviewEntity>)

    @Query("SELECT * FROM MovieReview WHERE  movie_id = :movieId")
    abstract suspend fun loadMovieReviews(movieId: Long): MovieReviewsRelation
}