plugins {
    id("popular.movies.kmp.library")
}

dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.domain)
    commonMainImplementation(projects.core.models)
    commonMainImplementation(projects.core.network)
    commonMainImplementation(projects.core.database)

    // Testing
    androidHostTestImplementation(libs.mockK)
    androidHostTestImplementation(libs.turbine)
    androidHostTestImplementation(libs.ktor.client.mock)
}
