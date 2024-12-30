package com.example.healthhub.models;

public class Product {

    private String name;
    private String category;
    private String price;
    private String usefulFor;
    private String dosage;
    private String postage;
    private int imageResId;

    public Product(String name, String category, String price, String usefulFor, String dosage, String postage, int imageResId) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.usefulFor = usefulFor;
        this.dosage = dosage;
        this.postage = postage;
        this.imageResId = imageResId;
    }

    // Getters
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getPrice() { return price; }
    public String getUsefulFor() { return usefulFor; }
    public String getDosage() { return dosage; }
    public String getPostage() { return postage; }
    public int getImageResId() { return imageResId; }

}
