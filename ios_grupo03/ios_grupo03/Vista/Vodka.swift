import SwiftUI

// Vista para Vodka
struct Vodka: View {
    var body: some View {
        NavigationStack {
            ScrollView {
                Text("Productos de Vodka")
                    .font(.largeTitle)
                    .padding()
                // Aquí puedes agregar más contenido específico de Vodka
            }
            .navigationTitle("Vodka")
        }
    }
}
