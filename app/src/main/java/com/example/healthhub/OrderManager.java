package com.example.healthhub;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.healthhub.models.Order;
import com.example.healthhub.models.OrderItem;
import com.example.healthhub.models.CartItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderManager {
    private DatabaseReference ordersRef;
    private DatabaseReference cartRef;
    private String userId;

    public OrderManager() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);
        ordersRef = userRef.child("orders");
        cartRef = userRef.child("cart");
    }

    public interface OrderCallback {
        void onSuccess(String orderId);
        void onFailure(Exception e);
    }

    public void createOrder(String address, String paymentMethod, List<CartItem> cartItems,
                            double totalAmount, OrderCallback callback) {
        String orderId = ordersRef.push().getKey();

        // Convert CartItems to OrderItems
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(
                    cartItem.getProduct().getName(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice()
            );
            orderItems.add(orderItem);
        }

        Order order = new Order(
                orderId,
                address,
                paymentMethod,
                orderItems,
                totalAmount
        );

        ordersRef.child(orderId).setValue(order)
                .addOnSuccessListener(aVoid -> {
                    // Clear the cart after successful order creation
                    cartRef.removeValue()
                            .addOnSuccessListener(aVoid1 -> callback.onSuccess(orderId))
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }
}