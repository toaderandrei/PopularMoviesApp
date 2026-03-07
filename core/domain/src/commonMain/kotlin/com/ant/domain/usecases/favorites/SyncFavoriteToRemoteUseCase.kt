package com.ant.domain.usecases.favorites

import com.ant.domain.repositories.FavoriteRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.model.Result
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Syncs a locally favorited item to the user's remote TMDb account.
 *
 * Requires an active session. After a successful remote sync, the local sync status
 * flag is updated to reflect the synchronized state.
 */
class SyncFavoriteToRemoteUseCase constructor(
    private val favoriteRepository: FavoriteRepository,
    private val sessionManager: SessionManager,
    private val dispatcher: CoroutineDispatcher,
) {
    /**
     * Syncs the favorite identified by [id] and [mediaType] to the remote TMDb account.
     * @return a Flow emitting the sync result (true if remote sync succeeded).
     * @throws IllegalStateException if no active session exists.
     */
    operator fun invoke(id: Long, mediaType: FavoriteType): Flow<Result<Boolean>> {
        return resultFlow(dispatcher) {
            val sessionId = sessionManager.getSessionId()
                ?: throw IllegalStateException("Login required to sync favorites to TMDb")

            val synced = favoriteRepository.syncFavoriteToRemote(
                RequestType.FavoriteRequest(
                    sessionId = sessionId,
                    favorite = true,
                    favoriteId = id.toInt(),
                    mediaType = mediaType,
                )
            )

            if (synced) {
                favoriteRepository.updateSyncStatus(
                    id = id,
                    mediaType = mediaType,
                    synced = true,
                )
            }

            synced
        }
    }
}
