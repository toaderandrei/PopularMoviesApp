package com.ant.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Paginated response containing a list of [MovieDto] results from the TMDb API. */
@Serializable
data class MovieResultsPageDto(
    val page: Int? = null,
    val results: List<MovieDto>? = null,
    @SerialName("total_pages") val totalPages: Int? = null,
    @SerialName("total_results") val totalResults: Int? = null,
)

/** TMDb movie data transfer object, used for both list items and detail responses. */
@Serializable
data class MovieDto(
    val id: Int? = null,
    val title: String? = null,
    @SerialName("original_title") val originalTitle: String? = null,
    @SerialName("vote_count") val voteCount: Int? = null,
    val overview: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int>? = null,
    val runtime: Int? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    val genres: List<GenreDto>? = null,
    val credits: CreditsDto? = null,
    val reviews: ReviewResultsDto? = null,
    val videos: VideoResultsDto? = null,
)

/** TMDb genre identifier and name pair. */
@Serializable
data class GenreDto(
    val id: Int? = null,
    val name: String? = null,
)

/** Wrapper for a list of [GenreDto] returned by the TMDb genre endpoints. */
@Serializable
data class GenreResultsDto(
    val genres: List<GenreDto>? = null,
)

/** TMDb credits response containing cast and crew lists. */
@Serializable
data class CreditsDto(
    val id: Int? = null,
    val cast: List<CastDto>? = null,
    val crew: List<CrewDto>? = null,
)

/** TMDb cast member data transfer object. */
@Serializable
data class CastDto(
    @SerialName("cast_id") val castId: Int? = null,
    val name: String? = null,
    val character: String? = null,
    val order: Int? = null,
    @SerialName("profile_path") val profilePath: String? = null,
)

/** TMDb crew member data transfer object. */
@Serializable
data class CrewDto(
    val name: String? = null,
    val department: String? = null,
    val job: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
)

/** Wrapper for a list of [ReviewDto] from the TMDb reviews endpoint. */
@Serializable
data class ReviewResultsDto(
    val results: List<ReviewDto>? = null,
)

/** TMDb user review data transfer object. */
@Serializable
data class ReviewDto(
    val author: String? = null,
    val content: String? = null,
    val url: String? = null,
)

/** Wrapper for a list of [VideoDto] from the TMDb videos endpoint. */
@Serializable
data class VideoResultsDto(
    val results: List<VideoDto>? = null,
)

/** TMDb video (trailer, teaser, clip, etc.) data transfer object. */
@Serializable
data class VideoDto(
    val key: String? = null,
    @SerialName("iso_639_1") val iso6391: String? = null,
    @SerialName("iso_3166_1") val iso31661: String? = null,
    val size: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val site: String? = null,
)
