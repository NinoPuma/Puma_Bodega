//
//  InfoLicor.swift
//  ios_grupo03
//
//  Created by Antonino Puma on 4/3/25.
//
import SwiftUI

struct InfoLicor: View {
    var licor: Licor
    
    var body: some View {
        VStack(spacing: 20) {
            Text(licor.nombre)
                .font(.title)
                .bold()
            
            Image(licor.imagen)
                .resizable()
                .frame(width: 150, height: 150)
                .clipShape(RoundedRectangle(cornerRadius: 12))
            
            Text(licor.descripcion)
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding()
            
            Text(String(format: "$%.2f", licor.precio))
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding()
            
            
            Button(action: {
                // Lógica para agregar al carrito
            }) {
                Text("Agregar al Carrito")
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .clipShape(RoundedRectangle(cornerRadius: 10))
            }
            .padding()
            
            Spacer()
        }
        .padding()
        .navigationTitle("Información detallada")
    }
}
