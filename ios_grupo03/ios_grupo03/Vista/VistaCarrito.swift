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
                    Text("Desliza a la izquierda para eliminar del carrito")
                        .font(.subheadline)
                        .foregroundColor(.gray)
                        .padding(.horizontal)
                        .padding(.bottom, 5)

                    List {
                        ForEach(carrito, id: \.licores.first?.nombre) { item in
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
                                Text(String(format: "%.2f €", (item.licores.first?.precio ?? 0) * Float(item.cantidad)))
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
                
                VStack {
                    Text("Total a pagar")
                        .font(.headline)
                        .foregroundColor(.white)
                    Text("\(String(format: "%.2f €", gestorDatos.calcularTotalCarrito()))")
                        .font(.title)
                        .bold()
                        .foregroundColor(.white)
                }
                .padding()
                .frame(maxWidth: .infinity)
                .background(Color.black)
                .cornerRadius(15)
                .shadow(radius: 5)
                .padding(.horizontal)

                Button(action: {
                    gestorDatos.realizarPedido()
                }) {
                    Text("Realizar Pedido")
                        .bold()
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                        .padding(.horizontal)
                }
            }
            .navigationTitle("Carrito")
        }
    }
}
