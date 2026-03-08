plugins {
    id("popular.movies.kmp.library")
    id("popular.movies.kmp.room")
}

dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.models)
    commonMainImplementation(libs.koin.core)

    // Room Database
    commonMainApi(libs.room.runtime)
    commonMainApi(libs.sqlite.bundled)  // SQLite driver for KMP
    commonMainApi(libs.kotlinx.datetime)  // For iOS framework export

    // Serialization
    commonMainImplementation(libs.kotlinSerialization)

    // Android-specific Koin for androidApplication() function
    androidMainImplementation(libs.koin.android)
}
