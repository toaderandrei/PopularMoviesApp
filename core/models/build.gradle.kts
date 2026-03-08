import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget

plugins {
    id("popular.movies.kmp.library")
}

kotlin {
    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
        .configureEach {
            namespace = "com.ant.models"
        }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinSerialization)
            // Changed to api to allow iOS framework export
            api(libs.kotlinx.datetime)
        }
    }
}
