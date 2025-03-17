import SwiftUI

struct VistaPedidos: View {
    @ObservedObject var gestorDatos: GestorDatos
    
    var body: some View {
        NavigationStack {
            VStack {
                Text("Pedidos Realizados")
                    .font(.largeTitle)
                    .bold()
                    .padding()
                
                if let pedidos = gestorDatos.perfilActual?.pedidos, !pedidos.isEmpty {
                    List(pedidos) { pedido in
                        VStack(alignment: .leading) {
                            Text("Pedido #\(pedido.id)")
                                .font(.headline)
                                .bold()
                            Text("Fecha: \(pedido.fecha)")
                                .foregroundColor(.gray)
                            Text("Estado: \(pedido.estado)")
                                .foregroundColor(pedido.estado == "Pendiente" ? .orange : .green)
                            
                            ForEach(pedido.licores) { licor in
                                HStack {
                                    Image(licor.imagen)
                                        .resizable()
                                        .frame(width: 50, height: 50)
                                    Text(licor.nombre)
                                        .font(.subheadline)
                                    Spacer()
                                    Text(String(format: "%.2f €", licor.precio))
                                        .font(.subheadline)
                                        .bold()
                                }
                            }
                            
                            let totalPedido = pedido.licores.reduce(0) { total, licor in
                                total + licor.precio
                            }
                            
                            Text("Total: \(String(format: "%.2f €", totalPedido))")
                                .font(.title3)
                                .bold()
                                .padding(.top)
                        }
                        .padding()
                    }
                } else {
                    Text("No hay pedidos realizados")
                        .foregroundColor(.gray)
                        .padding()
                }
            }
            .navigationTitle("Pedidos")
        }
    }
}
