package com.example.healthhub.models;

import java.util.List;

public class Order {
    private String orderId;
    private String shippingAddress;
    private String paymentMethod;
    private List<OrderItem> items;
    private double totalAmount;

    public Order() {} // Empty constructor for Firebase

    public Order(String orderId, String shippingAddress,
                 String paymentMethod, List<OrderItem> items, double totalAmount) {
        this.orderId = orderId;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    // Getters and setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}