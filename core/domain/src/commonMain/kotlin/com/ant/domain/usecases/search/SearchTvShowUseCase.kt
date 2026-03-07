package com.ant.domain.usecases.search

import com.ant.domain.repositories.SearchRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.TvShow
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Searches for TV shows matching a text query via the TMDb API.
 */
class SearchTvShowUseCase constructor(
    private val repository: SearchRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @param params the search query and pagination details. */
    operator fun invoke(params: RequestType.SearchTvShowRequest): Flow<Result<List<TvShow>>> {
        return resultFlow(dispatcher) {
            repository.searchTvShows(params)
        }
    }
}
