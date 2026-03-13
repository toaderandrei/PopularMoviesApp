package com.ant.popular.movies.ktx

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider

/**
 * Extension functions for working with dependencies in convention plugins.
 */

/**
 * Finds a library from the version catalog, throwing an error if not found.
 *
 * Usage:
 * ```kotlin
 * val koinCore = libs.library("koin-core")
 * dependencies {
 *     implementation(koinCore)
 * }
 * ```
 */
fun VersionCatalog.library(alias: String): Provider<MinimalExternalModuleDependency> {
    return findLibrary(alias).orElseThrow {
        IllegalArgumentException("Library '$alias' not found in version catalog")
    }
}

/**
 * Finds a version from the version catalog, throwing an error if not found.
 *
 * Usage:
 * ```kotlin
 * val kotlinVersion = libs.version("kotlin")
 * ```
 */
fun VersionCatalog.version(alias: String): String {
    return findVersion(alias).orElseThrow {
        IllegalArgumentException("Version '$alias' not found in version catalog")
    }.requiredVersion
}

/**
 * Safely finds a library, returning null if not found.
 */
fun VersionCatalog.libraryOrNull(alias: String): Provider<MinimalExternalModuleDependency>? {
    return findLibrary(alias).orElse(null)
}

/**
 * Safely finds a version, returning null if not found.
 */
fun VersionCatalog.versionOrNull(alias: String): String? {
    return findVersion(alias).orElse(null)?.requiredVersion
}
