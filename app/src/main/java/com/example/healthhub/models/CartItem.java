package com.example.healthhub.models;

public class CartItem {
    private String productId;
    private int quantity;
    private Product product;

    public CartItem() {
        // Required empty constructor for Firebase
    }

    public CartItem(String productId, int quantity, Product product) {
        this.productId = productId;
        this.quantity = quantity;
        this.product = product;
    }

    // Getters and setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
