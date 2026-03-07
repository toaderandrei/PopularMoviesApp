package com.ant.domain.repositories

import com.ant.models.entities.TvShow
import com.ant.models.entities.TvShowDetails
import com.ant.models.model.PaginatedResult
import com.ant.models.request.RequestType

/**
 * Repository for TV series data operations including listing, details, and local favorites persistence.
 */
interface TvSeriesRepository {
    /** Fetches a paginated list of TV series for the given category/page request. */
    suspend fun getTvSeriesList(params: RequestType.TvShowRequest): PaginatedResult<TvShow>

    /** Fetches extended TV series details for a specific show. */
    suspend fun getTvSeriesDetails(params: RequestType.TvSeriesRequestDetails): TvShowDetails

    /** Saves TV series details to local storage as a favorite. */
    suspend fun saveTvSeriesDetails(params: TvShowDetails)

    /** Deletes TV series details from local favorites storage. */
    suspend fun deleteTvSeriesDetails(params: TvShowDetails)

    /**
     * Retrieves locally stored TV series filtered by favorite status.
     * @param favored true to return only favored TV series, false for non-favored.
     */
    suspend fun getFavoredTvSeries(favored: Boolean): List<TvShow>
}
