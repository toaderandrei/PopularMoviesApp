import SwiftUI
import Shared

@main
struct PopularMoviesApp: App {

    init() {
        // Initialize Koin dependency injection before any UI
        // Note: Kotlin/Native adds "do" prefix to functions starting with "init"
        KoinInit_iosKt.doInitKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
