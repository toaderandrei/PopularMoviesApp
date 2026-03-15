package com.ant.domain.usecases.tvseries

import com.ant.common.exceptions.NetworkError
import com.ant.domain.repositories.FavoriteRepository
import com.ant.domain.repositories.TvSeriesRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.TvShowDetails
import com.ant.models.model.Result
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Saves TV series details to local favorites and attempts to sync the favorite to the
 * remote TMDb account if an active session exists.
 *
 * Remote sync failures are silently caught -- the TV series is still saved locally with
 * an unsynced status.
 */
class SaveTvSeriesDetailsUseCase constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    private val favoriteRepository: FavoriteRepository,
    private val sessionManager: SessionManager,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @param parameters the TV series details to save as a favorite. */
    operator fun invoke(parameters: TvShowDetails): Flow<Result<Unit>> {
        return resultFlow(dispatcher) {
            tvSeriesRepository.saveTvSeriesDetails(parameters)
                .also {
                    sessionManager.getSessionId()?.let { sessionId ->
                        try {
                            val synced = favoriteRepository.syncFavoriteToRemote(
                                RequestType.FavoriteRequest(
                                    sessionId = sessionId,
                                    favorite = true,
                                    favoriteId = parameters.tvSeriesData.id.toInt(),
                                    mediaType = FavoriteType.TV
                                )
                            )
                            if (synced) {
                                favoriteRepository.updateSyncStatus(
                                    id = parameters.tvSeriesData.id,
                                    mediaType = FavoriteType.TV,
                                    synced = true,
                                )
                            }
                        } catch (e: NetworkError.Unauthorized) {
                            throw e  // Propagate — user needs to re-login
                        } catch (_: NetworkError) {
                            // Transient failure — saved locally, will sync later
                        }
                    }
                }
        }
    }
}
