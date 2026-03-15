package com.ant.network.di

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
 * Provides HTTP client, data sources, and mappers.
 */
val networkModule = module {
    // HTTP Client
    single { createTmdbHttpClient(get(named("api_key"))) }

    // Mappers
    single { MovieDataMapper() }
    single { MoviesListMapper(get()) }
    single { MovieDetailsMapper(get()) }
    single { TvSeriesDataMapper() }
    single { TvSeriesMapper(get()) }
    single { TvSeriesDetailsMapper(get()) }
    single { LoginMapper() }
    single { LoginSessionMapper() }

    // Data Sources — now take HttpClient directly (no API interfaces)
    single { MovieListDataSource(get(), get()) }
    single { MovieDetailsExtendedSummaryDataSource(get(), get()) }
    single { SaveAsFavoriteDataSource(get(), get()) }
    single { TvSeriesListDataSource(get(), get()) }
    single { TvSeriesDetailsExtendedSummaryDataSource(get(), get()) }
    single { SearchMovieDataSource(get(), get()) }
    single { SearchTvShowDataSource(get(), get()) }
    single { FetchFavoriteMoviesDataSource(get()) }
    single { FetchFavoriteTvShowsDataSource(get()) }
}
