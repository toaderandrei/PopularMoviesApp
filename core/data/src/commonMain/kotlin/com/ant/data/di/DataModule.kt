package com.ant.data.di

import com.ant.data.repositories.DefaultAuthRepository
import com.ant.data.repositories.DefaultFavoriteRepository
import com.ant.data.repositories.DefaultMovieRepository
import com.ant.data.repositories.DefaultSearchRepository
import com.ant.data.repositories.DefaultTvSeriesRepository
import com.ant.data.repositories.favorites.FavoriteDetailsToRemoteRepository
import com.ant.data.repositories.favorites.SyncFavoritesFromRemoteRepository
import com.ant.data.repositories.favorites.UpdateFavoriteSyncStatusRepository
import com.ant.data.repositories.login.LoginUserTmDbRepository
import com.ant.data.repositories.login.LogoutUserAndClearSessionsRepository
import com.ant.data.repositories.movies.DeleteMovieDetailsRepository
import com.ant.data.repositories.movies.LoadFavoredMovieListRepository
import com.ant.data.repositories.movies.LoadMovieDetailsSummaryRepository
import com.ant.data.repositories.movies.LoadMovieListRepository
import com.ant.data.repositories.movies.SaveMovieDetailsToLocalRepository
import com.ant.data.repositories.search.SearchMovieRepository
import com.ant.data.repositories.search.SearchTvShowRepository
import com.ant.data.repositories.tvseries.DeleteTvSeriesDetailsRepository
import com.ant.data.repositories.tvseries.LoadFavoredTvSeriesListRepository
import com.ant.data.repositories.tvseries.LoadTvSeriesDetailsSummaryRepository
import com.ant.data.repositories.tvseries.LoadTvSeriesListRepository
import com.ant.data.repositories.tvseries.SaveTvSeriesDetailsRepository
import com.ant.domain.repositories.AuthRepository
import com.ant.domain.repositories.FavoriteRepository
import com.ant.domain.repositories.MovieRepository
import com.ant.domain.repositories.SearchRepository
import com.ant.domain.repositories.TvSeriesRepository
import org.koin.dsl.module

/**
 * Koin module for data layer dependencies.
 * Provides repository implementations (both specialized and default).
 */
val dataModule = module {
    // Sub-repositories: Movies
    single { LoadMovieListRepository(get()) }
    single { LoadMovieDetailsSummaryRepository(get(), get()) }
    single { SaveMovieDetailsToLocalRepository(get()) }
    single { DeleteMovieDetailsRepository(get()) }
    single { LoadFavoredMovieListRepository(get()) }

    // Sub-repositories: TV Series
    single { LoadTvSeriesListRepository(get()) }
    single { LoadTvSeriesDetailsSummaryRepository(get(), get()) }
    single { SaveTvSeriesDetailsRepository(get()) }
    single { DeleteTvSeriesDetailsRepository(get()) }
    single { LoadFavoredTvSeriesListRepository(get()) }

    // Sub-repositories: Search
    single { SearchMovieRepository(get()) }
    single { SearchTvShowRepository(get()) }

    // Sub-repositories: Login
    single { LoginUserTmDbRepository(get(), get()) }
    single { LogoutUserAndClearSessionsRepository(get(), get()) }

    // Sub-repositories: Favorites
    single { FavoriteDetailsToRemoteRepository(get()) }
    single { UpdateFavoriteSyncStatusRepository(get()) }
    single { SyncFavoritesFromRemoteRepository(get(), get(), get(), get(), get(), get(), get()) }  // sessionManager, authApi, fetchMoviesDS, fetchTvShowsDS, moviesDb, movieMapper, tvSeriesMapper

    // Default repositories (interface bindings)
    single<MovieRepository> { DefaultMovieRepository(get(), get(), get(), get(), get()) }
    single<TvSeriesRepository> { DefaultTvSeriesRepository(get(), get(), get(), get(), get()) }
    single<SearchRepository> { DefaultSearchRepository(get(), get()) }
    single<AuthRepository> { DefaultAuthRepository(get(), get()) }
    single<FavoriteRepository> { DefaultFavoriteRepository(get(), get(), get()) }
}
