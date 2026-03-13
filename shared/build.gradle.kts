import com.ant.popular.movies.ktx.configureIosFramework

plugins {
    id("popular.movies.kmp.library")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose)
}

kotlin {
    // iOS framework configuration - exports all modules for iOS
    configureIosFramework(
        baseName = "Shared",
        exports = listOf(
            projects.sharedUi,
            projects.core.ui,
            projects.core.resources  // Required for Compose resources to be bundled in iOS
        )
    )
}

dependencies {
    // Core Infrastructure (exposed as API for iOS framework)
    commonMainApi(projects.core.models)
    commonMainApi(projects.core.shared)
    commonMainApi(projects.core.domain)
    commonMainApi(projects.core.data)
    commonMainApi(projects.core.network)
    commonMainApi(projects.core.database)
    commonMainApi(projects.core.datastore)
    commonMainApi(projects.core.analytics)

    // UI modules (required for iOS framework export and resource bundling)
    commonMainApi(projects.core.ui)
    commonMainApi(projects.core.resources)
    commonMainApi(projects.sharedUi)
}
