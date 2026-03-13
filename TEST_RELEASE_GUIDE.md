# Testing Release Workflows

## Prerequisites

First, commit your changes to test the workflows properly:

```bash
# Commit the new workflow files
git add .github/workflows/android-release-simple.yml
git add .github/workflows/ios-release-simple.yml
git add iosApp/Podfile
git add iosApp/Gemfile
git add iosApp/ExportOptions.plist
git add iosApp/fastlane/
git add shared/shared.podspec
git add shared-ui/shared_ui.podspec
git add docs/RELEASE.md

git commit -m "Add release workflows for Android and iOS"
git push origin dev/fix_ios
```

## Testing Android Release

### Step 1: Test Locally First

```bash
# Test if the Android build works
./gradlew assembleRelease

# Check if APK was created
ls -la app/build/outputs/apk/release/
```

### Step 2: Test GitHub Actions Workflow

1. Go to your GitHub repository: https://github.com/toaderandrei/popular-movies-kt

2. Click on **Actions** tab

3. Find **"Android Release"** workflow in the left sidebar

4. Click **"Run workflow"** button

5. Fill in:
   - Branch: `dev/fix_ios`
   - Version: `0.0.1` (for testing)

6. Click **"Run workflow"**

7. Monitor the workflow:
   - Should take 5-10 minutes
   - Check each step for errors

### Step 3: Verify Android Release

After workflow completes:

1. Go to **Releases** page
2. Look for **"Android v0.0.1"** release
3. Download the APK
4. Test installation:
   ```bash
   # If you have an Android device/emulator
   adb install downloaded-app.apk
   ```

## Testing iOS Release

### Step 1: Test Locally First

```bash
# Test if KMP framework builds
./gradlew :shared:embedAndSignAppleFrameworkForXcode

# Test CocoaPods setup
cd iosApp
pod install --repo-update

# Check if workspace was created
ls -la *.xcworkspace
cd ..
```

### Step 2: Test GitHub Actions Workflow

1. Go to **Actions** tab on GitHub

2. Find **"iOS Release"** workflow

3. Click **"Run workflow"**

4. Fill in:
   - Branch: `dev/fix_ios`
   - Version: `0.0.1`

5. Click **"Run workflow"**

6. Monitor the workflow:
   - Should take 10-15 minutes (iOS builds are slower)
   - CocoaPods installation might take time

### Step 3: Verify iOS Release

After workflow completes:

1. Go to **Releases** page
2. Look for **"iOS v0.0.1"** release
3. Download the IPA
4. The IPA will be unsigned, so you can:
   - Inspect it: `unzip -l downloaded.ipa`
   - Install with sideloading tools

## Quick Troubleshooting

### If Android workflow fails:

```bash
# Check locally
./gradlew clean
./gradlew assembleRelease --stacktrace

# Common issues:
# - Missing TMDB_API_KEY secret
# - Gradle build issues
```

### If iOS workflow fails:

```bash
# Check locally
cd iosApp
pod deintegrate
pod install --verbose

# Common issues:
# - CocoaPods not finding podspecs
# - KMP framework build issues
# - Missing workspace file
```

## Checking Workflow Logs

1. Click on the failed workflow run
2. Click on the failed job
3. Expand the failed step
4. Look for error messages

## Testing Checklist

- [ ] Android workflow runs successfully
- [ ] Android APK is uploaded to release
- [ ] Android APK can be downloaded
- [ ] iOS workflow runs successfully
- [ ] iOS IPA is uploaded to release
- [ ] iOS IPA can be downloaded
- [ ] Both releases show in GitHub Releases page

## Next Steps After Testing

If both workflows succeed:
1. Delete test releases (v0.0.1)
2. You're ready for real releases
3. When ready for stores, add signing secrets

If workflows fail:
1. Check the logs
2. Fix issues locally first
3. Push fixes and retest

## Common Secrets Needed (Optional for now)

In GitHub Settings → Secrets → Actions, you might need:

```yaml
# Required
TMDB_API_KEY: your_api_key

# Optional (for signed builds later)
GOOGLE_SERVICES_JSON: base64_encoded_json
SIGNING_KEY_BASE64: base64_encoded_keystore
SIGNING_STORE_PASSWORD: keystore_password
SIGNING_KEY_ALIAS: key_alias
SIGNING_KEY_PASSWORD: key_password
```