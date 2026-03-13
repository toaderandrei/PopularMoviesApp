plugins {
    id("popular.movies.kmp.library")
}

dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.models)
    commonMainImplementation(projects.core.shared)

    // Test
    androidHostTestImplementation(libs.turbine)
    androidHostTestImplementation(libs.mockK)
}
