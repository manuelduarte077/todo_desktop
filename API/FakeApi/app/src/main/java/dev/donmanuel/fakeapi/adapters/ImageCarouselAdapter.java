package dev.donmanuel.fakeapi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dev.donmanuel.fakeapi.R;
import dev.donmanuel.fakeapi.models.Image;

/**
 * ImageCarouselAdapter that extends AbstractAdapter
 * Following the Single Responsibility Principle and Liskov Substitution Principle
 */
public class ImageCarouselAdapter extends AbstractAdapter<Image, ImageCarouselAdapter.ImageViewHolder> {

    public ImageCarouselAdapter(List<Image> images) {
        super(images);
    }
    
    /**
     * Constructor that converts string URLs to Image objects
     */
    public ImageCarouselAdapter(List<String> imageUrls, boolean convertFromUrls) {
        super();
        if (imageUrls != null && convertFromUrls) {
            List<Image> images = new ArrayList<>();
            for (int i = 0; i < imageUrls.size(); i++) {
                images.add(new Image(i, imageUrls.get(i)));
            }
            this.items = images;
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_carousel, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = getItem(position);
        if (image != null && image.getUrl() != null) {
            Picasso.get().load(image.getUrl()).into(holder.imageView);
        }
    }

    static class ImageViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.carouselImage);
        }
    }
}