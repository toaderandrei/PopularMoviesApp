plugins {
    id("popular.movies.kmp.library")
}

dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.models)
    commonMainImplementation(projects.core.common)

    // Test
    androidHostTestImplementation(libs.turbine)
    androidHostTestImplementation(libs.mockK)
}
