package com.ant.analytics

import android.util.Log

/**
 * Stub implementation of CrashlyticsHelper for Android.
 * TODO: Implement with Firebase Crashlytics.
 */
class AndroidCrashlyticsHelper : CrashlyticsHelper {
    override fun logError(throwable: Throwable) {
        // Stub: No-op for now, just print
        Log.e("PopularMovies", throwable.message ?: "Unknown error", throwable)
    }
}
