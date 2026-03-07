package com.ant.analytics

/** Abstraction for logging analytics events to a backend provider (e.g., Firebase Analytics). */
interface AnalyticsHelper {
    /** Logs the given [event] to the analytics backend. */
    fun logEvent(event: AnalyticsEvent)

    /** Common user-property keys set on the analytics provider. */
    companion object {
        const val DEVICE_ID = "deviceId"
        const val APP_VERSION_NAME = "app_version_name"
        const val APP_VERSION_CODE = "app_version_code"
    }
}
