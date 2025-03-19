package es.uem.android_grupo03;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
        recyclerViewProductos = findViewById(R.id.rvProductosPedido);
        ivBack = findViewById(R.id.ivBack);

        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));

        // Recibir datos del intent
        PedidoModelo pedido = (PedidoModelo) getIntent().getSerializableExtra("pedido");

        if (pedido != null) {
            tvEstado.setText("Estado: " + pedido.getEstado());
            tvFecha.setText("📅 Fecha: " + convertirFecha(pedido.getTimestamp()));
            tvTotal.setText("Total: $" + String.format(Locale.US, "%.2f", pedido.getPrecioTotal()));

            // 🔥 Convertir LicorPedido a LicorModelo asegurando los tipos de datos correctos
            List<LicorModelo> productos = new ArrayList<>();

            for (PedidoModelo.LicorPedido licorPedido : pedido.getLicores()) {
                LicorModelo licorModelo = new LicorModelo(
                        licorPedido.getNombre(),
                        licorPedido.getDescripcion() != null ? licorPedido.getDescripcion() : "Sin descripción",
                        licorPedido.getImagen() != null ? licorPedido.getImagen() : "imagen_por_defecto",
                        (float) licorPedido.getPrecio(),  // 🔥 Convertir double a float
                        licorPedido.getTipo() != null ? licorPedido.getTipo() : "Desconocido",
                        (int) licorPedido.getId()  // 🔥 Convertir long a int
                );
                productos.add(licorModelo);
            }

            // Configurar el adaptador con la lista convertida
            adaptadorProductos = new AdaptadorProductosPedido(this, productos);
            recyclerViewProductos.setAdapter(adaptadorProductos);

            // Botón para volver atrás
            ivBack.setOnClickListener(v -> finish());
        }
    }

    // ✅ Método para convertir timestamp a fecha legible
    private String convertirFecha(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new java.util.Date(timestamp));
    }
}
