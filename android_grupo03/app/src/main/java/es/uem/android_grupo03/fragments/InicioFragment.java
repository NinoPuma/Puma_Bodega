package es.uem.android_grupo03.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.uem.android_grupo03.AdaptadorBebidas;
import es.uem.android_grupo03.R;

public class InicioFragment extends Fragment {

    private int[] bebidas = {R.drawable.ron_generico, R.drawable.rondiplomatico};
    private String[] nombres = {"Bebida 1", "Bebida 2"};
    private String[] precios = {"$10.00", "$20.00"};
    private String[] descripciones = {"Ron suave", "Ron premium"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv);
        AdaptadorBebidas adaptadorBebidas = new AdaptadorBebidas(nombres, precios, descripciones, bebidas);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptadorBebidas);

        return view;
    }
}
