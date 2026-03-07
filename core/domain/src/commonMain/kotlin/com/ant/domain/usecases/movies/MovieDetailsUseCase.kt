package com.ant.domain.usecases.movies

import com.ant.domain.repositories.MovieRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.MovieDetails
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Fetches extended movie details (credits, reviews, videos) for a specific movie.
 */
class MovieDetailsUseCase constructor(
    private val repository: MovieRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @param parameters the movie ID and request configuration. */
    operator fun invoke(parameters: RequestType.MovieRequestDetails): Flow<Result<MovieDetails>> {
        return resultFlow(dispatcher) {
            repository.getMovieDetails(parameters)
        }
    }
}
