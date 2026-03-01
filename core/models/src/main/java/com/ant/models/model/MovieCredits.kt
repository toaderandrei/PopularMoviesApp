package com.ant.models.model

import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieCrew
import com.ant.models.entities.MovieReview

data class MovieCredits(
    val movieCastList: List<MovieCast>?,
    val movieCrew: List<MovieCrew>?,
    val reviews: List<MovieReview>?
)
