package com.ant.network.datasource.movies

import com.ant.shared.logger.Logger
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.network.api.TmdbAuthApi

/**
 * Marks or unmarks a media item as favorite on the user's TMDb account
 * via the authentication API.
 */
class SaveAsFavoriteDataSource(
    private val authApi: TmdbAuthApi,
    private val logger: Logger,
) {
    /**
     * Sends a favorite toggle request to TMDb.
     *
     * @param params contains the session ID, media type, media ID, and favorite flag.
     * @return `true` if the API responded with a positive status code, `false` otherwise.
     */
    suspend fun invoke(params: RequestType.FavoriteRequest): Boolean {
        val account = authApi.getAccountDetails(params.sessionId)
        val accountId = account.id ?: return false

        val mediaType = when (params.mediaType) {
            FavoriteType.TV -> "tv"
            FavoriteType.MOVIE -> "movie"
            FavoriteType.PERSON -> "person"
        }

        val response = authApi.markAsFavorite(
            accountId = accountId,
            sessionId = params.sessionId,
            mediaType = mediaType,
            mediaId = params.favoriteId,
            favorite = params.favorite,
        )

        logger.d("Favorite update: ${response.statusMessage} and status code: ${response.statusCode}")
        return (response.statusCode ?: 0) > 0
    }
}
