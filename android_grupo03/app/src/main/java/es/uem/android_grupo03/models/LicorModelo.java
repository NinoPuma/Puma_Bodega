package es.uem.android_grupo03.models;

import java.util.Objects;

public class LicorModelo {
    private int id;
    private String tipo;
    private String nombre;
    private String imagen;
    private float precio;  // ✅ Mantener como float
    private String descripcion;

    // 🔥 Constructor con parámetros
    public LicorModelo(String nombre, String descripcion, String imagen, float precio, String tipo, int id) {
        this.nombre = Objects.requireNonNullElse(nombre, "Sin nombre");
        this.descripcion = Objects.requireNonNullElse(descripcion, "Sin descripción");
        this.imagen = (imagen != null && !imagen.isEmpty()) ? imagen : "imagen_default";
        this.precio = precio;
        this.tipo = Objects.requireNonNullElse(tipo, "Desconocido");
        this.id = id;
    }

    // 🔥 Constructor vacío (Firebase lo necesita)
    public LicorModelo() {}

    // ✅ Getters
    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public String getNombre() { return nombre; }
    public String getImagen() { return imagen; }
    public float getPrecio() { return precio; }  // ✅ Mantener como float
    public String getDescripcion() { return descripcion; }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
