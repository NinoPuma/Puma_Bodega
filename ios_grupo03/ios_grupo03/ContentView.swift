//
//  ContentView.swift
//  ios_grupo03
//
//  Created by Antonino Puma on 11/2/25.
//

import SwiftUI

struct ContentView: View {
	//Instanciación de usuario y contraseña
	@State var usr: String = ""
	@State var pwd: String = ""
    var body: some View {
        VStack {
            Image("Logo_proyecto_integrador")
                .resizable()
                .scaledToFit()
                .frame(width: 700, height: 300)
            
            
        	TextField ("Usuario", text: $usr)
                	.keyboardType(.emailAddress)
                	.disableAutocorrection(true)
                	.autocapitalization(.none)
	                .font(.headline)
        	        .cornerRadius(6)
                	.padding(.horizontal, 60)
	                .foregroundStyle(Color.black)
	                .onChange(of: usr) { oldValue, newValue in
	                    print("Username nuevo valor: \(newValue)")
                	}
        
            
            
            SecureField("Contraseña", text: $pwd)
	                .disableAutocorrection(true)
        	        .autocapitalization(.none)
                	.font(.headline)
	                
        	        .cornerRadius(6)
                	.padding(.horizontal, 60)
	                .foregroundStyle(Color.black)
        	        .onChange(of: pwd) { oldValue, newValue in
                	    print("Contraseña nuevo valor: \(newValue)")
                	}
            
	           }
        .padding()
    }
}

#Preview {
    ContentView()
}
