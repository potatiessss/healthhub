package com.example.healthhub.models;

import java.util.Date;
import java.util.List;

public class Order {
    private String orderId;
    private Date orderDate;
    private String shippingAddress;
    private String paymentMethod;
    private List<OrderItem> items;
    private double totalAmount;

    public Order(String orderId, Date orderDate, String shippingAddress,
                 String paymentMethod, List<OrderItem> items, double totalAmount) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public Date getOrderDate() { return orderDate; }
    public String getShippingAddress() { return shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public List<OrderItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
}
