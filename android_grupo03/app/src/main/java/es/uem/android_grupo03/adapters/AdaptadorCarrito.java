package es.uem.android_grupo03.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.uem.android_grupo03.R;
import es.uem.android_grupo03.models.LicorModelo;

public class AdaptadorCarrito extends RecyclerView.Adapter<AdaptadorCarrito.ViewHolder> {
    private Context context;
    private List<LicorModelo> licoresEnCarrito;
    private List<Integer> cantidades;

    public AdaptadorCarrito(Context context) {
        this.context = context;
        this.licoresEnCarrito = new ArrayList<>();
        this.cantidades = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tarjet_bebida_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LicorModelo licor = licoresEnCarrito.get(position);
        int cantidad = cantidades.get(position);

        holder.nombre.setText(licor.getNombre());
        holder.precio.setText("$" + licor.getPrecio());
        holder.cantidad.setText("Cantidad: " + cantidad);

        // Cargar imagen desde recursos si es necesario
        int imagenRes = getDrawableResourceId(licor.getImagen());
        if (imagenRes != 0) {
            holder.imagen.setImageResource(imagenRes);
        } else {
            holder.imagen.setImageResource(R.drawable.whiskey_generico);
        }
    }

    @Override
    public int getItemCount() {
        return licoresEnCarrito.size();
    }

    private int getDrawableResourceId(String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio, cantidad;
        ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreCarrito);
            precio = itemView.findViewById(R.id.tvPrecioCarrito);
            cantidad = itemView.findViewById(R.id.tvCantidad);
            imagen = itemView.findViewById(R.id.ivCarrito);
        }
    }

    // 🔄 Método para actualizar la lista cuando cambie el carrito
    public void actualizarCarrito(List<LicorModelo> nuevosLicores, List<Integer> nuevasCantidades) {
        this.licoresEnCarrito = nuevosLicores;
        this.cantidades = nuevasCantidades;
        notifyDataSetChanged(); // 🔄 Refrescar UI
    }

    // 🚮 Método para vaciar el carrito
    public void vaciarCarrito() {
        this.licoresEnCarrito.clear();
        this.cantidades.clear();
        notifyDataSetChanged(); // 🔄 Refrescar UI después de vaciar
    }
}
