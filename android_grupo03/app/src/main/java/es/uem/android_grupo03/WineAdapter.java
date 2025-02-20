package es.uem.android_grupo03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WineAdapter extends RecyclerView.Adapter<WineAdapter.WineViewHolder> {

    private List<Wine> wineList;

    public WineAdapter(List<Wine> wineList) {
        this.wineList = wineList;
    }

    @NonNull
    @Override
    public WineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.Item_wine_card, parent, false);
        return new WineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WineViewHolder holder, int position) {
        Wine wine = wineList.get(position);
        holder.wineNameTextView.setText(wine.getName());
        holder.wineDescriptionTextView.setText(wine.getDescription());
        holder.wineImageView.setImageResource(wine.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return wineList.size();
    }

    public static class WineViewHolder extends RecyclerView.ViewHolder {
        public ImageView wineImageView;
        public TextView wineNameTextView;
        public TextView wineDescriptionTextView;

        public WineViewHolder(@NonNull View itemView) {
            super(itemView);
            wineImageView = itemView.findViewById(R.id.wineImageView);
            wineNameTextView = itemView.findViewById(R.id.wineNameTextView);
            wineDescriptionTextView = itemView.findViewById(R.id.wineDescriptionTextView);
        }
    }
}