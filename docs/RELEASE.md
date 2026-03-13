# Release Process

This document describes how to release the Popular Movies app for Android and iOS.

## Quick Start

### Android Release
```bash
# Trigger from GitHub Actions UI
# Go to Actions → Android Release → Run workflow
# Enter version (e.g., "1.0.3")
```

### iOS Release
```bash
# Trigger from GitHub Actions UI
# Go to Actions → iOS Release → Run workflow
# Enter version (e.g., "1.0.3")
```

## Prerequisites

### For Basic Releases (Unsigned)
- No special requirements
- Builds can be installed via sideloading

### For Store Releases (Future)

#### Android
1. Create keystore: `keytool -genkey -v -keystore release.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias my-key`
2. Add to GitHub Secrets:
   - `SIGNING_KEY_BASE64`: Base64 encoded keystore
   - `SIGNING_STORE_PASSWORD`: Keystore password
   - `SIGNING_KEY_ALIAS`: Key alias
   - `SIGNING_KEY_PASSWORD`: Key password

#### iOS
1. Apple Developer Account ($99/year)
2. Add to GitHub Secrets:
   - `CERTIFICATES_P12`: Base64 encoded certificate
   - `CERTIFICATES_PASSWORD`: Certificate password
   - `APPSTORE_ISSUER_ID`: App Store Connect API issuer ID
   - `APPSTORE_KEY_ID`: App Store Connect API key ID
   - `APPSTORE_PRIVATE_KEY`: App Store Connect API private key

## Release Workflows

### 1. Simple Release (Current Setup)

Both `android-release-simple.yml` and `ios-release-simple.yml` provide:
- Build the app with specified version
- Create unsigned APK/IPA
- Upload artifacts to GitHub
- Create GitHub release with download links

**Use when:**
- Testing releases
- Internal distribution
- You don't have signing certificates yet

### 2. Full Release (When Ready for Stores)

The `release-android.yml` and `release-ios.yml` workflows provide:
- Signed builds
- TestFlight/Play Store deployment
- Multiple environment support
- Automated testing

**Use when:**
- You have developer accounts
- Ready for public distribution
- Need TestFlight/beta testing

## Local Testing

### Android
```bash
# Build locally
./gradlew assembleRelease

# Install on device
adb install app/build/outputs/apk/release/app-release.apk
```

### iOS
```bash
# Build KMP framework
./gradlew :shared:embedAndSignAppleFrameworkForXcode

# Install pods
cd iosApp
pod install

# Open in Xcode
open PopularMovies.xcworkspace
# Then build and run from Xcode
```

## CocoaPods Setup (iOS)

The project uses CocoaPods for iOS dependencies. The setup is already configured:

1. `iosApp/Podfile` - Defines iOS dependencies
2. `shared/shared.podspec` - KMP framework pod specification
3. `shared-ui/shared_ui.podspec` - UI framework pod specification

To update iOS dependencies:
```bash
cd iosApp
pod update
```

## Version Management

Version is managed in:
- **Android**: `gradle/libs.versions.toml` or build.gradle
- **iOS**: Info.plist (CFBundleShortVersionString)

## Troubleshooting

### iOS Build Fails
- Ensure pods are installed: `cd iosApp && pod install`
- Clean build: `xcodebuild clean`
- Check Xcode version: Requires Xcode 15+

### Android Build Fails
- Clean build: `./gradlew clean`
- Check JDK version: Requires JDK 17
- Ensure TMDB_API_KEY is set in secrets

### CocoaPods Issues
- Update CocoaPods: `gem update cocoapods`
- Clean pods: `cd iosApp && rm -rf Pods && pod install`
- Update repo: `pod repo update`

## Next Steps

When ready for store distribution:

1. **Android Play Store**
   - Create Play Console account
   - Generate signing keystore
   - Switch to `release-android.yml` workflow

2. **iOS App Store**
   - Get Apple Developer account
   - Create certificates and provisioning profiles
   - Consider using Fastlane Match for certificate management
   - Switch to `release-ios.yml` workflow

## CI/CD Architecture

```
GitHub Actions
├── android-release-simple.yml  (Current - unsigned APK)
├── ios-release-simple.yml      (Current - unsigned IPA)
├── release-android.yml         (Future - Play Store)
└── release-ios.yml            (Future - App Store/TestFlight)
```

## Support

For issues with releases, check:
1. GitHub Actions logs
2. Gradle build reports
3. Xcode build logs
4. This documentation