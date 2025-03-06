import SwiftUI

struct VistaPerfil: View {
    @ObservedObject var gestorDatos: GestorDatos // 🔹 Se pasa desde la vista anterior
    let usuario: String

    var body: some View {
        VStack(spacing: 20) {
            if let perfil = gestorDatos.perfilActual { // 🔹 Usar `perfilActual` en lugar de `perfil`
                Text("Perfil de Usuario")
                    .font(.largeTitle)
                    .bold()
                    .padding()
                
                VStack(alignment: .leading, spacing: 10) {
                    HStack {
                        Text("Nombre:")
                            .bold()
                        Text(perfil.nombre)
                    }
                    
                    HStack {
                        Text("Email:")
                            .bold()
                        Text(perfil.email)
                    }
                    
                    HStack {
                        Text("Dirección:")
                            .bold()
                        Text(perfil.direccion)
                    }
                    
                    HStack {
                        Text("Tarjeta:")
                            .bold()
                        Text(perfil.tarjeta)
                            .foregroundColor(.gray)
                    }
                }
                .padding()
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(Color(.systemGray6))
                .clipShape(RoundedRectangle(cornerRadius: 12))
            } else {
                Text("Cargando perfil...")
                    .font(.headline)
                    .foregroundColor(.gray)
            }
        }
        .padding()
        .navigationTitle("Perfil")
        .onAppear {
            gestorDatos.cargarPerfil(nombre: usuario) // 🔹 Cargar el perfil autenticado
        }
    }
}
