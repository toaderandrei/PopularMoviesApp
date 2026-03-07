package com.ant.models.entities

/**
 * Aggregates a movie's core data with its associated videos, cast, reviews, and crew.
 */
data class MovieDetails(
    val movieData: MovieData,
    val videos: List<MovieVideo>? = emptyList(),
    val movieCasts: List<MovieCast>? = emptyList(),
    val reviews: List<MovieReview>? = emptyList(),
    val movieCrewList: List<MovieCrew>? = emptyList()
)