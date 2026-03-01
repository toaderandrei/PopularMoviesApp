package com.ant.models.entities

data class MovieCrew(
    override val id: Long = 0,

    val creditsId: Int? = null,

    var movieId: Long? = null,

    override val name: String? = null,

    val job: String? = null,

    val profilePath: String? = null,
) : TmdbEntity
