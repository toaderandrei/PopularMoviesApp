package com.ant.network.datasource.favorites

import com.ant.network.dto.TvShowResultsPageDto
import com.ant.network.ktx.safeResourceGet
import com.ant.network.resources.AccountResources
import io.ktor.client.HttpClient

class FetchFavoriteTvShowsDataSource(
    private val client: HttpClient,
) {
    suspend fun fetchFavoriteTvShows(
        accountId: Int,
        sessionId: String,
        page: Int = 1,
    ): TvShowResultsPageDto {
        return client.safeResourceGet(
            resource = AccountResources.FavoriteTvShows(
                accountId = accountId,
                session_id = sessionId,
                page = page,
            ),
            maxAttempts = 1,
        )
    }
}
