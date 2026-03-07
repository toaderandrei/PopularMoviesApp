import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget

plugins {
    alias(libs.plugins.popular.movies.kmp.library)
}

kotlin {
    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
        .configureEach {
            namespace = "com.ant.shared"
        }

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:models"))
            api(project(":core:common"))
            api(project(":core:domain"))
            api(project(":core:data"))
            api(project(":core:network"))
            api(project(":core:database"))
            api(project(":core:datastore"))
            api(project(":core:analytics"))
        }
    }
}
