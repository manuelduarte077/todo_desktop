package dev.donmanuel.fakeapi.models;

/**
 * Category model implementing BaseModel interface
 * Following the Liskov Substitution Principle
 */
public class Category implements BaseModel {
    private int id;
    private String name;
    private String image;

    // Getters y setters
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}