//import SwiftUI
//
//struct Ron: View {
//    @StateObject private var controlador = GestorDatos()
//    var body: some View {
//        NavigationStack {
//            List {
//                ForEach(controlador.rones) { ron in
//                    HStack(alignment: .top, spacing: 10) {
//                        // Imagen del ron
//                        Image(ron.imagen)
//                            .resizable()
//                            .frame(width: 50, height: 50)
//                            .clipShape(RoundedRectangle(cornerRadius: 8))
//                        
//                        VStack(alignment: .leading, spacing: 4) {
//                            // Nombre del ron en negrita
//                            Text(ron.nombre)
//                                .font(.headline)
//                                .bold()
//                            
//                            // Precio del ron
//                            Text(String(format: "%.2f €", ron.precio))
//                                .font(.subheadline)
//                                .foregroundColor(.gray)
//                        }
//                        
//                        Spacer() // Empuja la descripción hacia la derecha
//                        
//                        // Descripción del ron alineada a la izquierda con un límite de líneas
//                        Text(ron.descripcion)
//                            .foregroundColor(.secondary)
//                            .frame(maxWidth: 150, alignment: .leading)
//                            .lineLimit(2) // Evita desbordes
//                    }
//                    .padding(.vertical, 10) // Espaciado entre elementos
//                }
//            }
//        }
//        .onAppear {
//            controlador.cargarLicores(tipo: "Ron")
//        }
//    }
//
//}
//#Preview {
//    Ron()
//}
