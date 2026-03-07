package com.ant.app.analytics

import com.ant.analytics.CrashlyticsHelper
import com.google.firebase.crashlytics.FirebaseCrashlytics


class CrashlyticsHelperImpl(
    private val firebaseCrashlytics: FirebaseCrashlytics,
) : CrashlyticsHelper {
    override fun logError(throwable: Throwable) {
        firebaseCrashlytics.recordException(throwable)
    }
}
