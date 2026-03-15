package com.ant.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Instant

/** Room entity representing a movie record in the local database. */
@Entity(
    tableName = "moviedata",
    indices = [
        Index(value = ["id"], unique = true)
    ],
)
data class MovieDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long = 0,

    @ColumnInfo(name = "title")
    val name: String? = null,

    @ColumnInfo(name = "original_title")
    val originalTitle: String? = null,

    @ColumnInfo(name = "homepage")
    val homepage: String? = null,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int? = null,

    @ColumnInfo(name = "overview")
    val overview: String? = null,

    @ColumnInfo(name = "runtime")
    val runtime: String? = null,

    @ColumnInfo(name = "poster_path")
    val posterPath: String? = null,

    @ColumnInfo(name = "backdrop_path")
    val backDropPath: String? = null,

    @ColumnInfo(name = "release_date")
    val _releaseDate: Instant? = null,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double? = null,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String? = null,

    @ColumnInfo(name = "genres")
    var _genres: List<String>? = emptyList(),

    @ColumnInfo(name = "genres_ids")
    var _genres_ids: List<Int>? = emptyList(),

    @ColumnInfo(name = "favored")
    var favored: Boolean? = null,

    @ColumnInfo(name = "synced_to_remote")
    val syncedToRemote: Boolean? = false,
)
