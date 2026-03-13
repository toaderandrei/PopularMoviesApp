package com.ant.common.listeners

/** Callback for sharing an item externally. */
interface ShareCallback<T> {
    /** Shares the given [item] via a platform share sheet or equivalent. */
    fun onShare(item: T)
}