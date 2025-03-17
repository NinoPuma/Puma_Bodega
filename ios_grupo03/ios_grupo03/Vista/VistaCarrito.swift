import SwiftUI

struct VistaCarrito: View {
    @ObservedObject var gestorDatos: GestorDatos
    
    var body: some View {
        NavigationStack {
            VStack {
                Text("Carrito de Compras")
                    .font(.largeTitle)
                    .bold()
                    .padding()
                
                if let carrito = gestorDatos.perfilActual?.carrito, !carrito.isEmpty {
                    List {
                        ForEach(carrito.indices, id: \ .self) { index in
                            let item = carrito[index]
                            HStack {
                                Image(item.licores.first?.imagen ?? "placeholder")
                                    .resizable()
                                    .frame(width: 50, height: 50)
                                    .clipShape(RoundedRectangle(cornerRadius: 8))
                                
                                VStack(alignment: .leading) {
                                    Text(item.licores.first?.nombre ?? "Sin nombre")
                                        .font(.headline)
                                    Text("Cantidad: \(item.cantidad)")
                                        .font(.subheadline)
                                        .foregroundColor(.gray)
                                }
                                Spacer()
                                Text(String(format: "%.2f €", item.licores.first?.precio ?? 0))
                                    .font(.headline)
                            }
                        }
                        .onDelete(perform: gestorDatos.eliminarLicor)
                    }
                } else {
                    Text("El carrito está vacío")
                        .foregroundColor(.gray)
                        .padding()
                }
                
                Spacer()
                
                Button(action: {
                    gestorDatos.realizarPedido()
                }) {
                    Text("Realizar Pedido")
                        .bold()
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.green)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                        .padding(.horizontal)
                }
            }
            .navigationTitle("Carrito")
        }
    }
}

