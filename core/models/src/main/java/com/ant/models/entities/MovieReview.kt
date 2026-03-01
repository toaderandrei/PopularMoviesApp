package com.ant.models.entities

data class MovieReview(
    override val id: Long = 0,

    override val name: String? = null,

    val content: String? = null,

    val url: String? = null,

    var movieId: Long = 0,

    val tmdbId: Int? = null,
) : TmdbEntity
