package es.uem.android_grupo03.models;

public class LicorModelo {
    private String nombre;
    private String descripcion;
    private String imagen;
    private double precio;
    private String tipo;
    private int id; // 🔥 Agrega este campo si está en Firebase

    // 🔥 Constructor vacío requerido por Firebase
    public LicorModelo() {}

    // 🔥 Constructor con todos los parámetros
    public LicorModelo(String nombre, String descripcion, String imagen, double precio, String tipo, int id) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precio = precio;
        this.tipo = tipo;
        this.id = id;
    }

    // 🔥 Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
