enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
rootProject.name = "Popular-Movies"

val repoRootDir: File = rootProject.projectDir

data class Project(
    val dir: String,
    val name: String = dir.split("/").last(),
    val path: String = ":" + dir.replace("/", ":")
)

val projects = listOf(
    // App
    Project("app"),

    // Features
    Project("features/movies"),
    Project("features/tvshow"),
    Project("features/favorites"),
    Project("features/search"),
    Project("features/login"),
    Project("features/welcome"),

    // Core modules
    Project("core/ui"),
    Project("core/resources"),
    Project("core/domain"),
    Project("core/shared"),
    Project("core/network"),
    Project("core/models"),
    Project("core/analytics"),
    Project("core/database"),
    Project("core/data"),
    Project("core/datastore"),

    // Shared KMP modules (export iOS frameworks)
    Project("shared"),
    Project("shared-ui")
)

projects.forEach { project ->
    include(project.path)
    project(project.path).projectDir = repoRootDir.resolve(project.dir)
}