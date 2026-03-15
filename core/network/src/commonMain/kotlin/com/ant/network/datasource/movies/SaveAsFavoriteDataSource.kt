package com.ant.network.datasource.movies

import com.ant.common.exceptions.NetworkError
import com.ant.shared.logger.Logger
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.network.dto.AccountDto
import com.ant.network.dto.FavoriteRequestBody
import com.ant.network.dto.StatusResponseDto
import com.ant.network.ktx.safeResourceGet
import com.ant.network.ktx.safeResourcePost
import com.ant.network.resources.AccountResources
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class SaveAsFavoriteDataSource(
    private val client: HttpClient,
    private val logger: Logger,
) {
    suspend fun invoke(params: RequestType.FavoriteRequest): Boolean {
        val account: AccountDto = client.safeResourceGet(
            resource = AccountResources(session_id = params.sessionId),
            maxAttempts = 1,
        )

        val accountId = account.id
            ?: throw NetworkError.Unknown(message = "Account ID missing from response")

        val mediaType = when (params.mediaType) {
            FavoriteType.TV -> "tv"
            FavoriteType.MOVIE -> "movie"
            FavoriteType.PERSON -> "person"
        }

        val response: StatusResponseDto = client.safeResourcePost(
            resource = AccountResources.MarkFavorite(
                accountId = accountId,
                session_id = params.sessionId,
            ),
            maxAttempts = 1,
        ) {
            contentType(ContentType.Application.Json)
            setBody(FavoriteRequestBody(mediaType, params.favoriteId, params.favorite))
        }

        logger.d("Favorite update: ${response.statusMessage} and status code: ${response.statusCode}")
        return (response.statusCode ?: 0) > 0
    }
}
