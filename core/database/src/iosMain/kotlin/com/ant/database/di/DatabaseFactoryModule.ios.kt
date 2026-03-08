package com.ant.database.di

import com.ant.database.factory.MoviesDatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * iOS-specific database factory module.
 * No context needed for iOS.
 */
actual val databaseFactoryModule: Module = module {
    single { MoviesDatabaseFactory() }
}
