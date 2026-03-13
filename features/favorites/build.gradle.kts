plugins {
    id("popular.movies.kmp.feature")
    alias(libs.plugins.kotlin.serialization)
}

// Note: Test dependencies moved to androidHostTestImplementation below for consistency with other modules

dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.shared)
    commonMainImplementation(projects.core.domain)
    commonMainImplementation(projects.core.models)

    // UI
    commonMainImplementation(projects.core.ui)
    commonMainImplementation(projects.core.resources)
    commonMainImplementation(libs.coil.kt.compose)

    // Serialization
    commonMainImplementation(libs.kotlinSerialization)

    // Android-specific UI
    androidMainImplementation(platform(libs.androidx.compose.bom))
    androidMainImplementation(libs.androidx.compose.ui.tooling.preview)
    androidMainImplementation(libs.androidx.compose.material.icons)

    // Testing (consistent with other feature modules)
    androidHostTestImplementation(libs.robolectric)
    androidHostTestImplementation(libs.androidx.compose.ui.test)
    androidHostTestImplementation(libs.androidx.compose.ui.testManifest)
    androidHostTestImplementation(libs.mockK)
    androidHostTestImplementation(libs.kotlinx.coroutines.test)
}
