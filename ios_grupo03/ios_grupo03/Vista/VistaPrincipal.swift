import SwiftUI

// Vista principal con la barra de pestañas inferior
struct VistaPrincipal: View {
    @ObservedObject var gestorDatos: GestorDatos // 🔹 Recibe `GestorDatos` desde `ContentView`
    let usuario: String

    var body: some View {
        TabView {
            Inicio(gestorDatos: gestorDatos)
                .tabItem {
                    Label("Inicio", systemImage: "wineglass")
                }

            VistaCarrito(/*gestorDatos: gestorDatos*/)
                .tabItem {
                    Label("Carrito", systemImage: "cart")
                }

            VistaPedidos(/*gestorDatos: gestorDatos*/)
                .tabItem {
                    Label("Pedidos", systemImage: "cube.box.fill")
                }

            VistaPerfil(gestorDatos: gestorDatos, usuario: usuario)
                .tabItem {
                    Label("Perfil", systemImage: "person.circle")
                }
        }
        .onAppear {
            gestorDatos.cargarPerfiles() // 🔹 Cargar datos al iniciar sesión
            gestorDatos.cargarLicoresDesdeJSON()
        }
    }
}


//#Preview {
//    VistaPrincipal(usuario: "Antonino Puma")
//}
