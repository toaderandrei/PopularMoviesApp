package com.ant.datastore.di

import android.content.Context
import com.ant.datastore.factory.PopularMoviesDataStoreFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

//actual val datastoreFactoryModule2: Module = module {
//    single { PopularMoviesDataStoreFactory(androidApplication()) }
//}

/**
 * Android-specific DataStore factory module.
 * Provides the factory with Android Context injected from Koin.
 */
actual val datastoreFactoryModule: Module
    get() = module {
        single { provideDataStoreFactory(androidApplication()) }
    }

private fun provideDataStoreFactory(
    context: Context,
): PopularMoviesDataStoreFactory =
    PopularMoviesDataStoreFactory(
        context = context,
    )
