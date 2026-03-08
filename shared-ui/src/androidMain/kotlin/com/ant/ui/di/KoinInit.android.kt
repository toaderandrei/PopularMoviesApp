package com.ant.ui.di

import com.ant.network.api.createTmdbHttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Android-specific Koin module providing platform dependencies.
 * Note: Dispatchers, Database, DataStore, Analytics, and SessionManager are provided by core modules.
 */
actual val platformModule: Module = module {
    // API Key - TODO: Move to BuildConfig or environment
    single<String>(named("api_key")) {
        "c36d94a23c2be6bdfaa7733586dd6a29"
    }

    // HTTP Client with OkHttp engine
    single { createTmdbHttpClient(get(named("api_key"))) }
}
