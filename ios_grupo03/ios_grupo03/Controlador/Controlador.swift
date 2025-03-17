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

    // ✅ Agregar licor al carrito
    // ✅ Agregar licor al carrito con verificación mejorada
    func agregarAlCarrito(licor: Licor) {
        guard let perfilIndex = perfiles.firstIndex(where: { $0.id == perfilActual?.id }) else {
            print("❌ ERROR: No hay usuario autenticado.")
            return
        }

        // 🔹 Buscar si el licor ya está en el carrito
        if let index = perfiles[perfilIndex].carrito.firstIndex(where: {
            $0.licores.contains(where: { $0.id == licor.id })
        }) {
            perfiles[perfilIndex].carrito[index].cantidad += 1
            print("🔄 \(licor.nombre) ya estaba en el carrito. Nueva cantidad: \(perfiles[perfilIndex].carrito[index].cantidad)")
        } else {
            perfiles[perfilIndex].carrito.append(Carrito(licores: [licor], cantidad: 1))
            print("✅ \(licor.nombre) agregado al carrito.")
        }

        // 🔹 Verificar contenido del carrito antes de guardar
        print("📦 Carrito actualizado: \(perfiles[perfilIndex].carrito.map { "\($0.licores.first?.nombre ?? "Desconocido") - Cantidad: \($0.cantidad)" })")

        // 🔹 Actualizar perfil actual
        perfilActual = perfiles[perfilIndex]

        // 🔹 Guardar cambios en el JSON
        salvarJSON()

        // 🔹 Verificar que el JSON se guardó correctamente
        do {
            let jsonData = try JSONEncoder().encode(perfiles)
            if let jsonString = String(data: jsonData, encoding: .utf8) {
                print("📄 JSON actualizado con carrito:\n\(jsonString)")
            } else {
                print("❌ Error al convertir JSON a String")
            }
        } catch {
            print("❌ Error al codificar JSON: \(error)")
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

        // 🔹 Calcular el precio total del pedido
        let precioTotal = perfil.carrito.reduce(0) { total, item in
            total + item.licores.reduce(0) { subtotal, licor in
                subtotal + (licor.precio * Float(item.cantidad))
            }
        }

        // 🔹 Generar un ID único para el nuevo pedido
        let nuevoID = (perfil.pedidos.map { $0.id }.max() ?? 0) + 1

        // 🔹 Crear el nuevo pedido
        let nuevoPedido = Pedido(
            id: nuevoID,
            licores: perfil.carrito.flatMap { $0.licores }, // Extraer licores del carrito
            estado: "Realizado",
            fecha: obtenerFechaActual(),
            precioTotal: precioTotal
        )

        // 🔹 Agregar el nuevo pedido a la lista de pedidos
        perfil.pedidos.append(nuevoPedido)

        // 🔹 Vaciar el carrito después de agregar el pedido
        perfil.carrito.removeAll()

        // 🔹 Actualizar el perfil en la lista de perfiles
        perfiles[perfilIndex] = perfil
        perfilActual = perfil

        print("✅ Pedido realizado con éxito. ID: \(nuevoID)")
        print("📦 Pedidos actuales del usuario: \(perfil.pedidos.map { "ID: \($0.id) - Total: \($0.precioTotal)" })")

        // 🔹 Guardar cambios en el JSON
        salvarJSON()

        // 🔹 Verificar que el JSON se guardó correctamente
        do {
            let jsonData = try JSONEncoder().encode(perfiles)
            if let jsonString = String(data: jsonData, encoding: .utf8) {
                print("📄 JSON actualizado con pedidos:\n\(jsonString)")
            } else {
                print("❌ Error al convertir JSON a String")
            }
        } catch {
            print("❌ Error al codificar JSON: \(error)")
        }
    }


    // ✅ Guardar JSON
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

    // ✅ Obtener la fecha actual en formato "dd/MM/yyyy"
    private func obtenerFechaActual() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd/MM/yyyy"
        return dateFormatter.string(from: Date())
    }

    // ✅ Obtener la URL del JSON
    private func obtenerURLArchivo() -> URL {
        return URL(fileURLWithPath: "/Users/apuma/PI_puma_bodega/ios_grupo03/ios_grupo03/DatosJson/BBDD.json")
    }
}
