package es.uem.android_grupo03.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import es.uem.android_grupo03.R;
import es.uem.android_grupo03.models.LicorModelo;

public class AdaptadorProductosPedido extends RecyclerView.Adapter<AdaptadorProductosPedido.ViewHolder> {
    private Context context;
    private List<LicorModelo> listaProductos;

    public AdaptadorProductosPedido(Context context, List<LicorModelo> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LicorModelo producto = listaProductos.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvPrecio.setText(String.format(Locale.US, "$%.2f", producto.getPrecio()));

        // Asigna imagen si está en drawable
        int imagenRes = context.getResources().getIdentifier(producto.getImagen(), "drawable", context.getPackageName());
        holder.ivProducto.setImageResource(imagenRes != 0 ? imagenRes : R.drawable.whiskey_generico);
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio;
        ImageView ivProducto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProductoPedido);
            tvPrecio = itemView.findViewById(R.id.tvPrecioProductoPedido);
            ivProducto = itemView.findViewById(R.id.ivProductoPedido);
        }
    }
}
