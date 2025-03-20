import SwiftUI

let usuarios = [
    "Antonino Puma": "12345",
    "Hugo Rojo": "password",
    "Irene Profe": "contrasena1"
]

struct ContentView: View {
    @StateObject private var gestorDatos = GestorDatos()

    @State private var usr: String = ""
    @State private var pwd: String = ""
    @State private var aceptarTerminos: Bool = false
    @State private var esMayorDeEdad: Bool = false
    @State private var mostrarAlerta: Bool = false
    @State private var mensajeAlerta: String = ""
    @State private var usuarioAutenticado: String? = nil

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

                VStack(spacing: 10) {
                    HStack {
                        Toggle("", isOn: $esMayorDeEdad)
                            .labelsHidden()
                        Text("Confirmo que soy mayor de 18 años")
                            .font(.subheadline)
                        Spacer()
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.horizontal, 20)

                    HStack {
                        Toggle("", isOn: $aceptarTerminos)
                            .labelsHidden()
                        Text("Aceptar términos y condiciones")
                            .font(.subheadline)
                        Spacer()
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.horizontal, 20)
                }

                Button(action: {
                    if !esMayorDeEdad {
                        mensajeAlerta = "No puedes acceder sin cumplir la mayoría de edad"
                        mostrarAlerta = true
                    } else if !aceptarTerminos {
                        mensajeAlerta = "Debes aceptar términos y condiciones para acceder"
                        mostrarAlerta = true
                    } else if autenticarUsuario(usuario: usr, contraseña: pwd) {
                        usuarioAutenticado = usr
                        aceptarTerminos = false
                        esMayorDeEdad = false
                        pwd = ""
                        usr = ""
                    } else {
                        mensajeAlerta = "Usuario o contraseña incorrectos"
                        mostrarAlerta = true
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
            }
            .padding()
            .alert(mensajeAlerta, isPresented: $mostrarAlerta) {
                Button("OK", role: .cancel) { }
            }
            .navigationDestination(isPresented: Binding(
                get: { usuarioAutenticado != nil },
                set: { if !$0 { usuarioAutenticado = nil } }
            )) {
                if let usuario = usuarioAutenticado {
                    VistaPrincipal(gestorDatos: gestorDatos, usuario: usuario)
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
