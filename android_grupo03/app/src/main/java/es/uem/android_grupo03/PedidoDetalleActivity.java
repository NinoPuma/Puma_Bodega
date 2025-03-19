package es.uem.android_grupo03;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Locale;
import es.uem.android_grupo03.adapters.AdaptadorProductosPedido;
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
        recyclerViewProductos = findViewById(R.id.rvProductosPedido);
        ivBack = findViewById(R.id.ivBack);

        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));

        // Recibir datos del intent
        PedidoModelo pedido = (PedidoModelo) getIntent().getSerializableExtra("pedido");


        if (pedido != null) {
            tvEstado.setText("Estado: " + pedido.getEstado());
            tvFecha.setText("📅 Fecha: " + convertirFecha(pedido.getTimestamp()));

            // Calcular total desde los productos
            double totalCalculado = 0.0;
            for (PedidoModelo.LicorPedido licor : pedido.getLicores()) {
                totalCalculado += licor.getPrecio() * licor.getCantidad();
            }

            tvTotal.setText("Total: $" + String.format(Locale.US, "%.2f", totalCalculado));

            // Configurar el adaptador directamente con los datos originales (sin conversión innecesaria)
            adaptadorProductos = new AdaptadorProductosPedido(this, pedido.getLicores());
            recyclerViewProductos.setAdapter(adaptadorProductos);

            // Botón para volver atrás
            ivBack.setOnClickListener(v -> finish());
        }
    }

    // Método para convertir timestamp a fecha legible
    private String convertirFecha(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new java.util.Date(timestamp));
    }
}
