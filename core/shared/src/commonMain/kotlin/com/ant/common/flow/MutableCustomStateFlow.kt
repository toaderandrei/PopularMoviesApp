package com.ant.shared.flow

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Mutable implementation of [CustomStateFlow] backed by a [MutableSharedFlow] with replay of 1.
 *
 * Behaves like a [kotlinx.coroutines.flow.MutableStateFlow] but drops the oldest value on
 * buffer overflow instead of suspending, and allows a nullable initial state.
 */
class MutableCustomStateFlow<T> private constructor(private val sharedFlow: MutableSharedFlow<T>) :
    Flow<T> by sharedFlow, CustomStateFlow<T> {
    constructor() : this(
        MutableSharedFlow(
            replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    )

    constructor(initialValue: T) : this() {
        setValue(initialValue)
    }

    override fun getValue(): T? {
        return sharedFlow.replayCache.firstOrNull()
    }

    /** Emits [newValue], replacing the currently cached value. */
    fun setValue(newValue: T) {
        sharedFlow.tryEmit(newValue)
    }
}