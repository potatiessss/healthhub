package com.example.healthhub.models;

public class SavedArticle {

    private String articleId;
    private String title;
    private String articleImage;    // Change image to String to store image URL

    // No-argument constructor (required by Firebase)
    public SavedArticle() {
    }

    // Parameterized constructor
    public SavedArticle(String articleId, String title,  String articleImage) {
        this.articleId = articleId;
        this.title = title;
        this.articleImage = articleImage;
    }

    // Getters

    public String getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }


    public String getArticleImage() {
        return articleImage;  // Getter for image URL
    }

}