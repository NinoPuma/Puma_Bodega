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
            Carrito()
                .tabItem{
                    Label("Carrito", systemImage: "cart")
                }
            
                .tag(2)
            Envio()
                .tabItem {
                    Label("Pedidos", systemImage: "cube.box.fill")
                }
                .tag(3)
            VistaPerfil()
                .tabItem {
                    Label("Perfil", systemImage: "person.circle")
                }
                .tag(4)
        }
    }
}

#Preview {
    VistaPrincipal()
}
