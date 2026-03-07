package com.ant.models.request

/**
 * Enumerates the movie list categories available from the TMDb API.
 */
enum class MovieType {
    POPULAR,
    TOP_RATED,
    NOW_PLAYING,
    UPCOMING,
}

/**
 * Enumerates the additional data sections that can be appended to a movie details request.
 */
enum class MovieAppendToResponseItem {
    REVIEWS,
    CREDITS,
    VIDEOS,
    MOVIE_CREDITS,
}