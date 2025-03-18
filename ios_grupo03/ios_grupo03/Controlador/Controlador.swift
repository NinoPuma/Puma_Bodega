import SwiftUI
import Foundation

class GestorDatos: ObservableObject {
    @Published var perfilActual: Perfil?
    @Published var perfiles: [Perfil] = []
    @Published var licores: [Licor] = []
    
    @Published var rones: [Licor] = []
    @Published var vinos: [Licor] = []
    @Published var whiskeys: [Licor] = []
    @Published var vodkas: [Licor] = []
    
    @Published var mostrarAlerta = false
    @Published var mensajeAlerta = ""

    private let perfilesJSON = "BBDD"
    private let licoresJSON = "licores"

    
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

    // ✅ Cargar perfiles desde JSON
//    func cargarPerfiles() {
//        let url = obtenerURLArchivo()
//        
//        guard FileManager.default.fileExists(atPath: url.path) else {
//            print("❌ ERROR: No se encontró el archivo JSON de perfiles")
//            return
//        }
//        
//        do {
//            let data = try Data(contentsOf: url)
//            let decoder = JSONDecoder()
//            let jsonCompleto = try decoder.decode([String: [Perfil]].self, from: data)
//            
//            DispatchQueue.main.async {
//                self.perfiles = jsonCompleto["perfiles"] ?? []
//            }
//        } catch {
//            print("❌ ERROR al cargar el JSON de perfiles: \(error)")
//        }
//    }



    // ✅ Cargar un perfil específico
    func cargarPerfil(nombre: String) {
        if perfilActual?.nombre == nombre { return }
        
        if perfiles.isEmpty { cargarPerfiles() }

        DispatchQueue.main.async {
            self.perfilActual = self.perfiles.first(where: { $0.nombre == nombre })
            if let perfil = self.perfilActual {
                print("✅ Perfil cargado: \(perfil.nombre)")
            } else {
                print("❌ ERROR: No se encontró el perfil de \(nombre)")
            }
        }
    }
    
    func calcularTotalCarrito() -> Float {
        guard let perfil = perfilActual else { return 0 }

        var total: Float = 0
        for item in perfil.carrito {
            if let precio = item.licores.first?.precio {
                total += precio * Float(item.cantidad)
            }
        }
        
        return total
    }

    // ✅ Agregar licor al carrito con verificación mejorada
    func agregarAlCarrito(licor: Licor) {
            guard let perfilIndex = perfiles.firstIndex(where: { $0.id == perfilActual?.id }) else {
                print("❌ ERROR: No hay usuario autenticado.")
                return
            }
            
            if let index = perfiles[perfilIndex].carrito.firstIndex(where: { $0.licores.contains(where: { $0.id == licor.id }) }) {
                perfiles[perfilIndex].carrito[index].cantidad += 1
            } else {
                perfiles[perfilIndex].carrito.append(Carrito(licores: [licor], cantidad: 1))
            }
            
            perfilActual = perfiles[perfilIndex]
            salvarJSON()
            
            DispatchQueue.main.async {
                self.mensajeAlerta = "\(licor.nombre) ha sido añadido al carrito."
                self.mostrarAlerta = true
            }
        }



    // ✅ Eliminar licor del carrito (con Swipe)
    func eliminarLicor(at offsets: IndexSet) {
        guard let perfilIndex = perfiles.firstIndex(where: { $0.id == perfilActual?.id }) else { return }
        
        perfiles[perfilIndex].carrito.remove(atOffsets: offsets)
        perfilActual = perfiles[perfilIndex]
        salvarJSON()
    }

    // ✅ Realizar pedido
    func realizarPedido() {
        guard let perfilIndex = perfiles.firstIndex(where: { $0.id == perfilActual?.id }) else {
            print("❌ ERROR: No hay usuario autenticado.")
            return
        }

        var perfil = perfiles[perfilIndex]

        guard !perfil.carrito.isEmpty else {
            print("❌ ERROR: No hay productos en el carrito para realizar el pedido.")
            return
        }

        let precioTotal = perfil.carrito.reduce(0) { total, item in
            total + item.licores.reduce(0) { subtotal, licor in
                subtotal + (licor.precio * Float(item.cantidad))
            }
        }

        let nuevoID = (perfil.pedidos.map { $0.id }.max() ?? 0) + 1
        let nuevoPedido = Pedido(id: nuevoID, licores: perfil.carrito.flatMap { $0.licores }, estado: "Realizado", fecha: obtenerFechaActual(), precioTotal: precioTotal)

        perfil.pedidos.append(nuevoPedido)

        // **Vaciar carrito**
        perfil.carrito.removeAll()

        // **Actualizar perfil en la lista y en `perfilActual`**
        perfiles[perfilIndex] = perfil
        perfilActual = perfil

        salvarJSON()
    }



