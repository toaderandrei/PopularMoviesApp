plugins {
    alias(libs.plugins.popular.movies.android.library)
}

android {
    namespace  = "com.ant.models"
}

dependencies {
    implementation(libs.kotlinSerialization)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.kotlin.stdlib)

    testImplementation(libs.junit)
}
