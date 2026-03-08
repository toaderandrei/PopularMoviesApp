plugins {
    id("popular.movies.android.application")
    id("popular.movies.android.application.compose")
    id("popular.movies.android.firebase")
    alias(libs.plugins.gms)
    id("popular.movies.android.lint")
    id("popular.movies.android.config")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrains.compose)
}

android {
    defaultConfig {
        versionCode = 2
        versionName = "0.1.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level
    }

    buildFeatures {
        buildConfig = true
    }

    // Add this line
    namespace = "com.ant.app"
}

dependencies {
    // UI umbrella dependency (includes infrastructure transitively via :shared)
    implementation(projects.sharedUi)   // UI composition + infrastructure

    // Android-specific resources (for backwards compat)
    implementation(projects.core.resources)

    // UI libs
    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.material)

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.ui)
    implementation(compose.components.resources)

    // Navigation compose
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.compose.material3.adaptive.navigationSuite)


    // Tooling and preview
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Lifecycle runtime and runtime-compose.
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)

    // Ktor OkHttp engine (transitive via core:network)
    implementation(libs.ktor.client.okhttp)

    // firebase
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // TODO check if still needed after plugin
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.data.store)
    implementation(libs.data.store.preferences)

    // OkHttp for Coil
    implementation(libs.okhttp)

    // Room
    implementation(libs.room.runtime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
