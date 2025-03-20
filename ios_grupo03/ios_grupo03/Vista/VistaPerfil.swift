import SwiftUI

struct VistaPerfil: View {
    @ObservedObject var gestorDatos: GestorDatos
    @State private var email: String = ""
    @State private var direccion: String = ""
    @State private var tarjeta: String = ""
    @State private var suscritoNewsletter: Bool = false // ✅ Estado del toggle

    var body: some View {
        VStack(spacing: 15) { // 🔹 Espaciado optimizado
            if let perfil = gestorDatos.perfilActual {
                VStack {
                    Image(systemName: "person.circle.fill")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 90, height: 90)
                        .foregroundColor(.gray)
                        .padding(.top, 10)

                    Text("Datos de Usuario")
                        .font(.title2)
                        .bold()
                        .padding(.bottom, 8)
                }

                VStack(spacing: 10) { // 🔹 Mejor organización de los datos del usuario
                    perfilItem(titulo: "Nombre", valor: perfil.nombre)

                    campoEditable(titulo: "Email", texto: $email)
                    campoEditable(titulo: "Dirección", texto: $direccion)
                    campoEditable(titulo: "Tarjeta", texto: $tarjeta)
                }
                .padding()
                .background(RoundedRectangle(cornerRadius: 12)
                                .fill(Color(.systemBackground))
                                .shadow(radius: 3))
                .padding(.horizontal, 15)

                // ✅ Toggle sin espacios extras
                Toggle(isOn: $suscritoNewsletter) {
                    Text("Suscribirse al newsletter")
                        .font(.subheadline)
                }
                .padding()
                .background(RoundedRectangle(cornerRadius: 10).fill(Color(.systemGray6)))
                .padding(.horizontal, 15)

                // ✅ Botón bien alineado y visible sin espacios innecesarios
                Button(action: {
                    gestorDatos.actualizarPerfil(email: email, direccion: direccion, tarjeta: tarjeta, suscritoNewsletter: suscritoNewsletter)
                }) {
                    Text("Guardar Cambios")
                        .bold()
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(email.isEmpty || direccion.isEmpty || tarjeta.isEmpty ? Color.gray : Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                        .padding(.horizontal, 15)
                }
                .disabled(email.isEmpty || direccion.isEmpty || tarjeta.isEmpty) // 🔹 Deshabilita si los campos están vacíos
                .padding(.top, 10)
                .padding(.bottom, 10)
            } else {
                VStack {
                    ProgressView()
                    Text("Cargando perfil...")
                        .font(.headline)
                        .foregroundColor(.gray)
                }
                .padding(.top, 30)
            }
        }
        .padding()
        .navigationTitle("Perfil")
        .onAppear {
            if let perfil = gestorDatos.perfilActual {
                email = perfil.email
                direccion = perfil.direccion
                tarjeta = perfil.tarjeta
                suscritoNewsletter = perfil.suscritoNewsletter ?? false // ✅ Evita error si el JSON no tiene este valor
            }
        }
        .alert(isPresented: $gestorDatos.mostrarAlerta) {
            Alert(title: Text("Éxito"), message: Text(gestorDatos.mensajeAlerta), dismissButton: .default(Text("OK")))
        }
    }

    // ✅ Método para mostrar los campos NO editables
    private func perfilItem(titulo: String, valor: String) -> some View {
        VStack(alignment: .leading, spacing: 5) {
            Text("\(titulo):")
                .bold()
                .foregroundColor(.primary)

            Text(valor)
                .foregroundColor(.black) // 🔹 Texto más visible
                .padding(10)
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(Color(.systemGray5)) // 🔹 Fondo más claro
                .cornerRadius(8)
                .overlay(RoundedRectangle(cornerRadius: 8).stroke(Color.gray.opacity(0.5), lineWidth: 1)) // 🔹 Borde suave
        }
        .padding(.horizontal, 15)
    }

    // ✅ Método para mostrar los campos EDITABLES alineados con el label ENCIMA
    private func campoEditable(titulo: String, texto: Binding<String>) -> some View {
        VStack(alignment: .leading, spacing: 5) {
            Text("\(titulo):")
                .bold()
                .foregroundColor(.primary)

            TextField(titulo, text: texto)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .autocapitalization(.none)
                .disableAutocorrection(true)
        }
        .padding(.horizontal, 15)
    }
}
