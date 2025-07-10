package dev.donmanuel.cartoons.ui.simpsons.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dev.donmanuel.cartoons.R;
import dev.donmanuel.cartoons.model.Simpsons;

public class SimpsonsAdapter extends RecyclerView.Adapter<SimpsonsAdapter.SimpsonsViewHolder> {

    private List<Simpsons> simpsonsList = new ArrayList<>();
    private OnItemClickListener listener;
    private OnFavoriteClickListener favoriteClickListener;
    private List<Integer> favoriteIds = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(Simpsons simpsons);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Simpsons simpsons, boolean isFavorite);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteClickListener = listener;
    }

    public void setFavoriteIds(List<Integer> favoriteIds) {
        this.favoriteIds = favoriteIds;
        notifyDataSetChanged();
    }

    public void setSimpsonsList(List<Simpsons> simpsonsList) {
        this.simpsonsList = simpsonsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SimpsonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_simpsons, parent, false);
        return new SimpsonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpsonsViewHolder holder, int position) {
        Simpsons simpsons = simpsonsList.get(position);
        holder.bind(simpsons, favoriteIds.contains(simpsons.getId()));
    }

    @Override
    public int getItemCount() {
        return simpsonsList.size();
    }

    class SimpsonsViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnailImageView;
        private TextView titleTextView;
        private TextView seasonEpisodeTextView;
        private TextView ratingTextView;
        private TextView descriptionTextView;
        private TextView airDateTextView;
        private ImageButton favoriteButton;

        public SimpsonsViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.iv_thumbnail);
            titleTextView = itemView.findViewById(R.id.tv_episode_title);
            seasonEpisodeTextView = itemView.findViewById(R.id.tv_season_episode);
            ratingTextView = itemView.findViewById(R.id.tv_rating);
            descriptionTextView = itemView.findViewById(R.id.tv_description);
            airDateTextView = itemView.findViewById(R.id.tv_air_date);
            favoriteButton = itemView.findViewById(R.id.btn_favorite);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(simpsonsList.get(position));
                }
            });
        }

        public void bind(Simpsons simpsons, boolean isFavorite) {
            titleTextView.setText(simpsons.getName());

            seasonEpisodeTextView.setText(String.format(Locale.getDefault(),
                    "Season %d, Episode %d", simpsons.getSeason(), simpsons.getEpisode()));

            ratingTextView.setText(String.format(Locale.getDefault(),
                    "Rating: %.1f", simpsons.getRating()));

            descriptionTextView.setText(simpsons.getDescription());

            // Format the air date if available
            if (simpsons.getAirDate() != null) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date date = inputFormat.parse(simpsons.getAirDate());
                    String formattedDate = outputFormat.format(date);
                    airDateTextView.setText("Air Date: " + formattedDate);
                } catch (ParseException e) {
                    airDateTextView.setText("Air Date: " + simpsons.getAirDate());
                }
            }

            // Load thumbnail if available
            if (simpsons.getThumbnailUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(simpsons.getThumbnailUrl())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(thumbnailImageView);
            }

            // Update favorite button state
            updateFavoriteButton(isFavorite);

            // Set favorite button click listener
            favoriteButton.setOnClickListener(v -> {
                boolean newFavoriteState = !isFavorite;
                updateFavoriteButton(newFavoriteState);
                if (favoriteClickListener != null) {
                    favoriteClickListener.onFavoriteClick(simpsons, newFavoriteState);
                }
            });
        }

        private void updateFavoriteButton(boolean isFavorite) {
            if (isFavorite) {
                favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
            }
        }
    }
}
