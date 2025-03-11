import SwiftUI

struct VistaPerfil: View {
    @ObservedObject var gestorDatos: GestorDatos // 🔹 Solo necesita `gestorDatos`

    var body: some View {
        VStack(spacing: 20) {
            if let perfil = gestorDatos.perfilActual { // 🔹 Usar `perfilActual`
                VStack(spacing: 15) {
                    // Imagen de perfil
                    Image(systemName: "person.crop.circle.fill")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 100, height: 100)
                        .foregroundColor(.blue)
                        .padding(.top)
                    
                    Text("Perfil de Usuario")
                        .font(.largeTitle)
                        .fontWeight(.bold)
                        .padding(.bottom, 5)
                    
                    VStack(alignment: .leading, spacing: 15) {
                        InfoRow(icon: "person.fill", label: "Nombre", value: perfil.nombre)
                        InfoRow(icon: "envelope.fill", label: "Email", value: perfil.email)
                        InfoRow(icon: "house.fill", label: "Dirección", value: perfil.direccion)
                        InfoRow(icon: "creditcard.fill", label: "Tarjeta", value: perfil.tarjeta, color: .gray)
                    }
                    .padding()
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(RoundedRectangle(cornerRadius: 15).fill(Color(.systemGray6)))
                    .shadow(radius: 5)
                    .padding(.horizontal)
                }
            } else {
                VStack {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: .gray))
                        .scaleEffect(1.5)
                    Text("Cargando perfil...")
                        .font(.headline)
                        .foregroundColor(.gray)
                        .padding(.top, 8)
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
}

// Componente reutilizable para mostrar cada fila de información
struct InfoRow: View {
    var icon: String
    var label: String
    var value: String
    var color: Color = .primary
    
    var body: some View {
        HStack {
            Image(systemName: icon)
                .foregroundColor(.blue)
            Text("\(label):")
                .bold()
            Text(value)
                .foregroundColor(color)
            Spacer()
        }
    }
}
