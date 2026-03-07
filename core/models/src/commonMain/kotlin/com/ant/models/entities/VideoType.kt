package com.ant.models.entities

/**
 * Enumerates the types of video content available from TMDb.
 */
enum class VideoType(val genreValue: String) {
    TRAILER("Trailer"),
    TEASER("Teaser"),
    CLIP("Clip"),
    FEATURETTE("Featurette"),
    OPENING_CREDITS("Opening Credits");

    companion object {
        private val values by lazy { values() }
        /**
         * Finds the [VideoType] matching the given TMDb string value, or null if no match.
         */
        fun fromValue(value: String) = values.firstOrNull { it.genreValue == value }
    }
}
