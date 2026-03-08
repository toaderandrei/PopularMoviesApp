package com.ant.analytics.di

import com.ant.analytics.AnalyticsHelper
import com.ant.analytics.CrashlyticsHelper
import com.ant.analytics.IosAnalyticsHelper
import com.ant.analytics.IosCrashlyticsHelper
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * iOS-specific Analytics module.
 * Provides stub implementations for Analytics and Crashlytics.
 * TODO: Replace with Firebase implementations or custom solution.
 */
actual val analyticsModule: Module = module {
    single<AnalyticsHelper> { IosAnalyticsHelper() }
    single<CrashlyticsHelper> { IosCrashlyticsHelper() }
}
