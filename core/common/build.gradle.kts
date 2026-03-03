import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget

plugins {
    alias(libs.plugins.popular.movies.kmp.library)
}

kotlin {
    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
        .configureEach {
            namespace = "com.ant.common"
        }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:models"))
            api(libs.koin.core)
            implementation(libs.kermit)
        }

        androidMain.dependencies {
            implementation(libs.timber)
        }
    }
}
