package com.ant.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Paginated response containing a list of [TvShowDto] results from the TMDb API. */
@Serializable
data class TvShowResultsPageDto(
    val page: Int? = null,
    val results: List<TvShowDto>? = null,
    @SerialName("total_pages") val totalPages: Int? = null,
    @SerialName("total_results") val totalResults: Int? = null,
)

/** TMDb TV show data transfer object, used for both list items and detail responses. */
@Serializable
data class TvShowDto(
    val id: Int? = null,
    val name: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("vote_count") val voteCount: Int? = null,
    val overview: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("genre_ids") val genreIds: List<Int>? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("last_air_date") val lastAirDate: String? = null,
    @SerialName("number_of_episodes") val numberOfEpisodes: Int? = null,
    @SerialName("number_of_seasons") val numberOfSeasons: Int? = null,
    val status: String? = null,
    val genres: List<GenreDto>? = null,
    val credits: CreditsDto? = null,
    val videos: VideoResultsDto? = null,
)
