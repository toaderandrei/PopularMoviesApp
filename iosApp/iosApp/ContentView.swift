import SwiftUI
import Shared

struct ContentView: View {
    var body: some View {
        NavigationView {
            VStack {
                Text("Popular Movies")
                    .font(.largeTitle)
                    .padding()
                Text("Platform: \(PlatformKt.platformName())")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }
            .navigationTitle("Popular Movies")
        }
    }
}

#Preview {
    ContentView()
}
