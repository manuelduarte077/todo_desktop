package dev.donmanuel.cartoons.model;

import com.google.gson.annotations.SerializedName;

public class Simpsons {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("season")
    private int season;

    @SerializedName("episode")
    private int episode;

    @SerializedName("rating")
    private double rating;

    @SerializedName("airDate")
    private String airDate;

    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;

    public Simpsons() {
    }

    public Simpsons(int id, String name, String description, int season, int episode, double rating, String airDate, String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.season = season;
        this.episode = episode;
        this.rating = rating;
        this.airDate = airDate;
        this.thumbnailUrl = thumbnailUrl;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
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
}
