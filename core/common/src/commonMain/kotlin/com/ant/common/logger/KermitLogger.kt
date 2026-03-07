package com.ant.common.logger

import co.touchlab.kermit.Logger as KermitLog

/** [Logger] implementation that delegates to Kermit for multiplatform logging. */
class KermitLogger : Logger {

    override fun v(message: String) {
        KermitLog.v(TAG) { message }
    }

    override fun d(message: String) {
        KermitLog.d(TAG) { message }
    }

    override fun i(message: String) {
        KermitLog.i(TAG) { message }
    }

    override fun e(t: Throwable, message: String) {
        KermitLog.e(TAG, t) { message }
    }

    override fun e(t: Throwable) {
        KermitLog.e(TAG, t) { t.message ?: "Unknown error" }
    }

    companion object {
        private const val TAG = "PopularMovies"
    }
}
