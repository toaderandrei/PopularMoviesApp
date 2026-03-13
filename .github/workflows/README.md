# GitHub Actions Workflows

Organized using naming convention since GitHub doesn't support subfolders in workflows.

## 🏗️ Build Workflows

| Workflow | File | Trigger | Description |
|----------|------|---------|-------------|
| Main Build | `build-main.yml` | Push to main | Full CI on main branch |
| PR Build | `build-pr.yml` | Pull requests | CI for pull requests |

## 🚀 Release Workflows

| Workflow | File | Trigger | Description |
|----------|------|---------|-------------|
| Android Release | `release-android.yml` | Manual | Release Android app |
| iOS Release | `release-ios.yml` | Manual | Release iOS app |

## ✅ Test Workflows

| Workflow | File | Trigger | Description |
|----------|------|---------|-------------|
| Android Unit Tests | `test-android-unit.yml` | PR/Push | Run unit tests |
| iOS Tests | `test-ios.yml` | PR/Push | Run iOS tests |

## 🔍 Lint/Quality Workflows

| Workflow | File | Trigger | Description |
|----------|------|---------|-------------|
| Kotlin Lint | `lint-kotlin.yml` | PR/Push | Kotlin code style |
| Android Lint | `lint-android.yml` | PR/Push | Android lint checks |

## 🔧 Utility Workflows

| Workflow | File | Trigger | Description |
|----------|------|---------|-------------|
| Update Dependencies | `update-dependencies.yml` | Schedule | Check for updates |

## Usage

### Running a Release
```bash
# Android
Go to Actions → "Release - Android" → Run workflow → Enter version

# iOS
Go to Actions → "Release - iOS" → Run workflow → Enter version
```

### Local Testing
```bash
# Test Android locally
./gradlew assembleRelease

# Test iOS locally
./gradlew iosRelease

# Run tests
./gradlew test
```

## Naming Convention

```
<category>-<platform>-<purpose>.yml

Categories:
- build: CI/CD builds
- release: Production releases
- test: Testing workflows
- lint: Code quality checks
- update: Maintenance tasks
```

## Benefits

✅ **Organized** - Clear categorization
✅ **Searchable** - Easy to find workflows
✅ **Consistent** - Same pattern for all
✅ **Scalable** - Easy to add new workflows