package es.uem.android_grupo03.models;

import java.util.Objects;

public class LicorModelo {
    private int id;
    private String tipo;
    private String nombre;
    private String imagen;
    private float precio;
    private String descripcion;

    public LicorModelo(String tipo, String nombre, String imagen, float precio, String descripcion) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.imagen = imagen;
        this.precio = precio;
        this.descripcion = descripcion;
    }
    //constructor vacio requerido por firebase
    public LicorModelo() {
    }

    public int getId() {
        return id;
    }

    public float getPrecio() {
        return precio;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LicorModelo licor = (LicorModelo) obj;
        return id == licor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
