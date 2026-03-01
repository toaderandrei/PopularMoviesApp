package com.ant.models.entities

data class MovieCast(
    override var id: Long = 0,

    var creditId: Int? = null,

    var cast_id: Int? = null,

    var movieId: Long? = 0,

    override val name: String? = null,

    var order: Int? = 0,

    var profileImagePath: String? = null,
) : TmdbEntity
