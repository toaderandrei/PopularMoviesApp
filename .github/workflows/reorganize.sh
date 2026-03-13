#!/bin/bash

# Script to reorganize workflow files with consistent naming

echo "Reorganizing GitHub Actions workflows..."

# Create archive directory
mkdir -p _archive

# Function to safely move files
safe_move() {
    if [ -f "$1" ]; then
        mv "$1" "$2"
        echo "✓ Renamed: $1 → $2"
    fi
}

# Archive old/duplicate workflows
safe_move "android-release-simple.yml" "_archive/android-release-simple.yml"
safe_move "ios-release-simple.yml" "_archive/ios-release-simple.yml"
safe_move "ios-release-complete.yml" "_archive/ios-release-complete.yml"
safe_move "release-ios-unsigned.yml" "_archive/release-ios-unsigned.yml"
safe_move "android-gradle-release.yml" "_archive/android-gradle-release.yml"
safe_move "ios-gradle-release.yml" "_archive/ios-gradle-release.yml"

# Rename to organized structure
safe_move "master.yml" "build-main.yml"
safe_move "pull_request.yml" "build-pr.yml"
safe_move "release.yml" "release-legacy.yml"
safe_move "setup-android-sdk.yml" "utility-android-sdk.yml"
safe_move "update-dependencies.yml" "update-dependencies.yml"

echo ""
echo "Current workflow structure:"
echo "=========================="
ls -1 *.yml | grep -v "_archive" | sort | while read file; do
    category=$(echo "$file" | cut -d'-' -f1)
    case $category in
        build) echo "🏗️  $file" ;;
        release) echo "🚀 $file" ;;
        test) echo "✅ $file" ;;
        lint) echo "🔍 $file" ;;
        update|utility) echo "🔧 $file" ;;
        *) echo "📄 $file" ;;
    esac
done

echo ""
echo "Archived workflows in _archive/:"
ls -1 _archive/*.yml 2>/dev/null | wc -l | xargs echo "Total:"