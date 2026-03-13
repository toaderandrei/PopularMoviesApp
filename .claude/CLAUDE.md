# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Working Preferences

### Before Writing Code
- Explain clearly what and why
- If it is a new topic, do a deep dive, preferably use architect mode to deep dive
- Always document existing findings, current solution
- Never write code without my approval

### Writing Code
- All code should be testable
- Use SOLID principles as much as possible
- Use Android guidelines when writing code
- For Gradle always look at build-logic folder for inspiration or for updating existing plugins
- Tests should always start with "Should"
- If tests are repeatable try to use Parameterized tests
- If tests need data that is reusable in other tests, try to either use a TestData class and if it doesn't exist, try to create it

### Issue/Task Context

To get information on the task you're working on, use the Notion MCP tool (mention this should be installed if not found).
Search for the Notion issue related to this branch by filtering the EngTask database on the branch name.
Always fetch this information if you're asked to "Work on this branch" / "Work on this task".

### Merge Requests

For description use this format:

#### Problem
- Explain the problem in detail with reproduction steps

#### Implementation
- Explain the solution in detail and why it was chosen and how it fixes the problem.

##### Code Changes
- Explain code change in detail and why it was done. Do not get into details of the implementation, show the classes involved and the changes made.

#### Testing
- Explain how the solution was tested.

#### Demo
- If possible, add a video of the solution working.

## Project Overview

Popular Movies is a Kotlin Multiplatform (KMP) application that showcases popular movies and TV series using The Movie Database (TMDb) API. The app follows Clean Architecture principles with MVVM pattern and is organized into feature modules, with iOS target support.

## Architecture

### Clean Architecture Layers

The project follows Clean Architecture with clear separation of concerns:

- **app**: Main application entry point, UI composition, and navigation
  - Uses `MainActivityCompose` with Jetpack Compose
  - All UI is Compose-based (XML/Fragment migration complete)

- **features**: Feature modules organized by business domain:
  - `features:movies` - Movie browsing, categories, and details
  - `features:tvshow` - TV series browsing, categories, and details
  - `features:favorites` - Saved favorites management
  - `features:search` - Search functionality
  - `features:login` - User authentication via TMDb (login + welcome + account)

  **Feature Module Structure** (see `features:movies` as reference):
  ```
  features/[feature-name]/
  ├── [Feature]UiState.kt          # UI state data class
  ├── [Feature]ViewModel.kt        # ViewModel with StateFlow
  ├── ui/
  │   ├── [Feature]Route.kt        # Entry point (ViewModel injection)
  │   ├── [Feature]Screen.kt       # Pure UI composable
  │   └── components/              # Feature-specific components
  ├── details/                     # Detail screens (if applicable)
  ├── category/                    # Category list screens (if applicable)
  └── navigation/
      └── [Feature]Navigation.kt   # Navigation setup & extensions
  ```

- **core**: Shared infrastructure and business logic
  - `core:domain` - Use cases (business logic orchestration)
  - `core:data` - Repository implementations
  - `core:database` - Room KMP database, entity classes (`entity/`), entity-domain mappers (`mapper/`)
  - `core:network` - Ktor-based network layer, data sources, mappers
  - `core:datastore` - DataStore KMP for preferences/settings
  - `core:models` - Pure Kotlin domain models and DTOs (no Room annotations)
  - `core:shared` - Shared utilities, Koin DI, dispatcher qualifiers (previously `core:common`)
  - `core:ui` - Shared UI components, navigation destinations
  - `core:resources` - Shared resources (strings, drawables, etc.)
  - `core:analytics` - Analytics/Firebase integration

- **shared**: KMP framework modules for iOS export
  - `shared` - iOS framework that exports all core infrastructure
  - `shared-ui` - Aggregates all features and UI for iOS

- **build-logic**: Gradle convention plugins for consistent build configuration
  - Uses composite builds pattern (included build)
  - Custom convention plugins define common configurations across modules
  - **Auto-configured namespace**: All modules automatically get namespace based on path

### Data Flow Pattern

1. **UI Layer** (Feature modules) → ViewModels observe UI events
2. **Domain Layer** (Use Cases) → Orchestrates business logic, returns `Flow<Result<T>>`
   - `resultFlow(dispatcher)` wraps execution in Loading/Success/Error states with `flowOn`
   - Properly rethrows `CancellationException` for structured concurrency
3. **Data Layer** (Repositories) → Handles data sources (remote/local)
   - Plain classes with `suspend fun performRequest()`
   - Coordinates between network (TMDb API), database (Room), and DataStore

### Key Patterns

