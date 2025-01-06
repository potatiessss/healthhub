package com.example.healthhub;

public class Article {
    private String articleId;  // The unique ID of the article
    private String title;      // Title of the article
    private String articleImage;  // Image URL for the article
    private boolean saved;

    public Article() {
    }

    public Article(String articleId, String title, String articleImage) {
        this.articleId = articleId;
        this.title = title;
        this.articleImage = articleImage;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }
    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

}