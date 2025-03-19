package es.uem.android_grupo03.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
    private Spinner spinnerCategorias;
    private DatabaseReference databaseReference;
    private List<LicorModelo> licorList;
    private AdaptadorBebidas adaptadorBebidas;
    private boolean isDataLoaded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        recyclerView = view.findViewById(R.id.rv);
        spinnerCategorias = view.findViewById(R.id.spinnerCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        licorList = new ArrayList<>();
        adaptadorBebidas = new AdaptadorBebidas(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adaptadorBebidas);

        configurarSpinner();
        cargarBebidasDesdeFirebase();

        return view;
    }

    private void configurarSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.categorias_bebidas,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(spinnerAdapter);

        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isDataLoaded) {
                    Toast.makeText(getContext(), "Cargando bebidas, espera un momento...", Toast.LENGTH_SHORT).show();
                    return;
                }

                String categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                actualizarBebidas(categoriaSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void cargarBebidasDesdeFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("licores");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null) return;

                licorList.clear();
                System.out.println("📌 Cargando licores desde Firebase...");

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LicorModelo licor = dataSnapshot.getValue(LicorModelo.class);
                    if (licor != null && licor.getTipo() != null && !licor.getTipo().isEmpty()) {
                        licor.setTipo(convertirCategoria(licor.getTipo()));

                        System.out.println("✅ Bebida cargada: " + licor.getNombre() + " - Tipo: " + licor.getTipo());
                        licorList.add(licor);
                    }
                }

                System.out.println("✅ Total de bebidas cargadas: " + licorList.size());
                isDataLoaded = true;

                if (spinnerCategorias.getSelectedItem() != null) {
                    actualizarBebidas(spinnerCategorias.getSelectedItem().toString());
                }

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
        if (!isDataLoaded) {
            Toast.makeText(getContext(), "Cargando bebidas, espera un momento...", Toast.LENGTH_SHORT).show();
            return;
        }

        List<LicorModelo> bebidasFiltradas = new ArrayList<>();
        String categoriaFiltrada = convertirCategoria(categoria.trim());

        System.out.println("📌 Filtrando por categoría: " + categoriaFiltrada);

        for (LicorModelo licor : licorList) {
            if (licor.getTipo().equalsIgnoreCase(categoriaFiltrada)) {
                bebidasFiltradas.add(licor);
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

    private String convertirCategoria(String categoria) {
        if (categoria == null || categoria.isEmpty()) return "";

        categoria = categoria.trim().toLowerCase();

        switch (categoria) {
            case "whiskey":
            case "whisky":
                return "Whisky";
            case "ron":
                return "Ron";
            case "vodka":
                return "Vodka";
            case "vino":
            case "tinto":
            case "blanco":
            case "rosado":
            case "espumoso":
                return "Vino"; // 🔹 Agrupar todos los tipos de vino bajo "Vino"
            default:
                return categoria.substring(0, 1).toUpperCase() + categoria.substring(1);
        }
    }
}

