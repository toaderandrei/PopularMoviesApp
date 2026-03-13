plugins {
    id("popular.movies.kmp.library")
}

dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.models)

    // Dependency Injection (exposed as API)
    commonMainApi(libs.koin.core)

    // Logging
    commonMainImplementation(libs.kermit)
    androidMainImplementation(libs.timber)
}
