import SwiftUI

// Vista principal con la barra de pestañas inferior
struct VistaPrincipal: View {
    @ObservedObject var gestorDatos: GestorDatos
    let usuario: String
    @Environment(\.dismiss) private var dismiss

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

            VistaPerfil(gestorDatos: gestorDatos)
                .tabItem {
                    Label("Perfil", systemImage: "person.circle")
                }
        }
        .padding(.top, 10)
        .onAppear {
            gestorDatos.cargarPerfiles()
            gestorDatos.cargarLicoresDesdeJSON()
            gestorDatos.cargarPerfil(nombre: usuario)
        }
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: { dismiss() }) {
                    HStack {
                        Image(systemName: "arrow.backward")
                        Text("Cerrar sesión")
                    }
                    .foregroundColor(.blue)
                }
            }
        }
    }
}
