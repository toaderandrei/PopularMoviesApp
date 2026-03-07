package com.ant.network.mappers.movies

import com.ant.models.entities.MovieData
import com.ant.models.model.PaginatedResult
import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.MovieResultsPageDto
import com.ant.network.mappers.Mapper

/**
 * Maps a paginated movie results page and genre list to a [PaginatedResult] of [MovieData] domain models.
 *
 * Delegates individual movie mapping to [MovieDataMapper].
 */
class MoviesListMapper(
    private val movieDataMapper: MovieDataMapper,
) : Mapper<Pair<MovieResultsPageDto, GenreResultsDto>, PaginatedResult<MovieData>> {
    override suspend fun map(from: Pair<MovieResultsPageDto, GenreResultsDto>): PaginatedResult<MovieData> {
        val resultsPage = from.first
        val items = resultsPage.results?.map { movieDto ->
            movieDataMapper.map(movieDto)
        } ?: emptyList()
        return PaginatedResult(
            items = items,
            page = resultsPage.page ?: 1,
            totalPages = resultsPage.totalPages ?: 1,
        )
    }
}
