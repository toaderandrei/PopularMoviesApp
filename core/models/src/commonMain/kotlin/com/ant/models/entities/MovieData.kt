package com.ant.models.entities

import com.ant.models.extensions.toFormattedDateString
import com.ant.models.extensions.toTwoDigitNumber
import kotlinx.datetime.Instant

/**
 * Domain model representing a movie with its metadata, ratings, and favorite status.
 */
data class MovieData(
    override val id: Long = 0,

    override val name: String? = null,

    val originalTitle: String? = null,

    val homepage: String? = null,

    val voteCount: Int? = null,

    val overview: String? = null,

    val runtime: String? = null,

    val posterPath: String? = null,

    val backDropPath: String? = null,

    val _releaseDate: Instant? = null,

    val voteAverage: Double? = null,

    val originalLanguage: String? = null,

    var _genres: List<String>? = emptyList(),

    var _genres_ids: List<Int>? = emptyList(),

    var favored: Boolean? = null,

    val syncedToRemote: Boolean? = false,
) : TmdbEntity {

    val genres: String?
        get() = _genres?.joinToString(", ")

    val releaseDate: String?
        get() = _releaseDate?.toFormattedDateString(useDifferentFormat = true)

    val voteAverageRounded: String?
        get() = voteAverage?.toTwoDigitNumber()
}
