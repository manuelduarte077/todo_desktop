package dev.donmanuel.fakeapi.models;

/**
 * Image model implementing BaseModel interface
 * Following the Single Responsibility Principle and Liskov Substitution Principle
 */
public class Image implements BaseModel {
    private int id;
    private String url;

    public Image(int id, String url) {
        this.id = id;
        this.url = url;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
