import SwiftUI
import Foundation

class GestorDatos: ObservableObject {
    @Published var perfilActual: Perfil? // 🔹 Variable para el perfil autenticado
    @Published var perfiles: [Perfil] = []
    @Published var licores: [Licor] = []
    @Published var pedidos: [Pedido] = []

    @Published var rones: [Licor] = []
    @Published var vinos: [Licor] = []
    @Published var whiskeys: [Licor] = []
    @Published var vodkas: [Licor] = []

    private let perfilesJSON = "BBDD"
    private let licoresJSON = "licores" // Nuevo archivo JSON para licores

    // ✅ Cargar perfiles manualmente cuando sea necesario
    func cargarPerfiles() {
        guard let url = Bundle.main.url(forResource: perfilesJSON, withExtension: "json") else {
            print("❌ ERROR: No se encontró el archivo JSON de perfiles")
            return
        }
        do {
            let data = try Data(contentsOf: url)
            let jsonString = String(data: data, encoding: .utf8) ?? "No se pudo convertir a String"
            print("📄 JSON Cargado: \(jsonString)") // ✅ Verifica que el JSON se está cargando correctamente

            let decoder = JSONDecoder()
            let jsonCompleto = try decoder.decode([String: [Perfil]].self, from: data)

            if let perfilesData = jsonCompleto["perfiles"] {
                DispatchQueue.main.async {
                    self.perfiles = perfilesData
                    print("✅ Perfiles cargados correctamente: \(self.perfiles.count) perfiles")
                }
            } else {
                print("❌ ERROR: No se encontró la clave 'perfiles' en el JSON")
            }
        } catch {
            print("❌ ERROR al cargar el JSON de perfiles: \(error)")
        }
    }

    
    // ✅ Cargar los licores desde su propio JSON
    func cargarLicoresDesdeJSON() {
        guard let url = Bundle.main.url(forResource: licoresJSON, withExtension: "json") else {
            print("No se encontró el archivo JSON de licores")
            return
        }
        do {
            let data = try Data(contentsOf: url)
            let decoder = JSONDecoder()
            let licoresData = try decoder.decode([Licor].self, from: data)

            DispatchQueue.main.async {
                self.licores = licoresData
            }
        } catch {
            print("Error al cargar el JSON de licores: \(error)")
        }
    }
    // ✅ Filtrar los licores según el tipo
        func cargarLicores(tipo: String) {
            // Asegurar que la lista de licores ya esté cargada antes de filtrar
            if licores.isEmpty {
                cargarLicoresDesdeJSON()
            }
            
            DispatchQueue.main.async {
                let licoresFiltrados = self.licores.filter { $0.tipo == tipo }
                
                switch tipo {
                case "Ron":
                    self.rones = licoresFiltrados
                case "Vino":
                    self.vinos = licoresFiltrados
                case "Whiskey":
                    self.whiskeys = licoresFiltrados
                case "Vodka":
                    self.vodkas = licoresFiltrados
                default:
                    print("Tipo de licor no reconocido")
                }
            }
        }
    
    // ✅ Cargar el perfil de un usuario específico
    func cargarPerfil(nombre: String) {
        // Si `perfiles` está vacío, cargar los datos primero
        if perfiles.isEmpty {
            print("⚠️ La lista de perfiles está vacía. Cargando perfiles primero...")
            cargarPerfiles()
        }

        // Buscar y asignar el perfil dentro de la lista
        DispatchQueue.main.async {
            if let perfilEncontrado = self.perfiles.first(where: { $0.nombre == nombre }) {
                self.perfilActual = perfilEncontrado
                print("✅ Perfil encontrado: \(perfilEncontrado.nombre)")
            } else {
                print("❌ ERROR: No se encontró el perfil de \(nombre)")
            }
        }
    }


    // ✅ Guardar cambios en el JSON de perfiles
    func salvarJSON() {
        let fileURL = obtenerURLArchivo()
        do {
            let encoder = JSONEncoder()
            encoder.outputFormatting = .prettyPrinted
            let data = try encoder.encode(perfiles)
            try data.write(to: fileURL, options: .atomicWrite)
            print("Datos guardados correctamente")
        } catch {
            print("Error al guardar el JSON: \(error)")
        }
    }

    // ✅ Obtener la URL del archivo JSON en documentos
    private func obtenerURLArchivo() -> URL {
        FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0].appendingPathComponent("\(perfilesJSON).json")
    }
}
