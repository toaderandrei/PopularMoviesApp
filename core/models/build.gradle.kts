plugins {
    id("popular.movies.kmp.library")
}

dependencies {
    commonMainImplementation(libs.kotlinSerialization)
    // Changed to api to allow iOS framework export
    commonMainApi(libs.kotlinx.datetime)
}
