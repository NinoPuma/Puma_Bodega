//
//  Licor.swift
//  ios_grupo03
//
//  Created by Antonino Puma on 15/2/25.
//

struct Licor: Codable, Identifiable{
    let id: Int
    let tipo: String
    let nombre: String
    let precio: Float
    let imagen: String
    let descripcion: String
}
