package es.uem.android_grupo03.fragments;

import android.os.Bundle;
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
import java.util.List;

import es.uem.android_grupo03.R;
import es.uem.android_grupo03.adapters.AdaptadorPedidos;
import es.uem.android_grupo03.models.PedidoModelo;

public class PedidosFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdaptadorPedidos adaptadorPedidos;
    private List<PedidoModelo> listaPedidos;
    private DatabaseReference pedidosRef;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);

        recyclerView = view.findViewById(R.id.rv_pedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        listaPedidos = new ArrayList<>();
        adaptadorPedidos = new AdaptadorPedidos(getContext(), listaPedidos);
        recyclerView.setAdapter(adaptadorPedidos);

        if (currentUser != null) {
            cargarPedidosUsuario(currentUser.getUid());
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void cargarPedidosUsuario(String userId) {
        pedidosRef = FirebaseDatabase.getInstance().getReference("pedidos");

        pedidosRef.orderByChild("usuarioId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPedidos.clear();
                for (DataSnapshot pedidoSnapshot : snapshot.getChildren()) {
                    PedidoModelo pedido = pedidoSnapshot.getValue(PedidoModelo.class);
                    if (pedido != null) {
                        listaPedidos.add(pedido);
                    }
                }
                adaptadorPedidos.actualizarPedidos(listaPedidos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar los pedidos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
