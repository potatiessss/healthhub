package com.example.healthhub;

public class Article {
    private int id;
    private String title;
    private int image; // Assuming image is stored as a resource ID

    public Article(int id, String title, int image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}
