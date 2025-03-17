package es.uem.android_grupo03;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uem.android_grupo03.adapters.AdaptadorProductosPedido;
import es.uem.android_grupo03.models.LicorModelo;
import es.uem.android_grupo03.models.PedidoModelo;

public class PedidoDetalleActivity extends AppCompatActivity {
    private TextView tvEstado, tvFecha, tvTotal;
    private RecyclerView recyclerViewProductos;
    private AdaptadorProductosPedido adaptadorProductos;
    private ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_detalle);

        // Inicializar vistas
        tvEstado = findViewById(R.id.tvEstadoPedidoDetalle);
        tvFecha = findViewById(R.id.tvFechaPedidoDetalle);
        tvTotal = findViewById(R.id.tvTotalPedidoDetalle);
        recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
        ivBack = findViewById(R.id.ivBack);

        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));

        // Recibir datos del intent
        PedidoModelo pedido = (PedidoModelo) getIntent().getSerializableExtra("pedido");

        if (pedido != null) {
            tvEstado.setText("Estado: " + pedido.getEstado());
            tvFecha.setText("📅 Fecha: " + pedido.getTimestamp());
            tvTotal.setText("Total: $" + pedido.getPrecioTotal());

            List<LicorModelo> productos = pedido.getLicores();
            adaptadorProductos = new AdaptadorProductosPedido(this, productos);
            recyclerViewProductos.setAdapter(adaptadorProductos);
        }

        ivBack.setOnClickListener(v -> finish());
    }
}
