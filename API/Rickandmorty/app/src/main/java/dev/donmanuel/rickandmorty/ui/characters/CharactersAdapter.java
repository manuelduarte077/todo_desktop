package dev.donmanuel.rickandmorty.ui.characters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import dev.donmanuel.rickandmorty.R;
import dev.donmanuel.rickandmorty.model.Character;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder> {

    private List<Character> characters = new ArrayList<>();
    private OnCharacterClickListener listener;

    public interface OnCharacterClickListener {
        void onCharacterClick(Character character);
        void onFavoriteClick(Character character, boolean isFavorite);
    }

    public CharactersAdapter(OnCharacterClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_character, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        Character character = characters.get(position);
        holder.bind(character);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
        notifyDataSetChanged();
    }

    public void addCharacters(List<Character> newCharacters) {
        int startPosition = characters.size();
        characters.addAll(newCharacters);
        notifyItemRangeInserted(startPosition, newCharacters.size());
    }

    public void clear() {
        characters.clear();
        notifyDataSetChanged();
    }

    public void updateFavoriteStatus(String characterId, boolean isFavorite) {
        for (int i = 0; i < characters.size(); i++) {
            Character character = characters.get(i);
            if (String.valueOf(character.getId()).equals(characterId)) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    class CharacterViewHolder extends RecyclerView.ViewHolder {
        private ImageView characterImage;
        private TextView characterName;
        private TextView characterStatus;
        private TextView characterSpecies;
        private ImageView favoriteButton;
        private boolean isFavorite = false;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            characterImage = itemView.findViewById(R.id.characterImage);
            characterName = itemView.findViewById(R.id.characterName);
            characterStatus = itemView.findViewById(R.id.characterStatus);
            characterSpecies = itemView.findViewById(R.id.characterSpecies);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCharacterClick(characters.get(position));
                }
            });

            favoriteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    isFavorite = !isFavorite;
                    updateFavoriteIcon();
                    listener.onFavoriteClick(characters.get(position), isFavorite);
                }
            });
        }

        public void bind(Character character) {
            characterName.setText(character.getName());
            characterStatus.setText("Status: " + character.getStatus());
            characterSpecies.setText("Species: " + character.getSpecies());

            Glide.with(itemView.getContext())
                    .load(character.getImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_menu_camera)
                            .error(R.drawable.ic_menu_camera))
                    .into(characterImage);
            
            // Set favorite status (this would be set from the fragment)
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
