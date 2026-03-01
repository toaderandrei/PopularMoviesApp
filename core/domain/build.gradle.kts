plugins {
    alias(libs.plugins.popular.movies.android.library)
    alias(libs.plugins.popular.movies.hilt)
}

android {
    namespace  = "com.ant.domain"
}

dependencies {
    implementation(project(":core:models"))
    implementation(project(":core:common"))
}
