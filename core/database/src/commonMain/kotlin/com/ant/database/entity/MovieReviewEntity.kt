package com.ant.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Room entity representing a movie review, linked to [MovieDataEntity] via foreign key. */
@Entity(
    tableName = "MovieReview",
    indices = [
        Index(value = ["movie_id"])],
    foreignKeys = [
        ForeignKey(
            entity = MovieDataEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movie_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class MovieReviewEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long = 0,

    @ColumnInfo(name = "author")
    val name: String? = null,

    @ColumnInfo(name = "content") val content: String? = null,

    @ColumnInfo(name = "url") val url: String? = null,

    @ColumnInfo(name = "movie_id") var movieId: Long = 0,

    @ColumnInfo(name = "tmdb_id") val tmdbId: Int? = null,
)
