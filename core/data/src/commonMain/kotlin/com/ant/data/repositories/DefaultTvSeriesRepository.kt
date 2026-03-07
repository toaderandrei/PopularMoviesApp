package com.ant.data.repositories

import com.ant.data.repositories.tvseries.DeleteTvSeriesDetailsRepository
import com.ant.data.repositories.tvseries.LoadFavoredTvSeriesListRepository
import com.ant.data.repositories.tvseries.LoadTvSeriesDetailsSummaryRepository
import com.ant.data.repositories.tvseries.LoadTvSeriesListRepository
import com.ant.data.repositories.tvseries.SaveTvSeriesDetailsRepository
import com.ant.domain.repositories.TvSeriesRepository
import com.ant.models.entities.TvShow
import com.ant.models.entities.TvShowDetails
import com.ant.models.model.PaginatedResult
import com.ant.models.request.RequestType


/**
 * Default [TvSeriesRepository] implementation that delegates to single-responsibility
 * repositories for listing, details, saving, and deleting TV series.
 */
class DefaultTvSeriesRepository constructor(
    private val loadTvSeriesListRepository: LoadTvSeriesListRepository,
    private val loadTvSeriesDetailsSummaryRepository: LoadTvSeriesDetailsSummaryRepository,
    private val saveTvSeriesDetailsRepository: SaveTvSeriesDetailsRepository,
    private val deleteTvSeriesDetailsRepository: DeleteTvSeriesDetailsRepository,
    private val loadFavoredTvSeriesListRepository: LoadFavoredTvSeriesListRepository,
) : TvSeriesRepository {

    /** Fetches a paginated list of TV series from TMDb by category. */
    override suspend fun getTvSeriesList(params: RequestType.TvShowRequest): PaginatedResult<TvShow> {
        return loadTvSeriesListRepository.performRequest(params)
    }

    /** Loads TV series details from the local database if available, otherwise from the network. */
    override suspend fun getTvSeriesDetails(params: RequestType.TvSeriesRequestDetails): TvShowDetails {
        return loadTvSeriesDetailsSummaryRepository.performRequest(params)
    }

    /** Saves TV series details to the local database as a favorite. */
    override suspend fun saveTvSeriesDetails(params: TvShowDetails) {
        saveTvSeriesDetailsRepository.performRequest(params)
    }

    /** Deletes TV series details and associated data from the local database. */
    override suspend fun deleteTvSeriesDetails(params: TvShowDetails) {
        deleteTvSeriesDetailsRepository.performRequest(params)
    }

    /** Loads TV series from the local database filtered by their favored status. */
    override suspend fun getFavoredTvSeries(favored: Boolean): List<TvShow> {
        return loadFavoredTvSeriesListRepository.performRequest(favored)
    }
}
