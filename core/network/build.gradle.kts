plugins {
    id("popular.movies.kmp.library")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

dependencies {
    // Core dependencies
    commonMainApi(projects.core.shared)
    commonMainImplementation(projects.core.models)

    // Ktor Client (exposed as API for consuming modules)
    commonMainApi(libs.ktor.client.core)
    commonMainApi(libs.ktor.client.content.negotiation)
    commonMainApi(libs.ktor.serialization.kotlinx.json)
    commonMainApi(libs.ktor.client.logging)

    // Utilities
    commonMainApi(libs.kotlinx.datetime)  // For iOS framework export
    commonMainImplementation(libs.kotlinSerialization)
    commonMainImplementation(libs.koin.core)

    // Platform-specific HTTP engines (Android)
    androidMainImplementation(libs.ktor.client.okhttp)
    androidMainImplementation(libs.okhttp)

    // Testing
    androidHostTestImplementation(libs.mockK)
    androidHostTestImplementation(libs.turbine)
}
