package com.ant.app.di

import android.content.Context
import android.text.format.DateFormat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ant.analytics.AnalyticsHelper
import com.ant.analytics.CrashlyticsHelper
import com.ant.app.analytics.AnalyticsHelperImpl
import com.ant.app.analytics.CrashlyticsHelperImpl
import com.ant.app.BuildConfig
import com.ant.app.application.AppInitializers
import com.ant.app.application.TimberInitializer
import com.ant.app.ui.compose.app.viewmodel.MainActivityViewModel
import com.ant.common.logger.Logger
import com.ant.common.logger.TmdbLogger
import com.ant.data.repositories.DefaultAuthRepository
import com.ant.data.repositories.DefaultFavoriteRepository
import com.ant.data.repositories.DefaultMovieRepository
import com.ant.data.repositories.DefaultSearchRepository
import com.ant.data.repositories.DefaultTvSeriesRepository
import com.ant.data.repositories.favorites.FavoriteDetailsToRemoteRepository
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
import com.ant.database.database.MoviesDb
import com.ant.datastore.session.SessionManagerImpl
import com.ant.domain.repositories.AuthRepository
import com.ant.domain.repositories.FavoriteRepository
import com.ant.domain.repositories.MovieRepository
import com.ant.domain.repositories.SearchRepository
import com.ant.domain.repositories.TvSeriesRepository
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
import com.ant.feature.favorites.FavoritesViewModel
import com.ant.feature.favorites.details.FavoritesDetailsViewModel
import com.ant.feature.login.LoginViewModel
import com.ant.feature.login.account.AccountViewModel
import com.ant.feature.welcome.WelcomeViewModel
import com.ant.feature.movies.MoviesViewModel
import com.ant.feature.movies.category.MovieCategoryViewModel
import com.ant.feature.movies.details.MovieDetailsViewModel
import com.ant.feature.search.SearchViewModel
import com.ant.feature.tvshow.TvShowViewModel
import com.ant.feature.tvshow.category.TvShowCategoryViewModel
import com.ant.feature.tvshow.details.TvShowDetailsViewModel
import com.ant.models.session.SessionManager
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
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Qualifier names
const val IO_DISPATCHER = "IoDispatcher"
const val DEFAULT_DISPATCHER = "DefaultDispatcher"
const val MAIN_DISPATCHER = "MainDispatcher"
const val APP_SCOPE = "AppScope"
const val API_KEY = "api_key"

// ─── Coroutines ───────────────────────────────────────────────

val coroutinesModule = module {
    single(named(DEFAULT_DISPATCHER)) { Dispatchers.Default }
    single(named(IO_DISPATCHER)) { Dispatchers.IO }
    single(named(MAIN_DISPATCHER)) { Dispatchers.Main }
    single<CoroutineScope>(named(APP_SCOPE)) {
        CoroutineScope(SupervisorJob() + get<kotlinx.coroutines.CoroutineDispatcher>(named(DEFAULT_DISPATCHER)))
    }
}

// ─── App / Firebase / Analytics ───────────────────────────────

val appModule = module {
    single { Firebase.analytics }
    single { FirebaseCrashlytics.getInstance() }
    single { FirebaseAuth.getInstance() }

    single<String>(named(API_KEY)) { BuildConfig.TMDB_API_KEY }

    single<DateTimeFormatter>(named("dateTime")) {
        val context: Context = get()
        val dateF = DateFormat.getMediumDateFormat(context) as SimpleDateFormat
        val timeF = DateFormat.getTimeFormat(context) as SimpleDateFormat
        DateTimeFormatter.ofPattern("${dateF.toPattern()} ${timeF.toPattern()}")
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }

    single<DateTimeFormatter>(named("shortDate")) {
        val context: Context = get()
        val dateF = DateFormat.getDateFormat(context) as SimpleDateFormat
        DateTimeFormatter.ofPattern(dateF.toPattern())
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }
}

// ─── Logging & Initializers ───────────────────────────────────

val loggingModule = module {
    single { TmdbLogger() }
    single<Logger> { get<TmdbLogger>() }
    single { TimberInitializer(get()) }
    single {
        AppInitializers(
            setOf(
                get<TimberInitializer>(),
            )
        )
    }
}

// ─── Analytics ────────────────────────────────────────────────

val analyticsModule = module {
    single<AnalyticsHelper> {
        AnalyticsHelperImpl(
            firebaseAnalytics = get(),
            ioDispatcher = get(named(IO_DISPATCHER)),
        )
    }
    single<CrashlyticsHelper> {
        CrashlyticsHelperImpl(
            firebaseCrashlytics = get(),
        )
    }
}

