import SwiftUI

// Vista principal con la barra de pestañas inferior
struct VistaPrincipal: View {
    @ObservedObject var gestorDatos: GestorDatos // 🔹 Recibe `GestorDatos` desde `ContentView`
    let usuario: String // 🔹 Se mantiene el usuario aquí

    var body: some View {
        TabView {
            Inicio(gestorDatos: gestorDatos)
                .tabItem {
                    Label("Inicio", systemImage: "wineglass")
                }

            VistaCarrito(gestorDatos: gestorDatos)
                .tabItem {
                    Label("Carrito", systemImage: "cart")
                }

            VistaPedidos(gestorDatos: gestorDatos)
                .tabItem {
                    Label("Pedidos", systemImage: "cube.box.fill")
                }

            VistaPerfil(gestorDatos: gestorDatos) // 🔹 No se pasa usuario aquí
                .tabItem {
                    Label("Perfil", systemImage: "person.circle")
                }
        }
        .onAppear {
            gestorDatos.cargarPerfiles()
            gestorDatos.cargarLicoresDesdeJSON()
            gestorDatos.cargarPerfil(nombre: usuario) // 🔹 Cargar el perfil con el usuario autenticado
        }
    }
}

#Preview {
    VistaPrincipal(gestorDatos: GestorDatos(), usuario: "Antonino Puma") // 🔹 Se mantiene el usuario
}
