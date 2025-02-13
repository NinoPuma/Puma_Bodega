package es.uem.android_grupo03;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PaginaPrincipal extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView winesRecyclerView;
    private WineAdapter wineAdapter;
    private List<Wine> wineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        searchEditText = findViewById(R.id.searchEditText);
        winesRecyclerView = findViewById(R.id.winesRecyclerView);

        // Initialize the wine list
        wineList = new ArrayList<>();
        populateWineList();

        // Set up the RecyclerView
        wineAdapter = new WineAdapter(wineList);
        winesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        winesRecyclerView.setAdapter(wineAdapter);
    }

    private void populateWineList() {
        // Add sample wines to the list
        wineList.add(new Wine("Vino Tinto", "Un delicioso vino tinto.", R.drawable.ic_wine));
        wineList.add(new Wine("Vino Blanco", "Un refrescante vino blanco.", R.drawable.ic_wine));
        // Add more wines as needed
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}