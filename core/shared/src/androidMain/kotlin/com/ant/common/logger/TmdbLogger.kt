package com.ant.shared.logger

import timber.log.Timber

/** Android-specific [Logger] implementation that delegates to Timber. */
class TmdbLogger : Logger {

    /** Plants a [timber.log.Timber.DebugTree] when [debug] is true. Should be called once at app startup. */
    fun init(debug: Boolean = true) {
        if (debug) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun v(message: String) {
        Timber.v(message)
    }

    override fun d(message: String) {
        Timber.d(message)
    }

    override fun i(message: String) {
        Timber.i(message)
    }

    override fun e(t: Throwable, message: String) {
        Timber.e(t, message)
    }

    override fun e(t: Throwable) {
        Timber.e(t)
    }
}