    // ✅ Guardar JSON
//    func salvarJSON() {
//        let fileURL = obtenerURLArchivo()
//        do {
//            let encoder = JSONEncoder()
//            encoder.outputFormatting = .prettyPrinted
//            let data = try encoder.encode(["perfiles": perfiles])
//            
//            try data.write(to: fileURL, options: .atomicWrite)
//            print("✅ JSON guardado correctamente en: \(fileURL.path)")
//        } catch {
//            print("❌ ERROR al guardar el JSON: \(error)")
//        }
//    }

    // ✅ Obtener la fecha actual en formato "dd/MM/yyyy"
    private func obtenerFechaActual() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd/MM/yyyy"
        return dateFormatter.string(from: Date())
    }

    // ✅ Obtener la URL del JSON
//    private func obtenerURLArchivo() -> URL {
//        return Bundle.main.url(forResource: "BBDD", withExtension: "json")!
//    }
    private func obtenerURLArchivo() -> URL {
            let fileManager = FileManager.default
            let directory = fileManager.urls(for: .documentDirectory, in: .userDomainMask).first!
            return directory.appendingPathComponent("BBDD.json")
        }

        // ✅ Cargar perfiles desde JSON
        func cargarPerfiles() {
            let url = obtenerURLArchivo()
            
            guard FileManager.default.fileExists(atPath: url.path) else {
                print("❌ ERROR: No se encontró el archivo JSON de perfiles")
                return
            }
            
            do {
                let data = try Data(contentsOf: url)
                let decoder = JSONDecoder()
                let jsonCompleto = try decoder.decode([String: [Perfil]].self, from: data)
                
                DispatchQueue.main.async {
                    self.perfiles = jsonCompleto["perfiles"] ?? []
                }
            } catch {
                print("❌ ERROR al cargar el JSON de perfiles: \(error)")
            }
        }

        // ✅ Guardar JSON en Document Directory
        func salvarJSON() {
            let fileURL = obtenerURLArchivo()
            do {
                let encoder = JSONEncoder()
                encoder.outputFormatting = .prettyPrinted
                let data = try encoder.encode(["perfiles": perfiles])
                
                try data.write(to: fileURL, options: .atomicWrite)
                print("✅ JSON guardado correctamente en: \(fileURL.path)")
            } catch {
                print("❌ ERROR al guardar el JSON: \(error)")
            }
        }

        // ✅ Copiar el JSON del bundle a Document Directory si no existe
        private func copiarJSONSiNoExiste() {
            let fileManager = FileManager.default
            let destinoURL = obtenerURLArchivo()

            if !fileManager.fileExists(atPath: destinoURL.path) {
                if let origenURL = Bundle.main.url(forResource: "BBDD", withExtension: "json") {
                    do {
                        try fileManager.copyItem(at: origenURL, to: destinoURL)
                        print("✅ Archivo JSON copiado a Document Directory")
                    } catch {
                        print("❌ ERROR al copiar el JSON: \(error)")
                    }
                } else {
                    print("❌ ERROR: No se encontró el JSON en el Bundle")
                }
            }
        }
        
    // ✅ Permitir editar perfil y guardar cambios
        func actualizarPerfil(email: String, direccion: String, tarjeta: String) {
            guard let perfilIndex = perfiles.firstIndex(where: { $0.id == perfilActual?.id }) else {
                print("❌ ERROR: No se encontró el perfil")
                return
            }
            
            perfiles[perfilIndex].email = email
            perfiles[perfilIndex].direccion = direccion
            perfiles[perfilIndex].tarjeta = tarjeta
            perfilActual = perfiles[perfilIndex]
            salvarJSON()
        }
        // ✅ Llamar a la copia en la inicialización
        init() {
            copiarJSONSiNoExiste()
            cargarPerfiles()
        }
}
