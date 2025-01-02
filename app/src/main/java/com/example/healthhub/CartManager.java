package com.example.healthhub;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CartManager {
    private static final String TAG = "CartManager";
    private DatabaseReference cartRef;
    private String userId;

    public CartManager() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser () != null ? firebaseAuth.getCurrentUser ().getUid() : null;
        cartRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId).child("cart");
        Log.d(TAG, "CartManager initialized for user: " + userId);
    }

    public void addToCart(String productId) {
        if (userId == null) {
            Log.e(TAG, "User  is not logged in.");
            return;
        }

        Log.d(TAG, "Attempting to add product to cart: " + productId);
        cartRef.child(productId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Checked cart for product: " + productId);
                if (task.getResult().exists()) {
                    // Product already in cart, update quantity
                    Long currentQuantity = task.getResult().getValue(Long.class);
                    if (currentQuantity != null) {
                        Log.d(TAG, "Current quantity for product " + productId + ": " + currentQuantity);
                        cartRef.child(productId).setValue(currentQuantity + 1)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Updated quantity for product: " + productId))
                                .addOnFailureListener(e -> Log.e(TAG, "Failed to update quantity: " + e.getMessage()));
                    }
                } else {
                    // Product not in cart, add with quantity 1
                    cartRef.child(productId).setValue(1)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Added product to cart: " + productId))
                            .addOnFailureListener(e -> Log.e(TAG, "Failed to add product to cart: " + e.getMessage()));
                }
            } else {
                Log.e(TAG, "Failed to check cart: " + task.getException().getMessage());
            }
        });
    }
}