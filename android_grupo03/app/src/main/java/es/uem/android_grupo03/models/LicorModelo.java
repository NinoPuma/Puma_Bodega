package es.uem.android_grupo03.models;

import java.util.Objects;

public class LicorModelo {
    private String nombre;
    private String tipo;
    private String imagen;  // 🔹 Asegúrate de que esta variable existe
    private double precio;
    private String descripcion;

    // Constructor vacío requerido por Firebase
    public LicorModelo() {
    }

    // Constructor con parámetros
    public LicorModelo(String nombre, String tipo, String imagen, double precio, String descripcion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagen = imagen;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    // ✅ Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {  // 🔹 AGREGA ESTE MÉTODO
        this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
