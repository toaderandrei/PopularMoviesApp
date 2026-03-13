plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    alias(libs.plugins.gms) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.kotlin.jvm) apply false

    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.ksp) apply false

    // Dependency management
    alias(libs.plugins.ben.manes.versions)
    id("popular.movies.dependency.updates")

    // iOS development tasks
    id("popular.movies.ios.tasks")
}