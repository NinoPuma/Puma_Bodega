import SwiftUI

struct VistaPedidos: View {
    @ObservedObject var gestorDatos: GestorDatos
    @State private var pedidoSeleccionado: Pedido?

    var body: some View {
        NavigationStack {
            ZStack {
                // Imagen de fondo
                Image("fondo_bodega")
                    .resizable()
                    .scaledToFill()
                    .edgesIgnoringSafeArea(.all)
                    .opacity(0.3) // Ajusta la opacidad para mejor legibilidad
                
                VStack(spacing: 20) {
                    // Título con paleta de vino tinto y madera
                    Text("Pedidos Realizados")
                        .font(.largeTitle)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .padding()
                        .background(LinearGradient(gradient: Gradient(colors: [Color("vinoTinto"), Color("madera")] ), startPoint: .leading, endPoint: .trailing))
                        .clipShape(RoundedRectangle(cornerRadius: 15))
                        .shadow(radius: 10)
                        .padding(.horizontal)
                    
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
                                        
                                        Text("Fecha: \(pedido.fecha)")
                                            .font(.subheadline)
                                            .foregroundColor(Color("roble"))
                                    }
                                    Spacer()
                                    Image(systemName: "chevron.right")
                                        .foregroundColor(.white)
                                }
                                .padding()
                                .background(LinearGradient(gradient: Gradient(colors: [Color("vinoTinto").opacity(0.9), Color("madera").opacity(0.9)]), startPoint: .leading, endPoint: .trailing))
                                .clipShape(RoundedRectangle(cornerRadius: 15))
                                .shadow(radius: 8)
                            }
                            .listRowBackground(Color.clear) // Hace la lista más limpia
                        }
                        .listStyle(PlainListStyle()) // Quita líneas innecesarias
                    } else {
                        VStack(spacing: 15) {
                            Image(systemName: "doc.text.magnifyingglass")
                                .resizable()
                                .scaledToFit()
                                .frame(width: 120, height: 120)
                                .foregroundColor(Color("madera"))
                            
                            Text("No hay pedidos realizados")
                                .foregroundColor(Color("roble"))
                                .font(.title3)
                                .bold()
                                .padding()
                        }
                    }
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
