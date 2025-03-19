package es.uem.android_grupo03.models;

import java.util.HashMap;
import java.util.Map;

public class CarritoModelo {
    private Map<String, Integer> licores;

    public CarritoModelo() {
        this.licores = new HashMap<>();
    }

    public Map<String, Integer> getLicores() {
        return licores;
    }

    public void setLicores(Map<String, Integer> licores) {
        this.licores = licores;
    }

    // Método para agregar un licor al carrito
    public void agregarLicor(LicorModelo licor, int cantidad) {
        String licorId = licor.getNombre();

        if (licores.containsKey(licorId)) {
            licores.put(licorId, licores.get(licorId) + cantidad);
        } else {
            licores.put(licorId, cantidad);
        }
    }

    // Método para eliminar un licor del carrito
    public void eliminarLicor(String licorId) {
        licores.remove(licorId);
    }

    // Método para limpiar el carrito
    public void vaciarCarrito() {
        licores.clear();
    }
}
