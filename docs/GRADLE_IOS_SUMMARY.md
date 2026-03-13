# iOS Build Centralized in Gradle

## What We Built

All iOS build logic is now centralized in Gradle via the `IosTasksPlugin`, making iOS builds as simple as Android builds.

## Key Benefits

✅ **One Command Release**: `./gradlew iosRelease -Pversion=1.0.0`
✅ **Consistent with Android**: Similar commands for both platforms
✅ **CI/CD Friendly**: GitHub Actions just calls Gradle tasks
✅ **No Duplicate Logic**: All build steps in one plugin

## Architecture

```
build-logic/convention/
└── src/main/kotlin/
    └── IosTasksPlugin.kt     # All iOS build logic here

Root project:
└── build.gradle.kts
    └── id("popular.movies.ios.tasks")  # Applied here
```

## Usage Examples

### Local Development
```bash
# Setup and open Xcode
./gradlew openInXcode

# Build everything
./gradlew iosRelease
```

### GitHub Actions
```yaml
# Super simple workflow
- run: ./gradlew iosRelease -Pversion=${{ inputs.version }}
```

### Testing
```bash
# Test locally first
./gradlew iosRelease -Pversion=0.0.1

# Check output
ls -la iosApp/build/IPA/
```

## Comparison with tebi-main

**tebi-main approach:**
- Uses GitLab CI with Fastlane
- Separate Ruby/Fastlane configuration
- Multiple configuration files

**Our approach:**
- Everything in Gradle (single language)
- Reuses existing build-logic infrastructure
- Simpler for small projects

## Files Created

### Core Plugin
- `build-logic/convention/src/main/kotlin/IosTasksPlugin.kt` - All iOS tasks

### Workflows (Simplified)
- `.github/workflows/ios-gradle-release.yml` - Uses Gradle tasks
- `.github/workflows/android-gradle-release.yml` - Consistent with iOS

### CocoaPods Setup
- `iosApp/Podfile` - Dependencies
- `shared/shared.podspec` - KMP framework pod
- `shared-ui/shared_ui.podspec` - UI framework pod

### Documentation
- `docs/IOS_BUILD_GUIDE.md` - Complete usage guide
- `docs/RELEASE.md` - Release process

## Next Steps

1. **Test it:**
   ```bash
   ./gradlew iosRelease -Pversion=0.0.1
   ```

2. **Commit and push:**
   ```bash
   git add -A
   git commit -m "Centralize iOS build logic in Gradle"
   git push
   ```

3. **Run GitHub Actions:**
   - Go to Actions tab
   - Run "iOS Release (Gradle)" workflow

## Future Enhancements

When ready for App Store:
1. Add signing configuration to the plugin
2. Add Fastlane integration task
3. Add TestFlight upload task

The plugin is extensible - just add more tasks to `IosTasksPlugin.kt`!