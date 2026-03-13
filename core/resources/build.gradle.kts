import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget

plugins {
    id("popular.movies.kmp.library")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose)
}

kotlin {
    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
        .configureEach {
            namespace = "com.ant.resources"

            // Enable Android resources in KMP project (required for Compose Resources packaging)
            @Suppress("OPT_IN_USAGE")
            experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        }

    sourceSets {
        commonMain.dependencies {
            // Compose Runtime (required by Compose compiler)
            implementation(compose.runtime)
            // Compose Multiplatform Resources
            // Note: UI dependencies (material3, icons) should be in core:ui or feature modules
            api(compose.components.resources)
        }
    }
}

// Configure Compose Multiplatform Resources
compose.resources {
    packageOfResClass = "com.ant.resources"
    publicResClass = true
}
