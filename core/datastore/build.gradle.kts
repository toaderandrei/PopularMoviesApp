import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget

plugins {
    alias(libs.plugins.popular.movies.kmp.library)
}

kotlin {
    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
        .configureEach {
            namespace = "com.ant.datastore"
        }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:models"))
            implementation(libs.data.store.preferences)
        }

        val androidHostTest by getting {
            dependencies {
                implementation(libs.mockK)
                implementation(libs.turbine)
            }
        }
    }
}
