import SwiftUI

// Vista para Vino
struct Vino: View {
    @StateObject private var controlador = GestorDatos()
    @State private var vinos: [Licor] = []
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
        .onAppear {
            cargarVinos()
        }
    }
    func cargarVinos() {
            guard let url = Bundle.main.url(forResource: "licores", withExtension: "json") else {
                print("No se ha encontrado el archivo .json")
                return
            }
            do {
                let data = try Data(contentsOf: url)
                let decoder = JSONDecoder()
                let decodedLicores = try decoder.decode([Licor].self, from: data)
                
                // ✅ Actualizar en el hilo principal para evitar errores con @State
                DispatchQueue.main.async {
                    self.vinos = decodedLicores.filter { $0.tipo == "Vino" }
                }
            } catch {
                print("Error al decodificar el JSON: \(error)")
            }
        }
}
#Preview {
    Vino()
}
