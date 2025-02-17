import SwiftUI

struct ContentView: View {
    // Instanciación de usuario y contraseña
    @State var usr: String = ""
    @State var pwd: String = ""
    @State var isOn: Bool = false

    var body: some View {
        
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
                    .labelsHidden() // Oculta el label del Toggle para que se vea en la izquierda
                Text("Aceptar términos y condiciones")
            }
            
            

            // Botón de Iniciar Sesión
            Button(action: {
                // Acción para iniciar sesión
            }) {
                Text("Iniciar Sesión")
                    .font(.headline)
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.blue)
                    .cornerRadius(8)
            }
            //padding para que el boton sea mas corto en el medio
            .padding(.horizontal, 20)
            //para ir mas arriba
            .padding(.bottom, 100)
        }
        //padding para los lados
        .padding()
    }
}

#Preview {
    ContentView()
}
