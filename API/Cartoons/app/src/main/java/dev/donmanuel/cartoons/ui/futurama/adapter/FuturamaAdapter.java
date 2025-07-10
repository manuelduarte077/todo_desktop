package dev.donmanuel.cartoons.ui.futurama.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.donmanuel.cartoons.R;
import dev.donmanuel.cartoons.model.Futurama;

public class FuturamaAdapter extends RecyclerView.Adapter<FuturamaAdapter.FuturamaViewHolder> {

    private List<Futurama> futuramaList = new ArrayList<>();
    private OnItemClickListener listener;
    private OnFavoriteClickListener favoriteClickListener;
    private List<Integer> favoriteIds = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(Futurama futurama);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Futurama futurama, boolean isFavorite);
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

    public void setFuturamaList(List<Futurama> futuramaList) {
        this.futuramaList = futuramaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FuturamaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_futurama, parent, false);
        return new FuturamaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FuturamaViewHolder holder, int position) {
        Futurama futurama = futuramaList.get(position);
        holder.bind(futurama, favoriteIds.contains(futurama.getId()));
    }

    @Override
    public int getItemCount() {
        return futuramaList.size();
    }

    class FuturamaViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView categoryTextView;
        private TextView descriptionTextView;
        private TextView sloganTextView;
        private TextView priceTextView;
        private TextView stockTextView;
        private ImageButton favoriteButton;

        public FuturamaViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_title);
            categoryTextView = itemView.findViewById(R.id.tv_category);
            descriptionTextView = itemView.findViewById(R.id.tv_description);
            sloganTextView = itemView.findViewById(R.id.tv_slogan);
            priceTextView = itemView.findViewById(R.id.tv_price);
            stockTextView = itemView.findViewById(R.id.tv_stock);
            favoriteButton = itemView.findViewById(R.id.btn_favorite);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(futuramaList.get(position));
                }
            });
        }

        public void bind(Futurama futurama, boolean isFavorite) {
            titleTextView.setText(futurama.getTitle());

            if (futurama.getCategory() != null) {
                categoryTextView.setText(futurama.getCategory());
            }

            descriptionTextView.setText(futurama.getDescription());

            if (futurama.getSlogan() != null) {
                sloganTextView.setText(futurama.getSlogan());
            }

            // Format the price as currency if available
            if (futurama.getPrice() != null) {
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
                priceTextView.setText(numberFormat.format(futurama.getPrice()));
            }

            if (futurama.getStock() != null) {
                stockTextView.setText(String.valueOf(futurama.getStock()));
            }

            // Update favorite button state
            updateFavoriteButton(isFavorite);

            // Set favorite button click listener
            favoriteButton.setOnClickListener(v -> {
                boolean newFavoriteState = !isFavorite;
                updateFavoriteButton(newFavoriteState);
                if (favoriteClickListener != null) {
                    favoriteClickListener.onFavoriteClick(futurama, newFavoriteState);
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
