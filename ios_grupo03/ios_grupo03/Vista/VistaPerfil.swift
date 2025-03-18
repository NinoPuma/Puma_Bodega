import SwiftUI

struct VistaPerfil: View {
    @ObservedObject var gestorDatos: GestorDatos
    @State private var email: String = ""
    @State private var direccion: String = ""
    @State private var tarjeta: String = ""
    
    var body: some View {
        VStack(spacing: 20) {
            if let perfil = gestorDatos.perfilActual {
                VStack {
                    Image(systemName: "person.circle.fill")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 100, height: 100)
                        .foregroundColor(.gray)
                        .padding(.top, 20)
                    
                    Text("Datos de Usuario")
                        .font(.title)
                        .bold()
                        .padding(.bottom, 10)
                }
                
                VStack(spacing: 12) {
                    perfilItem(titulo: "Nombre", valor: perfil.nombre)
                    TextField("Email", text: $email)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        .padding()
                    
                    TextField("Dirección", text: $direccion)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        .padding()
                    
                    TextField("Tarjeta", text: $tarjeta)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        .padding()
                }
                .padding()
                .background(RoundedRectangle(cornerRadius: 12)
                                .fill(Color(.systemBackground))
                                .shadow(radius: 5))
                .padding()
                
                Button(action: {
                    gestorDatos.actualizarPerfil(email: email, direccion: direccion, tarjeta: tarjeta)
                }) {
                    Text("Guardar Cambios")
                        .bold()
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                        .padding(.horizontal)
                }
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
            if let perfil = gestorDatos.perfilActual {
                email = perfil.email
                direccion = perfil.direccion
                tarjeta = perfil.tarjeta
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
