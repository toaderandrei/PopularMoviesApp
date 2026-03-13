package com.ant.shared.exceptions

import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

/**
 * Executes [block] with automatic retry on failure using linear backoff.
 *
 * Retries are skipped for [CancellationException] (always rethrown) and when
 * [shouldRetry] returns false for the thrown exception.
 *
 * @param defaultDelay Base delay in milliseconds; actual delay is `attempt * defaultDelay`.
 * @param maxAttempts Maximum number of execution attempts (including the first).
 * @param shouldRetry Predicate to decide whether a given throwable is retryable.
 * @param block The suspending operation to execute.
 * @return The result of [block] on success.
 * @throws Throwable The last exception if all attempts fail.
 */
suspend fun <T> withRetry(
    defaultDelay: Long = 50,
    maxAttempts: Int = 3,
    shouldRetry: (Throwable) -> Boolean = { true },
    block: suspend () -> T
): T {
    repeat(maxAttempts) { attempt ->
        val response = runCatching { block() }

        when {
            response.isSuccess -> return response.getOrThrow()
            response.isFailure -> {
                val exception = response.exceptionOrNull()
                if (exception is CancellationException) throw exception

                exception?.run {
                    if (attempt == maxAttempts - 1 || !shouldRetry(exception)) {
                        throw exception
                    }

                    val nextDelay = attempt * defaultDelay
                    delay(nextDelay)
                }
            }
        }
    }

    throw IllegalStateException("Unknown exception from executeWithRetry")
}
