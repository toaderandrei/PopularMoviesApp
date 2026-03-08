#!/bin/bash

echo "🔧 Setting up iOS project with Shared framework..."

# Wait for Xcode project to be created
if [ ! -d "PopularMovies/PopularMovies.xcodeproj" ]; then
    echo "❌ Error: Xcode project not found at PopularMovies/PopularMovies.xcodeproj"
    echo "Please create the Xcode project first following the instructions."
    exit 1
fi

# Copy our Swift files to the new project
echo "📝 Copying Swift source files..."
cp -f iosApp/PopularMoviesApp.swift PopularMovies/PopularMovies/
cp -f iosApp/ContentView.swift PopularMovies/PopularMovies/

# Copy assets if they exist
if [ -d "iosApp/Assets.xcassets" ]; then
    echo "🎨 Copying assets..."
    cp -rf iosApp/Assets.xcassets/* PopularMovies/PopularMovies/Assets.xcassets/
fi

echo ""
echo "✅ Files copied successfully!"
echo ""
echo "Next steps in Xcode:"
echo "1. Open the project: PopularMovies/PopularMovies.xcodeproj"
echo "2. Select 'PopularMovies' project in navigator"
echo "3. Select 'PopularMovies' target"
echo "4. Go to 'General' tab"
echo "5. Scroll to 'Frameworks, Libraries, and Embedded Content'"
echo "6. Click '+' → 'Add Other...' → 'Add Files...'"
echo "7. Navigate to: ../shared/build/bin/iosSimulatorArm64/debugFramework/"
echo "8. Select 'Shared.framework'"
echo "9. Set to 'Embed & Sign'"
echo ""
echo "Then add build phase:"
echo "1. Go to 'Build Phases' tab"
echo "2. Click '+' → 'New Run Script Phase'"
echo "3. Rename to 'Build Shared Framework'"
echo "4. Add script:"
echo '   cd "$SRCROOT/../.."'
echo '   ./gradlew :shared:linkDebugFrameworkIosSimulatorArm64'
echo "5. Drag this phase BEFORE 'Compile Sources'"
echo ""
echo "Ready to run! Press Cmd+R in Xcode"

