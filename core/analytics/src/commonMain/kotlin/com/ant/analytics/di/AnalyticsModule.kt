package com.ant.analytics.di

import org.koin.core.module.Module

/**
 * Platform-specific module that provides Analytics and Crashlytics implementations.
 * Android: AndroidAnalyticsHelper and AndroidCrashlyticsHelper
 * iOS: IosAnalyticsHelper and IosCrashlyticsHelper
 */
expect val analyticsModule: Module
