package dev.donmanuel.rickandmorty.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Favorite {

    @PrimaryKey
    @NonNull
    private String itemId;

    private String name;
    private String imageUrl;
    private String type; // "character", "location", or "episode"
    private String details; // Additional details about the favorite item

    public Favorite(@NonNull String itemId, String name, String imageUrl, String type, String details) {
        this.itemId = itemId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
        this.details = details;
    }

    @NonNull
    public String getItemId() {
        return itemId;
    }

    public void setItemId(@NonNull String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
