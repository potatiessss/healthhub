package com.example.healthhub.models;

public class Product {

    private String productId;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private String usefulFor;
    private String dosage;
    private String postage;  // Keep postage as a String
    private String image;    // Change image to String to store image URL

    // No-argument constructor (required by Firebase)
    public Product() {
    }

    // Parameterized constructor
    public Product(String productId, String name, String category, int quantity, double price, String usefulFor, String dosage, String postage, String image) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.usefulFor = usefulFor;
        this.dosage = dosage;
        this.postage = postage;
        this.image = image;
    }

    // Getters

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getUsefulFor() {
        return usefulFor;
    }

    public String getDosage() {
        return dosage;
    }

    public String getPostage() {
        return postage;
    }

    public String getImage() {
        return image;  // Getter for image URL
    }
}