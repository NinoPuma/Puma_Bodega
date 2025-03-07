//
//  VistaPedidos.swift
//  ios_grupo03
//
//  Created by Antonino Puma on 24/2/25.
//

import SwiftUI

struct VistaPedidos: View {
    @ObservedObject var gestorDatos: GestorDatos // ✅ Se agrega para acceder a los pedidos

    var body: some View {
        NavigationStack {
            VStack {
                Text("Pedidos Realizados")
                    .font(.largeTitle)
                    .bold()
                    .padding()

                if let pedidos = gestorDatos.perfilActual?.pedidos, !pedidos.isEmpty {
                    List(pedidos) { pedido in
                        VStack(alignment: .leading, spacing: 8) {
                            Text("Pedido #\(pedido.id)")
                                .font(.headline)
                                .bold()

                            Text("Fecha: \(pedido.fecha)")
                                .font(.subheadline)
                                .foregroundColor(.gray)

                            Text("Estado: \(pedido.estado)")
                                .font(.subheadline)
                                .foregroundColor(pedido.estado == "Pendiente" ? .orange : .green)

                            Text("Total: \(String(format: "%.2f €", pedido.precioTotal))")
                                .font(.subheadline)
                                .bold()
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
            .onAppear {
                gestorDatos.cargarPerfil(nombre: gestorDatos.perfilActual?.nombre ?? "")
            }
        }
    }
}
