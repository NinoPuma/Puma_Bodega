//
//  Perfil.swift
//  ios_grupo03
//
//  Created by Antonino Puma on 15/2/25.
//

struct Perfil: Codable, Identifiable{
    let id: Int
    let nombre: String
    let email: String
    let direccion: String
    let pedido: [Pedido]
    let tarjeta: String
}
