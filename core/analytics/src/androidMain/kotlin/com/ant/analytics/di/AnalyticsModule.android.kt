package com.ant.analytics.di

import com.ant.analytics.AndroidAnalyticsHelper
import com.ant.analytics.AndroidCrashlyticsHelper
import com.ant.analytics.AnalyticsHelper
import com.ant.analytics.CrashlyticsHelper
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android-specific Analytics module.
 * Provides stub implementations for Analytics and Crashlytics.
 * TODO: Replace with Firebase implementations.
 */
actual val analyticsModule: Module = module {
    single<AnalyticsHelper> { AndroidAnalyticsHelper() }
    single<CrashlyticsHelper> { AndroidCrashlyticsHelper() }
}
