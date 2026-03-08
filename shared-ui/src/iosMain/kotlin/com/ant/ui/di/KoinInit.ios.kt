package com.ant.ui.di

import co.touchlab.kermit.Logger
import com.ant.network.api.createTmdbHttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * iOS-specific Koin module providing platform dependencies.
 * Note: Dispatchers, Database, DataStore, Analytics, and SessionManager are provided by core modules.
 */
actual val platformModule: Module = module {
    // API Key - TODO: Move to iOS build configuration (Info.plist or environment)
    single<String>(named("api_key")) {
        "c36d94a23c2be6bdfaa7733586dd6a29"
    }

    // HTTP Client with Darwin engine
    single { createTmdbHttpClient(get(named("api_key"))) }
}

/**
 * Initializes Koin for iOS in an idempotent manner.
 * Safe to call multiple times - will only initialize Koin once.
 */
fun initKoinIos() {
    try {
        initKoin()
    } catch (e: Exception) {
        // If KoinAppAlreadyStartedException or any other error, Koin is already running
        Logger.d("PopularMovies") { "Koin already initialized: ${e.message}" }
    }
}
