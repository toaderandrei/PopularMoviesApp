import com.ant.popular.movies.ktx.configureIosFramework

plugins {
    id("popular.movies.kmp.library")
}

kotlin {
    // iOS framework configuration - exports all modules for iOS
    configureIosFramework(
        baseName = "Shared",
        exports = listOf(projects.sharedUi)
    )
}

dependencies {
    // Core Infrastructure (exposed as API for iOS framework)
    commonMainApi(projects.core.models)
    commonMainApi(projects.core.common)
    commonMainApi(projects.core.domain)
    commonMainApi(projects.core.data)
    commonMainApi(projects.core.network)
    commonMainApi(projects.core.database)
    commonMainApi(projects.core.datastore)
    commonMainApi(projects.core.analytics)

    // UI module (for iOS framework export)
    commonMainApi(projects.sharedUi)
}
