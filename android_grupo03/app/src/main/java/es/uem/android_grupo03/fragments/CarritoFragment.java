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

import java.util.HashMap;
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
        adaptadorCarrito = new AdaptadorCarrito(getContext(), carritoModelo);
        recyclerView.setAdapter(adaptadorCarrito);

        if (currentUser != null) {
            cargarCarritoUsuario(currentUser.getEmail());
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        btnRealizarPedido.setOnClickListener(v -> realizarPedido());

        return view;
    }

    private void cargarCarritoUsuario(String emailUsuario) {
        databaseReference = FirebaseDatabase.getInstance().getReference("perfiles");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carritoModelo.getLicores().clear();
                for (DataSnapshot perfilSnapshot : snapshot.getChildren()) {
                    String email = perfilSnapshot.child("email").getValue(String.class);
                    if (email != null && email.equals(emailUsuario)) {
                        DataSnapshot carritoSnapshot = perfilSnapshot.child("carrito");
                        for (DataSnapshot itemCarrito : carritoSnapshot.getChildren()) {
                            LicorModelo licor = itemCarrito.child("licor").getValue(LicorModelo.class);
                            int cantidad = itemCarrito.child("cantidad").getValue(Integer.class);
                            carritoModelo.agregarLicor(licor, cantidad);
                        }
                    }
                }
                adaptadorCarrito.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar el carrito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void realizarPedido() {
        if (carritoModelo.getLicores().isEmpty()) {
            Toast.makeText(getContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference pedidosRef = FirebaseDatabase.getInstance().getReference("pedidos");
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Map<String, Object> pedidoData = new HashMap<>();
            pedidoData.put("usuarioId", userId);
            pedidoData.put("licores", carritoModelo.getLicores());
            pedidoData.put("estado", "Pendiente");

            pedidosRef.push().setValue(pedidoData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Pedido realizado con éxito", Toast.LENGTH_SHORT).show();
                        carritoModelo.vaciarCarrito();
                        adaptadorCarrito.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al realizar pedido", Toast.LENGTH_SHORT).show());
        }
    }
}

