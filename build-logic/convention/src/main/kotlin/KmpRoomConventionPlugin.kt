import com.ant.popular.movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import androidx.room.gradle.RoomExtension

/**
 * Convention plugin for configuring Room Database in Kotlin Multiplatform projects.
 * Automatically configures KSP processors for all supported platforms.
 */
class KmpRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("androidx.room")
                apply("com.google.devtools.ksp")
            }

            // Configure Room schema directory
            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            // Add Room KSP processors for all platforms
            dependencies {
                add("kspAndroid", libs.findLibrary("room.compiler").get())
                add("kspIosX64", libs.findLibrary("room.compiler").get())
                add("kspIosArm64", libs.findLibrary("room.compiler").get())
                add("kspIosSimulatorArm64", libs.findLibrary("room.compiler").get())
            }
        }
    }
}
