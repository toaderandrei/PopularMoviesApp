package com.ant.popular.movies.ktx

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

/**
 * Configures iOS framework export for KMP modules.
 *
 * This sets up a static framework with the specified base name and exports
 * the provided dependencies for iOS consumption.
 *
 * Usage:
 * ```kotlin
 * kotlin {
 *     configureIosFramework(
 *         baseName = "Shared",
 *         exports = listOf(projects.sharedUi, projects.core.models)
 *     )
 * }
 * ```
 *
 * @param baseName The framework's base name (e.g., "Shared")
 * @param exports List of project dependencies to export in the framework
 */
fun KotlinMultiplatformExtension.configureIosFramework(
    baseName: String,
    exports: List<Any> = emptyList()
) {
    applyDefaultHierarchyTemplate()

    targets.filterIsInstance<KotlinNativeTarget>().forEach { target ->
        target.binaries.framework {
            this.baseName = baseName
            isStatic = true

            exports.forEach { dependency ->
                export(dependency)
            }
        }
    }
}
