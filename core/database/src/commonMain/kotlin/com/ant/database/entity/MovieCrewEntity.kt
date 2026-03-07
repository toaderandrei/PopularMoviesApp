package com.ant.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Room entity representing a movie crew member, linked to [MovieDataEntity] via foreign key. */
@Entity(
    tableName = "MovieCrew",
    indices = [
        Index(value = ["movie_id"])],
    foreignKeys = [ForeignKey(
        entity = MovieDataEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("movie_id"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class MovieCrewEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long = 0,

    @ColumnInfo(name = "credits_id") val creditsId: Int? = null,

    @ColumnInfo(name = "movie_id") var movieId: Long? = null,

    @ColumnInfo(name = "department")
    val name: String? = null,

    @ColumnInfo(name = "job") val job: String? = null,

    @ColumnInfo(name = "profile_path") val profilePath: String? = null,
)
