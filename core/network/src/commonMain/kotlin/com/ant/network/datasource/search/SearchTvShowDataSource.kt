package com.ant.network.datasource.search

import com.ant.shared.exceptions.withRetry
import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import com.ant.network.api.TmdbGenreApi
import com.ant.network.api.TmdbSearchApi
import com.ant.network.mappers.tvseries.TvSeriesMapper

/**
 * Searches for TV shows on the TMDb API by query string
 * and maps the results to domain [TvShow] models.
 */
class SearchTvShowDataSource(
    private val searchApi: TmdbSearchApi,
    private val genreApi: TmdbGenreApi,
    private val tvSeriesMapper: TvSeriesMapper,
) {
    /**
     * Executes a TV show search for the query and page specified in [params].
     *
     * @return list of mapped [TvShow] domain models.
     */
    suspend operator fun invoke(params: RequestType.SearchTvShowRequest): List<TvShow> {
        val searchResults = withRetry {
            searchApi.searchTvShows(params.query, params.page)
        }
        val genres = genreApi.getTvGenres()
        return tvSeriesMapper.map(Pair(searchResults, genres)).items
    }
}
