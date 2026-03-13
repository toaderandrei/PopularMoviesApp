plugins {
    id("popular.movies.kmp.library")
}

dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.shared)
    commonMainImplementation(projects.core.models)

    // DataStore (exposed as API for consumers to create DataStore instances)
    commonMainApi(libs.data.store.preferences)

    // Koin
    commonMainApi(libs.koin.core)
    androidMainApi(libs.koin.android)
    // Koin for Android (needed for androidContext() in DatastoreModule.android.kt)
    androidMainImplementation(libs.koin.android)

    // Testing
    androidHostTestImplementation(libs.mockK)
    androidHostTestImplementation(libs.turbine)
}
