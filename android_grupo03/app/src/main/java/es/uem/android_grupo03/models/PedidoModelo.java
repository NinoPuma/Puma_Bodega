package es.uem.android_grupo03.models;

import java.util.List;

public class PedidoModelo {
    private String usuarioId;
    private String estado;
    private long timestamp;
    private List<LicorPedido> licores;

    public static class LicorPedido {
        private String nombre;
        private int cantidad;
        private double precio;
        private String imagen;

        public String getNombre() { return nombre; }
        public int getCantidad() { return cantidad; }
        public double getPrecio() { return precio; }
        public String getImagen() { return imagen; }
    }

    public String getUsuarioId() { return usuarioId; }
    public String getEstado() { return estado; }
    public long getTimestamp() { return timestamp; }
    public List<LicorPedido> getLicores() { return licores; }
}
