# GitHub Workflows Organization

Since GitHub Actions doesn't support subfolders in `.github/workflows/`, we use a **naming convention** to organize workflows:

## Naming Convention

```
<category>-<platform>-<purpose>.yml
```

## Current Structure

### Build Workflows (Reusable)
- `build-android.yml` - Reusable Android build (debug or release via `build-type` input)
- `build-ios.yml` - Reusable iOS framework build (linkDebugFramework for simulator + device)

### Pipeline Workflows
- `build-main.yml` - Main branch CI: lint → buildAndroid (release) + buildIos → test
- `build-pr.yml` - Pull request CI: lint → buildAndroid (debug) + buildIos → test

### Release Workflows
- `release-android.yml` - Android release: calls `build-android.yml` (release), downloads signed artifacts, creates GitHub release
- `release-ios.yml` - iOS release to GitHub/TestFlight

### Test Workflows
- `test-android-unit.yml` - Android unit tests (manual dispatch)

### Lint/Quality
- `lint-android.yml` - Reusable Android lint checks

### Maintenance
- `update-dependencies.yml` - Dependency updates
- `setup-android-sdk.yml` - Manual SDK setup and cache warming

## Reusable Workflow Architecture

The CI pipelines share logic via reusable workflows (`workflow_call`):

```
build-pr.yml ──┬── lint-android.yml
               ├── build-android.yml (build-type: debug)
               ├── build-ios.yml
               └── test (inline job)

build-main.yml ┬── lint-android.yml
               ├── build-android.yml (build-type: release)
               ├── build-ios.yml
               └── test (inline job)

release-android.yml
               ├── check-artifacts (looks for today's signed build from build-main.yml)
               ├── download-existing (if found) OR build-fresh via build-android.yml (if not)
               └── release (downloads artifacts, creates GitHub release)
```

### build-android.yml

Accepts a `build-type` input (`debug` or `release`):
- **debug**: Runs `assembleDebug`
- **release**: Decodes keystore, runs `assembleRelease` + `bundleRelease`, verifies APK signing, uploads signed APK/AAB as `release-artifacts`

### build-ios.yml

Builds iOS frameworks on `macos-latest`:
- `linkDebugFrameworkIosSimulatorArm64`
- `linkDebugFrameworkIosArm64`

## Caching Strategy

### JDK + Gradle
All workflows use `actions/setup-java@v4` with `cache: 'gradle'`. This handles:
- **JDK**: Cached automatically via GitHub's tool-cache (downloaded once per runner image)
- **Gradle wrapper + caches**: Cached by `setup-java` based on `*.gradle.kts` and `gradle-wrapper.properties` hashes

### Android SDK
Cached via `actions/cache@v4` with key based on `libs.versions.toml`. The `setup-android-sdk.yml` workflow can be run manually to warm the cache.

### Release Artifacts
The signed APK/AAB from `build-android.yml` (release) is uploaded via `actions/upload-artifact@v4`. The `release-android.yml` workflow first checks if a signed build from today's `build-main.yml` run exists. If found, it downloads and reuses those artifacts. If not (e.g., no main build ran today), it triggers a full build from scratch. This ensures releases always use same-day artifacts and avoids unnecessary rebuilds.

## Visual Organization in GitHub UI

In the Actions tab, workflows are grouped by their prefix:
- All `build-*` workflows appear together
- All `release-*` workflows appear together
- All `test-*` workflows appear together

## Benefits

✅ **Clear organization** without subfolders
✅ **Easy to find** related workflows
✅ **Consistent naming** across platforms
✅ **GitHub UI groups** them naturally
✅ **DRY** - shared build logic via reusable workflows
✅ **Efficient** - JDK, Gradle, and SDK cached across runs
✅ **Release reuse** - signed artifacts built once, released without rebuilding
