# Popular Movies - How-To Guide

This guide covers common development tasks for the Popular Movies Kotlin Multiplatform project.

## Table of Contents
- [Gradle Tasks](#gradle-tasks)
- [Building](#building)
- [Testing](#testing)
- [Running](#running)
- [Release Builds](#release-builds)
- [iOS Development](#ios-development)
- [Common Issues](#common-issues)

---

## Gradle Tasks

### Common Gradle Commands

```bash
# Clean build
./gradlew clean

# Build all modules
./gradlew build

# Build specific module
./gradlew :app:build
./gradlew :features:movies:build

# Run tests
./gradlew test

# Run tests for specific module
./gradlew :features:movies:test

# Check for dependency updates
./gradlew dependencyUpdates

# Lint check
./gradlew lint

# Format code with ktlint
./gradlew ktlintFormat
```

### KMP-Specific Tasks

```bash
# Build shared framework for iOS
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
./gradlew :shared:linkReleaseFrameworkIosArm64

# Build all iOS targets
./gradlew :shared:linkDebugFrameworkIosX64 \
          :shared:linkDebugFrameworkIosArm64 \
          :shared:linkDebugFrameworkIosSimulatorArm64

# Assemble Android Main
./gradlew :features:movies:assembleAndroidMain

# Check KMP configuration
./gradlew :shared:tasks --all
```

---

## Building

### Android Build

#### Debug Build
```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

#### Release Build
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release.apk`

### iOS Build

#### Prerequisites
- Xcode 15.0+ installed
- iOS 17.0+ SDK
- CocoaPods (if using pods)

#### Build Shared Framework
```bash
# For iOS Simulator (Apple Silicon Mac)
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64

# For iOS Device
./gradlew :shared:linkReleaseFrameworkIosArm64

# For Intel-based iOS Simulator
./gradlew :shared:linkDebugFrameworkIosX64
```

#### Build iOS App from Xcode
1. Open `iosApp/PopularMovies.xcodeproj` in Xcode
2. Select target device/simulator
3. Click Run (⌘+R) or build (⌘+B)

#### Build iOS App from Command Line
```bash
# List available simulators
xcrun simctl list devices

# Build for simulator
xcodebuild -project iosApp/PopularMovies.xcodeproj \
           -scheme PopularMovies \
           -destination 'platform=iOS Simulator,name=iPhone 15 Pro' \
           -configuration Debug \
           build

# Archive for App Store
xcodebuild -project iosApp/PopularMovies.xcodeproj \
           -scheme PopularMovies \
           -configuration Release \
           -archivePath build/PopularMovies.xcarchive \
           archive
```

---

## Testing

### Android Unit Tests

```bash
# Run all unit tests
./gradlew test

# Run tests for specific module
./gradlew :features:movies:testDebugUnitTest

# Run tests with coverage
./gradlew testDebugUnitTestCoverage

# Run specific test class
./gradlew :features:movies:testDebugUnitTest --tests "MoviesViewModelTest"

# Run specific test method
./gradlew :features:movies:testDebugUnitTest --tests "MoviesViewModelTest.shouldLoadMoviesSuccessfully"
```

### Android Instrumented Tests

```bash
# Run all instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Run instrumented tests for specific module
./gradlew :features:movies:connectedAndroidTest
```

### iOS Tests

```bash
# Run iOS tests from command line
xcodebuild test \
  -project iosApp/PopularMovies.xcodeproj \
  -scheme PopularMovies \
  -destination 'platform=iOS Simulator,name=iPhone 15 Pro'
```

### Test Reports

After running tests, reports are generated at:
- Android: `<module>/build/reports/tests/testDebugUnitTest/index.html`
- Coverage: `<module>/build/reports/coverage/testDebugUnitTestCoverage/index.html`

---

## Running

### Android

#### From Android Studio
1. Open project in Android Studio
2. Select `app` configuration
3. Click Run (▶️) or `Shift+F10`

#### From Command Line
```bash
# Install debug APK on connected device
./gradlew installDebug

# Install and run
./gradlew installDebug && adb shell am start -n com.ant.popularmovies/.ui.main.MainActivityCompose
```

#### Using ADB
```bash
# List connected devices
adb devices

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Launch app
adb shell am start -n com.ant.popularmovies/.ui.main.MainActivityCompose

# View logs
adb logcat -s PopularMovies:* *:E

# Clear app data
adb shell pm clear com.ant.popularmovies
```

### iOS

#### From Xcode
1. Open `iosApp/PopularMovies.xcodeproj`
2. Select simulator/device
3. Click Run (⌘+R)

#### From Command Line
```bash
# Build shared framework first
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64

# Boot simulator
xcrun simctl boot "iPhone 15 Pro"

# Build and run
xcodebuild -project iosApp/PopularMovies.xcodeproj \
           -scheme PopularMovies \
           -destination 'platform=iOS Simulator,name=iPhone 15 Pro' \
           -configuration Debug \
           build

# Install app on simulator
xcrun simctl install booted <path-to-app>

# Launch app
xcrun simctl launch booted com.ant.popularmovies

# View logs
xcrun simctl spawn booted log stream --predicate 'processImagePath contains "PopularMovies"' --level debug
```

---

## Release Builds

### Android Release

#### Setup (One-time)

1. **Create keystore** (if you don't have one):
   ```bash
   keytool -genkey -v -keystore ~/.android/release.keystore \
           -alias popular-movies \
           -keyalg RSA \
           -keysize 2048 \
           -validity 10000
   ```

2. **Configure `local.properties`**:
   ```properties
   SIGNING_KEY_ALIAS=popular-movies
   SIGNING_KEY_PASSWORD=your_key_password
   SIGNING_STORE_PASSWORD=your_store_password
   # Optional: custom keystore path
   # Default: ~/.android/release.keystore
   ```

#### Build Release APK
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release.apk`

#### Build App Bundle (for Play Store)
```bash
./gradlew bundleRelease
```
Output: `app/build/outputs/bundle/release/app-release.aab`

#### Version Management

Update version in `app/build.gradle.kts`:
```kotlin
android {
    defaultConfig {
        versionCode = 2
        versionName = "0.1.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level
    }
}
```

### iOS Release

#### Build Archive
```bash
xcodebuild -project iosApp/PopularMovies.xcodeproj \
           -scheme PopularMovies \
           -configuration Release \
           -archivePath build/PopularMovies.xcarchive \
           archive
```

#### Export for App Store
```bash
xcodebuild -exportArchive \
           -archivePath build/PopularMovies.xcarchive \
           -exportPath build/export \
           -exportOptionsPlist iosApp/ExportOptions.plist
```

#### Upload to App Store
```bash
xcrun altool --upload-app \
             --type ios \
             --file build/export/PopularMovies.ipa \
             --username "your@email.com" \
             --password "@keychain:AC_PASSWORD"
```

---

## iOS Development

### Initial iOS Setup (One-Time)

**IMPORTANT**: The iOS app project (`.xcodeproj`) MUST be created in Xcode. Gradle only builds the Kotlin framework, not the iOS app project.

#### Step 1: Create iOS App in Xcode

1. **Open Xcode**
2. **File → New → Project**
3. Select **iOS** → **App**
4. Configure:
   - **Product Name**: `PopularMovies`
   - **Team**: Your team or None (for simulator only)
   - **Organization Identifier**: `com.ant`
   - **Interface**: **SwiftUI**
   - **Language**: **Swift**
5. **Save location**: Navigate to `iosApp/` directory
   - **Important**: Set path to end at `/iosApp/`
   - Uncheck "Create Git repository"
6. Click **Create**

This creates `iosApp/PopularMovies.xcodeproj/`

#### Step 2: Configure Gradle Integration (Proper KMP Way)

**Use the official `embedAndSignAppleFrameworkForXcode` task** - this automatically handles Debug/Release configurations and different iOS targets.

In Xcode:

1. **Select PopularMovies project** in navigator (blue icon)
2. **Select PopularMovies target**
3. **Build Phases** tab
4. Click **+** → **New Run Script Phase**
5. Name it: **"Build Kotlin Framework"**
6. Add this script:

```bash
if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED"
  exit 0
fi
cd "$SRCROOT/.."
./gradlew :shared:embedAndSignAppleFrameworkForXcode
```

7. **Uncheck** "Based on dependency analysis"
8. **Drag this phase** to be **BEFORE** "Compile Sources"

9. **Build Settings** tab:
   - Search for "User Script Sandboxing"
   - Set to **No**

#### Step 3: Configure Framework Search Paths

1. **Build Settings** tab
2. Search for "Framework Search Paths"
3. Add: `$(SRCROOT)/../shared/build/XCFrameworks/$(CONFIGURATION)`

#### Step 4: Update Source Files

Replace the generated Swift files with the existing ones:
- `iosApp/iosApp/PopularMoviesApp.swift` (already exists)
- `iosApp/iosApp/ContentView.swift` (already exists)

### How It Works

The `embedAndSignAppleFrameworkForXcode` task:
- ✅ Automatically detects Xcode configuration (Debug/Release)
- ✅ Builds for correct architecture (simulator vs device)
- ✅ Signs the framework
- ✅ Embeds it where Xcode expects

**You never need to manually run** `linkDebugFrameworkIosSimulatorArm64` - that's the old approach!

### iOS Debugging

```bash
# View device logs
xcrun simctl spawn booted log stream --predicate 'subsystem contains "com.ant"' --level debug

# View app's console output
xcrun simctl spawn booted log stream --predicate 'processImagePath contains "PopularMovies"'

# Debug with lldb
lldb
(lldb) process attach --name PopularMovies
(lldb) continue
```

### Common iOS Tasks

```bash
# List simulators
xcrun simctl list devices

# Boot simulator
xcrun simctl boot "iPhone 15 Pro"

# Shutdown simulator
xcrun simctl shutdown "iPhone 15 Pro"

# Erase simulator data
xcrun simctl erase "iPhone 15 Pro"

# Take screenshot
xcrun simctl io booted screenshot screenshot.png

# Record video
xcrun simctl io booted recordVideo video.mp4
```

---

## Common Issues

### Gradle Build Fails

**Issue:** `Could not resolve dependencies`
```bash
# Solution: Clean and rebuild
./gradlew clean build --refresh-dependencies
```

**Issue:** `Execution failed for task ':app:mergeDebugResources'`
```bash
# Solution: Clean build directory
./gradlew clean
rm -rf .gradle
./gradlew build
```

### iOS Build Fails

**Issue:** `Framework not found Shared`
```bash
# Solution: Build shared framework first
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```

**Issue:** `No such module 'Shared'`
- Verify framework is embedded in Xcode project
- Check framework search paths in Build Settings
- Clean build folder (Cmd+Shift+K)

### KMP Issues

**Issue:** `expect/actual declarations not matching`
```bash
# Check that:
# 1. commonMain has expect declaration
# 2. androidMain and iosMain have actual implementations
# 3. Signatures match exactly
```

**Issue:** `Could not find androidx.compose.material:material-icons-extended`
```kotlin
// Solution: Add Compose BOM to androidMain dependencies
androidMain.dependencies {
    implementation(dependencies.platform(libs.androidx.compose.bom))
}
```

### Testing Issues

**Issue:** Tests not found
```bash
# Ensure test source sets are configured:
# - androidUnitTest for Android unit tests
# - commonTest for shared tests
```

**Issue:** `MockK not found in iOS tests`
- MockK is Android-only
- Move tests using MockK to `androidUnitTest`

---

## Configuration Files

### Required Files

**`local.properties`** (Git-ignored):
```properties
# TMDb API Key (required)
tmdb_api_key=YOUR_API_KEY_HERE

# Release signing (optional)
SIGNING_KEY_ALIAS=popular-movies
SIGNING_KEY_PASSWORD=your_key_password
SIGNING_STORE_PASSWORD=your_store_password
```

**Get TMDb API Key:**
1. Sign up at https://www.themoviedb.org/signup
2. Go to Settings → API
3. Request API key
4. Copy "API Key (v3 auth)"

### Build Configuration

**Convention Plugins:**
- `popular.movies.android.application` - Android app modules
- `popular.movies.android.library` - Android library modules
- `popular.movies.kmp.library` - KMP library modules
- `popular.movies.kmp.feature` - KMP feature modules
- `popular.movies.android.room` - Room database configuration

**Version Catalog:** `gradle/libs.versions.toml`

---

## Additional Resources

- **Architecture:** See `documentation/ARCHITECTURE.md`
- **Contributing:** See `CLAUDE.md` for project conventions
- **KMP Guide:** https://kotlinlang.org/docs/multiplatform.html
- **Compose Multiplatform:** https://www.jetbrains.com/lp/compose-multiplatform/
- **Navigation Compose:** https://kotlinlang.org/docs/multiplatform/compose-navigation-routing.html

---

*Last updated: 2026-03-07*
