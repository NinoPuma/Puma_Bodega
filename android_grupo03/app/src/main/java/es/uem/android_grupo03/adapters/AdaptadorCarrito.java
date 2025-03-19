package es.uem.android_grupo03.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import es.uem.android_grupo03.R;
import es.uem.android_grupo03.models.LicorModelo;

public class AdaptadorCarrito extends RecyclerView.Adapter<AdaptadorCarrito.ViewHolder> {
    private Context context;
    private List<LicorModelo> licoresEnCarrito;
    private List<Integer> cantidades;
    private OnItemDeleteListener deleteListener;
    private OnQuantityChangeListener quantityChangeListener;

    public AdaptadorCarrito(Context context) {
        this.context = context;
        this.licoresEnCarrito = new ArrayList<>();
        this.cantidades = new ArrayList<>();
    }

    public void setOnItemDeleteListener(OnItemDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
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
        holder.cantidad.setText(String.valueOf(cantidad));

        int imagenRes = getDrawableResourceId(licor.getImagen());
        if (imagenRes != 0) {
            holder.imagen.setImageResource(imagenRes);
        } else {
            holder.imagen.setImageResource(R.drawable.whiskey_generico);
        }

        // 🚀 Evento para eliminar el producto
        holder.btnEliminar.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onItemDelete(licor.getNombre(), position);
            }
        });

        // 🚀 Evento para aumentar cantidad
        holder.btnSumar.setOnClickListener(v -> {
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChange(licor.getNombre(), cantidad + 1);
            }
        });

        // 🚀 Evento para disminuir cantidad
        holder.btnRestar.setOnClickListener(v -> {
            if (cantidad > 1 && quantityChangeListener != null) {
                quantityChangeListener.onQuantityChange(licor.getNombre(), cantidad - 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return licoresEnCarrito.size();
    }
    public LicorModelo getItem(int position) {
        if (licoresEnCarrito != null && position >= 0 && position < licoresEnCarrito.size()) {
            return licoresEnCarrito.get(position);
        }
        return new LicorModelo(); // ⚠️ Retorna un objeto vacío en vez de null para evitar errores
    }


    public void eliminarItem(int position) {
        if (position >= 0 && position < licoresEnCarrito.size()) {
            licoresEnCarrito.remove(position);
            cantidades.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, licoresEnCarrito.size()); // 🔥 Ajusta posiciones
        }
    }


    public void actualizarCantidad(int position, int nuevaCantidad) {
        if (position >= 0 && position < cantidades.size()) {
            cantidades.set(position, nuevaCantidad);
            notifyItemChanged(position); // 🔥 Actualiza solo el item modificado
        }
    }


    private int getDrawableResourceId(String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio, cantidad;
        ImageView imagen;
        ImageButton btnEliminar, btnSumar, btnRestar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreCarrito);
            precio = itemView.findViewById(R.id.tvPrecioCarrito);
            cantidad = itemView.findViewById(R.id.tvCantidad);
            imagen = itemView.findViewById(R.id.ivCarrito);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCarrito);
            btnSumar = itemView.findViewById(R.id.btnSumar);
            btnRestar = itemView.findViewById(R.id.btnRestar);
        }
    }

    public interface OnItemDeleteListener {
        void onItemDelete(String licorId, int position);
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange(String licorId, int newQuantity);
    }

    public void actualizarCarrito(List<LicorModelo> nuevosLicores, List<Integer> nuevasCantidades) {
        this.licoresEnCarrito = nuevosLicores;
        this.cantidades = nuevasCantidades;
        notifyDataSetChanged();
    }
}
