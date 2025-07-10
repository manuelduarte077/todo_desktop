package dev.donmanuel.harrypotter.presentation.adapter;

import android.content.Context;
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

import dev.donmanuel.harrypotter.R;
import dev.donmanuel.harrypotter.data.model.Character;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    private List<Character> characters;
    private final Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Character character);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CharacterAdapter(Context context) {
        this.context = context;
        this.characters = new ArrayList<>();
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
        notifyDataSetChanged();
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

    class CharacterViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageCharacter;
        private final TextView textName;
        private final TextView textHouse;
        private final TextView textActor;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCharacter = itemView.findViewById(R.id.image_character);
            textName = itemView.findViewById(R.id.text_name);
            textHouse = itemView.findViewById(R.id.text_house);
            textActor = itemView.findViewById(R.id.text_actor);
        }

        public void bind(Character character) {
            textName.setText(character.getName());
            textHouse.setText(character.getHouse());
            textActor.setText(character.getActor());

            // Load image with Glide
            if (character.getImage() != null && !character.getImage().isEmpty()) {
                Glide.with(context)
                        .load(character.getImage())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.placeholder_character)
                                .error(R.drawable.placeholder_character))
                        .into(imageCharacter);
            } else {
                imageCharacter.setImageResource(R.drawable.placeholder_character);
            }
            
            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(characters.get(position));
                    }
                }
            });
        }
    }
}
