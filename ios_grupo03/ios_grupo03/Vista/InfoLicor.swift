import SwiftUI

struct InfoLicor: View {
    @ObservedObject var gestorDatos: GestorDatos
    var licor: Licor
    @State private var cantidad = 1

    var body: some View {
        ScrollView {
            VStack(spacing: 25) {
                // Imagen destacada
                Image(licor.imagen)
                    .resizable()
                    .scaledToFit()
                    .frame(height: 250)
                    .clipShape(RoundedRectangle(cornerRadius: 20))
                    .shadow(radius: 10)
                    .padding(.horizontal)
                    .padding(.top, 20)
                
                // Nombre del licor
                Text(licor.nombre)
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
                    .foregroundColor(.primary)
                    .shadow(radius: 2)
                
                // Descripción estilizada
                Text(licor.descripcion)
                    .font(.body)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 30)
                
                // Precio destacado
                Text(String(format: "$%.2f", licor.precio))
                    .font(.title)
                    .bold()
                    .foregroundColor(Color("vinoTinto"))
                    .padding(.top, 5)
                    .shadow(radius: 2)
                
                // Selector de cantidad con diseño mejorado
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
                .background(RoundedRectangle(cornerRadius: 15).fill(Color(.systemGray6)))
                .shadow(radius: 3)
                .padding(.horizontal)
                
                // Total calculado con un diseño llamativo
                Text("Total: \(String(format: "$%.2f", licor.precio * Float(cantidad)))")
                    .font(.title2)
                    .bold()
                    .foregroundColor(.green)
                    .padding(.top, 5)
                    .shadow(radius: 2)
                
                // Botón de agregar al carrito con efecto de profundidad
                Button(action: {
                    for _ in 1...cantidad {
                        gestorDatos.agregarAlCarrito(licor: licor)
                    }
                    cantidad = 1
                }) {
                    Text("Agregar al Carrito")
                        .font(.headline)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(LinearGradient(gradient: Gradient(colors: [Color.blue, Color.purple]), startPoint: .leading, endPoint: .trailing))
                        .foregroundColor(.white)
                        .clipShape(RoundedRectangle(cornerRadius: 15))
                        .shadow(radius: 6)
                        .overlay(
                            RoundedRectangle(cornerRadius: 15)
                                .stroke(Color.white.opacity(0.5), lineWidth: 1)
                        )
                }
                .padding(.horizontal)
                .padding(.bottom, 20)
            }
            .padding()
        }
        .navigationTitle("Detalles del Licor")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar(.hidden, for: .tabBar)
    }
}
