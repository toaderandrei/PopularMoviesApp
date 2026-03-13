package com.ant.network.di

import com.ant.network.api.KtorTmdbAuthApi
import com.ant.network.api.KtorTmdbGenreApi
import com.ant.network.api.KtorTmdbMoviesApi
import com.ant.network.api.KtorTmdbSearchApi
import com.ant.network.api.KtorTmdbTvSeriesApi
import com.ant.network.api.TmdbAuthApi
import com.ant.network.api.TmdbGenreApi
import com.ant.network.api.TmdbMoviesApi
import com.ant.network.api.TmdbSearchApi
import com.ant.network.api.TmdbTvSeriesApi
import com.ant.network.api.createTmdbHttpClient
import com.ant.network.datasource.favorites.FetchFavoriteMoviesDataSource
import com.ant.network.datasource.favorites.FetchFavoriteTvShowsDataSource
import com.ant.network.datasource.movies.MovieDetailsExtendedSummaryDataSource
import com.ant.network.datasource.movies.MovieListDataSource
import com.ant.network.datasource.movies.SaveAsFavoriteDataSource
import com.ant.network.datasource.search.SearchMovieDataSource
import com.ant.network.datasource.search.SearchTvShowDataSource
import com.ant.network.datasource.tvseries.TvSeriesDetailsExtendedSummaryDataSource
import com.ant.network.datasource.tvseries.TvSeriesListDataSource
import com.ant.network.mappers.login.LoginMapper
import com.ant.network.mappers.login.LoginSessionMapper
import com.ant.network.mappers.movies.MovieDataMapper
import com.ant.network.mappers.movies.MovieDetailsMapper
import com.ant.network.mappers.movies.MoviesListMapper
import com.ant.network.mappers.tvseries.TvSeriesDataMapper
import com.ant.network.mappers.tvseries.TvSeriesDetailsMapper
import com.ant.network.mappers.tvseries.TvSeriesMapper
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin module for network layer dependencies.
 * Provides HTTP client, API implementations, data sources, and mappers.
 */
val networkModule = module {
    // HTTP Client
    single { createTmdbHttpClient(get(named("api_key"))) }

    // API Implementations
    single<TmdbMoviesApi> { KtorTmdbMoviesApi(get()) }
    single<TmdbTvSeriesApi> { KtorTmdbTvSeriesApi(get()) }
    single<TmdbGenreApi> { KtorTmdbGenreApi(get()) }
    single<TmdbSearchApi> { KtorTmdbSearchApi(get()) }
    single<TmdbAuthApi> { KtorTmdbAuthApi(get()) }

    // Mappers
    single { MovieDataMapper() }
    single { MoviesListMapper(get()) }
    single { MovieDetailsMapper(get()) }
    single { TvSeriesDataMapper() }
    single { TvSeriesMapper(get()) }
    single { TvSeriesDetailsMapper(get()) }
    single { LoginMapper() }
    single { LoginSessionMapper() }

    // Data Sources
    single { MovieListDataSource(get(), get(), get()) }
    single { MovieDetailsExtendedSummaryDataSource(get(), get()) }
    single { SaveAsFavoriteDataSource(get(), get()) }
    single { TvSeriesListDataSource(get(), get(), get()) }
    single { TvSeriesDetailsExtendedSummaryDataSource(get(), get()) }
    single { SearchMovieDataSource(get(), get(), get()) }
    single { SearchTvShowDataSource(get(), get(), get()) }
    single { FetchFavoriteMoviesDataSource(get()) }
    single { FetchFavoriteTvShowsDataSource(get()) }
}
