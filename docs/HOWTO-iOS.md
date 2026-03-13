# How to Build and Run the iOS App

This document explains how to build and run the Popular Movies iOS application using Xcode.

## Overview

The project uses Kotlin Multiplatform (KMP) with the following architecture:

- **`shared`** - iOS framework wrapper module that exports `shared-ui`
- **`shared-ui`** - Aggregates all infrastructure + UI modules + features (not exported directly)

**For iOS, we build the `shared` framework** which acts as a lightweight wrapper following the iOS framework aggregator pattern (similar to how tebi-main structures their iOS exports).

## Prerequisites

- Xcode 15+ installed
- Gradle 9.3.1 or higher
- Kotlin 2.3.10 or higher

## Building the iOS Framework

### Step 1: Build the Shared Framework

From the project root directory, run:

```bash
# For iOS Simulator (ARM64 - M1/M2/M3 Macs)
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64

# For iOS Simulator (x64 - Intel Macs)
./gradlew :shared:linkDebugFrameworkIosX64

# For iOS Device (ARM64)
./gradlew :shared:linkDebugFrameworkIosArm64
```

The frameworks will be generated at:
- **Simulator ARM64**: `shared/build/bin/iosSimulatorArm64/debugFramework/Shared.framework`
- **Simulator x64**: `shared/build/bin/iosX64/debugFramework/Shared.framework`
- **Device ARM64**: `shared/build/bin/iosArm64/debugFramework/Shared.framework`

### Step 2: Open Xcode Project

```bash
open iosApp/PopularMovies.xcodeproj
```

### Step 3: Run from Xcode

1. Select the `PopularMovies` scheme
2. Choose your target device/simulator
3. Press `⌘ + R` to build and run

## How It Works

The iOS app integrates the Kotlin Multiplatform UI through:

1. **ContentView.swift** - Imports `Shared` framework and creates a SwiftUI view
2. **MainViewController()** - Kotlin function in `shared-ui/src/iosMain/kotlin/com/ant/ui/MainViewController.kt` that returns a `ComposeUIViewController`
3. **App()** - Main Compose Multiplatform composable with all navigation and features

## Framework Architecture

### Why Use the `shared` Module as iOS Framework Wrapper?

The `shared` module acts as an iOS framework aggregator (similar to tebi-main's `iosFramework` module) to solve Kotlin Native linker limitations:

**Problem**: Exporting multiple KMP modules directly causes type alias conflicts:
- `kotlinx.datetime.Instant` gets bound multiple times across exported modules
- Room KMP type converters fail iOS framework export

**Solution**: Single export point pattern:
1. `shared` module creates the iOS framework
2. It exports only `shared-ui` which transitively includes all dependencies
3. Avoids duplicate type alias bindings by having one export layer

### What's Accessible from iOS

Through the `Shared` framework, iOS has access to:

**UI (via shared-ui):**
- All Compose UI screens and navigation
- MainViewController entry point

**Infrastructure (transitively through shared-ui):**
- Domain models (via `core:models` - not directly exported)
- Use cases (via `core:domain`)
- Repositories (via `core:data`)
- Network layer (via `core:network`)
- Database (via `core:database` - Room KMP, not directly exported)
- Preferences (via `core:datastore` - not directly exported)
- Analytics (via `core:analytics`)

**Features:**
- Movies, TV Shows, Favorites, Search, Login, Welcome

**Note**: Some modules (`core:models`, `core:database`, `core:datastore`) are not exported directly due to iOS framework limitations but are accessible through modules that depend on them.

## Troubleshooting

### Framework Not Found Error

If Xcode shows "Framework not found Shared":
1. Build the framework first: `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64`
2. Clean Xcode build folder: `⌘ + Shift + K`
3. Rebuild in Xcode: `⌘ + B`

### Wrong Architecture Error

Make sure you're building the correct framework variant:
- **M1/M2/M3 Mac Simulator**: Use `iosSimulatorArm64`
- **Intel Mac Simulator**: Use `iosX64`
- **Physical Device**: Use `iosArm64`

### Linker Errors

If you see linker errors about missing symbols:
1. Clean Kotlin Native cache: `rm -rf ~/.konan/cache`
2. Rebuild the framework: `./gradlew clean :shared:linkDebugFrameworkIosSimulatorArm64`

### Unlinked Class Symbol Warnings

You may see informational warnings like:
```
i: Variable uses unlinked class symbol 'kotlinx.datetime/Instant|null[0]'
```

These are informational warnings from the Kotlin Native compiler about types not directly exported (like `Instant`, Room entities). They don't prevent the app from working - these types are accessible through the exported modules that use them.

## Build from Xcode

Xcode can automatically build the Kotlin framework before building the iOS app. This is configured in the Xcode build phases (if set up).

To manually add this:
1. In Xcode, select the `PopularMovies` target
2. Go to Build Phases
3. Add a "Run Script" phase before "Compile Sources"
4. Add the following script:

```bash
cd "$SRCROOT/.."
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```

## Release Builds

For release builds, use the release framework tasks:

```bash
./gradlew :shared:linkReleaseFrameworkIosSimulatorArm64
./gradlew :shared:linkReleaseFrameworkIosX64
./gradlew :shared:linkReleaseFrameworkIosArm64
```

## Technical Background

### Kotlin Native Linker Limitations

The current iOS framework structure works around several Kotlin Native limitations:

1. **Type Alias Conflicts**: When multiple modules export the same type (like `kotlinx.datetime.Instant`), the linker tries to bind the type alias multiple times, causing errors.

2. **Room KMP iOS Export**: Room's type converters use reflection and complex types that don't export cleanly to iOS frameworks yet.

3. **Expect/Actual Extension Functions**: Extension functions with platform-specific implementations can cause linker issues when exported directly.

By using a single export point (`shared` → `shared-ui`), we avoid these issues while maintaining full functionality.
