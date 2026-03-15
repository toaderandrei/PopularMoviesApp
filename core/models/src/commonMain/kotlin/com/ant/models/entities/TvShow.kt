package com.ant.models.entities

import com.ant.models.extensions.toFormattedDateString
import com.ant.models.extensions.toTwoDigitNumber
import kotlin.time.Instant

/**
 * Domain model representing a TV show with its metadata, ratings, and favorite status.
 */
data class TvShow(
    override val id: Long = 0,

    override val name: String? = null,

    val originalTitle: String? = null,

    val voteCount: Int? = null,

    val overview: String? = null,

    val voteAverage: Double? = null,

    val backDropPath: String? = null,

    val posterPath: String? = null,

    val originalLanguage: String? = null,

    val status: String? = null,

    val _firstAirDate: Instant? = null,

    val _lastAirDate: Instant? = null,

    val numberOfSeasons: Int? = null,

    val numberOfEpisodes: Int? = null,

    var _genres_ids: List<Int>? = emptyList(),

    var _genres: List<String>? = emptyList(),

    var favored: Boolean? = false,

    val syncedToRemote: Boolean? = false,
) : TmdbEntity {

    val genres: String?
        get() = _genres?.joinToString(",")

    val firstAirDate: String?
        get() = _firstAirDate?.toFormattedDateString(useDifferentFormat = true)

    val lastAirDate: String?
        get() = _lastAirDate?.toFormattedDateString(useDifferentFormat = true)

    val voteAverageRounded: String?
        get() = voteAverage?.toTwoDigitNumber()
}
