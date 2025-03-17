import SwiftUI

struct InfoLicor: View {
    @ObservedObject var gestorDatos: GestorDatos
    var licor: Licor
    @State private var cantidad = 1
    
    var body: some View {
        VStack(spacing: 20) {
            Text(licor.nombre)
                .font(.title)
                .bold()
                .multilineTextAlignment(.center)
            
            Image(licor.imagen)
                .resizable()
                .frame(width: 150, height: 150)
                .clipShape(RoundedRectangle(cornerRadius: 12))
                .shadow(radius: 5)
            
            Text(licor.descripcion)
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding()
            
            Text(String(format: "$%.2f", licor.precio))
                .font(.title2)
                .bold()
                .foregroundColor(Color("vinoTinto"))
                .padding(.bottom, 10)
            
            HStack {
                Text("Cantidad:")
                    .font(.headline)
                
                Stepper(value: $cantidad, in: 1...10) {
                    Text("\(cantidad)")
                        .font(.headline)
                        .bold()
                        .frame(width: 40)
                }
            }
            .padding()
            .background(RoundedRectangle(cornerRadius: 12).fill(Color(.systemGray6)))
            
            Text("Total: \(String(format: "$%.2f", licor.precio * Float(cantidad)))")
                .font(.title3)
                .bold()
                .padding(.top, 5)
                .foregroundColor(.green)
            
            Button(action: {
                for _ in 1...cantidad {
                    gestorDatos.agregarAlCarrito(licor: licor)
                }
                cantidad = 1
            }) {
                Text("Agregar al Carrito")
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .clipShape(RoundedRectangle(cornerRadius: 10))
            }
            .padding()
            .alert(gestorDatos.mensajeAlerta, isPresented: $gestorDatos.mostrarAlerta) {
                Button("OK", role: .cancel) { }
            }
            
            Spacer()
        }
        .padding()
        .navigationTitle("Detalles del Licor")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar(.hidden, for: .tabBar)
    }
}
