package com.ant.network.datasource.movies

import com.ant.models.entities.MovieDetails
import com.ant.models.request.MovieAppendToResponseItem
import com.ant.models.request.RequestType
import com.ant.network.dto.MovieDto
import com.ant.network.ktx.safeResourceGet
import com.ant.network.mappers.movies.MovieDetailsMapper
import com.ant.network.resources.MovieResources
import io.ktor.client.HttpClient

class MovieDetailsExtendedSummaryDataSource(
    private val client: HttpClient,
    private val movieDetailsMapper: MovieDetailsMapper,
) {
    suspend operator fun invoke(params: RequestType.MovieRequestDetails): MovieDetails {
        val appendStr = params.appendToResponseItems.joinToString(",") { it.toApiString() }
        val movieDto: MovieDto = client.safeResourceGet(
            MovieResources.Details(
                id = params.tmdbMovieId.toInt(),
                append_to_response = appendStr.ifEmpty { null },
            )
        )
        return movieDetailsMapper.map(movieDto)
    }
}

private fun MovieAppendToResponseItem.toApiString(): String = when (this) {
    MovieAppendToResponseItem.CREDITS -> "credits"
    MovieAppendToResponseItem.REVIEWS -> "reviews"
    MovieAppendToResponseItem.VIDEOS -> "videos"
    MovieAppendToResponseItem.MOVIE_CREDITS -> "movie_credits"
}
