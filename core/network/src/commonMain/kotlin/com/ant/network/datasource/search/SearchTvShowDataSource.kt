package com.ant.network.datasource.search

import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.TvShowResultsPageDto
import com.ant.network.ktx.safeResourceGet
import com.ant.network.mappers.tvseries.TvSeriesMapper
import com.ant.network.resources.GenreResources
import com.ant.network.resources.SearchResources
import io.ktor.client.HttpClient

class SearchTvShowDataSource(
    private val client: HttpClient,
    private val tvSeriesMapper: TvSeriesMapper,
) {
    suspend operator fun invoke(params: RequestType.SearchTvShowRequest): List<TvShow> {
        val searchResults: TvShowResultsPageDto = client.safeResourceGet(
            SearchResources.TvShows(query = params.query, page = params.page)
        )
        val genres: GenreResultsDto = client.safeResourceGet(GenreResources.TvGenres())
        return tvSeriesMapper.map(Pair(searchResults, genres)).items
    }
}
