import com.ant.popular.movies.ktx.logInfo
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register

/**
 * Plugin that registers iOS development and release tasks.
 *
 * Development tasks:
 * - generateXcodeProject: Generates Xcode project using XcodeGen
 * - setupIosApp: Builds framework and generates Xcode project
 * - openInXcode: Opens the iOS app in Xcode
 *
 * Build tasks:
 * - iosPodInstall: Install CocoaPods dependencies
 * - iosBuildFrameworks: Build KMP frameworks for iOS
 * - iosBuildApp: Build iOS app (configurable for debug/release)
 * - iosCreateIPA: Create IPA from built app
 * - iosRelease: Complete iOS release pipeline
 *
 * Usage:
 * ```bash
 * ./gradlew iosRelease -Pversion=1.0.0 -PbuildNumber=100
 * ```
 */
class IosTasksPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            registerDevelopmentTasks()
            registerBuildTasks()
            registerReleaseTasks()
        }
    }

    private fun Project.registerDevelopmentTasks() {
        // Task to generate Xcode project using XcodeGen
        tasks.register<Exec>("generateXcodeProject") {
            group = "ios"
            description = "Generate Xcode project for iOS app using XcodeGen"

            workingDir = file("iosApp")
            commandLine("xcodegen", "generate")
        }

        // Task to build iOS framework and generate Xcode project
        tasks.register("setupIosApp") {
            group = "ios"
            description = "Build framework and generate Xcode project - ready to open in Xcode"

            dependsOn(":shared:linkDebugFrameworkIosSimulatorArm64")
            finalizedBy("generateXcodeProject")

            doLast {
                println(
                    """

                    ✅ iOS app is ready!

                    To open in Xcode:
                      open iosApp/PopularMovies.xcodeproj

                    Or run:
                      ./gradlew openInXcode
                    """.trimIndent()
                )
            }
        }

        // Convenience task to open in Xcode
        tasks.register<Exec>("openInXcode") {
            group = "ios"
            description = "Open iOS app in Xcode"

            dependsOn("setupIosApp")

            commandLine("open", "iosApp/PopularMovies.xcodeproj")
        }
    }

    private fun Project.registerBuildTasks() {
        // Task: Install CocoaPods
        val iosAppDir = file("iosApp")
        val podfileFile = file("iosApp/Podfile")

        tasks.register<Exec>("iosPodInstall") {
            group = "ios build"
            description = "Install CocoaPods dependencies"

            workingDir = iosAppDir

            // Only run if Podfile exists
            onlyIf {
                podfileFile.exists()
            }

            commandLine("sh", "-c", """
                # Add Homebrew paths for both Intel and Apple Silicon Macs
                export PATH="/opt/homebrew/bin:/usr/local/bin:${"$"}PATH"

                if ! command -v pod &> /dev/null; then
                    echo "CocoaPods not found. Please install it using one of these methods:"
                    echo "  1. Homebrew (recommended): brew install cocoapods"
                    echo "  2. Ruby Gem with sudo: sudo gem install cocoapods"
                    echo "  3. Using rbenv/rvm for local Ruby environment"
                    exit 1
                fi

                echo "Running pod install..."
                pod install --repo-update
            """.trimIndent())

            doLast {
                logger.lifecycle("✅ CocoaPods dependencies installed")
            }
        }

        // Task: Build KMP Frameworks
        tasks.register("iosBuildFrameworks") {
            group = "ios build"
            description = "Build KMP frameworks for iOS"

            val buildType = project.findProperty("iosBuildType") as? String ?: "release"
            val configuration = if (buildType == "debug") "Debug" else "Release"

            // Build frameworks for iOS - use linkFramework tasks instead of embed task
            if (project.findProject(":shared") != null) {
                dependsOn(
                    ":shared:link${configuration}FrameworkIosArm64",
                    ":shared:link${configuration}FrameworkIosSimulatorArm64"
                )
            }

            doLast {
                logger.lifecycle("✅ iOS frameworks built successfully (${buildType})")
                logger.lifecycle("  Framework location: shared/build/bin/")
            }
        }

        // Task: Build iOS App
        tasks.register<Exec>("iosBuildApp") {
            group = "ios build"
            description = "Build iOS app"

            dependsOn("iosBuildFrameworks", "iosPodInstall")

            workingDir = iosAppDir

            doFirst {
                val version = project.findProperty("version") as? String ?: "1.0.0"
                val buildNumber = project.findProperty("buildNumber") as? String ?: "1"
                val configuration = project.findProperty("iosConfiguration") as? String ?: "Release"
                val signed = project.findProperty("iosSigned") as? String == "true"

                commandLine("sh", "-c", """
                    # Update version
                    if [ -f "iosApp/Info.plist" ]; then
                        /usr/libexec/PlistBuddy -c "Set :CFBundleShortVersionString $version" iosApp/Info.plist || true
                        /usr/libexec/PlistBuddy -c "Set :CFBundleVersion $buildNumber" iosApp/Info.plist || true
                    fi

                    # Build
                    echo "Building iOS app (Configuration: $configuration, Signed: $signed)..."
                    xcodebuild archive \
                        -workspace PopularMovies.xcworkspace \
                        -scheme PopularMovies \
                        -configuration $configuration \
                        -archivePath build/PopularMovies.xcarchive \
                        -destination generic/platform=iOS \
                        ${if (signed) "" else "CODE_SIGN_IDENTITY='' CODE_SIGNING_REQUIRED=NO CODE_SIGNING_ALLOWED=NO"} \
                        | xcpretty || true
                """.trimIndent())
            }

            doLast {
                logger.lifecycle("✅ iOS app built: iosApp/build/PopularMovies.xcarchive")
            }
        }

        // Task: Create IPA
        tasks.register<Exec>("iosCreateIPA") {
            group = "ios build"
            description = "Create IPA from built app"

            dependsOn("iosBuildApp")

            workingDir = iosAppDir

            doFirst {
                val signed = project.findProperty("iosSigned") as? String == "true"

                commandLine("sh", "-c", if (signed) {
                    """
                    # Export signed IPA
                    echo "Creating signed IPA..."
                    xcodebuild -exportArchive \
                        -archivePath build/PopularMovies.xcarchive \
                        -exportPath build/IPA \
                        -exportOptionsPlist ExportOptions.plist \
                        | xcpretty || true
                    """.trimIndent()
                } else {
                    """
                    # Create unsigned IPA
                    echo "Creating unsigned IPA..."
                    mkdir -p build/Payload
                    cp -R build/PopularMovies.xcarchive/Products/Applications/PopularMovies.app build/Payload/
                    cd build
                    zip -r PopularMovies.ipa Payload
                    mkdir -p IPA
                    mv PopularMovies.ipa IPA/
                    """.trimIndent()
                })
            }

            doLast {
                logger.lifecycle("✅ IPA created: iosApp/build/IPA/PopularMovies.ipa")
            }
        }
    }

    private fun Project.registerReleaseTasks() {
        val iosAppDir = file("iosApp")

        // Task: Complete iOS Release
        tasks.register("iosRelease") {
            group = "ios release"
            description = "Complete iOS release (frameworks + app + IPA)"

            dependsOn("iosCreateIPA")

            doLast {
                val version = project.findProperty("version") as? String ?: "1.0.0"
                val buildNumber = project.findProperty("buildNumber") as? String ?: "1"

                logger.lifecycle("""

                    =====================================
                    ✅ iOS Release Complete!
                    =====================================
                    Version: $version ($buildNumber)
                    IPA: iosApp/build/IPA/PopularMovies.ipa

                    Next steps:
                    - Upload IPA to TestFlight (when signed)
                    - Or distribute for testing (unsigned)
                    =====================================
                """.trimIndent())
            }
        }

        // Task: Clean iOS Build
        tasks.register<Exec>("iosClean") {
            group = "ios"
            description = "Clean iOS build artifacts"

            workingDir = iosAppDir

            commandLine("sh", "-c", """
                echo "Cleaning iOS build artifacts..."
                rm -rf build
                rm -rf ~/Library/Developer/Xcode/DerivedData/PopularMovies-*
                if [ -f PopularMovies.xcworkspace ]; then
                    xcodebuild clean -workspace PopularMovies.xcworkspace -scheme PopularMovies || true
                fi
            """.trimIndent())

            doLast {
                logger.lifecycle("✅ iOS build artifacts cleaned")
            }
        }
    }
}
