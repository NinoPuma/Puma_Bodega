//
//  Pedido.swift
//  ios_grupo03
//
//  Created by Antonino Puma on 15/2/25.
//

struct Pedido: Codable, Identifiable{
    let id: Int
    let licor: [Licor]
    let estado: String
    let fecha: String
    let precioTotal: Float
}
