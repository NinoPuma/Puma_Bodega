package es.uem.android_grupo03;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import es.uem.android_grupo03.adapters.ViewPagerAdapter;
import es.uem.android_grupo03.fragments.CarritoFragment;
import es.uem.android_grupo03.fragments.InicioFragment;
import es.uem.android_grupo03.fragments.PedidosFragment;
import es.uem.android_grupo03.fragments.PerfilFragment;

public class PaginaPrincipal extends AppCompatActivity {

    private Spinner spinnerCategorias;
    private InicioFragment inicioFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        spinnerCategorias = findViewById(R.id.spinnerCategorias);

        // Configurar el adaptador del ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        inicioFragment = new InicioFragment();
        adapter.addFragment(inicioFragment, "Inicio");
        adapter.addFragment(new CarritoFragment(), "Carrito");
        adapter.addFragment(new PedidosFragment(), "Pedidos");
        adapter.addFragment(new PerfilFragment(), "Perfil");

        viewPager.setAdapter(adapter);

        // Configurar el Spinner con las categorías de bebidas
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.categorias_bebidas,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(spinnerAdapter);

        // Manejar la selección del Spinner para filtrar bebidas en InicioFragment
        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                if (inicioFragment != null) {
                    inicioFragment.actualizarBebidas(categoriaSeleccionada);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });


        // Vincular el TabLayout con el ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Inicio");
                            tab.setIcon(R.drawable.ic_home);
                            break;
                        case 1:
                            tab.setText("Carrito");
                            tab.setIcon(R.drawable.ic_carrito);
                            break;
                        case 2:
                            tab.setText("Pedidos");
                            tab.setIcon(R.drawable.ic_pedidos);
                            break;
                        case 3:
                            tab.setText("Perfil");
                            tab.setIcon(R.drawable.ic_perfil);
                            break;
                    }
                }
        ).attach();
    }
}
