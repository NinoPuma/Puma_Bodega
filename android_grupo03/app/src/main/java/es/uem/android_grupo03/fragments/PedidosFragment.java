package es.uem.android_grupo03.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import es.uem.android_grupo03.adapters.AdaptadorPedidos;
import es.uem.android_grupo03.models.LicorModelo;
import es.uem.android_grupo03.models.PedidoModelo;

public class PedidosFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdaptadorPedidos adaptadorPedidos;
    private List<PedidoModelo> listaPedidos;
    private List<LicorModelo> listaLicoresGlobal = new ArrayList<>();

    private DatabaseReference pedidosRef, licoresRef;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);

        recyclerView = view.findViewById(R.id.rv_pedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        listaPedidos = new ArrayList<>();
        adaptadorPedidos = new AdaptadorPedidos(getContext(), listaPedidos);
        recyclerView.setAdapter(adaptadorPedidos);

        if (currentUser != null) {
            cargarLicoresYDespuesPedidos(currentUser.getUid());
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void cargarLicoresYDespuesPedidos(String userId) {
        licoresRef = FirebaseDatabase.getInstance().getReference("licores");
        licoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaLicoresGlobal.clear();
                for (DataSnapshot licorSnap : snapshot.getChildren()) {
                    LicorModelo licor = licorSnap.getValue(LicorModelo.class);
                    if (licor != null) {
                        listaLicoresGlobal.add(licor);
                    }
                }
                cargarPedidosUsuario(userId); // Ahora carga pedidos
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al cargar licores: " + error.getMessage());
            }
        });
    }

    private void cargarPedidosUsuario(String userId) {
        pedidosRef = FirebaseDatabase.getInstance()
                .getReference("perfiles")
                .child(userId)
                .child("pedidos");

        pedidosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPedidos.clear();

                for (DataSnapshot pedidoSnapshot : snapshot.getChildren()) {
                    PedidoModelo pedido = new PedidoModelo();
                    pedido.setEstado(pedidoSnapshot.child("estado").getValue(String.class));
                    pedido.setTimestamp(pedidoSnapshot.child("timestamp").getValue(Long.class));

                    Map<String, Long> licoresMap = new HashMap<>();
                    for (DataSnapshot licorSnapshot : pedidoSnapshot.child("licores").getChildren()) {
                        licoresMap.put(licorSnapshot.getKey(), licorSnapshot.getValue(Long.class));
                    }

                    List<PedidoModelo.LicorPedido> licoresPedido = new ArrayList<>();
                    for (Map.Entry<String, Long> entry : licoresMap.entrySet()) {
                        LicorModelo licorModelo = obtenerLicorPorNombre(entry.getKey());
                        if (licorModelo != null) {
                            PedidoModelo.LicorPedido licorPedido =
                                    new PedidoModelo.LicorPedido(
                                            licorModelo.getNombre(),
                                            entry.getValue().intValue(),
                                            licorModelo.getPrecio(),
                                            licorModelo.getImagen(),
                                            licorModelo.getDescripcion(),
                                            licorModelo.getTipo(),
                                            licorModelo.getId()
                                    );
                            licoresPedido.add(licorPedido);
                        }
                    }

                    pedido.setLicores(licoresPedido);
                    listaPedidos.add(pedido);
                }

                adaptadorPedidos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al cargar pedidos: " + error.getMessage());
            }
        });
    }

    private LicorModelo obtenerLicorPorNombre(String nombre) {
        for (LicorModelo licor : listaLicoresGlobal) {
            if (licor.getNombre().equalsIgnoreCase(nombre)) {
                return licor;
            }
        }
        return null;
    }
}
