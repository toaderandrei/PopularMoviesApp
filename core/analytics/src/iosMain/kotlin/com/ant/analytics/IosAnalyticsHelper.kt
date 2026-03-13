package com.ant.analytics

import co.touchlab.kermit.Logger

/**
 * Stub implementation of AnalyticsHelper for iOS.
 * TODO: Implement with Firebase Analytics or custom solution.
 */
class IosAnalyticsHelper : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        // Stub: No-op for now
        Logger.d("PopularMovies") { "iOS Analytics: ${event.type} - ${event.extras}" }
    }
}
