package es.uem.android_grupo03.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PedidoModelo {
    private String usuarioId;
    private String estado;
    private long timestamp;
    private List<LicorPedido> licores;
    private double precioTotal;

    // ✅ Clase interna `LicorPedido`
    public static class LicorPedido {
        private String nombre;
        private int cantidad;
        private double precio;
        private String imagen;
        private String descripcion;
        private String tipo;
        private long id;

        // 🔥 Constructor vacío requerido por Firebase
        public LicorPedido() {}

        // 🔥 Constructor con parámetros
        public LicorPedido(String nombre, int cantidad, double precio, String imagen, String descripcion, String tipo, long id) {
            this.nombre = nombre;
            this.cantidad = Math.max(cantidad, 1);
            this.precio = Math.max(precio, 0.0);
            this.imagen = (imagen != null && !imagen.isEmpty()) ? imagen : "imagen_default";
            this.descripcion = descripcion != null ? descripcion : "Sin descripción";
            this.tipo = tipo != null ? tipo : "Desconocido";
            this.id = id > 0 ? id : -1;
        }

        // ✅ Getters
        public String getNombre() { return nombre; }
        public int getCantidad() { return cantidad; }
        public double getPrecio() { return precio; }
        public String getImagen() { return imagen; }
        public String getDescripcion() { return descripcion; }
        public String getTipo() { return tipo; }
        public long getId() { return id; }
    }

    // ✅ Getters y Setters para `PedidoModelo`
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getPrecioTotal() { return precioTotal; }
    public void setPrecioTotal(double precioTotal) { this.precioTotal = precioTotal; }

    public List<LicorPedido> getLicores() {
        return licores != null ? licores : new ArrayList<>();
    }

    public void setLicores(List<LicorPedido> licores) {
        this.licores = licores;
    }
}
