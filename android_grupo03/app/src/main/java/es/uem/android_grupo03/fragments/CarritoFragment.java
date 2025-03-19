package es.uem.android_grupo03.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uem.android_grupo03.R;
import es.uem.android_grupo03.adapters.AdaptadorCarrito;
import es.uem.android_grupo03.models.CarritoModelo;
import es.uem.android_grupo03.models.LicorModelo;

public class CarritoFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdaptadorCarrito adaptadorCarrito;
    private CarritoModelo carritoModelo;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Button btnRealizarPedido;
    private List<String> productosEliminados = new ArrayList<>(); // Lista temporal de productos eliminados


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito, container, false);

        recyclerView = view.findViewById(R.id.rv);
        btnRealizarPedido = view.findViewById(R.id.btn_realizar_pedido);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        carritoModelo = new CarritoModelo();
        adaptadorCarrito = new AdaptadorCarrito(getContext());

        adaptadorCarrito.setOnQuantityChangeListener(this::actualizarCantidadProducto);
        adaptadorCarrito.setOnItemDeleteListener(this::eliminarProductoDelCarrito);

        recyclerView.setAdapter(adaptadorCarrito);

        if (currentUser != null) {
            productosEliminados.clear(); // 🔥 Al abrir la app, reiniciamos la lista de eliminados
            cargarCarritoUsuario(currentUser.getUid());
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        btnRealizarPedido.setOnClickListener(v -> realizarPedido());

        return view;
    }

    private void cargarCarritoUsuario(String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("perfiles").child(userId).child("carrito");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<LicorModelo> nuevosLicores = new ArrayList<>();
                List<Integer> nuevasCantidades = new ArrayList<>();
                Map<String, Integer> licoresFirebase = new HashMap<>();

                for (DataSnapshot itemCarrito : snapshot.getChildren()) {
                    LicorModelo licor = itemCarrito.child("licor").getValue(LicorModelo.class);
                    Long cantidadLong = itemCarrito.child("cantidad").getValue(Long.class);
                    int cantidad = (cantidadLong != null) ? cantidadLong.intValue() : 1;

                    if (licor != null && !productosEliminados.contains(licor.getNombre())) {
                        // 🚀 Solo agregar si NO está en la lista de eliminados
                        licoresFirebase.put(licor.getNombre(), cantidad);
                        nuevosLicores.add(licor);
                        nuevasCantidades.add(cantidad);
                    }
                }

                carritoModelo.setLicores(licoresFirebase);
                adaptadorCarrito.actualizarCarrito(nuevosLicores, nuevasCantidades);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar el carrito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarCantidadProducto(String licorId, int nuevaCantidad) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        DatabaseReference cantidadRef = FirebaseDatabase.getInstance()
                .getReference("perfiles")
                .child(userId)
                .child("carrito")
                .child(licorId)
                .child("cantidad");

        cantidadRef.setValue(nuevaCantidad)
                .addOnSuccessListener(aVoid -> {
                    carritoModelo.getLicores().put(licorId, nuevaCantidad);

                    // 🔥 Buscar el producto en el adaptador y actualizarlo
                    for (int i = 0; i < adaptadorCarrito.getItemCount(); i++) {
                        LicorModelo licor = adaptadorCarrito.getItem(i);
                        if (licor.getNombre().equals(licorId)) {
                            adaptadorCarrito.actualizarCantidad(i, nuevaCantidad);
                            break;
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al actualizar cantidad", Toast.LENGTH_SHORT).show());
    }


    private void eliminarProductoDelCarrito(String licorId, int position) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        DatabaseReference carritoRef = FirebaseDatabase.getInstance()
                .getReference("perfiles")
                .child(userId)
                .child("carrito")
                .child(licorId);

        carritoRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    productosEliminados.add(licorId); // 🚀 Guardamos el ID del producto eliminado
                    carritoModelo.eliminarLicor(licorId);
                    adaptadorCarrito.eliminarItem(position);
                    Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                });
    }

    private void realizarPedido() {
        if (carritoModelo == null || carritoModelo.getLicores().isEmpty()) {
            Toast.makeText(getContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference pedidosRef = FirebaseDatabase.getInstance()
                    .getReference("perfiles")
                    .child(userId)
                    .child("pedidos");

            String pedidoId = pedidosRef.push().getKey();
            Map<String, Object> pedidoData = Map.of(
                    "estado", "Pendiente",
                    "licores", carritoModelo.getLicores(),
                    "timestamp", System.currentTimeMillis()
            );

            pedidosRef.child(pedidoId).setValue(pedidoData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Pedido realizado con éxito", Toast.LENGTH_SHORT).show();
                        vaciarCarrito();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al realizar pedido", Toast.LENGTH_SHORT).show());
        }
    }

    private void vaciarCarrito() {
        DatabaseReference carritoRef = FirebaseDatabase.getInstance()
                .getReference("perfiles")
                .child(auth.getCurrentUser().getUid())
                .child("carrito");

        carritoRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    carritoModelo.getLicores().clear();
                    adaptadorCarrito.actualizarCarrito(new ArrayList<>(), new ArrayList<>());
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al vaciar el carrito", Toast.LENGTH_SHORT).show());
    }
}
