package com.ant.network.datasource.favorites

import com.ant.network.dto.MovieResultsPageDto
import com.ant.network.ktx.safeResourceGet
import com.ant.network.resources.AccountResources
import io.ktor.client.HttpClient

class FetchFavoriteMoviesDataSource(
    private val client: HttpClient,
) {
    suspend fun fetchFavoriteMovies(
        accountId: Int,
        sessionId: String,
        page: Int = 1,
    ): MovieResultsPageDto {
        return client.safeResourceGet(
            resource = AccountResources.FavoriteMovies(
                accountId = accountId,
                session_id = sessionId,
                page = page,
            ),
            maxAttempts = 1,
        )
    }
}
