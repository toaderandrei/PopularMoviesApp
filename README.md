# Popular Movies App

This Kotlin Multiplatform (KMP) application showcases popular movies and TV series using modern Android development practices. Built entirely with **Jetpack Compose** and **Material 3**, it follows the **MVVM pattern** and **Clean Architecture** principles with iOS target support.

## Table of Contents
- [Features](#features)
- [Architecture and Project Structure](#architecture-and-project-structure)
- [Technologies Used](#technologies-used)
- [Libraries Used](#libraries-used)
- [Screenshots](#screenshots)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Copyrights](#copyrights)

## Features
- Browse popular movies and TV series
  - Filter by popular, top-rated, upcoming, and now playing
  - Horizontal category rows on the main screen
  - Dedicated category list screens with full browsing
- View detailed information about each movie or TV show
  - Collapsible header with backdrop image
  - Cast, videos, reviews, and overview sections
  - Favorite toggle with optimistic UI updates
- Search movies and TV shows with debounced input
- Save and manage favorite movies and TV shows
  - Sync favorites to TMDb account
  - Visual sync status indicators
- Authentication via TMDb account or guest mode
- Adaptive navigation layout (Material 3 Navigation Suite)
- Welcome screen with animated backdrop

## Architecture and Project Structure

The project follows **Clean Architecture** with clear separation of concerns, organized into feature modules and core modules, built with **Kotlin Multiplatform (KMP)** for both Android and iOS.

For detailed architecture documentation, see [`documentation/ARCHITECTURE.md`](docs/ARCHITECTURE.md).

### Architecture Diagram

The application uses a layered architecture with KMP at its core:

![KMP Architecture Diagram](pictures/kmp_architecture.puml)

**To view the diagram:** Use [PlantUML Preview](https://plantuml.com/starting) or install a PlantUML plugin in your IDE.

Alternatively, you can view the [Module Dependencies Diagram](pictures/module_dependencies.puml) which shows the relationship between all modules.

### Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    Platform Layer                            │
│  ┌──────────────────┐              ┌──────────────────┐    │
│  │  Android App     │              │    iOS App       │    │
│  │ (MainActivity)   │              │ (ContentView)    │    │
│  └────────┬─────────┘              └────────┬─────────┘    │
│           └──────────────┬──────────────────┘              │
└───────────────────────────┼─────────────────────────────────┘
                            │
┌───────────────────────────┼─────────────────────────────────┐
│                  Shared UI Layer (KMP)                       │
│                     ┌─────▼──────┐                           │
│                     │ shared-ui  │                           │
│                     │ Koin DI    │                           │
│                     │ Navigation │                           │
│                     └─────┬──────┘                           │
└───────────────────────────┼─────────────────────────────────┘
                            │
┌───────────────────────────┼─────────────────────────────────┐
│              Feature Modules (commonMain)                    │
│  ┌────────┐  ┌─────────┐  ┌────────┐  ┌────────┐           │
│  │ Movies │  │Favorites│  │ Search │  │ Login  │  ...      │
│  │Route   │  │ Route   │  │ Route  │  │ Route  │           │
│  │Screen  │  │ Screen  │  │ Screen │  │ Screen │           │
│  │ViewModel│ │ViewModel│ │ViewModel│ │ViewModel│          │
│  │UiState │  │ UiState │  │ UiState│  │ UiState│           │
│  └────┬───┘  └────┬────┘  └────┬───┘  └────┬───┘           │
└───────┼───────────┼────────────┼───────────┼───────────────┘
        └───────────┴────────────┴───────────┘
                            │
┌───────────────────────────┼─────────────────────────────────┐
│               Domain Layer (commonMain)                      │
│                     ┌─────▼──────┐                           │
│                     │ Use Cases  │                           │
│                     │ (Business  │                           │
│                     │   Logic)   │                           │
│                     └─────┬──────┘                           │
│                     ┌─────▼──────┐                           │
│                     │Repository  │                           │
│                     │Interfaces  │                           │
│                     └─────┬──────┘                           │
└───────────────────────────┼─────────────────────────────────┘
                            │
┌───────────────────────────┼─────────────────────────────────┐
│                Data Layer (commonMain)                       │
│                     ┌─────▼──────┐                           │
│                     │Repository  │                           │
│                     │ Impls      │                           │
│                     └──┬───┬───┬─┘                           │
│          ┌─────────────┼───┼───┼─────────────┐              │
│          │             │   │   │             │              │
│     ┌────▼────┐  ┌────▼───▼───▼────┐  ┌─────▼─────┐       │
│     │ Network │  │    Database      │  │ DataStore │       │
│     │  (Ktor) │  │  (Room KMP)      │  │   (KMP)   │       │
│     │         │  │                  │  │           │       │
│     │ TMDb API│  │ MoviesDao        │  │ Session   │       │
│     │DataSource│ │ TvSeriesDao      │  │ Manager   │       │
│     └────┬────┘  └──────┬───────────┘  └───────────┘       │
└──────────┼──────────────┼─────────────────────────────────┘
           │              │
      ┌────▼────┐    ┌────▼────┐
      │  TMDb   │    │ SQLite  │
      │   API   │    │   DB    │
      └─────────┘    └─────────┘
```

**Key Principles:**
- ✅ **Dependency Rule**: Dependencies point inward (UI → Domain → Data)
- ✅ **Platform-Agnostic Core**: All business logic in `commonMain`
- ✅ **Platform-Specific Implementations**: HTTP client, database, analytics in `androidMain`/`iosMain`
- ✅ **Single Source of Truth**: Room database + DataStore for local data
- ✅ **Reactive Data Flow**: Kotlin Flow for async operations
- ✅ **Unidirectional Data Flow**: UI events → ViewModel → Use Cases → Repositories

https://developer.android.com/topic/architecture

### Module Structure

```
app/                           # Android application entry point
  └── src/main/                # Compose navigation, themes, MainActivity

iosApp/                        # iOS application (Xcode project)
  └── PopularMovies/           # SwiftUI views, ContentView

build-logic/convention/        # Gradle convention plugins for KMP

features/ (KMP)                # Feature modules - all in commonMain
  movies/                      # Movie browsing, categories, and details
    └── src/commonMain/        # Shared Compose UI, ViewModel, UiState
  tvshow/                      # TV series browsing, categories, and details
  favorites/                   # Saved favorites management with sync
  search/                      # Search functionality with debouncing
  login/                       # Authentication (login, welcome, account)
  welcome/                     # Welcome screen with backdrop animation

core/ (KMP)                    # Core infrastructure - all in commonMain
  domain/                      # Use cases (business logic), Repository interfaces
  data/                        # Repository implementations, data mappers
  models/                      # Pure Kotlin domain models and DTOs
  database/                    # Room KMP database, DAOs, entity mappers
  network/                     # Ktor client, TMDb API, data sources
  datastore/                   # DataStore KMP (session, preferences)
  shared/                      # Dispatchers, Logger (Kermit), Koin modules
  ui/                          # Shared Composables, Navigation destinations
  resources/                   # Compose Resources (strings, drawables)
  analytics/                   # Firebase Analytics/Crashlytics abstraction

shared/ (KMP)                  # iOS framework export
  └── src/
      ├── commonMain/          # Exports core modules
      ├── androidMain/         # Android platform implementations
      └── iosMain/             # iOS platform implementations

shared-ui/ (KMP)               # UI aggregation for iOS
  └── src/commonMain/          # Koin DI setup, aggregates all features
```

**Module Types:**
- **KMP Modules** (all `core/`, `features/`, `shared/`, `shared-ui/`):
  - `src/commonMain/kotlin/` - Shared Kotlin code for Android + iOS
  - `src/androidMain/kotlin/` - Android-specific implementations
  - `src/iosMain/kotlin/` - iOS-specific implementations

- **Platform Modules**:
  - `app/` - Android app (uses `shared-ui`)
  - `iosApp/` - iOS app (uses `shared.framework`)

### Key Patterns

- **Route/Screen separation** -- Route composables inject ViewModels, Screen composables are pure UI
- **`resultFlow()`** -- Centralized use case helper that wraps suspend blocks in `Loading/Success/Error` states
- **Dispatcher injection** -- Koin named qualifiers (`named(IO_DISPATCHER)`, `named(DEFAULT_DISPATCHER)`, `named(APP_SCOPE)`) for testable coroutine code
- **StateFlow + `.update{}`** -- Thread-safe UI state management in all ViewModels
- **Convention plugins** -- Shared Gradle build logic via `build-logic/` (based on [Square's approach](https://developer.squareup.com/blog/herding-elephants/) and [idiomatic Gradle](https://github.com/jjohannes/idiomatic-gradle))

### Data Flow

```
User Interaction → Route (collectAsStateWithLifecycle)
    → ViewModel (StateFlow) → Use Case (resultFlow on IO dispatcher)
    → Repository → Network (TMDb API) / Database (Room) / DataStore
    → Flow<Result<T>> (Loading/Success/Error)
    → ViewModel (.update{}) → Route → Screen (pure UI)
```

## Build System

The project uses modern Gradle patterns with custom convention plugins for consistent configuration across all modules.

### Gradle Features

**Type-Safe Project Accessors**: Enabled for compile-time safe project dependencies
```kotlin
// Modern style (type-safe)
implementation(projects.core.models)
implementation(projects.features.movies)

// Old style (deprecated)
implementation(project(":core:models"))
```

**Dependencies Block Style**: All KMP modules use the modern `dependencies {}` block instead of `sourceSets`
```kotlin
dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.models)
    commonMainImplementation(projects.core.shared)

    // UI
    commonMainImplementation(projects.core.ui)
    commonMainImplementation(libs.coil.kt.compose)

    // Testing
    commonTestImplementation(libs.turbine)
}
```

**Auto-Configured Namespace**: Convention plugins automatically set namespace based on module path
- `:features:movies` → `com.ant.features.movies`
- `:core:database` → `com.ant.core.database`

### Convention Plugins

Reusable Gradle build configuration located in `build-logic/convention/`. These plugins auto-configure namespaces, dependencies, and platform targets.

**Key Plugins:**
- **KMP**: `kmp.library`, `kmp.feature`, `kmp.room` - Auto-configure iOS targets, namespaces, Compose
- **Android**: `android.application`, `android.library`, `android.compose` - Android-specific setup
- **Special**: `android.firebase`, `android.room`, `android.lint` - Feature-specific configuration

See `build-logic/convention/src/main/kotlin/` for full implementation details.

### Gradle Commands

#### Android

```bash
# Build
./gradlew assembleDebug          # Debug APK
./gradlew assembleRelease        # Release APK (requires signing)
./gradlew bundleRelease          # AAB for Play Store

# Test
./gradlew test                   # Unit tests
./gradlew connectedAndroidTest   # Instrumented tests (requires device)
./gradlew :module:test --tests "TestClass.method"  # Single test

# Quality
./gradlew lint                   # Lint check
./gradlew clean                  # Clean build
./gradlew dependencyUpdates      # Check for updates
```

#### iOS

```bash
# Build Frameworks
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64    # Simulator (M1)
./gradlew :shared:linkDebugFrameworkIosArm64             # Device
./gradlew :shared:linkReleaseFrameworkIosArm64           # Release

# Build iOS App (requires Xcode)
cd iosApp
xcodebuild -scheme PopularMovies -configuration Debug -sdk iphonesimulator
open PopularMovies.xcodeproj  # Or use Xcode directly
```

#### Both Platforms

```bash
# Run all KMP tests
./gradlew allTests

# Build everything
./gradlew build
```

## Progress & Status

### Completed Migrations
- ✅ **XML to Compose UI** (100%) - All screens rebuilt with Jetpack Compose
- ✅ **Fragment Navigation to Compose Navigation** - Fully migrated to declarative navigation
- ✅ **Android-only to Kotlin Multiplatform** - iOS targets configured
- ✅ **Type-Safe Project Accessors** - All modules using `projects.core.models` syntax
- ✅ **Dependencies Block Modernization** - All KMP modules using `dependencies {}` style
- ✅ **Auto-Configured Namespaces** - Convention plugins handle namespace automatically
- ✅ **Room KMP Convention Plugin** - Centralized Room KSP configuration

### Current Architecture
- **Clean Architecture** with MVVM pattern
- **Koin** for dependency injection
- **Room KMP** for local database
- **Ktor** for networking
- **Compose Multiplatform** for UI
- **Convention Plugins** for build configuration

For detailed architecture and coding guidelines, see `.claude/CLAUDE.md`.

## Screenshots
<img src="pictures/popular_movies_2.jpg" alt="popular_movies_2" width="300"/> <img src="pictures/popular_movies_1.jpg" alt="popular_movies_1" width="200"/> <img src="pictures/popular_movies_3.jpg" alt="popular_movies_3" width="200"/>

## Technologies Used
- **Language:** Kotlin 2.3+
- **UI:** Jetpack Compose, Material 3, Kotlin Multiplatform (KMP)
- **Architecture:** MVVM + Clean Architecture
- **Async:** Kotlin Coroutines, Flow, StateFlow
- **DI:** Koin
- **Networking:** Ktor Client
- **Persistence:** Room KMP, DataStore KMP
- **Image Loading:** Coil (Compose integration)
- **Navigation:** Compose Navigation
- **Analytics:** Firebase Analytics, Crashlytics
- **Multiplatform:** iOS targets (iosArm64, iosX64, iosSimulatorArm64)

## Libraries Used
- [Jetpack Compose](https://developer.android.com/jetpack/compose): Modern declarative UI toolkit for Android
- [Ktor](https://ktor.io/): Multiplatform asynchronous HTTP client
- [Coil](https://github.com/coil-kt/coil): An image loading library for Android backed by Kotlin Coroutines
- [Koin](https://insert-koin.io/): Lightweight multiplatform dependency injection framework
- [Room KMP](https://developer.android.com/training/data-storage/room): Persistence library providing an abstraction layer over SQLite
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore): Data storage solution for key-value pairs
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation): Navigation framework for Compose
- [MockK](https://mockk.io/ANDROID.html): A mocking library for Kotlin
- [Kermit](https://github.com/touchlab/Kermit): Multiplatform logging library

## Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/toaderandrei/popular-movies-kt.git
    ```
2. Open the project in Android Studio and sync with Gradle.
3. Obtain an API key from [The Movie Database (TMDb)](https://www.themoviedb.org/documentation/api) and add it to `local.properties`:
    ```properties
    tmdb_api_key=YOUR_API_KEY
    ```

## Usage
1. Run the application on an Android device or emulator.
2. Log in with your TMDb account or continue as guest.
3. Browse through movies and TV shows by category.
4. Tap on any item to view detailed information.
5. Save favorites and sync them to your TMDb account.

## Copyrights
UI inspired from TIVI app.
https://github.com/chrisbanes/tivi

## Contributing
Contributions are welcome! Please fork, create a feature branch, commit changes, and submit a pull request.

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
