package com.ant.ui.di

import com.ant.analytics.di.analyticsModule
import com.ant.common.di.commonModule
import com.ant.common.qualifiers.DispatcherQualifiers
import com.ant.data.di.dataModule
import com.ant.database.di.databaseFactoryModule
import com.ant.database.di.databaseModule
import com.ant.datastore.di.datastoreFactoryModule
import com.ant.datastore.di.datastoreModule
import com.ant.domain.di.domainModule
import com.ant.feature.favorites.FavoritesViewModel
import com.ant.feature.favorites.details.FavoritesDetailsViewModel
import com.ant.feature.login.LoginViewModel
import com.ant.feature.login.account.AccountViewModel
import com.ant.feature.movies.MoviesViewModel
import com.ant.feature.movies.category.MovieCategoryViewModel
import com.ant.feature.movies.details.MovieDetailsViewModel
import com.ant.feature.search.SearchViewModel
import com.ant.feature.tvshow.TvShowViewModel
import com.ant.feature.tvshow.category.TvShowCategoryViewModel
import com.ant.feature.tvshow.details.TvShowDetailsViewModel
import com.ant.feature.welcome.WelcomeViewModel
import com.ant.network.di.networkModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Platform-specific module for dependencies that differ between Android/iOS.
 * Provides: HttpClient, DataStore, Analytics, API_KEY
 */
expect val platformModule: Module

/**
 * Initializes Koin DI framework with all required modules.
 * Must be called before any ViewModel injection or Compose UI rendering.
 */
fun initKoin(appDeclaration: KoinApplication.() -> Unit = {}): KoinApplication {
    return startKoin {
        appDeclaration()
        modules(
            platformModule,          // Platform-specific (Android/iOS)
            commonModule,            // Coroutines dispatchers & scopes, Logger
            analyticsModule,         // Analytics & Crashlytics (platform-specific)
            databaseFactoryModule,   // Database factory (platform-specific)
            databaseModule,          // Room database & DAOs (commonMain)
            datastoreFactoryModule,  // DataStore factory (platform-specific)
            datastoreModule,         // DataStore & SessionManager (commonMain)
            networkModule,           // Ktor APIs, mappers, data sources
            dataModule,              // Repositories
            domainModule,            // Use cases
            viewModelModule,         // ViewModels
        )
    }
}

// ─── ViewModels ──────────────────────────────────────────────

private val viewModelModule = module {
    // Movies
    viewModel { MoviesViewModel(get(), get()) }
    viewModel { MovieDetailsViewModel(get(), get(), get(), get()) }
    viewModel { MovieCategoryViewModel(get(), get()) }

    // TV Shows
    viewModel { TvShowViewModel(get(), get()) }
    viewModel { TvShowDetailsViewModel(get(), get(), get(), get()) }
    viewModel { TvShowCategoryViewModel(get(), get()) }

    // Search
    viewModel { SearchViewModel(get(), get()) }

    // Login
    viewModel { LoginViewModel(get(), get(), get(), get(), get()) }  // SessionManager, LoginUseCase, Analytics, Crashlytics, Logger
    viewModel { WelcomeViewModel(get(), get()) }
    viewModel { AccountViewModel(get(), get(), get(), get()) }  // LoadAccountProfileUseCase, LogoutUseCase, Analytics, Crashlytics

    // Favorites
    viewModel { FavoritesViewModel(get(), get(), get(), get(), get()) }  // Added SyncFavoritesFromRemoteUseCase
    viewModel { FavoritesDetailsViewModel(get()) }
}
