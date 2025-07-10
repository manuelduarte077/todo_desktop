package dev.donmanuel.harrypotter.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wand implements Serializable {
    @SerializedName("wood")
    private String wood;
    
    @SerializedName("core")
    private String core;
    
    @SerializedName("length")
    private Double length;

    // Getters and setters
    public String getWood() {
        return wood;
    }

    public void setWood(String wood) {
        this.wood = wood;
    }

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}
