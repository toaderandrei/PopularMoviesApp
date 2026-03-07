package com.ant.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Room entity representing a movie cast member, linked to [MovieDataEntity] via foreign key. */
@Entity(
    tableName = "MovieCast",
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
data class MovieCastEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long = 0,

    @ColumnInfo(name = "credit_id")
    var creditId: Int? = null,

    @ColumnInfo(name = "cast_id")
    var cast_id: Int? = null,

    @ColumnInfo(name = "movie_id")
    var movieId: Long? = 0,

    @ColumnInfo(name = "character")
    val name: String? = null,

    @ColumnInfo(name = "order")
    var order: Int? = 0,

    @ColumnInfo(name = "profile_path")
    var profileImagePath: String? = null,
)
