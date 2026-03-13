package com.ant.network.api

import com.ant.network.dto.AccountDto
import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.MovieDto
import com.ant.network.dto.MovieResultsPageDto
import com.ant.network.dto.RequestTokenDto
import com.ant.network.dto.SessionDto
import com.ant.network.dto.StatusResponseDto
import com.ant.network.dto.TvShowDto
import com.ant.network.dto.TvShowResultsPageDto

/** TMDb Movies API contract for fetching movie listings and details. */
interface TmdbMoviesApi {
    /** Fetches popular movies for the given [page]. */
    suspend fun getPopular(page: Int): MovieResultsPageDto

    /** Fetches top-rated movies for the given [page]. */
    suspend fun getTopRated(page: Int): MovieResultsPageDto

    /** Fetches now-playing movies for the given [page]. */
    suspend fun getNowPlaying(page: Int): MovieResultsPageDto

    /** Fetches upcoming movies for the given [page]. */
    suspend fun getUpcoming(page: Int): MovieResultsPageDto

    /**
     * Fetches full movie details.
     *
     * @param movieId TMDb movie identifier.
     * @param appendToResponse comma-separated list of sub-requests (e.g. "credits,videos").
     */
    suspend fun getDetails(movieId: Int, appendToResponse: String?): MovieDto
}

/** TMDb TV Series API contract for fetching TV show listings and details. */
interface TmdbTvSeriesApi {
    /** Fetches popular TV shows for the given [page]. */
    suspend fun getPopular(page: Int): TvShowResultsPageDto

    /** Fetches top-rated TV shows for the given [page]. */
    suspend fun getTopRated(page: Int): TvShowResultsPageDto

    /** Fetches currently on-the-air TV shows for the given [page]. */
    suspend fun getOnTheAir(page: Int): TvShowResultsPageDto

    /** Fetches TV shows airing today for the given [page]. */
    suspend fun getAiringToday(page: Int): TvShowResultsPageDto

    /**
     * Fetches full TV series details.
     *
     * @param tvSeriesId TMDb TV series identifier.
     * @param appendToResponse comma-separated list of sub-requests (e.g. "credits,videos").
     */
    suspend fun getDetails(tvSeriesId: Int, appendToResponse: String?): TvShowDto
}

/** TMDb Genre API contract for fetching genre lists. */
interface TmdbGenreApi {
    /** Fetches the full list of movie genres. */
    suspend fun getMovieGenres(): GenreResultsDto

    /** Fetches the full list of TV show genres. */
    suspend fun getTvGenres(): GenreResultsDto
}

/** TMDb Search API contract for searching movies and TV shows. */
interface TmdbSearchApi {
    /** Searches movies matching the given [query] for the given [page]. */
    suspend fun searchMovies(query: String, page: Int): MovieResultsPageDto

    /** Searches TV shows matching the given [query] for the given [page]. */
    suspend fun searchTvShows(query: String, page: Int): TvShowResultsPageDto
}

/** TMDb Authentication API contract for login, session management, and account operations. */
interface TmdbAuthApi {
    /** Creates a new unauthenticated request token. */
    suspend fun createRequestToken(): RequestTokenDto

    /** Validates a request token with the user's TMDb credentials. */
    suspend fun validateTokenWithLogin(username: String, password: String, requestToken: String): RequestTokenDto

    /** Creates an authenticated session from a validated request token. */
    suspend fun createSession(requestToken: String): SessionDto

    /** Deletes (logs out) the session identified by [sessionId]. */
    suspend fun deleteSession(sessionId: String): StatusResponseDto

    /** Fetches account details for the given [sessionId]. */
    suspend fun getAccountDetails(sessionId: String): AccountDto

    /** Marks or unmarks a media item as favorite on the user's TMDb account. */
    suspend fun markAsFavorite(accountId: Int, sessionId: String, mediaType: String, mediaId: Int, favorite: Boolean): StatusResponseDto

    /** Fetches favorite movies from the user's TMDb account. */
    suspend fun getFavoriteMovies(accountId: Int, sessionId: String, page: Int = 1): MovieResultsPageDto

    /** Fetches favorite TV shows from the user's TMDb account. */
    suspend fun getFavoriteTvShows(accountId: Int, sessionId: String, page: Int = 1): TvShowResultsPageDto
}
