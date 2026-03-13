package com.ant.network.datasource.movies

import com.ant.shared.exceptions.withRetry
import com.ant.models.entities.MovieDetails
import com.ant.models.request.MovieAppendToResponseItem
import com.ant.models.request.RequestType
import com.ant.network.api.TmdbMoviesApi
import com.ant.network.mappers.movies.MovieDetailsMapper

/**
 * Fetches extended movie details (including credits, reviews, videos) from the TMDb API
 * and maps the response to a domain [MovieDetails] model.
 */
class MovieDetailsExtendedSummaryDataSource(
    private val moviesApi: TmdbMoviesApi,
    private val movieDetailsMapper: MovieDetailsMapper,
) {
    /**
     * Fetches and maps movie details for the movie specified in [params].
     *
     * @param params contains the TMDb movie ID and the list of append-to-response items.
     * @return mapped [MovieDetails] domain model.
     */
    suspend operator fun invoke(params: RequestType.MovieRequestDetails): MovieDetails {
        val appendStr = params.appendToResponseItems.joinToString(",") { it.toApiString() }
        val movieDto = withRetry {
            moviesApi.getDetails(
                movieId = params.tmdbMovieId.toInt(),
                appendToResponse = appendStr.ifEmpty { null },
            )
        }
        return movieDetailsMapper.map(movieDto)
    }
}

private fun MovieAppendToResponseItem.toApiString(): String = when (this) {
    MovieAppendToResponseItem.CREDITS -> "credits"
    MovieAppendToResponseItem.REVIEWS -> "reviews"
    MovieAppendToResponseItem.VIDEOS -> "videos"
    MovieAppendToResponseItem.MOVIE_CREDITS -> "movie_credits"
}