// ─── Network ──────────────────────────────────────────────────

val networkModule = module {
    single { createTmdbHttpClient(get(named(API_KEY))) }

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
}

// ─── Storage (DataStore) ──────────────────────────────────────

val storageModule = module {
    single<DataStore<Preferences>> {
        val context: Context = get()
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("AppPreferenceStorage")
        }
    }
    single<SessionManager> {
        SessionManagerImpl(
            scope = get(named(APP_SCOPE)),
            dataStore = get(),
        )
    }
}

// ─── Room Database ────────────────────────────────────────────

private val MIGRATION_40_41 = object : Migration(40, 41) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE moviedata ADD COLUMN synced_to_remote INTEGER DEFAULT 0")
        db.execSQL("ALTER TABLE tvseriesdata ADD COLUMN synced_to_remote INTEGER DEFAULT 0")
    }
}

val databaseModule = module {
    single {
        val context: Context = get()
        Room.databaseBuilder(context, MoviesDb::class.java, "tmdb_movies.db")
            .addMigrations(MIGRATION_40_41)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<MoviesDb>().moviesDao() }
    single { get<MoviesDb>().tvSeriesDao() }
}

// ─── Data (Repositories) ─────────────────────────────────────

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

    // Default repositories (interface bindings)
    single<MovieRepository> { DefaultMovieRepository(get(), get(), get(), get(), get()) }
    single<TvSeriesRepository> { DefaultTvSeriesRepository(get(), get(), get(), get(), get()) }
    single<SearchRepository> { DefaultSearchRepository(get(), get()) }
    single<AuthRepository> { DefaultAuthRepository(get(), get()) }
    single<FavoriteRepository> { DefaultFavoriteRepository(get(), get()) }
}

// ─── Domain (Use Cases) ──────────────────────────────────────

val domainModule = module {
    // Movie use cases
    factory { MovieListUseCase(get(), get(named(IO_DISPATCHER))) }
    factory { MovieDetailsUseCase(get(), get(named(IO_DISPATCHER))) }
    factory { SaveMovieDetailsUseCase(get(), get(), get(), get(named(IO_DISPATCHER))) }
    factory { DeleteMovieDetailsUseCase(get(), get(), get(), get(named(IO_DISPATCHER))) }
    factory { LoadFavoredMoviesUseCase(get(), get(named(IO_DISPATCHER))) }

    // TV Series use cases
    factory { TvShowListUseCase(get(), get(named(IO_DISPATCHER))) }
    factory { TvSeriesDetailsUseCase(get(), get(named(IO_DISPATCHER))) }
    factory { SaveTvSeriesDetailsUseCase(get(), get(), get(), get(named(IO_DISPATCHER))) }
    factory { DeleteTvSeriesDetailsUseCase(get(), get(), get(), get(named(IO_DISPATCHER))) }
    factory { LoadFavoredTvSeriesUseCase(get(), get(named(IO_DISPATCHER))) }

    // Search use cases
    factory { SearchMovieUseCase(get(), get(named(IO_DISPATCHER))) }
    factory { SearchTvShowUseCase(get(), get(named(IO_DISPATCHER))) }

    // Login use cases
    factory { LoginUserToTmDbUseCase(get(), get(named(IO_DISPATCHER))) }
    factory { LoginUserAndSaveSessionUseCase(get(), get(), get(), get(named(IO_DISPATCHER))) }
    factory { LogoutUserAndClearSessionUseCase(get(), get(named(IO_DISPATCHER))) }
    factory { LoadAccountProfileUseCase(get(named(IO_DISPATCHER)), get()) }

    // Favorite use cases
    factory { SyncFavoriteToRemoteUseCase(get(), get(), get(named(IO_DISPATCHER))) }
}

// ─── ViewModels ──────────────────────────────────────────────

val viewModelModule = module {
    viewModel { MainActivityViewModel(get(), get()) }

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
    viewModel { LoginViewModel(get(), get(), get(), get(), get()) }
    viewModel { WelcomeViewModel(get(), get()) }
    viewModel { AccountViewModel(get(), get(), get(), get()) }

    // Favorites
    viewModel { FavoritesViewModel(get(), get(), get(), get()) }
    viewModel { FavoritesDetailsViewModel(get()) }
}

// ─── All modules ─────────────────────────────────────────────

val allKoinModules = listOf(
    coroutinesModule,
    appModule,
    loggingModule,
    analyticsModule,
    networkModule,
    storageModule,
    databaseModule,
    dataModule,
    domainModule,
    viewModelModule,
)
