package com.ant.models.entities

/**
 * Aggregates a TV show's core data with its associated videos, cast, and crew.
 */
data class TvShowDetails(
    val tvSeriesData: TvShow,
    val videos: List<MovieVideo>? = emptyList(),
    val tvSeriesCasts: List<MovieCast>? = emptyList(),
    val movieCrewList: List<MovieCrew>? = emptyList()
)