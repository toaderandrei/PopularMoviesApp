plugins {
    id("popular.movies.kmp.feature")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.common)
    commonMainImplementation(projects.core.domain)
    commonMainImplementation(projects.core.models)
    commonMainImplementation(projects.core.datastore)
    commonMainImplementation(projects.core.analytics)

    // UI
    commonMainImplementation(projects.core.ui)
    commonMainImplementation(projects.core.resources)
    commonMainImplementation(libs.coil.kt.compose)

    // Serialization
    commonMainImplementation(libs.kotlinSerialization)

    // Android-specific UI
    androidMainImplementation(platform(libs.androidx.compose.bom))
    androidMainImplementation(libs.androidx.compose.ui.tooling.preview)
    androidMainImplementation(libs.accompanist.adaptive)

    // Testing
    commonTestImplementation(libs.turbine)
    androidHostTestImplementation(libs.robolectric)
    androidHostTestImplementation(libs.androidx.compose.ui.test)
    androidHostTestImplementation(libs.androidx.compose.ui.testManifest)
    androidHostTestImplementation(libs.mockK)
    androidHostTestImplementation(libs.kotlinx.coroutines.test)
}
