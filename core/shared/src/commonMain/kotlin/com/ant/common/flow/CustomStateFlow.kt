package com.ant.shared.flow

import kotlinx.coroutines.flow.Flow

/**
 * A read-only [Flow] that exposes the most recently emitted value.
 *
 * Unlike [kotlinx.coroutines.flow.StateFlow], the value is nullable to represent
 * the case where no value has been emitted yet.
 */
interface CustomStateFlow<T> : Flow<T> {
    /** Returns the most recently emitted value, or null if none has been emitted. */
    fun getValue(): T?
}