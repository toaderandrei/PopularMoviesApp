package com.ant.network.datasource.tvseries

import com.ant.models.entities.TvShowDetails
import com.ant.models.request.RequestType
import com.ant.models.request.TvSeriesAppendToResponseItem
import com.ant.network.dto.TvShowDto
import com.ant.network.ktx.safeResourceGet
import com.ant.network.mappers.tvseries.TvSeriesDetailsMapper
import com.ant.network.resources.TvSeriesResources
import io.ktor.client.HttpClient

class TvSeriesDetailsExtendedSummaryDataSource(
    private val client: HttpClient,
    private val tvSeriesDetailsMapper: TvSeriesDetailsMapper,
) {
    suspend operator fun invoke(params: RequestType.TvSeriesRequestDetails): TvShowDetails {
        val appendStr = params.appendToResponseItems.joinToString(",") { it.toApiString() }
        val tvShowDto: TvShowDto = client.safeResourceGet(
            TvSeriesResources.Details(
                id = params.tmdbTvSeriesId.toInt(),
                append_to_response = appendStr.ifEmpty { null },
            )
        )
        return tvSeriesDetailsMapper.map(tvShowDto)
    }
}

private fun TvSeriesAppendToResponseItem.toApiString(): String = when (this) {
    TvSeriesAppendToResponseItem.CREDITS -> "credits"
    TvSeriesAppendToResponseItem.REVIEWS -> "reviews"
    TvSeriesAppendToResponseItem.VIDEOS -> "videos"
}
