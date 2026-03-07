package com.ant.models.request

/**
 * Sealed hierarchy representing all types of requests that can be made to the TMDb API.
 */
sealed interface RequestType {

    /** Request for a paginated list of movies by category. */
    data class MovieRequest(val movieType: MovieType, val page: Int = 1) : RequestType

    /** Request for detailed movie information with optional appended sections. */
    data class MovieRequestDetails(
        val tmdbMovieId: Long,
        val appendToResponseItems: List<MovieAppendToResponseItem> = mutableListOf()
    ) : RequestType

    /** Sealed hierarchy for login and session management requests. */
    sealed interface LoginSessionRequest : RequestType {
        data class WithCredentials(
            val username: String,
            val password: String? = null,
        ) : LoginSessionRequest

        data class Logout(
            val username: String?,
        ) : LoginSessionRequest

        data object GetUser : LoginSessionRequest
    }

    /** Request for a paginated list of TV shows by category. */
    data class TvShowRequest(val tvSeriesType: TvShowType, val page: Int = 1) : RequestType

    /** Request for detailed TV series information with optional appended sections. */
    data class TvSeriesRequestDetails(
        val tmdbTvSeriesId: Long,
        val appendToResponseItems: List<TvSeriesAppendToResponseItem> = mutableListOf()
    ) : RequestType

    /** Request to search movies by query string. */
    data class SearchMovieRequest(val query: String, val page: Int = 1) : RequestType
    /** Request to search TV shows by query string. */
    data class SearchTvShowRequest(val query: String, val page: Int = 1) : RequestType

    /** Request to add or remove a media item from the user's favorites. */
    data class FavoriteRequest(
        val sessionId: String,
        val favorite: Boolean,
        val favoriteId: Int,
        val mediaType: FavoriteType,
    ) : RequestType
}