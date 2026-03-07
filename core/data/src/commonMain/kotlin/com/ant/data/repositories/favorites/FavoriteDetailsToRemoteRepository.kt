package com.ant.data.repositories.favorites

import com.ant.models.request.RequestType
import com.ant.network.datasource.movies.SaveAsFavoriteDataSource


/**
 * Sends a favorite/unfavorite request to the TMDb remote API.
 */
class FavoriteDetailsToRemoteRepository constructor(
    private val saveMovieAsFavoriteDataSource: SaveAsFavoriteDataSource,
) {
    /**
     * Posts the favorite state change to TMDb.
     *
     * @return `true` if the remote API accepted the request.
     */
    suspend fun performRequest(params: RequestType.FavoriteRequest): Boolean {
        return saveMovieAsFavoriteDataSource.invoke(params)
    }
}