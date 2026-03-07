package com.ant.common.listeners

/** Callback for saving or removing an item from favorites. */
interface FavoriteCallback<T> {
    /** Saves [item] as a favorite. */
    fun onSave(item: T)
    /** Removes [item] from favorites. */
    fun delete(item: T)
}