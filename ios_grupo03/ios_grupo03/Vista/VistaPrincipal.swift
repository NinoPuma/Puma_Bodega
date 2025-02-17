import SwiftUI

// Vista principal con la barra de pestañas inferior
struct VistaPrincipal: View {
    var body: some View {
        TabView {
            Inicio()
                .tabItem {
                    Label("Inicio", systemImage: "wineglass")
                }
                .tag(1)
            
            VistaPerfil()
                .tabItem {
                    Label("Perfil", systemImage: "person.circle")
                }
                .tag(2)
            
            Envio()
                .tabItem {
                    Label("Envio", systemImage: "cube.box.fill")
                }
                .tag(3)
        }
    }
}

#Preview {
    VistaPrincipal()
}
