package com.ant.network.datasource.favorites

import com.ant.network.api.TmdbAuthApi
import com.ant.network.dto.MovieResultsPageDto

/**
 * Data source for fetching favorite movies from TMDb backend.
 */
class FetchFavoriteMoviesDataSource(
    private val authApi: TmdbAuthApi
) {
    suspend fun fetchFavoriteMovies(
        accountId: Int,
        sessionId: String,
        page: Int = 1
    ): MovieResultsPageDto {
        return authApi.getFavoriteMovies(accountId, sessionId, page)
    }
}
