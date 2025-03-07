import SwiftUI

struct VistaPedidos: View {
    @ObservedObject var gestorDatos: GestorDatos
    @State private var pedidoSeleccionado: Pedido?

    var body: some View {
        NavigationStack {
            VStack {
                Text("Pedidos Realizados")
                    .font(.largeTitle)
                    .bold()
                    .foregroundColor(Color("vinoTinto")) // Color temático
                    .padding()

                if let pedidos = gestorDatos.perfilActual?.pedidos, !pedidos.isEmpty {
                    List(pedidos) { pedido in
                        Button(action: {
                            pedidoSeleccionado = pedido
                        }) {
                            HStack {
                                VStack(alignment: .leading, spacing: 5) {
                                    Text("Pedido #\(pedido.id)")
                                        .font(.headline)
                                        .bold()
                                        .foregroundColor(.white)

                                    Text("\(pedido.fecha)")
                                        .font(.subheadline)
                                        .foregroundColor(Color(.lightGray))
                                }
                                Spacer()
                                Image(systemName: "chevron.right")
                                    .foregroundColor(.white)
                            }
                            .padding()
                            .background(RoundedRectangle(cornerRadius: 12)
                                .fill(Color("vinoTinto"))) // Fondo temático
                            .shadow(radius: 3)
                        }
                        .listRowBackground(Color.clear) // Hace la lista más limpia
                    }
                    .listStyle(PlainListStyle()) // Quita líneas innecesarias
                } else {
                    Text("No hay pedidos realizados")
                        .foregroundColor(.gray)
                        .padding()
                }
            }
            .navigationTitle("Pedidos")
            .onAppear {
                gestorDatos.cargarPerfil(nombre: gestorDatos.perfilActual?.nombre ?? "")
            }
            .sheet(item: $pedidoSeleccionado) { pedido in
                InfoPedido(pedido: pedido)
            }
        }
    }
}
