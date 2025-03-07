import SwiftUI

struct ListaLicores: View {
    @ObservedObject var gestorDatos: GestorDatos
    var tipo: String

    var body: some View {
        NavigationStack {
            List {
                Section {
                    Text(tipo)
                        .font(.title)
                        .bold()
                        .frame(maxWidth: .infinity, alignment: .center)
                        .listRowBackground(Color.clear)
                }
                
                ForEach(obtenerLicoresFiltrados()) { licor in
                    NavigationLink(destination: InfoLicor(gestorDatos: gestorDatos, licor: licor)) {
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
            gestorDatos.cargarLicores(tipo: tipo)
        }
    }
    
    // ✅ Función para obtener licores filtrados y evitar recargar la lista innecesariamente
    private func obtenerLicoresFiltrados() -> [Licor] {
        return gestorDatos.licores.filter { $0.tipo == tipo }
    }
}
