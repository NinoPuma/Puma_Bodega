import SwiftUI

struct VistaCarrito: View {
    @ObservedObject var gestorDatos: GestorDatos
    
    var body: some View {
        NavigationStack {
            VStack {
                // Título estilizado
                Text("Carrito de Compras")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .padding()
                
                if let carrito = gestorDatos.perfilActual?.carrito, !carrito.isEmpty {
                    List {
                        ForEach(carrito.indices, id: \.self) { index in
                            let item = carrito[index]
                            HStack {
                                Image(item.licores.first?.imagen ?? "placeholder")
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 60, height: 60)
                                    .clipShape(RoundedRectangle(cornerRadius: 10))
                                    .shadow(radius: 3)
                                
                                VStack(alignment: .leading, spacing: 5) {
                                    Text(item.licores.first?.nombre ?? "Sin nombre")
                                        .font(.headline)
                                    Text("Cantidad: \(item.cantidad)")
                                        .font(.subheadline)
                                        .foregroundColor(.gray)
                                }
                                Spacer()
                                Text(String(format: "%.2f €", item.licores.first?.precio ?? 0))
                                    .font(.headline)
                                    .foregroundColor(.green)
                            }
                        }
                        .onDelete(perform: gestorDatos.eliminarLicor)
                    }
                } else {
                    VStack {
                        Image(systemName: "cart.fill")
                            .resizable()
                            .scaledToFit()
                            .frame(width: 100, height: 100)
                            .foregroundColor(.gray)
                        Text("El carrito está vacío")
                            .foregroundColor(.gray)
                            .font(.headline)
                            .padding()
                    }
                }
                
                Spacer()
                
                Button(action: {
                    gestorDatos.realizarPedido()
                }) {
                    Text("Realizar Pedido")
                        .bold()
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(LinearGradient(gradient: Gradient(colors: [Color.green, Color.blue]), startPoint: .leading, endPoint: .trailing))
                        .foregroundColor(.white)
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                        .shadow(radius: 4)
                        .padding(.horizontal)
                }
            }
            .navigationTitle("Carrito")
        }
    }
}
