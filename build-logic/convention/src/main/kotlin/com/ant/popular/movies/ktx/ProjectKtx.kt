package com.ant.popular.movies.ktx

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * Gets the version catalog for type-safe dependency access in convention plugins.
 *
 * Usage in plugins:
 * ```kotlin
 * val libs = project.libs
 * dependencies {
 *     implementation(libs.findLibrary("koin-core").get())
 * }
 * ```
 */
val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Checks if this project is a Kotlin Multiplatform module.
 */
val Project.isKmpModule: Boolean
    get() = pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")

/**
 * Checks if this project is an Android application module.
 */
val Project.isAndroidApplication: Boolean
    get() = pluginManager.hasPlugin("com.android.application")

/**
 * Checks if this project is an Android library module.
 */
val Project.isAndroidLibrary: Boolean
    get() = pluginManager.hasPlugin("com.android.library")

/**
 * Checks if this project has Compose enabled.
 */
val Project.hasCompose: Boolean
    get() = pluginManager.hasPlugin("org.jetbrains.kotlin.plugin.compose") ||
            pluginManager.hasPlugin("org.jetbrains.compose")

/**
 * Safely applies a plugin only if not already applied.
 * Returns true if the plugin was applied, false if it was already present.
 */
fun Project.applyPluginSafely(id: String): Boolean {
    return if (!pluginManager.hasPlugin(id)) {
        pluginManager.apply(id)
        true
    } else {
        false
    }
}

/**
 * Gets a property from gradle.properties or local.properties, with an optional default value.
 *
 * Usage:
 * ```kotlin
 * val apiKey = project.getPropertyOrDefault("tmdb_api_key", "")
 * ```
 */
fun Project.getPropertyOrDefault(key: String, defaultValue: String = ""): String {
    return findProperty(key)?.toString() ?: defaultValue
}

/**
 * Logs a message with the project name prefix for easier debugging.
 */
fun Project.logInfo(message: String) {
    logger.lifecycle("[$name] $message")
}

/**
 * Logs a warning with the project name prefix.
 */
fun Project.logWarn(message: String) {
    logger.warn("[$name] $message")
}
