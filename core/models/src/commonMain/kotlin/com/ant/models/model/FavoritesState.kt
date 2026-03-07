package com.ant.models.model

import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow

/**
 * Holds the UI state for the user's favorited TV shows and movies.
 */
data class FavoritesState(
    val tvSeriesFavored: UIState<List<TvShow>> = UIState(),
    val moviesFavored: UIState<List<MovieData>> = UIState(),
)