package com.ant.domain.usecases.favorites

import com.ant.domain.repositories.FavoriteRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Use case to sync favorites from TMDb backend to local database.
 *
 * Fetches favorites from the remote TMDb account and performs bidirectional sync:
 * - Adds remote favorites that aren't in local DB
 * - Marks items that exist in both as synced
 * - Keeps local-only favorites (to be synced later)
 *
 * @param dispatcher Coroutine dispatcher for background execution
 * @param repository Favorite repository for sync operations
 */
class SyncFavoritesFromRemoteUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val repository: FavoriteRepository,
) {
    operator fun invoke(): Flow<Result<Unit>> = resultFlow(dispatcher) {
        repository.syncFavoritesFromRemote()
    }
}
