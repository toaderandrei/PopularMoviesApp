package com.ant.database.entity

import androidx.room.Embedded
import androidx.room.Relation

/** Room relation linking a [MovieDataEntity] to its list of [MovieCrewEntity] entries. */
class MovieCrewsRelation {
    @Embedded
    var movie: MovieDataEntity? = null

    @Relation(parentColumn = "id", entityColumn = "movie_id")
    var crewList: List<MovieCrewEntity>? = null
}
