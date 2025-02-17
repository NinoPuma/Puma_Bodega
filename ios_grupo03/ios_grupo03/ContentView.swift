import SwiftUI

let usuarios = [
    "NinoPuma": "12345",
    "Hurogojo": "password",
    "Irene": "contraseña1"
]

struct ContentView: View {
    // Instanciación de usuario y contraseña
    @State private var usr: String = ""
    @State private var pwd: String = ""
    @State private var isOn: Bool = false
    @State private var mostrarError: Bool = false
    @State private var autenticacionExitoso: Bool = false
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 20) {
                Image("Logo_proyecto_integrador")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 700, height: 300)
                
                // Campo de correo electrónico o usuario
                TextField("Usuario o Correo electrónico", text: $usr)
                    .keyboardType(.emailAddress)
                    .disableAutocorrection(true)
                    .autocapitalization(.none)
                    .font(.headline)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(8)
                    .padding(.horizontal, 20)
                
                // Campo de contraseña
                SecureField("Contraseña", text: $pwd)
                    .disableAutocorrection(true)
                    .autocapitalization(.none)
                    .font(.headline)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(8)
                    .padding(.horizontal, 20)
                
                // Toggle para aceptar términos y condiciones
                HStack {
                    Toggle("", isOn: $isOn)
                        .labelsHidden()
                    Text("Aceptar términos y condiciones")
                        .font(.subheadline)
                }
                .padding(.horizontal, 20)
                
                
                
                // Botón de Iniciar Sesión
                Button(action: {
                    if autenticarUsuario(usuario: usr, contraseña: pwd) && isOn {
                        autenticacionExitoso = true
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
            .navigationDestination(isPresented: $autenticacionExitoso) {
                VistaPrincipal()
        
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
