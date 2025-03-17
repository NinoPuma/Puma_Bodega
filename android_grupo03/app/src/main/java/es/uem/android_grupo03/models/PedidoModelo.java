package es.uem.android_grupo03.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public List<LicorPedido> getLicores() { return licores; }

    public void setLicores(Object licoresObject) {
        if (licoresObject instanceof List) {
            this.licores = (List<LicorPedido>) licoresObject;
        } else if (licoresObject instanceof Map) {
            Map<String, Object> licoresMap = (Map<String, Object>) licoresObject;
            List<LicorPedido> listaLicores = new ArrayList<>();

            for (Object value : licoresMap.values()) {
                if (value instanceof Map) {
                    Map<String, Object> licorData = (Map<String, Object>) value;
                    LicorPedido licor = new LicorPedido();
                    licor.nombre = (String) licorData.get("nombre");
                    licor.cantidad = ((Long) licorData.get("cantidad")).intValue();
                    licor.precio = ((Double) licorData.get("precio"));
                    licor.imagen = (String) licorData.get("imagen");
                    listaLicores.add(licor);
                }
            }
            this.licores = listaLicores;
        } else {
            this.licores = new ArrayList<>();
        }
    }
}
