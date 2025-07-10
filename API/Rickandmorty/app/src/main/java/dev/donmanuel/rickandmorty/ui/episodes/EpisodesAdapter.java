package dev.donmanuel.rickandmorty.ui.episodes;

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
import dev.donmanuel.rickandmorty.model.Episode;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {

    private List<Episode> episodes = new ArrayList<>();
    private OnEpisodeClickListener listener;

    public interface OnEpisodeClickListener {
        void onEpisodeClick(Episode episode);
        void onFavoriteClick(Episode episode, boolean isFavorite);
    }

    public EpisodesAdapter(OnEpisodeClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_episode, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        Episode episode = episodes.get(position);
        holder.bind(episode);
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    public void addEpisodes(List<Episode> newEpisodes) {
        int startPosition = episodes.size();
        episodes.addAll(newEpisodes);
        notifyItemRangeInserted(startPosition, newEpisodes.size());
    }

    public void clear() {
        episodes.clear();
        notifyDataSetChanged();
    }

    public void updateFavoriteStatus(String episodeId, boolean isFavorite) {
        for (int i = 0; i < episodes.size(); i++) {
            Episode episode = episodes.get(i);
            if (String.valueOf(episode.getId()).equals(episodeId)) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    class EpisodeViewHolder extends RecyclerView.ViewHolder {
        private TextView episodeName;
        private TextView episodeCode;
        private TextView episodeAirDate;
        private TextView episodeCharacters;
        private ImageView favoriteButton;
        private boolean isFavorite = false;

        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            episodeName = itemView.findViewById(R.id.episodeName);
            episodeCode = itemView.findViewById(R.id.episodeCode);
            episodeAirDate = itemView.findViewById(R.id.episodeAirDate);
            episodeCharacters = itemView.findViewById(R.id.episodeCharacters);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEpisodeClick(episodes.get(position));
                }
            });

            favoriteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    isFavorite = !isFavorite;
                    updateFavoriteIcon();
                    listener.onFavoriteClick(episodes.get(position), isFavorite);
                }
            });
        }

        public void bind(Episode episode) {
            episodeName.setText(episode.getName());
            episodeCode.setText(episode.getEpisode());
            episodeAirDate.setText("Air Date: " + episode.getAirDate());

            int characterCount = episode.getCharacters() != null ? episode.getCharacters().size() : 0;
            episodeCharacters.setText("Characters: " + characterCount);
            
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
