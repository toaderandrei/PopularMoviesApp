# GitHub Workflows Organization

Since GitHub Actions doesn't support subfolders in `.github/workflows/`, we use a **naming convention** to organize workflows:

## Naming Convention

```
<category>-<platform>-<purpose>.yml
```

## Current Structure

### Build Workflows
- `build-main.yml` - Main branch CI (Android + tests)
- `build-pr.yml` - Pull request CI (Android + tests)

### Release Workflows
- `release-android.yml` - Android release to GitHub/Play Store
- `release-ios.yml` - iOS release to GitHub/TestFlight

### Test Workflows
- `test-android-unit.yml` - Android unit tests
- `test-android-instrumented.yml` - Android UI tests
- `test-ios.yml` - iOS tests

### Lint/Quality
- `lint-android.yml` - Android lint checks
- `lint-kotlin.yml` - Kotlin code style

### Maintenance
- `update-dependencies.yml` - Dependency updates
- `setup-android-sdk.yml` - Reusable SDK setup

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