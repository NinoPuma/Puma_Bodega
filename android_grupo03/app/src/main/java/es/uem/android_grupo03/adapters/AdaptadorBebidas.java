package es.uem.android_grupo03.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.uem.android_grupo03.R;

public class AdaptadorBebidas extends RecyclerView.Adapter<AdaptadorBebidas.BebidasHolder> {

    private String[] nombres;
    private String[] precios;
    private String[] descripciones;
    private int[] imagenes;

    public AdaptadorBebidas(String[] nombres, String[] precios, String[] descripciones, int[] imagenes) {
        this.nombres = nombres;
        this.precios = precios;
        this.descripciones = descripciones;
        this.imagenes = imagenes;
    }

    @NonNull
    @Override
    public BebidasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjet_bebidas, parent, false);
        return new BebidasHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BebidasHolder holder, int position) {
        holder.tvName.setText(nombres[position]);
        holder.tvPrice.setText(precios[position]);
        holder.tvDescription.setText(descripciones[position]);
        holder.iv.setImageResource(imagenes[position]);
    }

    @Override
    public int getItemCount() {
        return nombres.length;
    }

    public static class BebidasHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tvName, tvPrice, tvDescription;

        public BebidasHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv1);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}