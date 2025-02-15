import SwiftUI
import Foundation

class GestorDatos: ObservableObject {
    @Published var perfiles: [Perfil] = []
    @Published var licores: [Licor] = []
    @Published var pedidos: [Pedido] = []
    
    private let nomFichJSON = "BBDD.json"

    init() {
        cargarJSON()
    }
    
    // Obtiene la URL del archivo en el directorio de documentos
    private func obtenerDirectorioDocumentos() -> URL {
        FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0]
    }
    
    // Obtiene la ruta completa del archivo JSON en el directorio de documentos
    private func obtenerURLArchivo() -> URL {
        obtenerDirectorioDocumentos().appendingPathComponent(nomFichJSON)
    }
    
    // Carga el JSON (si no existe, lo copia desde el bundle)
    func cargarJSON() {
        let fileURL = obtenerURLArchivo()
        print("Ruta del archivo JSON: \(fileURL.path)")
        
        if !FileManager.default.fileExists(atPath: fileURL.path) {
            // Copiar el archivo desde el bundle si no existe
            if let bundleURL = Bundle.main.url(forResource: "BBDD", withExtension: "json") {
                do {
                    try FileManager.default.copyItem(at: bundleURL, to: fileURL)
                } catch {
                    print("Error copiando el JSON desde el bundle: \(error)")
                }
            }
        }
        
        do {
            let data = try Data(contentsOf: fileURL)
            let decoder = JSONDecoder()
            self.perfiles = try decoder.decode([Perfil].self, from: data)
        } catch {
            print("Error al cargar el JSON: \(error)")
        }
    }
    
    // Guarda los datos en el JSON
    func salvarJSON() {
        let fileURL = obtenerURLArchivo()
        do {
            let encoder = JSONEncoder()
            encoder.outputFormatting = .prettyPrinted
            let data = try encoder.encode(perfiles)
            try data.write(to: fileURL, options: .atomicWrite)
        } catch {
            print("Error al guardar el JSON: \(error)")
        }
    }
    
    // Agrega una nueva persona y guarda los cambios
    func agregarPerfil(nombre: String, email: String, tarjeta: String, direccion: String, pedido: [Pedido]) {
        let newPerfil = Perfil(id: (perfiles.last?.id ?? 0) + 1, nombre: nombre, email: email, direccion: direccion, pedido: pedido, tarjeta: tarjeta)
        perfiles.append(newPerfil)
        salvarJSON()
    }
    func agregarLicor(tipo: String, nombre: String, precio: Float, imagen: String, descripcion: String) {
        let newLicor = Licor(id: (licores.last?.id ?? 0) + 1, tipo: tipo, nombre: nombre, precio: precio, imagen: imagen, descripcion: descripcion)
        licores.append(newLicor)
        salvarJSON()
    }
    func agregarPedido(licor: [Licor], estado: String, fecha: String, precioTotal: Float) {
        let newPedido = Pedido(id: (pedidos.last?.id ?? 0) + 1, licor: licor, estado: estado, fecha: fecha, precioTotal: precioTotal)
        pedidos.append(newPedido)
        salvarJSON()
    }
    // Elimina una persona de la lista y guarda los cambios
    func borrarPerfil(at indexSet: IndexSet) {
        perfiles.remove(atOffsets: indexSet)
        salvarJSON()
    }
    func borrarLicor(at indexSet: IndexSet) {
        licores.remove(atOffsets: indexSet)
        salvarJSON()
    }
    func borrarPedido(at indexSet: IndexSet) {
        pedidos.remove(atOffsets: indexSet)
        salvarJSON()
    }

//    // Modifica una persona en la lista y guarda los cambios
//    func modificarPerfil(id: Int, nuevoNombre: String, nuevaEdad: Int) {
//        if let index = gente.firstIndex(where: { $0.id == id }) {
//            gente[index] = Persona(id: id, nombre: nuevoNombre, edad: nuevaEdad)
//            salvarJSON()
//        }
//    }
}
