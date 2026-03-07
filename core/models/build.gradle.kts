import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget

plugins {
    alias(libs.plugins.popular.movies.kmp.library)
}

kotlin {
    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
        .configureEach {
            namespace = "com.ant.models"
        }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinSerialization)
            implementation(libs.kotlinx.datetime)
        }
    }
}
