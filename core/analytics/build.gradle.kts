import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget

plugins {
    alias(libs.plugins.popular.movies.kmp.library)
}

kotlin {
    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
        .configureEach {
            namespace = "com.ant.analytics"
        }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:models"))
            implementation(project(":core:common"))
        }
    }
}
