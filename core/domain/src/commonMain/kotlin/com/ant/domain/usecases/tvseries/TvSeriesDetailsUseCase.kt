package com.ant.domain.usecases.tvseries

import com.ant.domain.repositories.TvSeriesRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.TvShowDetails
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Fetches extended TV series details for a specific show.
 */
class TvSeriesDetailsUseCase constructor(
    private val repository: TvSeriesRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @param parameters the TV series ID and request configuration. */
    operator fun invoke(parameters: RequestType.TvSeriesRequestDetails): Flow<Result<TvShowDetails>> {
        return resultFlow(dispatcher) {
            repository.getTvSeriesDetails(parameters)
        }
    }
}
