import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Convention plugin that configures the Ben Manes dependency updates plugin
 * with standard settings for this project.
 *
 * Applies to: Root project
 *
 * Configuration:
 * - Output format: JSON
 * - Output directory: build/dependencyUpdates/
 * - Rejects: alpha, beta, rc, cr, m, preview, dev versions
 *
 * Usage:
 * ```kotlin
 * // In root build.gradle.kts
 * plugins {
 *     alias(libs.plugins.ben.manes.versions)
 *     alias(libs.plugins.popular.movies.dependency.updates)
 * }
 * ```
 */
class DependencyUpdatesConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.withType<DependencyUpdatesTask>().configureEach {
                outputFormatter = "json"
                outputDir = layout.buildDirectory.dir("dependencyUpdates").get().asFile.absolutePath

                rejectVersionIf {
                    val dominated = listOf("alpha", "beta", "rc", "cr", "m", "preview", "dev")
                    dominated.any { qualifier ->
                        candidate.version.lowercase().contains(qualifier)
                    }
                }
            }
        }
    }
}
