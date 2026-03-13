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
    commonMainImplementation(projects.core.domain)
    commonMainImplementation(projects.core.models)
    commonMainImplementation(projects.core.network)
    commonMainImplementation(projects.core.database)
}
