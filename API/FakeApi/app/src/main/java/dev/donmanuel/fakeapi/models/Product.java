package dev.donmanuel.fakeapi.models;

import java.util.List;

/**
 * Product model implementing BaseModel interface
 * Following the Liskov Substitution Principle
 */
public class Product implements BaseModel {
    private int id;
    private String title;
    private int price;
    private String description;
    private Category category;
    private List<String> images;

    // Getters y setters
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}