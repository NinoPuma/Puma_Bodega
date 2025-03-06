import SwiftUI

// Vista de Inicio con pestañas anidadas
struct Inicio: View {
    @ObservedObject var gestorDatos: GestorDatos // 🔹 Recibe gestorDatos

    var body: some View {
        TabView {
            ListaLicores(gestorDatos: gestorDatos ,tipo: "Vino") // 🔹 Pasamos gestorDatos
                .tabItem {
                    Label("Vino", systemImage: "wineglass.fill")
                }
                .tag(1)
            
            ListaLicores(gestorDatos: gestorDatos, tipo: "Whiskey")
                .tabItem {
                    Label("Whiskey", systemImage: "drop.fill")
                }
                .tag(2)
            
            ListaLicores(gestorDatos: gestorDatos, tipo: "Ron")
                .tabItem {
                    Label("Ron", systemImage: "flame.fill")
                }
                .tag(3)
            
            ListaLicores(gestorDatos: gestorDatos, tipo: "Vodka")
                .tabItem {
                    Label("Vodka", systemImage: "bolt.fill")
                }
                .tag(4)
        }
    }
}

#Preview {
    Inicio(gestorDatos: GestorDatos()) // 🔹 Se pasa una instancia para la vista previa
}
