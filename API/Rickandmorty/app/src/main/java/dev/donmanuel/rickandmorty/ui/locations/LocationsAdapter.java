package dev.donmanuel.rickandmorty.ui.locations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.donmanuel.rickandmorty.R;
import dev.donmanuel.rickandmorty.model.Location;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {

    private List<Location> locations = new ArrayList<>();
    private OnLocationClickListener listener;

    public interface OnLocationClickListener {
        void onLocationClick(Location location);
        void onFavoriteClick(Location location, boolean isFavorite);
    }

    public LocationsAdapter(OnLocationClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    public void addLocations(List<Location> newLocations) {
        int startPosition = locations.size();
        locations.addAll(newLocations);
        notifyItemRangeInserted(startPosition, newLocations.size());
    }

    public void clear() {
        locations.clear();
        notifyDataSetChanged();
    }

    public void updateFavoriteStatus(String locationId, boolean isFavorite) {
        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            if (String.valueOf(location.getId()).equals(locationId)) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView locationName;
        private TextView locationType;
        private TextView locationDimension;
        private TextView locationResidents;
        private ImageView favoriteButton;
        private boolean isFavorite = false;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.locationName);
            locationType = itemView.findViewById(R.id.locationType);
            locationDimension = itemView.findViewById(R.id.locationDimension);
            locationResidents = itemView.findViewById(R.id.locationResidents);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onLocationClick(locations.get(position));
                }
            });

            favoriteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    isFavorite = !isFavorite;
                    updateFavoriteIcon();
                    listener.onFavoriteClick(locations.get(position), isFavorite);
                }
            });
        }

        public void bind(Location location) {
            locationName.setText(location.getName());
            locationType.setText("Type: " + location.getType());
            locationDimension.setText("Dimension: " + location.getDimension());

            int residentCount = location.getResidents() != null ? location.getResidents().size() : 0;
            locationResidents.setText("Residents: " + residentCount);
            
            // Set favorite status
            updateFavoriteIcon();
        }

        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
            updateFavoriteIcon();
        }

        private void updateFavoriteIcon() {
            favoriteButton.setImageResource(isFavorite ? 
                    android.R.drawable.btn_star_big_on : 
                    android.R.drawable.btn_star_big_off);
        }
    }
}
