package com.ant.models.model

import com.ant.models.entities.TvShow

/**
 * Holds the UI state for all TV show list categories displayed on the TV shows screen.
 */
data class TvShowListState(
    val popularTvSeries: UIState<List<TvShow>> = UIState(),
    val topRated: UIState<List<TvShow>> = UIState(),
    val onTvNow: UIState<List<TvShow>> = UIState(),
    val airingToday: UIState<List<TvShow>> = UIState()
)
