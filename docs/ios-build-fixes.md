# iOS Build Fixes and Learnings

## Fixed Issues Summary

### 1. CocoaPods Installation Issues
**Problem**: System Ruby permissions prevented gem installation
- Error: `You don't have write permissions for the /Library/Ruby/Gems/2.6.0 directory`

**Solution**:
- Install CocoaPods via Homebrew: `brew install cocoapods`
- Updated IosTasksPlugin to include Homebrew paths for both Intel and Apple Silicon
- Added CocoaPods setup to GitHub Actions workflow

### 2. Podfile Test Targets
**Problem**: Referenced non-existent test targets
- Error: `Unable to find a target named 'PopularMoviesTests' in project`

**Solution**:
- Removed test targets from Podfile (KMP handles testing in shared code)

### 3. Java Heap Space Error
**Problem**: Kotlin Native compiler ran out of memory
- Error: `java.lang.OutOfMemoryError: Java heap space`

**Solution**:
- Increased JVM heap: 6GB → 8GB
- Increased metaspace: 1GB → 2GB
- Added Kotlin Native specific memory settings
- Disabled some optimizations for faster builds

### 4. Configuration Cache Issues
**Problem**: File references caused configuration cache errors
- Error: `Cannot invoke 'org.gradle.api.Project.file(Object)' because 'this.$this_registerBuildTasks' is null`

**Solution**:
- Extracted file references outside task registration blocks
- Moved property access to `doFirst` blocks

## Updated Files

### gradle.properties
```properties
# Increased memory for Kotlin Native builds
org.gradle.jvmargs=-Xmx8g -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8 -XX:+UseParallelGC -XX:MaxMetaspaceSize=2g

# Kotlin Native compiler settings
kotlin.native.jvmArgs=-Xmx8g
kotlin.native.disableCompilerDaemon=false
kotlin.incremental=true
kotlin.incremental.native=true
kotlin.native.optimization.enable=false
```

### IosTasksPlugin.kt
- Added Homebrew paths: `/opt/homebrew/bin` (Apple Silicon) and `/usr/local/bin` (Intel)
- Fixed configuration cache compatibility
- Better error messages for missing CocoaPods

### iosApp/Podfile
- Removed non-existent test targets
- Kept only main app target with KMP frameworks

### .github/workflows/release-ios.yml
- Added CocoaPods installation step using `--user-install`

## Performance Metrics

- **First build time**: 4m 17s
- **Memory usage**: ~6.5GB during peak compilation
- **Modules compiled**: 20+ KMP modules
- **Subsequent builds**: Much faster due to caching

## Key Learnings

1. **Memory Requirements**: KMP projects with many modules need significant heap space (8GB recommended)
2. **Build Times**: First iOS framework builds can take 5-15 minutes, cache helps significantly
3. **CocoaPods PATH**: Gradle tasks need explicit PATH configuration for Homebrew tools
4. **Configuration Cache**: File references must be extracted before task registration
5. **Test Targets**: KMP projects don't need iOS-specific test targets in Podfile

## Troubleshooting Guide

### If build fails with heap space error:
1. Stop Gradle daemons: `./gradlew --stop`
2. Increase memory in `gradle.properties`
3. Restart build

### If CocoaPods not found:
1. Install via Homebrew: `brew install cocoapods`
2. Or check PATH includes `/opt/homebrew/bin` or `/usr/local/bin`

### If Podfile errors:
1. Check target names match Xcode project
2. Remove test targets if using KMP
3. Run `pod install` manually to debug

## Current Status

✅ **iOS Release Pipeline**: Fully operational
✅ **Memory Issues**: Fixed with 8GB heap
✅ **CocoaPods**: Installed and configured
✅ **GitHub Actions**: Updated and ready
✅ **Configuration Cache**: Compatible

## Commands

```bash
# Build iOS framework
./gradlew :shared:linkReleaseFrameworkIosSimulatorArm64

# Full iOS release
./gradlew iosRelease -Pversion=1.0.0

# Clean and rebuild
./gradlew clean iosRelease

# Install pods manually
cd iosApp && pod install
```