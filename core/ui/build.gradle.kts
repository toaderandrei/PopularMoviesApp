plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.android.library.compose)
}

android {
    namespace = "com.ant.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:resources"))
    implementation(project(":core:models"))
    implementation(project(":core:network"))

    // Coil compose
    implementation(libs.coil.kt.compose)
    implementation(libs.coil)

    implementation(libs.coreKtx)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.ui)

    // Tooling and preview
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
