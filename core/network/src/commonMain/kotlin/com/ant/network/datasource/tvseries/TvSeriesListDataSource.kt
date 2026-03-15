package com.ant.network.datasource.tvseries

import com.ant.models.entities.TvShow
import com.ant.models.model.PaginatedResult
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.TvShowResultsPageDto
import com.ant.network.ktx.safeResourceGet
import com.ant.network.mappers.tvseries.TvSeriesMapper
import com.ant.network.resources.GenreResources
import com.ant.network.resources.TvSeriesResources
import io.ktor.client.HttpClient

class TvSeriesListDataSource(
    private val client: HttpClient,
    private val tvSeriesMapper: TvSeriesMapper,
) {
    suspend operator fun invoke(params: RequestType.TvShowRequest): PaginatedResult<TvShow> {
        val tvResults: TvShowResultsPageDto = when (params.tvSeriesType) {
            TvShowType.ONTV_NOW -> client.safeResourceGet(TvSeriesResources.OnTheAir(page = params.page))
            TvShowType.POPULAR -> client.safeResourceGet(TvSeriesResources.Popular(page = params.page))
            TvShowType.TOP_RATED -> client.safeResourceGet(TvSeriesResources.TopRated(page = params.page))
            TvShowType.AIRING_TODAY -> client.safeResourceGet(TvSeriesResources.AiringToday(page = params.page))
        }

        val genres: GenreResultsDto = client.safeResourceGet(GenreResources.TvGenres())
        return tvSeriesMapper.map(Pair(tvResults, genres))
    }
}
