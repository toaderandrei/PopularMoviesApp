package com.ant.database.di

import com.ant.database.factory.MoviesDatabaseFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Platform-specific module that provides the DatabaseFactory.
 * Android: Factory takes Context from androidApplication()
 * iOS: Factory requires no context
 */
expect val databaseFactoryModule: Module

/**
 * Common database module.
 * The MoviesDatabaseFactory is provided by platform-specific modules.
 */
val databaseModule: Module = module {
    // Database instance created by factory (provided without qualifier for direct injection)
    single {
        get<MoviesDatabaseFactory>().createDatabase()
    }

    // DAOs
    single { get<com.ant.database.database.MoviesDb>().moviesDao() }
    single { get<com.ant.database.database.MoviesDb>().tvSeriesDao() }
}

/**
 * Legacy name for backwards compatibility.
 * TODO: Remove after updating all references.
 */
@Deprecated("Use databaseModule instead", ReplaceWith("databaseModule"))
expect val databasePlatformModule: Module
