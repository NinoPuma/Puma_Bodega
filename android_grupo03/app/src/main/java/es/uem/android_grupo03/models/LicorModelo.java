package es.uem.android_grupo03.models;

public class LicorModelo {
    private int id;
    private String tipo;
    private String nombre;
    private int imagen;
    private String descripcion;

    public LicorModelo(int id, String tipo, String nombre, int imagen, String descripcion) {
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImagen() {
        return imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
