package dev.donmanuel.rickandmorty.ui.favorites;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import dev.donmanuel.rickandmorty.R;
import dev.donmanuel.rickandmorty.database.Favorite;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private List<Favorite> favorites = new ArrayList<>();
    private final OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Favorite favorite);
        void onRemoveFavorite(Favorite favorite);
    }

    public FavoritesAdapter(OnFavoriteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite favorite = favorites.get(position);
        holder.bind(favorite);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
        notifyDataSetChanged();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private final ImageView favoriteImage;
        private final TextView favoriteName;
        private final TextView favoriteType;
        private final TextView favoriteDetails;
        private final ImageView removeButton;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteImage = itemView.findViewById(R.id.favoriteImage);
            favoriteName = itemView.findViewById(R.id.favoriteName);
            favoriteType = itemView.findViewById(R.id.favoriteType);
            favoriteDetails = itemView.findViewById(R.id.favoriteDetails);
            removeButton = itemView.findViewById(R.id.removeFavoriteButton);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onFavoriteClick(favorites.get(position));
                }
            });

            removeButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onRemoveFavorite(favorites.get(position));
                }
            });
        }

        public void bind(Favorite favorite) {
            favoriteName.setText(favorite.getName());
            favoriteType.setText(favorite.getType());
            favoriteDetails.setText(favorite.getDetails());

            // Load image if available
            if (!TextUtils.isEmpty(favorite.getImageUrl())) {
                favoriteImage.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(favorite.getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.ic_menu_camera)
                        .into(favoriteImage);
            } else {
                favoriteImage.setVisibility(View.GONE);
            }

            // Set icon based on type
            int typeIcon;
            switch (favorite.getType()) {
                case "character":
                    typeIcon = R.drawable.ic_menu_camera;
                    break;
                case "location":
                    typeIcon = R.drawable.ic_menu_gallery;
                    break;
                case "episode":
                    typeIcon = R.drawable.ic_menu_slideshow;
                    break;
                default:
                    typeIcon = R.drawable.ic_menu_camera;
            }

            // If no image URL, use the type icon
            if (TextUtils.isEmpty(favorite.getImageUrl())) {
                favoriteImage.setVisibility(View.VISIBLE);
                favoriteImage.setImageResource(typeIcon);
            }
        }
    }
}
