plugins {
    alias(libs.plugins.popular.movies.android.application)
    alias(libs.plugins.popular.movies.android.application.compose)
    alias(libs.plugins.popular.movies.android.firebase)
    alias(libs.plugins.gms)
    alias(libs.plugins.popular.movies.android.lint)
    alias(libs.plugins.popular.movies.android.config)
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
    // module dependency
    implementation(project(":core:common"))
    implementation(project(":core:models"))
    implementation(project(":core:ui"))
    implementation(project(":core:resources"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:database"))
    implementation(project(":core:analytics"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))

    // features
    implementation(project(":features:movies"))
    implementation(project(":features:tvshow"))
    implementation(project(":features:favorites"))
    implementation(project(":features:search"))
    implementation(project(":features:login"))
    implementation(project(":features:welcome"))

    // UI libs
    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.material)

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.ui)

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