- **Result Wrapper**: All use cases return `Flow<Result<T>>` where Result is Loading, Success, or Error
- **Repository Pattern**: Single responsibility repositories (e.g., `LoadMovieListRepository`, `SaveMovieDetailsToLocalRepository`)
- **Entity-Domain Mapping**: Room entities in `core:database/entity/` map to/from domain models via `toDomain()`/`toEntity()` extension functions in `core:database/mapper/EntityMappers.kt`. Mapping happens at the repository boundary in `core:data`.
- **Dependency Injection**: Koin for DI throughout all layers
- **Coroutine Dispatchers**: Injected via Koin named qualifiers (`named(IO_DISPATCHER)`, `named(DEFAULT_DISPATCHER)`, `named(MAIN_DISPATCHER)`)
- **Application Scope**: `named(APP_SCOPE) CoroutineScope` with `SupervisorJob()` for app-level work
- **UI State Pattern**: Each feature has a dedicated `UiState` data class managed by ViewModel via `MutableStateFlow` + `.asStateFlow()`
- **Route/Screen Separation**:
  - **Route composables** inject ViewModels and collect state (e.g., `MoviesRoute`)
  - **Screen composables** are pure UI functions receiving state and callbacks (e.g., `MoviesScreen`)

## Build System

### Gradle Modernization (2026-03)

The project uses modern Gradle patterns:

✅ **Type-Safe Project Accessors**: Enabled in `settings.gradle.kts`
```kotlin
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
```

✅ **Dependencies Block Style**: All modules use `dependencies {}` instead of `sourceSets`
```kotlin
dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.models)
    commonMainImplementation(projects.core.shared)

    // UI
    commonMainImplementation(projects.core.ui)

    // Android-specific
    androidMainImplementation(platform(libs.androidx.compose.bom))
}
```

✅ **Auto-Configured Namespace**: Convention plugins automatically set namespace based on module path
- `:features:movies` → `com.ant.features.movies`
- `:core:database` → `com.ant.core.database`

✅ **Organized Dependencies**: Group by category with comments
```kotlin
dependencies {
    // Core dependencies
    ...

    // UI
    ...

    // Testing
    ...
}
```

### Gradle Commands

```bash
# Build the app
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run all tests
./gradlew test

# Run single test
./gradlew :module:test --tests "TestClass.testMethod"

# Run connected (instrumented) tests on device/emulator
./gradlew connectedAndroidTest

# Clean build
./gradlew clean

# Lint check
./gradlew lint

# Check for dependency updates
./gradlew dependencyUpdates
```

### Custom Gradle Plugins

The project uses convention plugins defined in `build-logic/convention/`:

**Android Plugins:**
- `popular.movies.android.application` - Configures Android application modules
- `popular.movies.android.library` - Configures Android library modules
- `popular.movies.android.application.compose` - Adds Compose dependencies
- `popular.movies.android.library.compose` - Compose for library modules
- `popular.movies.android.feature` - Feature module conventions
- `popular.movies.android.room` - Room database configuration
- `popular.movies.android.firebase` - Firebase integration
- `popular.movies.android.lint` - Lint configuration
- `popular.movies.android.config` - Build config fields

**KMP Plugins:**
- `popular.movies.kmp.library` - KMP library module configuration (auto-configures namespace, iOS targets)
- `popular.movies.kmp.feature` - KMP feature module conventions (auto-configures namespace, Compose, Koin)
- `popular.movies.kmp.room` - Room database for KMP (auto-configures KSP for all platforms)

**Key Features:**
- **Auto-namespace**: All modules get namespace automatically based on path
- **Auto-iOS targets**: KMP plugins automatically configure `iosX64`, `iosArm64`, `iosSimulatorArm64`
- **Auto-Room KSP**: Room plugin configures KSP processors for Android + all iOS targets

When modifying build configuration, update the convention plugins rather than individual module build files.

### Version Catalog

Dependencies are managed in `gradle/libs.versions.toml` using Gradle version catalogs. This centralizes dependency versions across all modules.

**Type-Safe Access Examples:**
```kotlin
// Project dependencies (type-safe accessors)
implementation(projects.core.models)
implementation(projects.features.movies)

// Library dependencies (version catalog)
implementation(libs.koin.core)
implementation(libs.coil.kt.compose)
```

## Configuration

### Required Setup

1. **TMDb API Key**: Add your API key to `local.properties`:
   ```properties
   tmdb_api_key=YOUR_API_KEY
   ```
   Get an API key from: https://www.themoviedb.org/documentation/api

2. **Release Keystore** (for release builds): Configure path in `local.properties`:
   ```properties
   # Default location: ~/.android/release.keystore
   ```

## Technology Stack

