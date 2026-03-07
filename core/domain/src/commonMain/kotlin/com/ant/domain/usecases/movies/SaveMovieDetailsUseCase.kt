package com.ant.domain.usecases.movies

import com.ant.domain.repositories.FavoriteRepository
import com.ant.domain.repositories.MovieRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.MovieDetails
import com.ant.models.model.Result
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Saves movie details to local favorites and attempts to sync the favorite to the
 * remote TMDb account if an active session exists.
 *
 * Remote sync failures are silently caught -- the movie is still saved locally with
 * an unsynced status.
 */
class SaveMovieDetailsUseCase constructor(
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteRepository,
    private val sessionManager: SessionManager,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @param parameters the movie details to save as a favorite. */
    operator fun invoke(parameters: MovieDetails): Flow<Result<Boolean>> {
        return resultFlow(dispatcher) {
            movieRepository.saveMovieDetails(parameters)
                .also {
                    sessionManager.getSessionId()?.let { sessionId ->
                        try {
                            val synced = favoriteRepository.syncFavoriteToRemote(
                                RequestType.FavoriteRequest(
                                    sessionId = sessionId,
                                    favorite = true,
                                    favoriteId = parameters.movieData.id.toInt(),
                                    mediaType = FavoriteType.MOVIE
                                )
                            )
                            if (synced) {
                                favoriteRepository.updateSyncStatus(
                                    id = parameters.movieData.id,
                                    mediaType = FavoriteType.MOVIE,
                                    synced = true,
                                )
                            }
                        } catch (_: Exception) {
                            // Remote sync failed; favorite is saved locally with syncedToRemote=false
                        }
                    }
                }
        }
    }
}
