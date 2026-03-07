# Popular Movies App

A **Kotlin Multiplatform (KMP)** application that showcases popular movies and TV series using The Movie Database (TMDb) API. Built entirely with **Jetpack Compose** and **Material 3**, following **MVVM** + **Clean Architecture** principles with iOS target support.

## Table of Contents
- [Features](#features)
- [Screenshots](#screenshots)
- [Architecture](#architecture)
- [Module Structure](#module-structure)
- [Tech Stack](#tech-stack)
- [Build System](#build-system)
- [Testing](#testing)
- [CI/CD](#cicd)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Copyrights](#copyrights)

## Features

- **Browse Movies** -- Popular, top-rated, upcoming, and now playing categories with horizontal rows and dedicated category screens
- **Browse TV Series** -- Popular, top-rated, on the air, and airing today
- **Movie & TV Show Details** -- Collapsible backdrop header, cast, videos, reviews, overview, and favorite toggle with optimistic UI
- **Search** -- Debounced search across movies and TV shows
- **Favorites** -- Save and manage favorites, sync to TMDb account with visual sync status
- **Authentication** -- TMDb account login or guest mode
- **Welcome Screen** -- Animated movie backdrop on first launch
- **Adaptive Navigation** -- Material 3 Navigation Suite (bottom nav, rail, drawer based on screen size)

## Screenshots

<img src="pictures/popular_movies_2.jpg" alt="Movies list" width="300"/> <img src="pictures/popular_movies_1.jpg" alt="Movie details" width="200"/> <img src="pictures/popular_movies_3.jpg" alt="TV shows" width="200"/>

## Architecture

The project follows **Clean Architecture** with clear separation of concerns. For detailed documentation, see [`documentation/ARCHITECTURE.md`](documentation/ARCHITECTURE.md).

![Clean Architecture](pictures/clean_architecture_diagram.png)

### Data Flow

```
User Interaction → Route (collectAsStateWithLifecycle)
    → ViewModel (StateFlow) → Use Case (resultFlow on IO dispatcher)
    → Repository → Network (TMDb API) / Database (Room) / DataStore
    → Flow<Result<T>> (Loading / Success / Error)
    → ViewModel (.update{}) → Route → Screen (pure UI)
```

### Key Patterns

| Pattern | Description |
|---------|-------------|
| **Route/Screen separation** | Route composables inject ViewModels and collect state; Screen composables are pure UI functions |
| **`resultFlow()`** | Centralized use case helper wrapping suspend blocks in `Loading/Success/Error` states |
| **Dispatcher injection** | Koin named qualifiers (`IO_DISPATCHER`, `DEFAULT_DISPATCHER`, `APP_SCOPE`) for testable coroutines |
| **StateFlow + `.update{}`** | Thread-safe UI state management in all ViewModels |
| **Convention plugins** | Shared Gradle build logic via `build-logic/` composite build |
| **Entity-Domain mapping** | Room entities map to/from domain models at the repository boundary |

## Module Structure

```
app/                            # Main entry point, Compose navigation, DI setup, themes
build-logic/convention/         # Gradle convention plugins (10 plugins)

features/
  movies/                       # Movie browsing, categories, and details
  tvshow/                       # TV series browsing, categories, and details
  favorites/                    # Saved favorites management
  search/                       # Search functionality
  login/                        # TMDb authentication and account management
  welcome/                      # Welcome/onboarding screen

core/
  domain/                       # Use cases (business logic orchestration)          [KMP]
  data/                         # Repository implementations                       [KMP]
  models/                       # Domain models, DTOs, enums                       [KMP]
  database/                     # Room KMP database, entities, mappers             [KMP]
  network/                      # Ktor client, data sources, API mappers           [KMP]
  datastore/                    # DataStore KMP (session, guest mode)               [KMP]
  common/                       # Shared utilities, Koin DI, dispatchers           [KMP]
  analytics/                    # Firebase Analytics/Crashlytics abstractions       [KMP]
  ui/                           # Shared Compose UI components                     [Android]
  resources/                    # Shared strings, drawables                        [Android]

shared/                         # KMP shared framework (iOS export)
iosApp/                         # iOS application consumer
```

### Feature Module Structure

Each feature module follows a consistent structure:

```
features/[feature-name]/
├── [Feature]ViewModel.kt
├── [Feature]UiState.kt
├── ui/
│   ├── [Feature]Route.kt          # ViewModel injection, state collection
│   ├── [Feature]Screen.kt         # Pure UI composable
│   └── components/                # Feature-specific components
├── details/                       # Detail screens (if applicable)
├── category/                      # Category list screens (if applicable)
└── navigation/
    └── [Feature]Navigation.kt     # Routes, NavController extensions
```

## Tech Stack

| Category | Technology | Version |
|----------|-----------|---------|
| **Language** | Kotlin | 2.3.10 |
| **UI** | Jetpack Compose (BOM) | 2026.02.00 |
| **Design** | Material 3 | 1.13.0 |
| **Architecture** | MVVM + Clean Architecture | — |
| **DI** | Koin | 4.1.0 |
| **Networking** | Ktor Client | 3.1.0 |
| **Serialization** | kotlinx-serialization | 1.10.0 |
| **Database** | Room KMP | 2.8.4 |
| **Preferences** | DataStore KMP | 1.2.0 |
| **Image Loading** | Coil (Compose) | 2.7.0 |
| **Navigation** | Compose Navigation | 2.9.7 |
| **Coroutines** | kotlinx-coroutines | 1.10.1 |
| **Firebase** | Firebase BOM (Analytics, Crashlytics) | 34.9.0 |
| **Logging** | Timber (Android), Kermit (commonMain) | 5.0.1 / 2.0.4 |
| **Build** | AGP / Gradle / KSP | 9.0.1 / 9.3.1 / 2.3.6 |
| **Min SDK** | | 27 |
| **Target SDK** | | 35 |
| **Compile SDK** | | 36 |

### KMP Targets

- **Android** (primary)
- **iOS**: iosArm64, iosX64, iosSimulatorArm64

The `shared` module exports a static iOS framework (`Shared`) that bundles all core KMP modules.

## Build System

### Convention Plugins

The project uses 10 convention plugins in `build-logic/convention/` for consistent configuration:

| Plugin | Purpose |
|--------|---------|
| `popular.movies.android.application` | Application module config |
| `popular.movies.android.application.compose` | Compose for app module |
| `popular.movies.android.application.firebase` | Firebase integration |
| `popular.movies.android.library` | Library module config |
| `popular.movies.android.library.compose` | Compose for library modules |
| `popular.movies.android.feature` | Feature module conventions |
| `popular.movies.android.room` | Room database with KSP |
| `popular.movies.android.lint` | Lint configuration |
| `popular.movies.android.config` | BuildConfig fields (API key) |
| `popular.movies.kmp.library` | KMP library with iOS targets |

### Gradle Commands

```bash
./gradlew assembleDebug           # Build debug APK
./gradlew assembleRelease         # Build release APK
./gradlew test                    # Run all unit tests
./gradlew lint                    # Run lint checks
./gradlew clean                   # Clean build
./gradlew dependencyUpdates       # Check for dependency updates

# Run tests for a specific module
./gradlew :features:movies:test
./gradlew :core:network:test
```

## Testing

The project has **35 test files** across all layers with comprehensive coverage:

| Layer | Module | Tests | Framework |
|-------|--------|-------|-----------|
| **UI** | features:movies | `MoviesScreenComposeTest`, `MovieCategoryScreenComposeTest` | Robolectric + Compose Test |
| **UI** | features:tvshow | `TvShowScreenComposeTest`, `TvShowCategoryScreenComposeTest` | Robolectric + Compose Test |
| **UI** | features:welcome | `WelcomeScreenComposeTest` | Robolectric + Compose Test |
| **ViewModel** | features:welcome | `WelcomeViewModelTest` | MockK + Turbine |
| **UiState** | features:movies, login, favorites | `MovieDetailsUiStateTest`, `AccountUiStateTest`, `FavoritesUiStateTest` | JUnit |
| **Domain** | core:domain | 6 use case tests (Movie, Login, Favorites) | MockK + Turbine |
| **Data** | core:data | 5 repository tests (Auth, Movie, Favorites, Login) | MockK |
| **Network** | core:network | 12 tests (mappers, data sources, extensions) | MockK |
| **Database** | core:database | `EntityMappersTest` | JUnit |
| **DataStore** | core:datastore | `SessionManagerImplTest` | Turbine + real DataStore |

**Test frameworks:** JUnit 4.13.2, MockK 1.14.9, Turbine 1.2.0, Robolectric 4.14.1, Compose UI Test

## CI/CD

GitHub Actions workflows in `.github/workflows/`:

| Workflow | Trigger | Pipeline |
|----------|---------|----------|
| **pull_request.yml** | PR to `main` | Lint → Build → Test |
| **master.yml** | Push to `main` | Lint → Build → Sign → Release |
| **release.yml** | Manual dispatch | Signed APK build + publish |
| **update-dependencies.yml** | Scheduled | Gradle dependency updates |

All workflows use JDK 17 (Temurin), cached Android SDK, and cached Gradle with concurrency control.

## Installation

1. **Clone the repository:**
    ```bash
    git clone https://github.com/toaderandrei/popular-movies-kt.git
    ```

2. **Open** the project in Android Studio and sync with Gradle.

3. **Add your TMDb API key** to `local.properties`:
    ```properties
    tmdb_api_key=YOUR_API_KEY
    ```
    Get an API key from [The Movie Database (TMDb)](https://www.themoviedb.org/documentation/api).

4. **Run** the app on an Android device or emulator (min SDK 27).

## Usage

1. Launch the app and choose to log in with your TMDb account or continue as guest.
2. Browse movies and TV shows by category (popular, top-rated, upcoming, etc.).
3. Tap any item for detailed information including cast, videos, and reviews.
4. Save favorites and sync them to your TMDb account.
5. Use the search tab to find specific movies or TV shows.

## Copyrights

UI inspired by the [TIVI app](https://github.com/chrisbanes/tivi) by Chris Banes.

## Contributing

Contributions are welcome! Please fork the repository, create a feature branch, commit your changes, and submit a pull request.

## License

MIT License

(c) 2026 Andrei Toader-Stanescu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
