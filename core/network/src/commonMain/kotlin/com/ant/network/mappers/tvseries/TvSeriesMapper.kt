package com.ant.network.mappers.tvseries

import com.ant.models.entities.TvShow
import com.ant.models.model.PaginatedResult
import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.TvShowResultsPageDto
import com.ant.network.mappers.Mapper

/**
 * Maps a paginated TV show results page and genre list to a [PaginatedResult] of [TvShow] domain models.
 *
 * Delegates individual TV show mapping to [TvSeriesDataMapper].
 */
class TvSeriesMapper(
    private val tvSeriesDataMapper: TvSeriesDataMapper,
) : Mapper<Pair<TvShowResultsPageDto, GenreResultsDto>, PaginatedResult<TvShow>> {
    override suspend fun map(from: Pair<TvShowResultsPageDto, GenreResultsDto>): PaginatedResult<TvShow> {
        val resultsPage = from.first
        val items = resultsPage.results?.map { tvShowDto ->
            tvSeriesDataMapper.map(tvShowDto)
        } ?: emptyList()
        return PaginatedResult(
            items = items,
            page = resultsPage.page ?: 1,
            totalPages = resultsPage.totalPages ?: 1,
        )
    }
}
