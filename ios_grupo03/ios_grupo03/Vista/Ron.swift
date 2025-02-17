import SwiftUI

// Vista para Ron
struct Ron: View {
    var body: some View {
        NavigationStack {
            ScrollView {
                Text("Productos de Ron")
                    .font(.largeTitle)
                    .padding()
                // Aquí puedes agregar más contenido específico de Ron
            }
            .navigationTitle("Ron")
        }
    }
}
