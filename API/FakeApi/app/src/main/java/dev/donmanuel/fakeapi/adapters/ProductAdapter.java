package dev.donmanuel.fakeapi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.List;

import dev.donmanuel.fakeapi.R;
import dev.donmanuel.fakeapi.models.Product;

/**
 * ProductAdapter that extends AbstractAdapter
 * Following the Single Responsibility Principle and Liskov Substitution Principle
 */
public class ProductAdapter extends AbstractAdapter<Product, ProductAdapter.ProductViewHolder> {
    private final OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    // Constructor del adaptador
    public ProductAdapter(List<Product> products, OnProductClickListener listener) {
        super(products);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = getItem(position);
        if (product == null) return;

        holder.title.setText(product.getTitle());
        holder.price.setText("$" + product.getPrice());
        holder.category.setText(product.getCategory().getName());
        
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            Picasso.get().load(product.getImages().get(0)).into(holder.productImage);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    // ViewHolder para el RecyclerView
    static class ProductViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TextView title, price, category;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            category = itemView.findViewById(R.id.category);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}