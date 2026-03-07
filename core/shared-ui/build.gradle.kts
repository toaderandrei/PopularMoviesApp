import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.popular.movies.kmp.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose)
}

kotlin {
    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
        .configureEach {
            namespace = "com.ant.shared.ui"
        }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:models"))
            implementation(project(":core:shared"))

            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)

            // Coil 3 for Compose Multiplatform
            implementation(libs.coil.kt.compose)
            implementation(libs.coil.network.ktor)
        }

        androidMain.dependencies {
            // Android-specific dependencies
            implementation(project(":core:resources"))
            implementation(libs.androidx.compose.ui.tooling.preview)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.ant.shared.ui"
    generateResClass = always
}
