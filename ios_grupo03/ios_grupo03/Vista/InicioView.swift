import SwiftUI

// Vista de Inicio con pestañas anidadas
struct Inicio: View {
    var body: some View {
        TabView {
            ListaLicores(tipo: "Vino")
                .tabItem {
                    Label("Vino", systemImage: "wineglass.fill")
                }
                .tag(1)
            
            ListaLicores(tipo: "Whiskey")
                .tabItem {
                    Label("Whiskey", systemImage: "drop.fill")
                }
                .tag(2)
            
            ListaLicores(tipo: "Ron")
                .tabItem {
                    Label("Ron", systemImage: "flame.fill")
                }
                .tag(3)
            
            ListaLicores(tipo: "Vodka")
                .tabItem {
                    Label("Vodka", systemImage: "bolt.fill")
                }
                .tag(4)
        }
    }
}
#Preview {
    Inicio()
}
