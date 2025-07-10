package dev.donmanuel.cartoons.ui.favorites.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dev.donmanuel.cartoons.R;
import dev.donmanuel.cartoons.model.Favorite;

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SIMPSONS = 0;
    private static final int TYPE_FUTURAMA = 1;

    private List<Favorite> favorites = new ArrayList<>();
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Favorite favorite);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Favorite favorite = favorites.get(position);
        return favorite.isSimpsons() ? TYPE_SIMPSONS : TYPE_FUTURAMA;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SIMPSONS) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_favorite_simpsons, parent, false);
            return new SimpsonsViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_favorite_futurama, parent, false);
            return new FuturamaViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Favorite favorite = favorites.get(position);

        if (holder instanceof SimpsonsViewHolder) {
            ((SimpsonsViewHolder) holder).bind(favorite);
        } else if (holder instanceof FuturamaViewHolder) {
            ((FuturamaViewHolder) holder).bind(favorite);
        }
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    // ViewHolder for Simpsons favorites
    class SimpsonsViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnailImageView;
        private TextView titleTextView;
        private TextView seasonEpisodeTextView;
        private TextView ratingTextView;
        private TextView descriptionTextView;
        private TextView airDateTextView;
        private ImageButton deleteButton;

        public SimpsonsViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.iv_thumbnail);
            titleTextView = itemView.findViewById(R.id.tv_episode_title);
            seasonEpisodeTextView = itemView.findViewById(R.id.tv_season_episode);
            ratingTextView = itemView.findViewById(R.id.tv_rating);
            descriptionTextView = itemView.findViewById(R.id.tv_description);
            airDateTextView = itemView.findViewById(R.id.tv_air_date);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(Favorite favorite) {
            titleTextView.setText(favorite.getTitle());

            if (favorite.getSeason() != null && favorite.getEpisode() != null) {
                seasonEpisodeTextView.setText(String.format(Locale.getDefault(),
                        "Season %d, Episode %d", favorite.getSeason(), favorite.getEpisode()));
            }

            if (favorite.getRating() != null) {
                ratingTextView.setText(String.format(Locale.getDefault(),
                        "Rating: %.1f", favorite.getRating()));
            }

            descriptionTextView.setText(favorite.getDescription());

            // Format the air date if available
            if (favorite.getAirDate() != null) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date date = inputFormat.parse(favorite.getAirDate());
                    String formattedDate = outputFormat.format(date);
                    airDateTextView.setText("Air Date: " + formattedDate);
                } catch (ParseException e) {
                    airDateTextView.setText("Air Date: " + favorite.getAirDate());
                }
            }

            // Load thumbnail if available
            if (favorite.getThumbnailUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(favorite.getThumbnailUrl())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(thumbnailImageView);
            }

            // Set delete button click listener
            deleteButton.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(favorite);
                }
            });
        }
    }

    // ViewHolder for Futurama favorites
    class FuturamaViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView categoryTextView;
        private TextView descriptionTextView;
        private TextView sloganTextView;
        private TextView priceTextView;
        private TextView stockTextView;
        private ImageButton deleteButton;

        public FuturamaViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_title);
            categoryTextView = itemView.findViewById(R.id.tv_category);
            descriptionTextView = itemView.findViewById(R.id.tv_description);
            sloganTextView = itemView.findViewById(R.id.tv_slogan);
            priceTextView = itemView.findViewById(R.id.tv_price);
            stockTextView = itemView.findViewById(R.id.tv_stock);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(Favorite favorite) {
            titleTextView.setText(favorite.getTitle());

            if (favorite.getCategory() != null) {
                categoryTextView.setText(favorite.getCategory());
            }

            descriptionTextView.setText(favorite.getDescription());

            if (favorite.getSlogan() != null) {
                sloganTextView.setText(favorite.getSlogan());
            }

            // Format the price as currency if available
            if (favorite.getPrice() != null) {
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
                priceTextView.setText(numberFormat.format(favorite.getPrice()));
            }

            if (favorite.getStock() != null) {
                stockTextView.setText(String.valueOf(favorite.getStock()));
            }

            // Set delete button click listener
            deleteButton.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(favorite);
                }
            });
        }
    }
}
