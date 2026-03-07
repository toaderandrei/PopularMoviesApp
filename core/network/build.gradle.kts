import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget

plugins {
    alias(libs.plugins.popular.movies.kmp.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
        .configureEach {
            namespace = "com.ant.network"
        }

    sourceSets {
        val androidHostTest by getting {
            dependencies {
                implementation(libs.mockK)
                implementation(libs.turbine)
            }
        }
        commonMain.dependencies {
            api(project(":core:shared"))
            implementation(project(":core:models"))

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinSerialization)
            implementation(libs.koin.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}