- **Language**: Kotlin 2.3+
- **UI**: Jetpack Compose with Material 3, Kotlin Multiplatform (KMP)
- **Architecture**: MVVM + Clean Architecture
- **DI**: Koin
- **Networking**: Ktor Client + Kotlin Serialization
- **Image Loading**: Coil 3 (KMP-compatible)
- **Async**: Kotlin Coroutines + Flow (dispatchers injected via Koin named qualifiers)
- **Database**: Room KMP
- **Preferences**: DataStore KMP
- **Navigation**: Compose Navigation (JetBrains Multiplatform) with Material 3 Adaptive Navigation Suite
- **Firebase**: Analytics and Crashlytics
- **Logging**: Timber (Android), Kermit (commonMain)
- **Multiplatform**: iOS targets (iosArm64, iosX64, iosSimulatorArm64)
- **Testing**: JUnit, MockK, Turbine, Robolectric

## Module Dependencies

When adding new dependencies:

1. Use **type-safe project accessors**: `projects.core.models` instead of `project(":core:models")`
2. Use **dependencies block style** with organized groups (see Gradle Modernization above)
3. Features depend on core modules (domain, models, ui, resources)
4. Core modules have specific dependencies:
   - `core:domain` depends on `core:models`, `core:shared`
   - `core:data` depends on `core:domain`, `core:models`, `core:network`, `core:database`
   - `core:database` depends on `core:models` (for domain types used in mappers)
   - `core:shared` provides dispatcher qualifiers, `named(APP_SCOPE)`, Koin modules, and shared utilities
   - `core:datastore` depends on `core:shared` and `core:models`
5. Avoid circular dependencies between features
6. Keep feature modules independent from each other

**Example Feature Module Dependencies:**
```kotlin
dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.shared)
    commonMainImplementation(projects.core.domain)
    commonMainImplementation(projects.core.models)

    // UI
    commonMainImplementation(projects.core.ui)
    commonMainImplementation(projects.core.resources)

    // Libraries
    commonMainImplementation(libs.coil.kt.compose)
    commonMainImplementation(libs.kotlinSerialization)
}
```

## Navigation

The app uses **Compose Navigation** exclusively (Fragment-based navigation fully removed).

Each feature defines its navigation in a dedicated `navigation/` package with:
- Route constants (e.g., `MOVIES_ROUTE = "movies"`)
- Navigation extension functions (e.g., `NavController.navigateToMovies()`)
- NavGraphBuilder extensions (e.g., `NavGraphBuilder.moviesScreen()`)

## Code Style & Conventions

### Naming Conventions
- ViewModels: Suffixed with `ViewModel` (e.g., `MoviesViewModel`)
- UI State: Suffixed with `UiState` (e.g., `MoviesUiState`)
- Use Cases: Suffixed with `UseCase` (e.g., `MovieListUseCase`)
- Repositories: Suffixed with `Repository` (e.g., `LoadMovieListRepository`)
- Route composables: Suffixed with `Route` (e.g., `MoviesRoute`)
- Screen composables: Suffixed with `Screen` (e.g., `MoviesScreen`)
- Always follow existing naming and formatting conventions in files

### Package Structure
- Base package: `com.ant.<layer>.<feature>`
- Feature UI: `com.ant.feature.<feature>.ui`
- Feature navigation: `com.ant.feature.<feature>.navigation`
- Feature components: `com.ant.feature.<feature>.ui.components`

### Compose Best Practices
- Separate Route and Screen composables
- Keep composables pure and stateless when possible
- Use preview annotations for development
- Prefer immutable state with data classes
- Use StateFlow with `.asStateFlow()` for ViewModel state management
- Use `.update{}` for thread-safe state modifications
- Side effects (LaunchedEffect for navigation) belong in Route composables only

### Coroutine Best Practices
- Always rethrow `CancellationException` in catch blocks
- Use `named(IO_DISPATCHER)` / `named(DEFAULT_DISPATCHER)` Koin qualifiers instead of hardcoding `Dispatchers.IO`
- Use `named(APP_SCOPE)` for app-level coroutine work (not ad-hoc `CoroutineScope()`)
- Use `flowOn(dispatcher)` in use cases, let ViewModels collect in `viewModelScope`

### Gradle Best Practices
- Use type-safe project accessors (`projects.core.models`) for all project dependencies
- Organize dependencies into logical groups with comments
- Use `dependencies {}` block style, not `sourceSets {}`
- Let convention plugins handle namespace configuration (don't set it manually)
- For Room KMP modules, use `popular.movies.kmp.room` plugin

## Progress & Status

### ✅ Completed Migrations
- XML to Compose UI migration (100%)
- Fragment-based navigation to Compose Navigation
- Android-only to Kotlin Multiplatform
- Type-safe project accessors
- Dependencies block style modernization
- Auto-configured namespaces via convention plugins
- Room KMP convention plugin

### 🚧 Current Architecture
- Clean Architecture with MVVM
- Koin for dependency injection
- Room KMP for local database
- Ktor for networking
- Compose Multiplatform for UI

For detailed architecture documentation, see `docs/kmp_architecture.md`.

## Reference Code
- Check apps for references: /home/andrei29/workspace/workspace-android/reference-code
