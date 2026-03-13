# iOS Build Guide

All iOS build logic is centralized in Gradle through the `IosTasksPlugin` convention plugin.

## Available Gradle Tasks

### Development Tasks
```bash
# Setup iOS development environment
./gradlew setupIosApp

# Open in Xcode
./gradlew openInXcode

# Generate Xcode project (if using XcodeGen)
./gradlew generateXcodeProject
```

### Build Tasks
```bash
# Install CocoaPods dependencies
./gradlew iosPodInstall

# Build KMP frameworks
./gradlew iosBuildFrameworks

# Build iOS app
./gradlew iosBuildApp

# Create IPA
./gradlew iosCreateIPA

# Complete release (all steps)
./gradlew iosRelease -Pversion=1.0.0 -PbuildNumber=100
```

### Clean
```bash
# Clean iOS build artifacts
./gradlew iosClean
```

## Configuration Options

Pass these as Gradle properties:

| Property | Description | Default |
|----------|-------------|---------|
| `version` | App version (e.g., 1.0.0) | 1.0.0 |
| `buildNumber` | Build number | 1 |
| `iosConfiguration` | Build configuration (Debug/Release) | Release |
| `iosSigned` | Whether to sign the app | false |
| `iosBuildType` | Framework build type | release |

## Examples

### Local Development
```bash
# Quick setup for development
./gradlew setupIosApp

# Build and open in Xcode
./gradlew openInXcode
```

### Release Build
```bash
# Build unsigned release
./gradlew iosRelease -Pversion=1.2.0 -PbuildNumber=45

# Build signed release (requires certificates)
./gradlew iosRelease -Pversion=1.2.0 -PbuildNumber=45 -PiosSigned=true
```

### CI/CD Usage

The GitHub Actions workflows use these tasks:

```yaml
# Simple one-line iOS release
- name: Build iOS Release
  run: ./gradlew iosRelease -Pversion=${{ inputs.version }}
```

## File Locations

After building:
- **Framework**: `shared/build/outputs/framework/`
- **Archive**: `iosApp/build/PopularMovies.xcarchive`
- **IPA**: `iosApp/build/IPA/PopularMovies.ipa`

## Troubleshooting

### Pod Install Fails
```bash
# Clean and retry
./gradlew iosClean
cd iosApp
rm -rf Pods Podfile.lock
cd ..
./gradlew iosPodInstall
```

### Framework Not Found
```bash
# Rebuild frameworks
./gradlew clean
./gradlew iosBuildFrameworks
```

### Build Fails
```bash
# Check Xcode is installed
xcode-select -p

# Clean everything
./gradlew iosClean
./gradlew clean

# Retry
./gradlew iosRelease
```

## Benefits of Gradle Integration

1. **Single Source of Truth**: All iOS build logic in one place
2. **Consistent with Android**: Similar commands for both platforms
3. **CI/CD Friendly**: One command builds everything
4. **Parameterized**: Easy to configure via properties
5. **Dependency Management**: Gradle handles task ordering

## Comparison

### Before (Multiple Steps)
```bash
# Old way - multiple manual steps
./gradlew :shared:embedAndSignAppleFrameworkForXcode
cd iosApp
pod install
xcodebuild archive ...
# Create IPA manually
```

### After (Single Command)
```bash
# New way - one command
./gradlew iosRelease -Pversion=1.0.0
```

## Next Steps

When ready for App Store:
1. Add signing certificates to GitHub Secrets
2. Set `-PiosSigned=true` in workflow
3. Add TestFlight upload step