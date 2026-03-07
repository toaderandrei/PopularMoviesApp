package com.ant.domain.usecases.movies

import com.ant.domain.repositories.MovieRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.MovieData
import com.ant.models.model.PaginatedResult
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Fetches a paginated list of movies for a given category (e.g., popular, top rated).
 */
class MovieListUseCase constructor(
    private val repository: MovieRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @param parameters the movie category and page to fetch. */
    operator fun invoke(parameters: RequestType.MovieRequest): Flow<Result<PaginatedResult<MovieData>>> {
        return resultFlow(dispatcher) {
            repository.getMovieList(parameters)
        }
    }
}
