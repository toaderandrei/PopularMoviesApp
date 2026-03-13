package com.ant.app.di

import com.ant.app.application.AppInitializers
import com.ant.app.application.TimberInitializer
import com.ant.shared.ui.app.MainActivityViewModelImpl
import com.ant.shared.di.Initializer
import com.ant.shared.logger.TmdbLogger
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * App-level Koin module providing application initializers.
 */
val appModule = module {
    // Android-specific TmdbLogger (wraps Timber)
    single { TmdbLogger() }

    // Timber logger initializer
    single<Initializer> { TimberInitializer(get()) }

    // App initializers - collects all Initializer instances
    single {
        AppInitializers(
            initializers = getAll<Initializer>().toSet()
        )
    }

    // App-level ViewModel
    viewModel { MainActivityViewModelImpl(get(), get()) }
}
