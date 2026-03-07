package com.ant.domain.usecases.tvseries

import com.ant.domain.repositories.TvSeriesRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.TvShow
import com.ant.models.model.PaginatedResult
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Fetches a paginated list of TV shows for a given category (e.g., popular, top rated).
 */
class TvShowListUseCase constructor(
    private val repository: TvSeriesRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @param parameters the TV show category and page to fetch. */
    operator fun invoke(parameters: RequestType.TvShowRequest): Flow<Result<PaginatedResult<TvShow>>> {
        return resultFlow(dispatcher) {
            repository.getTvSeriesList(parameters)
        }
    }
}
