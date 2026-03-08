import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.ant.popular.movies.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.composeMultiplatform.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.ben.manes.versions.plugin)

    // Test dependencies
    testImplementation(libs.junit)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "popular.movies.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "popular.movies.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "popular.movies.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "popular.movies.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidLibrary") {
            id = "popular.movies.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFirebase") {
            id = "popular.movies.android.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidLint") {
            id = "popular.movies.android.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("androidConfig") {
            id = "popular.movies.android.config"
            implementationClass = "AndroidBuildConfigPlugin"
        }
        register("androidRoom") {
            id = "popular.movies.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("kmpLibrary") {
            id = "popular.movies.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kmpFeature") {
            id = "popular.movies.kmp.feature"
            implementationClass = "KmpFeatureConventionPlugin"
        }
        register("kmpRoom") {
            id = "popular.movies.kmp.room"
            implementationClass = "KmpRoomConventionPlugin"
        }
        register("iosTasks") {
            id = "popular.movies.ios.tasks"
            implementationClass = "IosTasksPlugin"
        }
        register("dependencyUpdates") {
            id = "popular.movies.dependency.updates"
            implementationClass = "DependencyUpdatesConventionPlugin"
        }
    }
}
