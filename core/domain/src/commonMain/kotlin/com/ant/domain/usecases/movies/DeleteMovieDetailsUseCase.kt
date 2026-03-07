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
 * Removes a movie from local favorites and syncs the removal to the remote TMDb account
 * if an active session exists.
 */
class DeleteMovieDetailsUseCase constructor(
    private val movieRepository: MovieRepository,
    private val sessionManager: SessionManager,
    private val favoriteRepository: FavoriteRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @param parameters the movie details to delete from favorites. */
    operator fun invoke(parameters: MovieDetails): Flow<Result<Unit>> {
        return resultFlow(dispatcher) {
            movieRepository.deleteMovieDetails(parameters)
                .also {
                    sessionManager.getSessionId()?.let { sessionId ->
                        favoriteRepository.syncFavoriteToRemote(
                            RequestType.FavoriteRequest(
                                sessionId = sessionId,
                                favorite = false,
                                favoriteId = parameters.movieData.id.toInt(),
                                mediaType = FavoriteType.MOVIE
                            )
                        )
                    }
                }
        }
    }
}
