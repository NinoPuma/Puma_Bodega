package es.uem.android_grupo03.models;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class PerfilModelo {
    private String nombre;
    private String direccion;
    private String codigoPostal;
    private String correo;
    private String fotoPerfilUrl;
    private List<PedidoModelo.LicorPedido> licores; // 🔥 Usar el nuevo LicorPedido


    public PerfilModelo(String nombre, String direccion, String codigoPostal, String correo, String fotoPerfilUrl) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.correo = correo;
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    // Constructor vacío requerido por Firebase
    public PerfilModelo() {
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public String getCorreo() {
        return correo;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public List<PedidoModelo.LicorPedido> getLicores() {
        return licores != null ? licores : new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PerfilModelo perfil = (PerfilModelo) obj;
        return Objects.equals(correo, perfil.correo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correo);
    }
}
