import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.ant.popular.movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("popular.movies.kmp.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                // Auto-configure namespace for feature modules
                // e.g., :features:movies -> com.ant.feature.movies
                targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
                    .configureEach {
                        namespace = project.path
                            .removePrefix(":")
                            .replace(":", ".")
                            .let { "com.ant.$it" }
                    }
                sourceSets.apply {
                    commonMain.dependencies {
                        // Navigation Compose (Mature KMP solution with full iOS support)
                        implementation(libs.findLibrary("navigation.compose.multiplatform").get())

                        // Koin for KMP
                        implementation(libs.findLibrary("koin.core").get())
                        implementation(libs.findLibrary("koin.compose").get())
                        implementation(libs.findLibrary("koin.compose.viewmodel").get())
                    }

                    androidMain.dependencies {
                        // Android-specific Koin
                        implementation(libs.findLibrary("koin.android").get())
                        implementation(libs.findLibrary("koin.compose.viewmodel.navigation").get())

                        // Android Lifecycle (Android-only, not available for iOS)
                        implementation(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                        implementation(libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())

                        // Android Compose UI Tooling
                        implementation(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
                    }
                }
            }
        }
    }
}
