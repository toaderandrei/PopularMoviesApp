package com.ant.datastore.di

import com.ant.common.qualifiers.DispatcherQualifiers
import com.ant.datastore.factory.PopularMoviesDataStoreFactory
import com.ant.datastore.session.SessionManagerImpl
import com.ant.models.session.SessionManager
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Platform-specific module that provides the DataStoreFactory.
 * Android: Factory takes Context from androidContext()
 * iOS: Factory requires no context
 */
expect val datastoreFactoryModule: Module

/**
 * Common DataStore module.
 * The PopularMoviesDataStoreFactory is provided by platform-specific modules.
 */
val datastoreModule: Module = module {
    // DataStore instance created by factory (named for clarity)
    single(named("AppDataStore")) {
        get<PopularMoviesDataStoreFactory>().createDataStore()
    }

    // SessionManager depends on DataStore
    single<SessionManager> {
        SessionManagerImpl(
            scope = get(named(DispatcherQualifiers.APP_SCOPE)),
            dataStore = get(named("AppDataStore")),
        )
    }
}