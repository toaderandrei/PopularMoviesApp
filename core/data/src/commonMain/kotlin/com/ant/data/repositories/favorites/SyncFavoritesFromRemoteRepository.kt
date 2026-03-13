package com.ant.data.repositories.favorites

import com.ant.database.database.MoviesDb
import com.ant.database.mapper.toEntity
import com.ant.models.session.SessionManager
import com.ant.network.api.TmdbAuthApi
import com.ant.network.datasource.favorites.FetchFavoriteMoviesDataSource
import com.ant.network.datasource.favorites.FetchFavoriteTvShowsDataSource
import com.ant.network.mappers.movies.MovieDataMapper
import com.ant.network.mappers.tvseries.TvSeriesDataMapper

/**
 * Repository for syncing favorites from the remote TMDb account to the local database.
 * Performs bidirectional sync:
 * - Fetches favorites from TMDb backend
 * - Compares with local database
 * - Adds missing remote favorites to local DB (marked as synced)
 * - Marks items that exist in both as synced
 * - Keeps local-only favorites (to be synced later)
 */
class SyncFavoritesFromRemoteRepository(
    private val sessionManager: SessionManager,
    private val authApi: TmdbAuthApi,
    private val fetchFavoriteMoviesDataSource: FetchFavoriteMoviesDataSource,
    private val fetchFavoriteTvShowsDataSource: FetchFavoriteTvShowsDataSource,
    private val moviesDb: MoviesDb,
    private val movieDataMapper: MovieDataMapper,
    private val tvSeriesDataMapper: TvSeriesDataMapper,
) {
    suspend fun syncFavorites() {
        val sessionId = sessionManager.getSessionId() ?: return

        // Get account ID from TMDb
        val account = authApi.getAccountDetails(sessionId)
        val accountId = account.id ?: return

        // Sync favorite movies
        syncFavoriteMovies(accountId, sessionId)

        // Sync favorite TV shows
        syncFavoriteTvShows(accountId, sessionId)
    }

    private suspend fun syncFavoriteMovies(accountId: Int, sessionId: String) {
        try {
            // Fetch favorites from backend
            val remoteFavoritesDto = fetchFavoriteMoviesDataSource.fetchFavoriteMovies(
                accountId = accountId,
                sessionId = sessionId,
                page = 1 // For now, just fetch first page
            )

            val remoteFavorites = remoteFavoritesDto.results?.map { movieDataMapper.map(it) } ?: emptyList()
            val remoteFavoriteIds = remoteFavorites.map { it.id }.toSet()

            // Get current local favorites
            val localFavorites = moviesDb.moviesDao().loadFavoredMovies(favored = true)
            val localFavoriteIds = localFavorites.map { it.id.toLong() }.toSet()

            // 1. Add remote favorites that aren't in local DB
            val missingInLocal = remoteFavorites.filter { it.id !in localFavoriteIds }
            missingInLocal.forEach { movie ->
                moviesDb.moviesDao().insert(
                    movie.toEntity().copy(favored = true, syncedToRemote = true)
                )
            }

            // 2. Mark items that exist in both as synced
            val existingInBoth = localFavoriteIds.intersect(remoteFavoriteIds)
            existingInBoth.forEach { id ->
                moviesDb.moviesDao().updateSyncStatus(id, synced = true)
            }
        } catch (e: Exception) {
            // Log error but don't throw - allow partial sync
            e.printStackTrace()
        }
    }

    private suspend fun syncFavoriteTvShows(accountId: Int, sessionId: String) {
        try {
            // Fetch favorites from backend
            val remoteFavoritesDto = fetchFavoriteTvShowsDataSource.fetchFavoriteTvShows(
                accountId = accountId,
                sessionId = sessionId,
                page = 1 // For now, just fetch first page
            )

            val remoteFavorites = remoteFavoritesDto.results?.map { tvSeriesDataMapper.map(it) } ?: emptyList()
            val remoteFavoriteIds = remoteFavorites.map { it.id }.toSet()

            // Get current local favorites
            val localFavorites = moviesDb.tvSeriesDao().loadFavoredTvSeriesData(favored = true)
            val localFavoriteIds = localFavorites.map { it.id.toLong() }.toSet()

            // 1. Add remote favorites that aren't in local DB
            val missingInLocal = remoteFavorites.filter { it.id !in localFavoriteIds }
            missingInLocal.forEach { tvShow ->
                moviesDb.tvSeriesDao().insert(
                    tvShow.toEntity().copy(favored = true, syncedToRemote = true)
                )
            }

            // 2. Mark items that exist in both as synced
            val existingInBoth = localFavoriteIds.intersect(remoteFavoriteIds)
            existingInBoth.forEach { id ->
                moviesDb.tvSeriesDao().updateSyncStatus(id, synced = true)
            }
        } catch (e: Exception) {
            // Log error but don't throw - allow partial sync
            e.printStackTrace()
        }
    }
}
