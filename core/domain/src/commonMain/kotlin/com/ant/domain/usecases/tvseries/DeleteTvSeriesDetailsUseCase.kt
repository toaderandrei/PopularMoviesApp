package com.ant.domain.usecases.tvseries

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
 * Removes a TV series from local favorites and syncs the removal to the remote TMDb account
 * if an active session exists.
 */
class DeleteTvSeriesDetailsUseCase constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    private val sessionManager: SessionManager,
    private val favoriteRepository: FavoriteRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @param parameters the TV series details to delete from favorites. */
    operator fun invoke(parameters: TvShowDetails): Flow<Result<Unit>> {
        return resultFlow(dispatcher) {
            tvSeriesRepository.deleteTvSeriesDetails(parameters)
                .also {
                    sessionManager.getSessionId()?.let { sessionId ->
                        favoriteRepository.syncFavoriteToRemote(
                            RequestType.FavoriteRequest(
                                sessionId = sessionId,
                                favorite = false,
                                favoriteId = parameters.tvSeriesData.id.toInt(),
                                mediaType = FavoriteType.TV
                            )
                        )
                    }
                }
        }
    }
}
