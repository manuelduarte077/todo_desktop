package dev.donmanuel.cartoons.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Favorite {

    public static final String TYPE_SIMPSONS = "SIMPSONS";
    public static final String TYPE_FUTURAMA = "FUTURAMA";

    @PrimaryKey(autoGenerate = true)
    private long favoriteId;
    private int itemId;
    private String itemType; // "SIMPSONS" or "FUTURAMA"

    // Common fields
    private String title;
    private String description;

    // Simpsons specific fields
    private Integer season;
    private Integer episode;
    private Double rating;
    private String airDate;
    private String thumbnailUrl;

    // Futurama specific fields
    private String category;
    private String slogan;
    private Double price;
    private Integer stock;

    public Favorite() {
        // Required empty constructor for Room
    }

    // Constructor for Simpsons
    public Favorite(int itemId, String itemType, String title, String description,
                    Integer season, Integer episode, Double rating, String airDate, String thumbnailUrl) {
        this.itemId = itemId;
        this.itemType = itemType;
        this.title = title;
        this.description = description;
        this.season = season;
        this.episode = episode;
        this.rating = rating;
        this.airDate = airDate;
        this.thumbnailUrl = thumbnailUrl;
    }

    // Constructor for Futurama
    public Favorite(int itemId, String itemType, String title, String description,
                    String category, String slogan, Double price, Integer stock) {
        this.itemId = itemId;
        this.itemType = itemType;
        this.title = title;
        this.description = description;
        this.category = category;
        this.slogan = slogan;
        this.price = price;
        this.stock = stock;
    }

    // Static factory methods
    public static Favorite fromSimpsons(Simpsons simpsons) {
        return new Favorite(
            simpsons.getId(),
            TYPE_SIMPSONS,
            simpsons.getName(),
            simpsons.getDescription(),
            simpsons.getSeason(),
            simpsons.getEpisode(),
            simpsons.getRating(),
            simpsons.getAirDate(),
            simpsons.getThumbnailUrl()
        );
    }

    public static Favorite fromFuturama(Futurama futurama) {
        return new Favorite(
            futurama.getId(),
            TYPE_FUTURAMA,
            futurama.getTitle(),
            futurama.getDescription(),
            futurama.getCategory(),
            futurama.getSlogan(),
            futurama.getPrice(),
            futurama.getStock()
        );
    }

    // Getters and setters
    public long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public boolean isSimpsons() {
        return TYPE_SIMPSONS.equals(itemType);
    }

    public boolean isFuturama() {
        return TYPE_FUTURAMA.equals(itemType);
    }
}
