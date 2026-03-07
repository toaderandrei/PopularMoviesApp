package com.ant.network.api

import com.ant.network.dto.AccountDto
import com.ant.network.dto.CreateSessionBody
import com.ant.network.dto.DeleteSessionBody
import com.ant.network.dto.FavoriteRequestBody
import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.MovieDto
import com.ant.network.dto.MovieResultsPageDto
import com.ant.network.dto.RequestTokenDto
import com.ant.network.dto.SessionDto
import com.ant.network.dto.StatusResponseDto
import com.ant.network.dto.TvShowDto
import com.ant.network.dto.TvShowResultsPageDto
import com.ant.network.dto.ValidateTokenBody
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/** Ktor-based implementation of [TmdbMoviesApi]. */
class KtorTmdbMoviesApi(private val client: HttpClient) : TmdbMoviesApi {
    override suspend fun getPopular(page: Int): MovieResultsPageDto =
        client.get("3/movie/popular") { parameter("page", page) }.body()

    override suspend fun getTopRated(page: Int): MovieResultsPageDto =
        client.get("3/movie/top_rated") { parameter("page", page) }.body()

    override suspend fun getNowPlaying(page: Int): MovieResultsPageDto =
        client.get("3/movie/now_playing") { parameter("page", page) }.body()

    override suspend fun getUpcoming(page: Int): MovieResultsPageDto =
        client.get("3/movie/upcoming") { parameter("page", page) }.body()

    override suspend fun getDetails(movieId: Int, appendToResponse: String?): MovieDto =
        client.get("3/movie/$movieId") {
            appendToResponse?.let { parameter("append_to_response", it) }
        }.body()
}

/** Ktor-based implementation of [TmdbTvSeriesApi]. */
class KtorTmdbTvSeriesApi(private val client: HttpClient) : TmdbTvSeriesApi {
    override suspend fun getPopular(page: Int): TvShowResultsPageDto =
        client.get("3/tv/popular") { parameter("page", page) }.body()

    override suspend fun getTopRated(page: Int): TvShowResultsPageDto =
        client.get("3/tv/top_rated") { parameter("page", page) }.body()

    override suspend fun getOnTheAir(page: Int): TvShowResultsPageDto =
        client.get("3/tv/on_the_air") { parameter("page", page) }.body()

    override suspend fun getAiringToday(page: Int): TvShowResultsPageDto =
        client.get("3/tv/airing_today") { parameter("page", page) }.body()

    override suspend fun getDetails(tvSeriesId: Int, appendToResponse: String?): TvShowDto =
        client.get("3/tv/$tvSeriesId") {
            appendToResponse?.let { parameter("append_to_response", it) }
        }.body()
}

/** Ktor-based implementation of [TmdbGenreApi]. */
class KtorTmdbGenreApi(private val client: HttpClient) : TmdbGenreApi {
    override suspend fun getMovieGenres(): GenreResultsDto =
        client.get("3/genre/movie/list").body()

    override suspend fun getTvGenres(): GenreResultsDto =
        client.get("3/genre/tv/list").body()
}

/** Ktor-based implementation of [TmdbSearchApi]. */
class KtorTmdbSearchApi(private val client: HttpClient) : TmdbSearchApi {
    override suspend fun searchMovies(query: String, page: Int): MovieResultsPageDto =
        client.get("3/search/movie") {
            parameter("query", query)
            parameter("page", page)
            parameter("include_adult", false)
        }.body()

    override suspend fun searchTvShows(query: String, page: Int): TvShowResultsPageDto =
        client.get("3/search/tv") {
            parameter("query", query)
            parameter("page", page)
            parameter("include_adult", false)
        }.body()
}

/** Ktor-based implementation of [TmdbAuthApi]. */
class KtorTmdbAuthApi(private val client: HttpClient) : TmdbAuthApi {
    override suspend fun createRequestToken(): RequestTokenDto =
        client.get("3/authentication/token/new").body()

    override suspend fun validateTokenWithLogin(
        username: String,
        password: String,
        requestToken: String,
    ): RequestTokenDto =
        client.post("3/authentication/token/validate_with_login") {
            contentType(ContentType.Application.Json)
            setBody(ValidateTokenBody(username, password, requestToken))
        }.body()

    override suspend fun createSession(requestToken: String): SessionDto =
        client.post("3/authentication/session/new") {
            contentType(ContentType.Application.Json)
            setBody(CreateSessionBody(requestToken))
        }.body()

    override suspend fun deleteSession(sessionId: String): StatusResponseDto =
        client.delete("3/authentication/session") {
            contentType(ContentType.Application.Json)
            setBody(DeleteSessionBody(sessionId))
        }.body()

    override suspend fun getAccountDetails(sessionId: String): AccountDto =
        client.get("3/account") { parameter("session_id", sessionId) }.body()

    override suspend fun markAsFavorite(
        accountId: Int,
        sessionId: String,
        mediaType: String,
        mediaId: Int,
        favorite: Boolean,
    ): StatusResponseDto =
        client.post("3/account/$accountId/favorite") {
            parameter("session_id", sessionId)
            contentType(ContentType.Application.Json)
            setBody(FavoriteRequestBody(mediaType, mediaId, favorite))
        }.body()
}
