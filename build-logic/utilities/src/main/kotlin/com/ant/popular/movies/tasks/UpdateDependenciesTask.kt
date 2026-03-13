package com.ant.popular.movies.tasks

import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Task to automatically update dependency versions in libs.versions.toml
 * based on the ben-manes versions plugin JSON report.
 *
 * Usage:
 * ```kotlin
 * tasks.register<UpdateDependenciesTask>("updateDependencies") {
 *     versionCatalogFile.set(file("gradle/libs.versions.toml"))
 *     versionsReport.set(file("build/dependencyUpdates/report.json"))
 *     excludeKeys.set(listOf("popular-movies-version", "kotlin", "androidGradlePlugin"))
 * }
 * ```
 */
abstract class UpdateDependenciesTask : DefaultTask() {

    /**
     * The libs.versions.toml file to update.
     */
    @get:InputFile
    abstract val versionCatalogFile: RegularFileProperty

    /**
     * The JSON report from ben-manes versions plugin.
     */
    @get:InputFile
    abstract val versionsReport: RegularFileProperty

    /**
     * Version keys that should never be auto-updated.
     */
    @get:Input
    abstract val excludeKeys: ListProperty<String>

    init {
        group = "dependencies"
        description = "Updates dependency versions in libs.versions.toml based on available updates"

        // Default exclude keys
        excludeKeys.convention(listOf("popular-movies-version", "kotlin", "androidGradlePlugin"))
    }

    @TaskAction
    fun updateDependencies() {
        val tomlFile = versionCatalogFile.get().asFile
        val reportFile = versionsReport.get().asFile

        if (!reportFile.exists()) {
            logger.error("Versions report not found: ${reportFile.absolutePath}")
            logger.error("Run './gradlew dependencyUpdates' first to generate the report")
            return
        }

        val availableUpdates = parseAvailableUpdates(reportFile)
        if (availableUpdates.isEmpty()) {
            logger.lifecycle("No dependency updates available")
            return
        }

        val tomlLines = tomlFile.readLines()
        val versionRefMap = parseVersionRefMap(tomlLines)
        val versionUpdates = resolveVersionUpdates(versionRefMap, availableUpdates)

        if (versionUpdates.isEmpty()) {
            logger.lifecycle("No version catalog updates needed")
            return
        }

        val updatedLines = applyVersionUpdates(tomlLines, versionUpdates)
        tomlFile.writeText(updatedLines.joinToString("\n"))

        logger.lifecycle("✅ Updated ${versionUpdates.size} dependencies:")
        versionUpdates.forEach { (key, version) ->
            logger.lifecycle("  $key -> $version")
        }
    }

    /**
     * Parses the ben-manes JSON report and returns a map of maven coordinates (group:name)
     * to the latest stable version available.
     */
    @Suppress("UNCHECKED_CAST")
    private fun parseAvailableUpdates(reportFile: File): Map<String, String> {
        val json = JsonSlurper().parseText(reportFile.readText()) as Map<String, Any>
        val deps = (json["outdated"] as? Map<String, Any>)
            ?.get("dependencies") as? List<Map<String, Any>> ?: return emptyMap()

        val updates = mutableMapOf<String, String>()
        for (dep in deps) {
            val group = dep["group"] as? String ?: continue
            val name = dep["name"] as? String ?: continue
            val available = dep["available"] as? Map<String, Any> ?: continue
            // Prefer stable milestone, then fall back through minor/patch/major
            val version = (available["milestone"] ?: available["minor"]
                    ?: available["patch"] ?: available["major"]) as? String ?: continue
            updates["$group:$name"] = version
        }
        return updates
    }

    /**
     * Parses the [libraries] section of a TOML version catalog and builds a mapping
     * from version.ref keys to their maven coordinates (group:name).
     */
    private fun parseVersionRefMap(tomlLines: List<String>): Map<String, Set<String>> {
        val refRegex = """^\s*[\w\-]+\s*=\s*\{.*version\.ref\s*=\s*"([\w\-]+)".*\}""".toRegex()
        val coordRegex = """(?:module\s*=\s*"([^"]+:[^"]+)"|group\s*=\s*"([^"]+)".*name\s*=\s*"([^"]+)")""".toRegex()

        val result = mutableMapOf<String, MutableSet<String>>()
        for (line in tomlLines) {
            val versionRef = refRegex.find(line)?.groupValues?.get(1) ?: continue
            val coordMatch = coordRegex.find(line) ?: continue
            val coordinate = if (coordMatch.groupValues[1].isNotEmpty()) {
                coordMatch.groupValues[1]
            } else {
                "${coordMatch.groupValues[2]}:${coordMatch.groupValues[3]}"
            }
            result.getOrPut(versionRef) { mutableSetOf() }.add(coordinate)
        }
        return result
    }

    /**
     * Resolves which TOML version keys should be updated by cross-referencing
     * the version.ref -> coordinates map with available maven updates.
     */
    private fun resolveVersionUpdates(
        versionRefMap: Map<String, Set<String>>,
        availableUpdates: Map<String, String>
    ): Map<String, String> {
        val excluded = excludeKeys.get().toSet()
        val updates = mutableMapOf<String, String>()
        for ((versionKey, coordinates) in versionRefMap) {
            if (versionKey in excluded) continue
            for (coord in coordinates) {
                val newVersion = availableUpdates[coord]
                if (newVersion != null) {
                    updates[versionKey] = newVersion
                    break
                }
            }
        }
        return updates
    }

    /**
     * Applies version updates to the [versions] section of the TOML file lines.
     * Returns the updated lines.
     */
    private fun applyVersionUpdates(
        lines: List<String>,
        updates: Map<String, String>
    ): List<String> {
        return lines.map { line ->
            val match = """^(\s*)([\w\-]+)(\s*=\s*)"([^"]+)"\s*$""".toRegex().find(line)
            if (match != null) {
                val (indent, key, equals, _) = match.destructured
                val newVersion = updates[key]
                if (newVersion != null) {
                    """${indent}${key}${equals}"${newVersion}""""
                } else {
                    line
                }
            } else {
                line
            }
        }
    }
}
