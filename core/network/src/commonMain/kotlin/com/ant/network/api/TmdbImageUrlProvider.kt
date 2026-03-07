package com.ant.network.api

/** Constructs full TMDb image URLs from a relative path and size qualifier. */
object TmdbImageUrlProvider {
    private const val IMAGE_URL = "https://image.tmdb.org/t/p/"

    /**
     * Builds a complete TMDb image URL.
     *
     * @param path relative image path returned by the API (e.g. "/abc123.jpg").
     * @param imageSize size qualifier string (use [BackdropSizes.wSize] or [PosterSizes.wSize]).
     */
    fun getUrl(path: String, imageSize: String): String {
        return "$IMAGE_URL$imageSize/$path"
    }
}

/** Available TMDb backdrop image widths. */
enum class BackdropSizes(val wSize: String) {
    W300("w300"),
    W780("w780"),
    W1280("w1280"),
    ORIGINAL("original"),
}

/** Available TMDb poster image widths. */
enum class PosterSizes(val wSize: String) {
    W92("w92"),
    W154("w154"),
    W185("w185"),
    W342("w342"),
    W500("w500"),
    W780("w780"),
    ORIGINAL("original"),
}
