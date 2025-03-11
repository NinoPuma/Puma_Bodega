import SwiftUI

struct VistaPerfil: View {
    @ObservedObject var gestorDatos: GestorDatos // 🔹 Solo necesita `gestorDatos`

    var body: some View {
        VStack(spacing: 20) {
            if let perfil = gestorDatos.perfilActual { // 🔹 Usar `perfilActual`
                VStack {
                    // Imagen de perfil predeterminada
                    Image(systemName: "person.circle.fill")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 100, height: 100)
                        .foregroundColor(.gray)
                        .padding(.top, 20)
                    
                    Text("Perfil de Usuario")
                        .font(.title)
                        .bold()
                        .padding(.bottom, 10)
                }
                
                VStack(spacing: 12) {
                    perfilItem(titulo: "Nombre", valor: perfil.nombre)
                    perfilItem(titulo: "Email", valor: perfil.email)
                    perfilItem(titulo: "Dirección", valor: perfil.direccion)
                    perfilItem(titulo: "Tarjeta", valor: perfil.tarjeta)
                }
                .padding()
                .background(RoundedRectangle(cornerRadius: 12)
                                .fill(Color(.systemBackground))
                                .shadow(radius: 5))
                .padding()
            } else {
                VStack {
                    ProgressView()
                    Text("Cargando perfil...")
                        .font(.headline)
                        .foregroundColor(.gray)
                }
            }
        }
        .padding()
        .navigationTitle("Perfil")
        .onAppear {
            if gestorDatos.perfilActual == nil {
                gestorDatos.cargarPerfil(nombre: gestorDatos.perfilActual?.nombre ?? "") // 🔹 Cargar el perfil autenticado
            }
        }
    }
    
    private func perfilItem(titulo: String, valor: String) -> some View {
        HStack {
            Text(titulo + ":")
                .bold()
                .foregroundColor(.primary)
            Spacer()
            Text(valor)
                .foregroundColor(.secondary)
        }
        .padding()
        .background(RoundedRectangle(cornerRadius: 10)
                        .fill(Color(.systemGray6)))
    }
}
