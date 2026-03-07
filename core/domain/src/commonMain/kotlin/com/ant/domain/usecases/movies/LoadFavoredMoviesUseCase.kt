package com.ant.domain.usecases.movies

import com.ant.domain.repositories.MovieRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.MovieData
import com.ant.models.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Loads movies from local storage filtered by favorite status.
 */
class LoadFavoredMoviesUseCase constructor(
    private val movieRepository: MovieRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @param parameters true to load favored movies, false for non-favored. */
    operator fun invoke(parameters: Boolean): Flow<Result<List<MovieData>>> {
        return resultFlow(dispatcher) {
            movieRepository.getFavoredMovies(parameters)
        }
    }
}
