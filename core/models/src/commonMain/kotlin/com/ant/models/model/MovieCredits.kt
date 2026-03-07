package com.ant.models.model

import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieCrew
import com.ant.models.entities.MovieReview

/**
 * Aggregates cast, crew, and review data for a movie's credits section.
 */
data class MovieCredits(
    val movieCastList: List<MovieCast>?,
    val movieCrew: List<MovieCrew>?,
    val reviews: List<MovieReview>?
)
