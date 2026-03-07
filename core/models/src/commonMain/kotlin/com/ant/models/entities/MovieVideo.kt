package com.ant.models.entities

/**
 * Represents a video (trailer, teaser, clip, etc.) associated with a movie or TV show.
 */
data class MovieVideo(
    override var id: Long = 0,

    val key: String? = null,

    override val name: String? = null,

    val iso_639_1: String? = null,

    val iso_3166_1: String? = null,

    val site: String? = null,

    val size: Int? = null,

    val type: VideoType? = null,

    var movieId: Long = 0,
) : TmdbEntity
