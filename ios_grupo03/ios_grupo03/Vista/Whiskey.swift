import SwiftUI

// Vista para Whiskey
struct Whiskey: View {
    var body: some View {
        NavigationStack {
            ScrollView {
                Text("Productos de Whiskey")
                    .font(.largeTitle)
                    .padding()
                // Aquí puedes agregar más contenido específico de Whiskey
            }
            .navigationTitle("Whiskey")
        }
    }
}
