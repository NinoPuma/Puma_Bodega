package es.uem.android_grupo03.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PedidoModelo implements Serializable {
    private String usuarioId;
    private String estado;
    private long timestamp;
    private List<LicorPedido> licores;
    private double precioTotal;

    public PedidoModelo() {}

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public List<LicorPedido> getLicores() {
        return licores != null ? licores : new ArrayList<>();
    }

    public void setLicores(List<LicorPedido> licores) {
        this.licores = licores;
    }

    public static class LicorPedido {
        private String nombre;
        private int cantidad;
        private double precio;
        private String imagen;
        private String descripcion;
        private String tipo;
        private long id;

        public LicorPedido() {}

        // Constructor simplificado para crear desde un LicorModelo y una cantidad
        public LicorPedido(LicorModelo licor, int cantidad) {
            this.nombre = licor.getNombre();
            this.cantidad = cantidad;
            this.precio = licor.getPrecio();
            this.imagen = licor.getImagen();
            this.descripcion = licor.getDescripcion();
            this.tipo = licor.getTipo();
            this.id = licor.getId();
        }

        public LicorPedido(String nombre, int cantidad, double precio, String imagen, String descripcion, String tipo, long id) {
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precio = precio;
            this.imagen = imagen;
            this.descripcion = descripcion;
            this.tipo = tipo;
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public int getCantidad() {
            return cantidad;
        }

        public double getPrecio() {
            return precio;
        }

        public String getImagen() {
            return imagen;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getTipo() {
            return tipo;
        }

        public long getId() {
            return id;
        }
    }
}
