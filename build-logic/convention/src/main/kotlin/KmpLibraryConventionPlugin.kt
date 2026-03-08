import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.ant.popular.movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
                    .configureEach {
                        compileSdk = 36
                        minSdk = 27
                        withHostTestBuilder {}

                        // Auto-configure namespace based on project path
                        // e.g., :core:models -> com.ant.models
                        namespace = project.path
                            .removePrefix(":")
                            .replace(":", ".")
                            .let { "com.ant.$it" }
                    }

                iosX64()
                iosArm64()
                iosSimulatorArm64()

                sourceSets.commonMain.dependencies {
                    implementation(libs.findLibrary("kotlinx.coroutines.core").get())
                }

                sourceSets.commonTest.dependencies {
                    implementation(kotlin("test"))
                    implementation(libs.findLibrary("kotlinx.coroutines.test").get())
                }
            }

            // Configure JVM target for all Kotlin compilation tasks
            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }
}
