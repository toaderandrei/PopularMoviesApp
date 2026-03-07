package com.ant.models.request

/**
 * Enumerates the TV show list categories available from the TMDb API.
 */
enum class TvShowType {
    POPULAR,
    TOP_RATED,
    AIRING_TODAY,
    ONTV_NOW,
}

/**
 * Enumerates the additional data sections that can be appended to a TV series details request.
 */
enum class TvSeriesAppendToResponseItem {
    REVIEWS,
    CREDITS,
    VIDEOS,
}