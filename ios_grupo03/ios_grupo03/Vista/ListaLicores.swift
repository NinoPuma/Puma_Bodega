//
//  ListaLicores.swift
//  ios_grupo03
//
//  Created by Antonino Puma on 4/3/25.
//

import SwiftUI

struct ListaLicores: View {
    @StateObject private var controlador = GestorDatos()
    var tipo: String  // Esto permite que sea reutilizable para cualquier tipo de licor
    
    var body: some View {
        NavigationStack {
            List {
                // Sección del título centrado dentro de la lista
                Section {
                    Text(tipo)
                        .font(.title)
                        .bold()
                        .frame(maxWidth: .infinity, alignment: .center)
                        .listRowBackground(Color.clear) // Evita el fondo gris en algunos dispositivos
                }
                
                // Lista de licores filtrados por tipo
                ForEach(controlador.licores.filter { $0.tipo == tipo }) { licor in
                    NavigationLink(destination: InfoLicor(licor: licor)) {
                        HStack(alignment: .top, spacing: 10) {
                            Image(licor.imagen)
                                .resizable()
                                .frame(width: 50, height: 50)
                                .clipShape(RoundedRectangle(cornerRadius: 8))
                            
                            VStack(alignment: .leading, spacing: 4) {
                                Text(licor.nombre)
                                    .font(.headline)
                                    .bold()
                                
                                Text(String(format: "%.2f €", licor.precio))
                                    .font(.subheadline)
                                    .foregroundColor(.gray)
                            }
                            Spacer()
                        }
                        .padding(.vertical, 10)
                    }
                }
            }
        }
        .onAppear {
            controlador.cargarLicores(tipo: tipo)
        }
    }
}
