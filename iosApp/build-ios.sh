#!/bin/bash

# iOS Build Script
# Builds the iOS app and generates .app and .ipa artifacts

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
IOS_APP_DIR="$PROJECT_ROOT/iosApp"
SCHEME="PopularMovies"
CONFIGURATION="${1:-Debug}"  # Debug or Release
DESTINATION="${2:-generic/platform=iOS Simulator}"  # Default to simulator

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}iOS Build Script${NC}"
echo -e "${GREEN}========================================${NC}"
echo "Configuration: $CONFIGURATION"
echo "Destination: $DESTINATION"
echo ""

# Step 1: Build Kotlin Framework
echo -e "${YELLOW}Step 1/5: Building Kotlin Multiplatform framework...${NC}"
cd "$PROJECT_ROOT"
if [ "$CONFIGURATION" == "Release" ]; then
    ./gradlew :shared:embedAndSignAppleFrameworkForXcode -Pconfiguration.build.type=release
else
    ./gradlew :shared:embedAndSignAppleFrameworkForXcode
fi
echo -e "${GREEN}✓ Kotlin framework built${NC}"
echo ""

# Step 2: Generate Xcode project (if using XcodeGen)
if [ -f "$IOS_APP_DIR/project.yml" ]; then
    echo -e "${YELLOW}Step 2/5: Generating Xcode project with XcodeGen...${NC}"
    cd "$IOS_APP_DIR"

    # Check if xcodegen is installed
    if ! command -v xcodegen &> /dev/null; then
        echo -e "${RED}ERROR: xcodegen not found${NC}"
        echo "Install with: brew install xcodegen"
        exit 1
    fi

    xcodegen generate
    echo -e "${GREEN}✓ Xcode project generated${NC}"
else
    echo -e "${YELLOW}Step 2/5: Skipping XcodeGen (no project.yml found)${NC}"
fi
echo ""

# Step 3: Clean build folder
echo -e "${YELLOW}Step 3/5: Cleaning build folder...${NC}"
cd "$IOS_APP_DIR"
xcodebuild clean \
    -project PopularMovies.xcodeproj \
    -scheme "$SCHEME" \
    -configuration "$CONFIGURATION" \
    > /dev/null 2>&1 || true
echo -e "${GREEN}✓ Build folder cleaned${NC}"
echo ""

# Step 4: Build the app
echo -e "${YELLOW}Step 4/5: Building iOS app...${NC}"
cd "$IOS_APP_DIR"

BUILD_DIR="$IOS_APP_DIR/build"
ARCHIVE_PATH="$BUILD_DIR/$SCHEME.xcarchive"

if [[ "$DESTINATION" == *"Simulator"* ]]; then
    # Simulator build
    echo "Building for iOS Simulator..."
    xcodebuild \
        -project PopularMovies.xcodeproj \
        -scheme "$SCHEME" \
        -configuration "$CONFIGURATION" \
        -destination "$DESTINATION" \
        -derivedDataPath "$BUILD_DIR/DerivedData" \
        build

    APP_PATH="$BUILD_DIR/DerivedData/Build/Products/$CONFIGURATION-iphonesimulator/$SCHEME.app"

elif [[ "$DESTINATION" == *"device"* ]] || [[ "$DESTINATION" == "generic/platform=iOS" ]]; then
    # Device build (requires code signing)
    echo "Building for iOS Device..."
    xcodebuild archive \
        -project PopularMovies.xcodeproj \
        -scheme "$SCHEME" \
        -configuration "$CONFIGURATION" \
        -archivePath "$ARCHIVE_PATH" \
        -destination "generic/platform=iOS" \
        CODE_SIGN_IDENTITY="" \
        CODE_SIGNING_REQUIRED=NO \
        CODE_SIGNING_ALLOWED=NO

    APP_PATH="$ARCHIVE_PATH/Products/Applications/$SCHEME.app"
fi

if [ ! -d "$APP_PATH" ]; then
    echo -e "${RED}ERROR: Build failed - .app not found at $APP_PATH${NC}"
    exit 1
fi

echo -e "${GREEN}✓ iOS app built successfully${NC}"
echo "  App location: $APP_PATH"
echo ""

# Step 5: Create output artifacts
echo -e "${YELLOW}Step 5/5: Creating output artifacts...${NC}"

OUTPUT_DIR="$BUILD_DIR/outputs"
mkdir -p "$OUTPUT_DIR"

# Copy .app bundle
cp -R "$APP_PATH" "$OUTPUT_DIR/"
echo -e "${GREEN}✓ .app bundle: $OUTPUT_DIR/$SCHEME.app${NC}"

# Create .ipa if building for device
if [[ "$DESTINATION" == *"device"* ]] || [[ "$DESTINATION" == "generic/platform=iOS" ]]; then
    IPA_PATH="$OUTPUT_DIR/$SCHEME.ipa"

    # Create Payload directory
    PAYLOAD_DIR="$BUILD_DIR/Payload"
    rm -rf "$PAYLOAD_DIR"
    mkdir -p "$PAYLOAD_DIR"

    # Copy app to Payload
    cp -R "$APP_PATH" "$PAYLOAD_DIR/"

    # Create IPA
    cd "$BUILD_DIR"
    zip -r "$IPA_PATH" Payload > /dev/null

    # Cleanup
    rm -rf "$PAYLOAD_DIR"

    echo -e "${GREEN}✓ .ipa file: $IPA_PATH${NC}"
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Build Complete!${NC}"
echo -e "${GREEN}========================================${NC}"
echo "Artifacts location: $OUTPUT_DIR"
ls -lh "$OUTPUT_DIR"
echo ""

# Print summary
echo -e "${GREEN}Summary:${NC}"
echo "  Configuration: $CONFIGURATION"
echo "  Platform: $(echo $DESTINATION | grep -q Simulator && echo 'Simulator' || echo 'Device')"
echo "  Output: $OUTPUT_DIR"
