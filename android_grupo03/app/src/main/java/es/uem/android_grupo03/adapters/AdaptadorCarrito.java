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
import java.util.Map;
import java.util.List;

import es.uem.android_grupo03.R;
import es.uem.android_grupo03.models.CarritoModelo;
import es.uem.android_grupo03.models.LicorModelo;

public class AdaptadorCarrito extends RecyclerView.Adapter<AdaptadorCarrito.ViewHolder> {
    private Context context;
    private CarritoModelo carrito;
    private List<Map.Entry<LicorModelo, Integer>> licoresEnCarrito;

    public AdaptadorCarrito(Context context, CarritoModelo carrito) {
        this.context = context;
        this.carrito = carrito;
        this.licoresEnCarrito = new ArrayList<>(carrito.getLicores().entrySet());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tarjet_bebida_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map.Entry<LicorModelo, Integer> entry = licoresEnCarrito.get(position);
        LicorModelo licor = entry.getKey();
        int cantidad = entry.getValue();

        holder.nombre.setText(licor.getNombre());
        holder.precio.setText("$" + licor.getPrecio());
        holder.cantidad.setText(String.valueOf(cantidad));

        // Cargar imagen desde recursos si es necesario
        int imagenRes = getDrawableResourceId(licor.getImagen());
        if (imagenRes != 0) { // Si se encuentra el recurso
            holder.imagen.setImageResource(imagenRes);
        } else { // Si no se encuentra, usa una imagen por defecto
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
        ImageView imagen, btnSumar, btnRestar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreCarrito);
            precio = itemView.findViewById(R.id.tvPrecioCarrito);
            cantidad = itemView.findViewById(R.id.tvCantidad);
            imagen = itemView.findViewById(R.id.ivCarrito);
            btnSumar = itemView.findViewById(R.id.btnSumar);
            btnRestar = itemView.findViewById(R.id.btnRestar);
        }
    }
}

