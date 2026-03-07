package com.ant.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ant.models.entities.VideoType

/** Room entity representing a movie video (e.g. trailer), linked to [MovieDataEntity] via foreign key. */
@Entity(
    tableName = "MovieVideo",
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
data class MovieVideoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long = 0,

    @ColumnInfo(name = "key") val key: String? = null,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "iso_639_1") val iso_639_1: String? = null,

    @ColumnInfo(name = "iso_3166_1") val iso_3166_1: String? = null,

    @ColumnInfo(name = "site") val site: String? = null,

    @ColumnInfo(name = "size") val size: Int? = null,

    @ColumnInfo(name = "type") val type: VideoType? = null,

    @ColumnInfo(name = "movie_id") var movieId: Long = 0,
)
