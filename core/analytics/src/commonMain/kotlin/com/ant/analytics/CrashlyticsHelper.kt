package com.ant.analytics

/** Abstraction for reporting non-fatal errors to a crash reporting service (e.g., Firebase Crashlytics). */
interface CrashlyticsHelper {
    /** Records [throwable] as a non-fatal error in the crash reporter. */
    fun logError(throwable: Throwable)
}