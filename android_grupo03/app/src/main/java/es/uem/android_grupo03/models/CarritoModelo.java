package es.uem.android_grupo03.models;

import java.util.HashMap;
import java.util.Map;

public class CarritoModelo {
    private Map<LicorModelo, Integer> licores;

    public CarritoModelo() {
        this.licores = new HashMap<>();
    }

    public CarritoModelo(Map<LicorModelo, Integer> licores) {
        this.licores = licores;
    }
    public Map<LicorModelo, Integer> getLicores() {
        return licores;
    }

    public void setLicores(Map<LicorModelo, Integer> licores) {
        this.licores = licores;
    }

    // Método para agregar un licor al carrito
    public void agregarLicor(LicorModelo licor, int cantidad) {
        if (licores.containsKey(licor)) {
            licores.put(licor, licores.get(licor) + cantidad);
        } else {
            licores.put(licor, cantidad);
        }
    }

    // Método para eliminar un licor del carrito
    public void eliminarLicor(LicorModelo licor) {
        licores.remove(licor);
    }

    // Método para limpiar el carrito
    public void vaciarCarrito() {
        licores.clear();
    }
}