package com.example.healthhub.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class Cart_Firebase {
    private static final String TAG = "Cart_Firebase";
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final String USERS_PATH = "Registered Users";
    private static final String CART_PATH = "cart";
    private static final String PRODUCTS_PATH = "Products";

    public interface CartCallback {
        void onCartItemsFetched(List<CartItem> cartItems);
        void onError(Exception e);
    }

    public static void fetchCartItems(CartCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = database.getReference(USERS_PATH)
                .child(userId)
                .child(CART_PATH);

        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot cartSnapshot) {
                List<CartItem> cartItems = new ArrayList<>();
                long totalItems = cartSnapshot.getChildrenCount();
                if (totalItems == 0) {
                    callback.onCartItemsFetched(cartItems);
                    return;
                }

                for (DataSnapshot productSnapshot : cartSnapshot.getChildren()) {
                    String productId = productSnapshot.getKey();
                    Integer quantity = productSnapshot.getValue(Integer.class);

                    if (productId != null && quantity != null) {
                        fetchProductDetails(productId, quantity, cartItems, totalItems, callback);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching cart items: " + error.getMessage());
                callback.onError(error.toException());
            }
        });
    }

    private static void fetchProductDetails(String productId, int quantity,
                                            List<CartItem> cartItems, long totalItems, CartCallback callback) {
        DatabaseReference productRef = database.getReference(PRODUCTS_PATH).child(productId);

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                if (product != null) {
                    CartItem cartItem = new CartItem(productId, quantity, product);
                    cartItems.add(cartItem);

                    // Only callback when we have all items
                    if (cartItems.size() == totalItems) {
                        callback.onCartItemsFetched(cartItems);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching product details: " + error.getMessage());
                callback.onError(error.toException());
            }
        });
    }

    public static void updateCartItemQuantity(String productId, int quantity) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = database.getReference(USERS_PATH)
                .child(userId)
                .child(CART_PATH)
                .child(productId);

        cartRef.setValue(quantity);
    }

    public static void removeFromCart(String productId) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = database.getReference(USERS_PATH)
                .child(userId)
                .child(CART_PATH)
                .child(productId);

        cartRef.removeValue();
    }
}
