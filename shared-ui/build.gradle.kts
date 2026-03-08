plugins {
    id("popular.movies.kmp.library")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

// Note: iOS targets are automatically configured by popular.movies.kmp.library plugin

dependencies {
    // Core Infrastructure (exposed as API)
    commonMainApi(projects.core.models)
    commonMainApi(projects.core.common)
    commonMainApi(projects.core.domain)
    commonMainApi(projects.core.data)
    commonMainApi(projects.core.network)
    commonMainApi(projects.core.database)
    commonMainApi(projects.core.datastore)
    commonMainApi(projects.core.analytics)

    // UI Core
    commonMainApi(projects.core.ui)

    // Features
    commonMainApi(projects.features.movies)
    commonMainApi(projects.features.tvshow)
    commonMainApi(projects.features.favorites)
    commonMainApi(projects.features.search)
    commonMainApi(projects.features.login)
    commonMainApi(projects.features.welcome)

    // Kotlin Serialization (for Navigation 3 routes)
    commonMainImplementation(libs.kotlinSerialization)

    // Compose Multiplatform
    commonMainImplementation(compose.runtime)
    commonMainImplementation(compose.foundation)
    commonMainImplementation(compose.material3)
    commonMainImplementation(libs.compose.ui)
    commonMainImplementation(libs.compose.ui.tooling.preview)
    commonMainImplementation(libs.compose.resources)

    // Navigation
    commonMainImplementation(libs.navigation.compose.multiplatform)

    // Dependency Injection
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.koin.compose)
    commonMainImplementation(libs.koin.compose.viewmodel)

    // Material3 Adaptive Navigation Suite (KMP)
    commonMainImplementation(libs.compose.material3.adaptive.navigation.suite)

    // Android-specific
    androidMainImplementation(platform(libs.androidx.compose.bom))
    androidMainImplementation(libs.androidx.lifecycle.runtime.compose)
    androidMainImplementation(libs.androidx.lifecycle.viewmodel.compose)
    androidMainImplementation(projects.core.resources)  // Backwards compat during migration

    // Testing
    commonTestImplementation(kotlin("test"))
    // Compose UI tests only work on Android/JVM (JUnit4) - not available for iOS
    // androidUnitTestImplementation(libs.compose.ui.test)
}
