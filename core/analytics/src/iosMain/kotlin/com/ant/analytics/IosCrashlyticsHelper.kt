package com.ant.analytics

import co.touchlab.kermit.Logger

/**
 * Stub implementation of CrashlyticsHelper for iOS.
 * TODO: Implement with Firebase Crashlytics or custom solution.
 */
class IosCrashlyticsHelper : CrashlyticsHelper {
    override fun logError(throwable: Throwable) {
        // Stub: No-op for now, just print
        Logger.e("PopularMovies", throwable) { throwable.message ?: "Unknown error" }
    }
}
