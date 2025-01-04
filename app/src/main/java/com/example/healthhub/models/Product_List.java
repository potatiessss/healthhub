package com.example.healthhub.models;

public class Product_List {

    String name;
    double price;
    String image;  // Image URL as String

    String productId;

    public Product_List(String name, double price, String image, String productId) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {  // Return the image URL as String
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductId() {  // Return the image URL as String
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}