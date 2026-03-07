package com.ant.models.model

/**
 * Generic UI state container that tracks data, loading status, and error state.
 */
data class UIState<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val error: Throwable? = null,
)

/** Returns a copy of this state with loading cleared and data preserved. */
val <T> UIState<T>.Success: UIState<T>
    get() = this.copy(
        loading = false,
        data = this.data,
        error = null
    )

/** Returns a copy of this state with loading cleared and error preserved. */
val <T> UIState<T>.Error: UIState<T>
    get() = this.copy(
        loading = false,
        data = null,
        error = this.error
    )

/** Returns a copy of this state with loading set to true and data/error cleared. */
val <T> UIState<T>.Loading: UIState<T>
    get() = this.copy(
        loading = true,
        data = null,
        error = null
    )

val <T> UIState<T>.isLoading: Boolean
    get() = loading

val <T> UIState<T>.isError: Boolean
    get() = error != null

val <T> UIState<T>.isSuccess: Boolean
    get() = data != null

val <T> UIState<T>.errorMessage: String?
    get() = error?.message
