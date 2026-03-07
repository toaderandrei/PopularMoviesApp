package com.ant.network.datasource.tvseries

import com.ant.shared.exceptions.withRetry
import com.ant.models.entities.TvShowDetails
import com.ant.models.request.RequestType
import com.ant.models.request.TvSeriesAppendToResponseItem
import com.ant.network.api.TmdbTvSeriesApi
import com.ant.network.mappers.tvseries.TvSeriesDetailsMapper

/**
 * Fetches extended TV series details (including credits, videos) from the TMDb API
 * and maps the response to a domain [TvShowDetails] model.
 */
class TvSeriesDetailsExtendedSummaryDataSource(
    private val tvSeriesApi: TmdbTvSeriesApi,
    private val tvSeriesDetailsMapper: TvSeriesDetailsMapper,
) {
    /**
     * Fetches and maps TV series details for the show specified in [params].
     *
     * @param params contains the TMDb TV series ID and the list of append-to-response items.
     * @return mapped [TvShowDetails] domain model.
     */
    suspend operator fun invoke(params: RequestType.TvSeriesRequestDetails): TvShowDetails {
        val appendStr = params.appendToResponseItems.joinToString(",") { it.toApiString() }
        val tvShowDto = withRetry {
            tvSeriesApi.getDetails(
                tvSeriesId = params.tmdbTvSeriesId.toInt(),
                appendToResponse = appendStr.ifEmpty { null },
            )
        }
        return tvSeriesDetailsMapper.map(tvShowDto)
    }
}

private fun TvSeriesAppendToResponseItem.toApiString(): String = when (this) {
    TvSeriesAppendToResponseItem.CREDITS -> "credits"
    TvSeriesAppendToResponseItem.REVIEWS -> "reviews"
    TvSeriesAppendToResponseItem.VIDEOS -> "videos"
}
