//
//  Perfil.swift
//  ios_grupo03
//
//  Created by Antonino Puma on 15/2/25.
//

struct Perfil: Codable, Identifiable{
    let id: Int
    let nombre: String
    var email: String
    var direccion: String
    var pedidos: [Pedido]
    var tarjeta: String
    var carrito: [Carrito]
}
