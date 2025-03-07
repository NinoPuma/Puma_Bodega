import SwiftUI

struct InfoPedido: View {
    var pedido: Pedido

    var body: some View {
        VStack(spacing: 20) {
            Text("Detalles del Pedido")
                .font(.title)
                .bold()
                .padding(.top)

            VStack(alignment: .leading, spacing: 15) {
                HStack {
                    Image(systemName: "number.circle.fill")
                        .foregroundColor(.blue)
                    Text("Pedido #\(pedido.id)")
                        .font(.headline)
                        .bold()
                }

                HStack {
                    Image(systemName: "calendar")
                        .foregroundColor(.purple)
                    Text("Fecha: \(pedido.fecha)")
                        .font(.subheadline)
                        .foregroundColor(.gray)
                }

                HStack {
                    Image(systemName: "hourglass")
                        .foregroundColor(pedido.estado == "Pendiente" ? .orange : .green)
                    Text("Estado: \(pedido.estado)")
                        .font(.subheadline)
                        .bold()
                        .foregroundColor(pedido.estado == "Pendiente" ? .orange : .green)
                }

                HStack {
                    Image(systemName: "cart.fill")
                        .foregroundColor(.blue)
                    Text("Total: \(String(format: "%.2f €", pedido.precioTotal))")
                        .font(.headline)
                        .bold()
                }
            }
            .padding()
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(RoundedRectangle(cornerRadius: 15)
                .fill(Color(.systemGray6))
                .shadow(radius: 3))

            Spacer()
        }
        .padding()
        .presentationDetents([.medium, .large]) // 🔹 Define el tamaño del popup
    }
}
