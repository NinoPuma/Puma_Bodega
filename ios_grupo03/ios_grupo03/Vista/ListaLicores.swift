import SwiftUI

struct ListaLicores: View {
    @ObservedObject var gestorDatos: GestorDatos // 🔹 Ahora recibe `GestorDatos`
    var tipo: String  // 🔹 Permite mostrar diferentes tipos de licores

    var body: some View {
        NavigationStack {
            List {
                // Sección del título centrado dentro de la lista
                Section {
                    Text(tipo) // ✅ Muestra el tipo de licor
                        .font(.title)
                        .bold()
                        .frame(maxWidth: .infinity, alignment: .center)
                        .listRowBackground(Color.clear) // Evita el fondo gris en algunos dispositivos
                }
                
                // Lista de licores filtrados por tipo
                ForEach(gestorDatos.licores.filter { $0.tipo == tipo }) { licor in
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
            gestorDatos.cargarLicores(tipo: tipo) // 🔹 Cargar solo el tipo necesario
        }
    }
}

