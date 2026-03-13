package com.ant.analytics

import android.util.Log

/**
 * Stub implementation of AnalyticsHelper for Android.
 * TODO: Implement with Firebase Analytics.
 */
class AndroidAnalyticsHelper : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        // Stub: No-op for now
        Log.d("PopularMovies", "Android Analytics: ${event.type} - ${event.extras}")
    }
}
