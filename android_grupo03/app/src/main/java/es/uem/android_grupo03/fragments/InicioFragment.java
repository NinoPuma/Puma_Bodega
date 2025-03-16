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

        // Cargar las bebidas desde Firebase
        cargarBebidasDesdeFirebase();

        return view;
    }

    private void cargarBebidasDesdeFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("licores");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null) return;

                licorList.clear();
                System.out.println("📌 Cargando licores desde Firebase...");

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LicorModelo licor = dataSnapshot.getValue(LicorModelo.class);
                    if (licor != null) {
                        if (licor.getTipo() == null || licor.getTipo().isEmpty()) {
                            System.out.println("⚠️ Bebida sin tipo en Firebase: " + licor.getNombre());
                            continue;
                        }

                        // 🔥 Normaliza la categoría para comparación segura
                        licor.setTipo(normalizarTexto(licor.getTipo()));

                        System.out.println("✅ Bebida cargada: " + licor.getNombre() + " - Tipo: " + licor.getTipo());
                        licorList.add(licor);
                    }
                }

                System.out.println("✅ Total de bebidas cargadas: " + licorList.size());
                adaptadorBebidas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void actualizarBebidas(String categoria) {
        if (licorList.isEmpty()) {
            Toast.makeText(getContext(), "La lista de bebidas aún no se ha cargado", Toast.LENGTH_SHORT).show();
            return;
        }

        List<LicorModelo> bebidasFiltradas = new ArrayList<>();
        String categoriaFiltrada = convertirCategoria(categoria.trim()); // Usa el método de conversión

        System.out.println("📌 Filtrando por categoría: " + categoriaFiltrada);
        System.out.println("📋 Lista de bebidas en memoria:");

        for (LicorModelo licor : licorList) {
            if (licor.getTipo() != null) {
                String tipoLicor = licor.getTipo().trim();
                System.out.println("🔍 Revisando: " + licor.getNombre() + " - Tipo en Firebase: " + tipoLicor);

                if (tipoLicor.equalsIgnoreCase(categoriaFiltrada)) {
                    bebidasFiltradas.add(licor);
                }
            }
        }

        if (bebidasFiltradas.isEmpty()) {
            System.out.println("❌ No se encontraron bebidas para: " + categoriaFiltrada);
            Toast.makeText(getContext(), "No hay bebidas en esta categoría", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("✅ Bebidas encontradas para: " + categoriaFiltrada);
        }

        adaptadorBebidas.actualizarLista(bebidasFiltradas);
    }

    // 🔥 Normalizar texto (sin tildes y en minúsculas)
    private String normalizarTexto(String texto) {
        return texto.trim().toLowerCase();
    }

    // 🔥 Mapeo correcto de categorías
    private String convertirCategoria(String categoria) {
        switch (categoria.toLowerCase()) {
            case "whiskey":
            case "whisky":
                return "whisky"; // Estándar en Firebase
            case "ron":
                return "ron";
            case "vodka":
                return "vodka";
            case "vino":
                return "vino";
            default:
                return categoria;
        }
    }
}
