plugins {
    id("popular.movies.kmp.library")
}

dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.models)
    commonMainImplementation(projects.core.shared)

    // Koin
    commonMainApi(libs.koin.core)
}
