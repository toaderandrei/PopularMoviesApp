package com.ant.domain.usecases

import com.ant.models.model.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Creates a [Flow] that wraps a suspend [block] in [Result.Loading], [Result.Success],
 * and [Result.Error] states. Execution is dispatched on the given [dispatcher].
 *
 * [CancellationException] is rethrown to preserve structured concurrency.
 *
 * @param dispatcher the [CoroutineDispatcher] to run the block on.
 * @param block the suspend function whose result is emitted.
 * @return a cold Flow emitting Loading, then Success or Error.
 */
fun <R> resultFlow(
    dispatcher: CoroutineDispatcher,
    block: suspend () -> R,
): Flow<Result<R>> {
    return flow {
        emit(Result.Loading)
        val response = block()
        emit(Result.Success(response))
    }.catch { e ->
        if (e is CancellationException) throw e
        emit(Result.Error(e))
    }.flowOn(dispatcher)
}
