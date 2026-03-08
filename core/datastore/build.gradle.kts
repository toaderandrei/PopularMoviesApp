
plugins {
    id("popular.movies.kmp.library")
}

kotlin {
    sourceSets {
        val androidHostTest by getting {
            dependencies {
                implementation(libs.mockK)
                implementation(libs.turbine)
            }
        }
    }
}

dependencies {
    // Core dependencies
    commonMainImplementation(projects.core.common)
    commonMainImplementation(projects.core.models)

    // DataStore (exposed as API for consumers to create DataStore instances)
    commonMainApi(libs.data.store.preferences)

    androidMainApi(libs.koin.android)
    commonMainApi(libs.koin.core)
    // Koin for Android (needed for androidContext() in DatastoreModule.android.kt)
    androidMainImplementation(libs.koin.android)
}
