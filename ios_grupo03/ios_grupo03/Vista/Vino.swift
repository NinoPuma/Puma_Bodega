import SwiftUI

// Vista para Vino
struct Vino: View {
    var body: some View {
        NavigationStack {
            ScrollView {
                Text("Productos de Vino")
                    .font(.largeTitle)
                    .padding()
                // Aquí puedes agregar más contenido específico de Vino
            }
            .navigationTitle("Vino")
        }
    }
}
