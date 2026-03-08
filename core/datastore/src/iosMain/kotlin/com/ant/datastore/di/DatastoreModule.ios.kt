package com.ant.datastore.di

import com.ant.datastore.factory.PopularMoviesDataStoreFactory
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * iOS-specific DataStore factory module.
 * No context needed for iOS.
 */
actual val datastoreFactoryModule: Module = module {
    single { PopularMoviesDataStoreFactory() }
}
