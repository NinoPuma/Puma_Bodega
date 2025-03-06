import SwiftUI

let usuarios = [
    "Antonino Puma": "12345",
    "Hugo Rojo": "password",
    "Irene Profe": "contrasena1"
]

struct ContentView: View {
    @StateObject private var gestorDatos = GestorDatos() // 🔹 Se crea aquí para mantenerlo en toda la app

    @State private var usr: String = ""
    @State private var pwd: String = ""
    @State private var isOn: Bool = false
    @State private var mostrarError: Bool = false
    @State private var usuarioAutenticado: String? = nil // Guarda el usuario autenticado

    var body: some View {
        NavigationStack {
            VStack(spacing: 20) {
                Image("Logo_proyecto_integrador")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 700, height: 300)

                TextField("Usuario o Correo electrónico", text: $usr)
                    .keyboardType(.emailAddress)
                    .disableAutocorrection(true)
                    .autocapitalization(.none)
                    .font(.headline)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(8)
                    .padding(.horizontal, 20)

                SecureField("Contraseña", text: $pwd)
                    .disableAutocorrection(true)
                    .autocapitalization(.none)
                    .font(.headline)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(8)
                    .padding(.horizontal, 20)

                HStack {
                    Toggle("", isOn: $isOn)
                        .labelsHidden()
                    Text("Aceptar términos y condiciones")
                        .font(.subheadline)
                }
                .padding(.horizontal, 20)

                Button(action: {
                    if autenticarUsuario(usuario: usr, contraseña: pwd) && isOn {
                        usuarioAutenticado = usr // Guarda el usuario autenticado
                        isOn = false
                        pwd = ""
                        usr = ""
                    } else {
                        mostrarError = true
                    }
                }) {
                    Text("Iniciar Sesión")
                        .font(.headline)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .cornerRadius(8)
                }
                .padding(.horizontal, 20)
                .padding(.bottom, 100)

                if mostrarError {
                    Text("Usuario o contraseña incorrectos")
                        .foregroundColor(.red)
                        .padding()
                }
            }
            .padding()
            .navigationDestination(isPresented: Binding(
                get: { usuarioAutenticado != nil },
                set: { if !$0 { usuarioAutenticado = nil } }
            )) {
                if let usuario = usuarioAutenticado {
                    VistaPrincipal(gestorDatos: gestorDatos, usuario: usuario) // 🔹 Pasamos gestorDatos
                }
            }
        }
    }

    func autenticarUsuario(usuario: String, contraseña: String) -> Bool {
        if let contraseñaGuardada = usuarios[usuario] {
            return contraseñaGuardada == contraseña
        } else {
            return false
        }
    }
}

#Preview {
    ContentView()
}
