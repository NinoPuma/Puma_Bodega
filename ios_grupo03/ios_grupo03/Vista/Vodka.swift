import SwiftUI

struct Vodka: View {
    @State private var vodkas: [Licor] = [] // Inicialización correcta
    var body: some View {
        NavigationStack {
            List {
                ForEach(vodkas) { vodka in
                    HStack(alignment: .top, spacing: 10) {
                        // Imagen del ron
                        Image(vodka.imagen)
                            .resizable()
                            .frame(width: 50, height: 50)
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                        
                        VStack(alignment: .leading, spacing: 4) {
                            // Nombre del ron en negrita
                            Text(vodka.nombre)
                                .font(.headline)
                                .bold()
                            
                            // Precio del ron
                            Text(String(format: "%.2f €", vodka.precio))
                                .font(.subheadline)
                                .foregroundColor(.gray)
                        }
                        
                        Spacer() // Empuja la descripción hacia la derecha
                        
                        // Descripción del ron alineada a la izquierda con un límite de líneas
                        Text(vodka.descripcion)
                            .foregroundColor(.secondary)
                            .frame(maxWidth: 150, alignment: .leading)
                            .lineLimit(2) // Evita desbordes
                    }
                    .padding(.vertical, 10) // Espaciado entre elementos
                }
            }
        }
        .onAppear {
            cargarVodka()
        }
    }
    
    func cargarVodka() {
        guard let url = Bundle.main.url(forResource: "licores", withExtension: "json") else {
            print("No se ha encontrado el archivo .json")
            return
        }
        do {
            let data = try Data(contentsOf: url)
            let decoder = JSONDecoder()
            let decodedLicores = try decoder.decode([Licor].self, from: data)
            
            //Actualizar en el hilo principal para evitar errores con @State
            DispatchQueue.main.async {
                self.vodkas = decodedLicores.filter { $0.tipo == "Vodka" }
            }
        } catch {
            print("Error al decodificar el JSON: \(error)")
        }
    }
}
#Preview {
    Vodka()
}
