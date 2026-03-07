package com.ant.domain.usecases.search

import com.ant.domain.repositories.SearchRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.MovieData
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Searches for movies matching a text query via the TMDb API.
 */
class SearchMovieUseCase constructor(
    private val repository: SearchRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @param params the search query and pagination details. */
    operator fun invoke(params: RequestType.SearchMovieRequest): Flow<Result<List<MovieData>>> {
        return resultFlow(dispatcher) {
            repository.searchMovies(params)
        }
    }
}
