package es.uem.android_grupo03.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import es.uem.android_grupo03.R;
import es.uem.android_grupo03.models.LicorModelo;

public class AdaptadorBebidas extends RecyclerView.Adapter<AdaptadorBebidas.BebidasHolder> {

    private Context context;
    private List<LicorModelo> licorList;

    public AdaptadorBebidas(Context context, List<LicorModelo> licorList) {
        this.context = context;
        this.licorList = licorList;
    }

    @NonNull
    @Override
    public BebidasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.tarjet_bebidas, parent, false);
        return new BebidasHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BebidasHolder holder, int position) {
        LicorModelo licor = licorList.get(position);

        holder.tvName.setText(licor.getNombre());
        holder.tvPrice.setText("$" + licor.getPrecio());
        holder.tvDescription.setText(licor.getDescripcion());

        // 🔹 Convertir el nombre de la imagen en Firebase a un recurso en drawable
        int imagenRes = getDrawableResourceId(licor.getImagen());
        holder.iv.setImageResource(imagenRes);

        // Evento de clic para agregar al carrito
        holder.btnAddToCart.setOnClickListener(v -> agregarAlCarrito(licor));
    }

    private int getDrawableResourceId(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return R.drawable.whiskey_generico;  // 🔹 Imagen por defecto
        }
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    private void agregarAlCarrito(LicorModelo licor) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(context, "Debes iniciar sesión para agregar al carrito", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference carritoRef = FirebaseDatabase.getInstance()
                .getReference("perfiles")
                .child(user.getUid())
                .child("carrito")
                .child(licor.getNombre().replace(" ", "_")); // 🔹 Evita IDs nulos

        carritoRef.child("licor").setValue(licor);
        carritoRef.child("cantidad").setValue(1)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(context, "Añadido al carrito", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Error al añadir", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return licorList.size();
    }

    public static class BebidasHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvName, tvPrice, tvDescription;
        Button btnAddToCart;

        public BebidasHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv1);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnAddToCart = itemView.findViewById(R.id.boton_anadir);
        }
    }

    // **Método para actualizar la lista en el RecyclerView**
    public void actualizarLista(List<LicorModelo> nuevaLista) {
        this.licorList.clear();
        this.licorList.addAll(nuevaLista);
        notifyDataSetChanged();
    }
}
