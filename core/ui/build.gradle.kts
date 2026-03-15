plugins {
    id("popular.movies.kmp.library")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.models)
    commonMainImplementation(projects.core.shared)

    // Kotlin Serialization (for Navigation 3 routes)
    commonMainImplementation(libs.kotlinSerialization)

    // Navigation (for NavDestination in navigation components)
    commonMainApi(libs.navigation.compose.multiplatform)

    // Material3 Adaptive Navigation Suite (KMP)
    commonMainApi(libs.compose.material3.adaptive.navigation.suite)

    // Compose Multiplatform (exposed as API)
    commonMainApi(compose.runtime)
    commonMainApi(compose.foundation)
    commonMainApi(compose.material3)
    commonMainApi(compose.material)  // For Material basic components
    commonMainApi(libs.compose.ui)
    commonMainApi(libs.compose.ui.tooling.preview)
    commonMainApi(compose.materialIconsExtended)  // Material Icons Extended (pinned to 1.7.3 in Compose 1.10.1)

    // Image loading
    commonMainImplementation(libs.coil.kt.compose)
    commonMainImplementation(libs.coil.network.ktor)

    // Android-specific
    androidMainImplementation(platform(libs.androidx.compose.bom))
    androidMainImplementation(projects.core.resources)  // Backwards compat during migration
    androidRuntimeClasspath(libs.androidx.compose.ui.tooling)

    // Testing
    commonTestImplementation(kotlin("test"))
    // Compose UI tests only work on Android/JVM (JUnit4) - not available for iOS
    // androidUnitTestImplementation(libs.compose.ui.test)
}
