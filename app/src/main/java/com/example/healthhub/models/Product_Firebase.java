package com.example.healthhub.models;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Product_Firebase {

    private static final String TAG = "Product_Firebase";

    public static void fetchProductsFromFirebase(ProductDataCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("Products");

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    List<Product> products = new ArrayList<>();
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        try {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null) {
                                Log.d(TAG, "Fetched product: " + product.getName());
                                products.add(product);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing product data: ", e);
                        }
                    }
                    // Log the total number of products fetched
                    Log.d(TAG, "Total products fetched: " + products.size());
                    callback.onProductsFetched(products);
                } else {
                    Log.w(TAG, "No products found in Firebase");
                    callback.onProductsFetched(new ArrayList<>());
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                Log.e(TAG, "Error fetching products: " + databaseError.getMessage());
                callback.onProductsFetchFailed(databaseError.toException());
            }
        });
    }

    // Callback interface to handle the result
    public interface ProductDataCallback {
        void onProductsFetched(List<Product> products);
        void onProductsFetchFailed(Exception exception);
    }
}
