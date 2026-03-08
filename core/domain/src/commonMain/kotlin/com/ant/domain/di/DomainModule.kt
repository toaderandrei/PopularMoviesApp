package com.ant.domain.di

import com.ant.domain.usecases.favorites.SyncFavoriteToRemoteUseCase
import com.ant.domain.usecases.login.LoadAccountProfileUseCase
import com.ant.domain.usecases.login.LoginUserAndSaveSessionUseCase
import com.ant.domain.usecases.login.LoginUserToTmDbUseCase
import com.ant.domain.usecases.login.LogoutUserAndClearSessionUseCase
import com.ant.domain.usecases.movies.DeleteMovieDetailsUseCase
import com.ant.domain.usecases.movies.LoadFavoredMoviesUseCase
import com.ant.domain.usecases.movies.MovieDetailsUseCase
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.domain.usecases.movies.SaveMovieDetailsUseCase
import com.ant.domain.usecases.search.SearchMovieUseCase
import com.ant.domain.usecases.search.SearchTvShowUseCase
import com.ant.domain.usecases.tvseries.DeleteTvSeriesDetailsUseCase
import com.ant.domain.usecases.tvseries.LoadFavoredTvSeriesUseCase
import com.ant.domain.usecases.tvseries.SaveTvSeriesDetailsUseCase
import com.ant.domain.usecases.tvseries.TvSeriesDetailsUseCase
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.common.qualifiers.DispatcherQualifiers
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin module for domain layer dependencies.
 * Provides use cases with proper dispatcher injection.
 *
 * Note: Dispatchers are injected via qualifiers from core:common
 * - DispatcherQualifiers.IO for I/O operations
 * - DispatcherQualifiers.DEFAULT for CPU-intensive operations
 * - DispatcherQualifiers.MAIN for UI operations
 */
val domainModule = module {
    // Movie use cases
    factory { MovieListUseCase(get(), get(named(DispatcherQualifiers.IO))) }
    factory { MovieDetailsUseCase(get(), get(named(DispatcherQualifiers.IO))) }
    factory { SaveMovieDetailsUseCase(get(), get(), get(), get(named(DispatcherQualifiers.IO))) }
    factory { DeleteMovieDetailsUseCase(get(), get(), get(), get(named(DispatcherQualifiers.IO))) }
    factory { LoadFavoredMoviesUseCase(get(), get(named(DispatcherQualifiers.IO))) }

    // TV Series use cases
    factory { TvShowListUseCase(get(), get(named(DispatcherQualifiers.IO))) }
    factory { TvSeriesDetailsUseCase(get(), get(named(DispatcherQualifiers.IO))) }
    factory { SaveTvSeriesDetailsUseCase(get(), get(), get(), get(named(DispatcherQualifiers.IO))) }
    factory { DeleteTvSeriesDetailsUseCase(get(), get(), get(), get(named(DispatcherQualifiers.IO))) }
    factory { LoadFavoredTvSeriesUseCase(get(), get(named(DispatcherQualifiers.IO))) }

    // Search use cases
    factory { SearchMovieUseCase(get(), get(named(DispatcherQualifiers.IO))) }
    factory { SearchTvShowUseCase(get(), get(named(DispatcherQualifiers.IO))) }

    // Login use cases
    factory { LoginUserToTmDbUseCase(get(), get(named(DispatcherQualifiers.IO))) }
    factory { LoginUserAndSaveSessionUseCase(get(), get(), get(), get(named(DispatcherQualifiers.IO))) }
    factory { LoadAccountProfileUseCase(get(named(DispatcherQualifiers.IO)), get()) }
    factory { LogoutUserAndClearSessionUseCase(get(), get(named(DispatcherQualifiers.IO))) }

    // Favorites use cases
    factory { SyncFavoriteToRemoteUseCase(get(), get(), get(named(DispatcherQualifiers.IO))) }
}
