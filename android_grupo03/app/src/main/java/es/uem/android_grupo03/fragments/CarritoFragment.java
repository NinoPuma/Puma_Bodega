package es.uem.android_grupo03.fragments;

import android.os.Bundle;
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
    private Button btnRealizarPedido, btnVaciarCarrito;  // 📌 Añadir botón de vaciar carrito

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito, container, false);

        recyclerView = view.findViewById(R.id.rv);
        btnRealizarPedido = view.findViewById(R.id.btn_realizar_pedido);
        btnVaciarCarrito = view.findViewById(R.id.btnVaciarCarrito); // 📌 Referencia al botón

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        carritoModelo = new CarritoModelo();
        adaptadorCarrito = new AdaptadorCarrito(getContext());
        recyclerView.setAdapter(adaptadorCarrito);

        if (currentUser != null) {
            cargarCarritoUsuario(currentUser.getUid());
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        btnRealizarPedido.setOnClickListener(v -> realizarPedido());

        // 📌 Agregar funcionalidad al botón de vaciar carrito
        btnVaciarCarrito.setOnClickListener(v -> vaciarCarrito());

        return view;
    }

    private void cargarCarritoUsuario(String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("perfiles").child(userId).child("carrito");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<LicorModelo> nuevosLicores = new ArrayList<>();
                List<Integer> nuevasCantidades = new ArrayList<>();

                carritoModelo.vaciarCarrito(); // 💡 Limpiar antes de actualizar

                for (DataSnapshot itemCarrito : snapshot.getChildren()) {
                    LicorModelo licor = itemCarrito.child("licor").getValue(LicorModelo.class);
                    Long cantidadLong = itemCarrito.child("cantidad").getValue(Long.class);
                    int cantidad = (cantidadLong != null) ? cantidadLong.intValue() : 1;

                    if (licor != null) {
                        nuevosLicores.add(licor);
                        nuevasCantidades.add(cantidad);
                        carritoModelo.agregarLicor(licor, cantidad);
                    }
                }

                adaptadorCarrito.actualizarCarrito(nuevosLicores, nuevasCantidades);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar el carrito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void realizarPedido() {
        if (carritoModelo == null || carritoModelo.getLicores() == null || carritoModelo.getLicores().isEmpty()) {
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

            String pedidoId = pedidosRef.push().getKey(); // Genera un ID único para el pedido

            Map<String, Object> pedidoData = new HashMap<>();
            pedidoData.put("estado", "Pendiente");
            pedidoData.put("licores", carritoModelo.getLicores());
            pedidoData.put("timestamp", System.currentTimeMillis());

            pedidosRef.child(pedidoId).setValue(pedidoData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Pedido realizado con éxito", Toast.LENGTH_SHORT).show();
                        vaciarCarrito();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al realizar pedido", Toast.LENGTH_SHORT).show());
        }
    }

    // 🚮 Vaciar el carrito cuando se presiona el botón
    private void vaciarCarrito() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference carritoRef = FirebaseDatabase.getInstance()
                .getReference("perfiles")
                .child(currentUser.getUid())
                .child("carrito");

        carritoRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    carritoModelo.getLicores().clear();
                    adaptadorCarrito.actualizarCarrito(new ArrayList<>(), new ArrayList<>());
                    Toast.makeText(getContext(), "Carrito vaciado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al vaciar el carrito", Toast.LENGTH_SHORT).show());
    }
}
