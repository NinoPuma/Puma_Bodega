import SwiftUI

struct InfoPedido: View {
    var pedido: Pedido

    var body: some View {
        VStack(spacing: 20) {
            // Título estilizado
            Text("Detalles del Pedido")
                .font(.largeTitle)
                .fontWeight(.bold)
                .padding(.top)
            
            VStack(alignment: .leading, spacing: 15) {
                HStack {
                    Image(systemName: "number.circle.fill")
                        .foregroundColor(.blue)
                    Text("Pedido #\(pedido.id)")
                        .font(.headline)
                        .bold()
                }
                .padding(.vertical, 5)
                
                HStack {
                    Image(systemName: "calendar")
                        .foregroundColor(.purple)
                    Text("Fecha: \(pedido.fecha)")
                        .font(.subheadline)
                        .foregroundColor(.gray)
                }
                .padding(.vertical, 5)
                
                HStack {
                    Image(systemName: "hourglass")
                        .foregroundColor(pedido.estado == "Pendiente" ? .orange : .green)
                    Text("Estado: \(pedido.estado)")
                        .font(.subheadline)
                        .bold()
                        .foregroundColor(pedido.estado == "Pendiente" ? .orange : .green)
                }
                .padding(.vertical, 5)
                
                HStack {
                    Image(systemName: "cart.fill")
                        .foregroundColor(.blue)
                    Text("Total: \(String(format: "%.2f €", pedido.precioTotal))")
                        .font(.headline)
                        .bold()
                }
                .padding(.vertical, 5)
            }
            .padding()
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(RoundedRectangle(cornerRadius: 15)
                .fill(Color(.systemGray6))
                .shadow(radius: 5))
            .padding(.horizontal)
            
            Spacer()
        }
        .padding()
        .presentationDetents([.medium, .large]) // Define el tamaño del popup
    }
}
