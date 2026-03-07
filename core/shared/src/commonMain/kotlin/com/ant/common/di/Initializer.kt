package com.ant.shared.di

/** Contract for components that require one-time initialization at app startup. */
interface Initializer {
    /** Performs the initialization. Called once during application creation. */
    fun init()
}