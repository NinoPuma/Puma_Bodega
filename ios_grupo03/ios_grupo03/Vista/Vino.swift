import SwiftUI

// Vista para Vino
struct Vino: View {
    let vinos: [Licor] = [
        Licor(id: 2, tipo: "Vino", nombre: "Alta", precio: 149.99, imagen: "Vino_Tintoalta", descripcion: "Tinto con aroma afrutado."),
        Licor(id: 3, tipo: "Vino", nombre: "Flor", precio: 29.99, imagen: "Vino_Tintoflor", descripcion: "Tinto con olor a miel."),
        Licor(id: 4, tipo: "Vino", nombre: "Valserrano", precio: 19.99, imagen: "Vino_Blancovalserrano", descripcion: "Blanco con fuerte olor a roble.")
    ]
    var body: some View {
        NavigationStack {
            List {
                ForEach(vinos) { vino in
                    HStack(alignment: .top, spacing: 10) {
                        // Imagen del vino
                        Image(vino.imagen)
                            .resizable()
                            .frame(width: 50, height: 50)
                            .clipShape(RoundedRectangle(cornerRadius: 8))

                        VStack(alignment: .leading, spacing: 4) {
                            // Nombre del vino en negrita
                            Text(vino.nombre)
                                .font(.headline)
                                .bold()

                            // Precio del vino
                            Text(String(format: "%.2f €", vino.precio))
                                .font(.subheadline)
                                .foregroundColor(.gray)
                        }

                        Spacer() // Empuja la descripción hacia la derecha

                        // Descripción del vino alineada a la derecha
                        Text(vino.descripcion)
                            .foregroundColor(.secondary)
                            .frame(maxWidth: 150, alignment: .leading) // Controla el ancho de la descripción
                    }
                    .padding(.vertical, 10) // Espaciado entre elementos
                }
            }
            .navigationTitle("Vinos")
        }
    }
}
#Preview {
    Vino()
}
