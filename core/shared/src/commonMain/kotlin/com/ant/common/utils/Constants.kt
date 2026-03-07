package com.ant.shared.utils

/** Application-wide constant values for navigation arguments and UI thresholds. */
object Constants {

    /** Navigation argument key for the TMDb content type (movie or TV series). */
    const val TMDB_TYPE: String = "MOVIE_TYPE_KEY"
    /** Navigation argument key for the TMDb item identifier. */
    const val TMDB_KEY_ID = "MOVIE_ID"
    /** Navigation argument key for generic navigation identifiers. */
    const val NAV_ID = "NAV_ID"
    /** Scroll percentage at which the collapsing toolbar is considered fully collapsed. */
    const val PERCENTAGE_TO_SHOW_TOOLBAR_COLLAPSED = 0.95f
}