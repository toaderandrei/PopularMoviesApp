package com.ant.network.datasource.favorites

import com.ant.network.api.TmdbAuthApi
import com.ant.network.dto.TvShowResultsPageDto

/**
 * Data source for fetching favorite TV shows from TMDb backend.
 */
class FetchFavoriteTvShowsDataSource(
    private val authApi: TmdbAuthApi
) {
    suspend fun fetchFavoriteTvShows(
        accountId: Int,
        sessionId: String,
        page: Int = 1
    ): TvShowResultsPageDto {
        return authApi.getFavoriteTvShows(accountId, sessionId, page)
    }
}
