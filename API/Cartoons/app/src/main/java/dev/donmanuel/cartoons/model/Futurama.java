package dev.donmanuel.cartoons.model;

import com.google.gson.annotations.SerializedName;

public class Futurama {
    @SerializedName("id")
    private int id;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("category")
    private String category;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("slogan")
    private String slogan;
    
    @SerializedName("price")
    private Double price;
    
    @SerializedName("stock")
    private Integer stock;

    public Futurama() {
    }

    public Futurama(int id, String title, String category, String description, String slogan, Double price, Integer stock) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.slogan = slogan;
        this.price = price;
        this.stock = stock;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
