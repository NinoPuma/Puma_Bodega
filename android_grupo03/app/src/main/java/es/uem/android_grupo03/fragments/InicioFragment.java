package es.uem.android_grupo03.fragments;

import android.content.Context;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.uem.android_grupo03.adapters.AdaptadorBebidas;
import es.uem.android_grupo03.R;
import es.uem.android_grupo03.models.LicorModelo;

public class InicioFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<LicorModelo> licorList;
    private AdaptadorBebidas adaptadorBebidas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar la lista y el adaptador
        licorList = new ArrayList<>();
        adaptadorBebidas = new AdaptadorBebidas(getContext(), licorList);
        recyclerView.setAdapter(adaptadorBebidas);

        // Obtener datos desde Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("licores");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                licorList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LicorModelo licor = dataSnapshot.getValue(LicorModelo.class);

                    // Convertir nombre de imagen de Firebase a R.drawable
                    int imagenRes = getDrawableResourceId(licor.getImagen());

                    LicorModelo licorConImagen = new LicorModelo(
                            licor.getTipo(),
                            licor.getNombre(),
                            String.valueOf(imagenRes),  // 🔹 Convertir int a String si es necesario
                            licor.getPrecio(),
                            licor.getDescripcion()
                    );

                    licorList.add(licorConImagen);
                }
                adaptadorBebidas.notifyDataSetChanged();
            }

            private int getDrawableResourceId(String imageName) {
                Context context = getContext();
                return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

