import com.ant.popular.movies.ktx.logInfo
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register

/**
 * Plugin that registers iOS development tasks.
 *
 * Provides the following tasks:
 * - generateXcodeProject: Generates Xcode project using XcodeGen
 * - setupIosApp: Builds framework and generates Xcode project
 * - openInXcode: Opens the iOS app in Xcode
 *
 * Apply in root build.gradle.kts:
 * ```kotlin
 * plugins {
 *     alias(libs.plugins.popular.movies.ios.tasks) apply false
 * }
 * ```
 */
class IosTasksPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
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
    }
}
