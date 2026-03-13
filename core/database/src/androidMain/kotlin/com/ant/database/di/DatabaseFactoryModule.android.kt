package com.ant.database.di

import android.content.Context
import com.ant.database.factory.MoviesDatabaseFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android-specific database factory module.
 * Provides the factory with Android Context injected from Koin.
 */
actual val databaseFactoryModule: Module
    get() = module {
        single { provideDatabaseFactory(androidApplication()) }
    }

private fun provideDatabaseFactory(
    context: Context,
): MoviesDatabaseFactory =
    MoviesDatabaseFactory(
        context = context,
    )
