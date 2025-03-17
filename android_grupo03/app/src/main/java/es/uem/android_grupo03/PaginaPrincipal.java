package es.uem.android_grupo03;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        // Configurar el adaptador del ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new InicioFragment(), "Inicio");
        adapter.addFragment(new CarritoFragment(), "Carrito");
        adapter.addFragment(new PedidosFragment(), "Pedidos");
        adapter.addFragment(new PerfilFragment(), "Perfil");

        viewPager.setAdapter(adapter);

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
