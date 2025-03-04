import SwiftUI

struct Whiskey: View {
    @State private var whiskeys: [Licor] = [] // Inicialización correcta
    var body: some View {
        NavigationStack {
            List {
                ForEach(whiskeys) { whiskey in
                    HStack(alignment: .top, spacing: 10) {
                        // Imagen del whiskey
                        Image(whiskey.imagen)
                            .resizable()
                            .frame(width: 50, height: 50)
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                        
                        VStack(alignment: .leading, spacing: 4) {
                            // Nombre del whiskey en negrita
                            Text(whiskey.nombre)
                                .font(.headline)
                                .bold()
                            
                            // Precio del whiskey
                            Text(String(format: "%.2f €", whiskey.precio))
                                .font(.subheadline)
                                .foregroundColor(.gray)
                        }
                        
                        Spacer() // Empuja la descripción hacia la derecha
                        
                        // Descripción del whiskey alineada a la izquierda con un límite de líneas
                        Text(whiskey.descripcion)
                            .foregroundColor(.secondary)
                            .frame(maxWidth: 150, alignment: .leading)
                            .lineLimit(2) // Evita desbordes
                    }
                    .padding(.vertical, 10) // Espaciado entre elementos
                }
            }
        }
        .onAppear {
            cargarWhiskey()
        }
    }
    
    func cargarWhiskey() {
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
                self.whiskeys = decodedLicores.filter { $0.tipo == "Whiskey" }
            }
        } catch {
            print("Error al decodificar el JSON: \(error)")
        }
    }
}
#Preview {
    Whiskey()
}
