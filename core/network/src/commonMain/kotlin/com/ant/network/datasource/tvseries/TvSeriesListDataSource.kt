package com.ant.network.datasource.tvseries

import com.ant.shared.exceptions.withRetry
import com.ant.models.entities.TvShow
import com.ant.models.model.PaginatedResult
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import com.ant.network.api.TmdbGenreApi
import com.ant.network.api.TmdbTvSeriesApi
import com.ant.network.mappers.tvseries.TvSeriesMapper

/**
 * Fetches a paginated list of TV shows by category (popular, top-rated, etc.) from the TMDb API
 * and maps the results to domain [TvShow] models.
 */
class TvSeriesListDataSource(
    private val tvSeriesApi: TmdbTvSeriesApi,
    private val genreApi: TmdbGenreApi,
    private val tvSeriesMapper: TvSeriesMapper,
) {
    /**
     * Fetches TV shows for the category and page specified in [params].
     *
     * @return paginated result of mapped [TvShow] domain models.
     */
    suspend operator fun invoke(params: RequestType.TvShowRequest): PaginatedResult<TvShow> {
        val tvResults = withRetry {
            when (params.tvSeriesType) {
                TvShowType.ONTV_NOW -> tvSeriesApi.getOnTheAir(params.page)
                TvShowType.POPULAR -> tvSeriesApi.getPopular(params.page)
                TvShowType.TOP_RATED -> tvSeriesApi.getTopRated(params.page)
                TvShowType.AIRING_TODAY -> tvSeriesApi.getAiringToday(params.page)
            }
        }

        val genres = genreApi.getTvGenres()
        return tvSeriesMapper.map(Pair(tvResults, genres))
    }
}
