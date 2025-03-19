package es.uem.android_grupo03.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.uem.android_grupo03.R;
import es.uem.android_grupo03.models.PedidoModelo;

public class AdaptadorPedidos extends RecyclerView.Adapter<AdaptadorPedidos.ViewHolder> {
    private final Context context;
    private List<PedidoModelo> listaPedidos;

    public AdaptadorPedidos(Context context, List<PedidoModelo> listaPedidos) {
        this.context = context;
        this.listaPedidos = listaPedidos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tarjeta_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PedidoModelo pedido = listaPedidos.get(position);

        holder.estadoPedido.setText("Estado: " + Objects.requireNonNullElse(pedido.getEstado(), "Desconocido"));

        // Vuelve a hacer visible la fecha
        holder.fechaEntrega.setVisibility(View.VISIBLE);
        holder.fechaEntrega.setText("📅 Llega el " + convertirFecha(pedido.getTimestamp()));

        holder.contenedorProductos.removeAllViews();

        double totalPedido = 0.0;

        if (pedido.getLicores() != null) {
            for (PedidoModelo.LicorPedido licor : pedido.getLicores()) {
                View productoView = LayoutInflater.from(context).inflate(R.layout.tarjet_bebida_pedido, holder.contenedorProductos, false);

                TextView nombreProducto = productoView.findViewById(R.id.tvNombreProducto);
                TextView cantidadProducto = productoView.findViewById(R.id.tvCantidadProducto);
                TextView precioProducto = productoView.findViewById(R.id.tvPrecioProducto);
                ImageView imagenProducto = productoView.findViewById(R.id.ivProducto);

                nombreProducto.setText(licor.getNombre());
                cantidadProducto.setText("Cantidad: " + licor.getCantidad());

                double precioCalculado = licor.getPrecio() * licor.getCantidad();
                precioProducto.setText(String.format(Locale.US, "$%.2f", precioCalculado));

                cargarImagenProducto(imagenProducto, licor.getImagen());

                totalPedido += precioCalculado;

                holder.contenedorProductos.addView(productoView);
            }
        }

        holder.totalPedido.setText("Total: $" + String.format(Locale.US, "%.2f", totalPedido));

        holder.itemView.setOnClickListener(null);
    }


    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    public void actualizarPedidos(List<PedidoModelo> nuevosPedidos) {
        this.listaPedidos = nuevosPedidos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView estadoPedido, fechaEntrega, totalPedido;
        LinearLayout contenedorProductos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            estadoPedido = itemView.findViewById(R.id.tvEstadoPedido);
            fechaEntrega = itemView.findViewById(R.id.tvFechaEntrega);
            totalPedido = itemView.findViewById(R.id.tvTotalPedido);
            contenedorProductos = itemView.findViewById(R.id.contenedorProductos);
        }
    }

    private String convertirFecha(Long timestamp) {
        if (timestamp == null) return "Fecha desconocida";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    private void cargarImagenProducto(ImageView imageView, String imagenNombre) {
        int imagenRes = context.getResources().getIdentifier(imagenNombre, "drawable", context.getPackageName());
        imageView.setImageResource(imagenRes != 0 ? imagenRes : R.drawable.whiskey_generico);
    }
}
