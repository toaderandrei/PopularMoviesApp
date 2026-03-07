package com.ant.models.model

import com.ant.models.entities.MovieData

/**
 * Holds the UI state for all movie list categories displayed on the movies screen.
 */
data class MoviesListState(
    val popularMovies: UIState<List<MovieData>> = UIState(),
    val topMovies: UIState<List<MovieData>> = UIState(),
    val nowPlayingMovies: UIState<List<MovieData>> = UIState(),
    val upcomingMovies: UIState<List<MovieData>> = UIState()
)


