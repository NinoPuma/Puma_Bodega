import SwiftUI

// Vista de Inicio con pestañas anidadas
struct Inicio: View {
    var body: some View {
        TabView {
            Vino()
                .tabItem {
                    Label("Vino", systemImage: "wineglass.fill")
                }
                .tag(1)
            
            Whiskey()
                .tabItem {
                    Label("Whiskey", systemImage: "drop.fill")
                }
                .tag(2)
            
            Ron()
                .tabItem {
                    Label("Ron", systemImage: "flame.fill")
                }
                .tag(3)
            
            Vodka()
                .tabItem {
                    Label("Vodka", systemImage: "bolt.fill")
                }
                .tag(4)
        }
    }
}
