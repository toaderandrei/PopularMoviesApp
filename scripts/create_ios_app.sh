#!/bin/bash

# Create new iOS app project using Xcode template
cd iosApp

# Create the Xcode project using command line
xcodebuild -project PopularMovies.xcodeproj || true

# Actually, let's use the Swift package manager approach
# Create Package.swift for easier setup

echo "Creating iOS app structure..."

# Ensure directories exist
mkdir -p iosApp/Assets.xcassets/AppIcon.appiconset
mkdir -p iosApp/Preview\ Content

# Create Info.plist
cat > iosApp/Info.plist << 'PLIST'
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleDevelopmentRegion</key>
    <string>$(DEVELOPMENT_LANGUAGE)</string>
    <key>CFBundleExecutable</key>
    <string>$(EXECUTABLE_NAME)</string>
    <key>CFBundleIdentifier</key>
    <string>com.ant.popularmovies</string>
    <key>CFBundleInfoDictionaryVersion</key>
    <string>6.0</string>
    <key>CFBundleName</key>
    <string>$(PRODUCT_NAME)</string>
    <key>CFBundlePackageType</key>
    <string>$(PRODUCT_BUNDLE_PACKAGE_TYPE)</string>
    <key>CFBundleShortVersionString</key>
    <string>0.1.0</string>
    <key>CFBundleVersion</key>
    <string>1</string>
    <key>LSRequiresIPhoneOS</key>
    <true/>
    <key>UIApplicationSceneManifest</key>
    <dict>
        <key>UIApplicationSupportsMultipleScenes</key>
        <true/>
    </dict>
    <key>UILaunchScreen</key>
    <dict/>
    <key>UIRequiredDeviceCapabilities</key>
    <array>
        <string>armv7</string>
    </array>
    <key>UISupportedInterfaceOrientations</key>
    <array>
        <string>UIInterfaceOrientationPortrait</string>
        <string>UIInterfaceOrientationLandscapeLeft</string>
        <string>UIInterfaceOrientationLandscapeRight</string>
    </array>
</dict>
</plist>
PLIST

echo "iOS app structure created!"
echo ""
echo "Next steps:"
echo "1. Open Xcode"
echo "2. File → New → Project"
echo "3. Choose 'App' template"
echo "4. Fill in:"
echo "   - Product Name: PopularMovies"
echo "   - Team: Your team"
echo "   - Organization Identifier: com.ant"
echo "   - Interface: SwiftUI"
echo "   - Language: Swift"
echo "5. Save to: $(pwd)"
echo "6. Replace the created files with existing ones"

