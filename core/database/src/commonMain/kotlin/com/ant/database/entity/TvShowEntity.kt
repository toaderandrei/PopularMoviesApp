package com.ant.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

/** Room entity representing a TV series record in the local database. */
@Entity(
    tableName = "tvseriesdata",
    indices = [Index(value = ["id"], unique = true)]
)
data class TvShowEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "original_title")
    val originalTitle: String? = null,

    @ColumnInfo(name = "vote")
    val voteCount: Int? = null,

    @ColumnInfo(name = "overview")
    val overview: String? = null,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double? = null,

    @ColumnInfo(name = "backdrop_path")
    val backDropPath: String? = null,

    @ColumnInfo(name = "poster_path")
    val posterPath: String? = null,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String? = null,

    @ColumnInfo(name = "status")
    val status: String? = null,

    @ColumnInfo(name = "first_air_date")
    val _firstAirDate: Instant? = null,

    @ColumnInfo(name = "last_air_date")
    val _lastAirDate: Instant? = null,

    @ColumnInfo(name = "number_of_seasons")
    val numberOfSeasons: Int? = null,

    @ColumnInfo(name = "number_of_episodes")
    val numberOfEpisodes: Int? = null,

    @ColumnInfo(name = "genres_ids")
    var _genres_ids: List<Int>? = emptyList(),

    @ColumnInfo(name = "genres")
    var _genres: List<String>? = emptyList(),

    @ColumnInfo(name = "favored")
    var favored: Boolean? = false,

    @ColumnInfo(name = "synced_to_remote")
    val syncedToRemote: Boolean? = false,
)
