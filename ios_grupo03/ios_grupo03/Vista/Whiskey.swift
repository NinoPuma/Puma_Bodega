//import SwiftUI
//
//struct Whiskey: View {
//    @StateObject private var controlador = GestorDatos()
//    var body: some View {
//        NavigationStack {
//            List {
//                ForEach(controlador.whiskeys) { whiskey in
//                    HStack(alignment: .top, spacing: 10) {
//                        // Imagen del whiskey
//                        Image(whiskey.imagen)
//                            .resizable()
//                            .frame(width: 50, height: 50)
//                            .clipShape(RoundedRectangle(cornerRadius: 8))
//                        
//                        VStack(alignment: .leading, spacing: 4) {
//                            // Nombre del whiskey en negrita
//                            Text(whiskey.nombre)
//                                .font(.headline)
//                                .bold()
//                            
//                            // Precio del whiskey
//                            Text(String(format: "%.2f €", whiskey.precio))
//                                .font(.subheadline)
//                                .foregroundColor(.gray)
//                        }
//                        
//                        Spacer() // Empuja la descripción hacia la derecha
//                        
//                        // Descripción del whiskey alineada a la izquierda con un límite de líneas
//                        Text(whiskey.descripcion)
//                            .foregroundColor(.secondary)
//                            .frame(maxWidth: 150, alignment: .leading)
//                            .lineLimit(2) // Evita desbordes
//                    }
//                    .padding(.vertical, 10) // Espaciado entre elementos
//                }
//            }
//        }
//        .onAppear {
//            controlador.cargarLicores(tipo: "Whiskey")
//        }
//    }
//    
//}
//#Preview {
//    Whiskey()
//}
