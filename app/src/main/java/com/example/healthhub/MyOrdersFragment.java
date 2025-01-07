package com.example.healthhub;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.models.Order;
import com.example.healthhub.models.OrderItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment implements OrderAdapter.OnOrderClickListener {
    private static final String TAG = "MyOrdersFragment";
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orders;
    private TextView tvNoOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewOrders);
        tvNoOrders = view.findViewById(R.id.tvNoOrders);

        orders = new ArrayList<>();
        adapter = new OrderAdapter(orders, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadOrders();

        return view;
    }

    private void loadOrders() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Please log in first", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ordersRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Registered Users")
                .child(auth.getCurrentUser().getUid())
                .child("orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.clear();

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    try {
                        // Check if this order has an "items" child node
                        if (!orderSnapshot.hasChild("items")) {
                            Log.d(TAG, "Skipping order " + orderSnapshot.getKey() + " - no items found");
                            continue; // Skip this order and continue with the next one
                        }

                        String orderId = orderSnapshot.getKey();
                        Double totalAmount = orderSnapshot.child("totalAmount").getValue(Double.class);
                        String shippingAddress = orderSnapshot.child("shippingAddress").getValue(String.class);
                        String paymentMethod = orderSnapshot.child("paymentMethod").getValue(String.class);

                        // Get items
                        List<OrderItem> items = new ArrayList<>();
                        DataSnapshot itemsSnapshot = orderSnapshot.child("items");

                        // Additional check to ensure items node has children
                        if (!itemsSnapshot.hasChildren()) {
                            Log.d(TAG, "Skipping order " + orderId + " - items node is empty");
                            continue;
                        }

                        for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                            String productName = itemSnapshot.child("productName").getValue(String.class);
                            Long quantity = itemSnapshot.child("quantity").getValue(Long.class);
                            Double price = itemSnapshot.child("price").getValue(Double.class);

                            if (productName != null && quantity != null && price != null) {
                                items.add(new OrderItem(
                                        productName,
                                        quantity.intValue(),
                                        price
                                ));
                            }
                        }

                        // Only add the order if it has items and required fields
                        if (orderId != null && totalAmount != null && !items.isEmpty()) {
                            Order order = new Order(
                                    orderId,
                                    shippingAddress != null ? shippingAddress : "",
                                    paymentMethod != null ? paymentMethod : "",
                                    items,
                                    totalAmount
                            );
                            orders.add(order);
                            Log.d(TAG, "Added order: " + orderId + " with " + items.size() + " items");
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing order: " + e.getMessage());
                    }
                }

                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to load orders", error.toException());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load orders: " +
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI() {
        if (getContext() == null) return;

        if (orders.isEmpty()) {
            tvNoOrders.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoOrders.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onOrderClick(Order order) {
        if (order != null && getActivity() != null) {
            Bundle bundle = new Bundle();
            bundle.putString("orderId", order.getOrderId());

            OrderDetailsFragment fragment = new OrderDetailsFragment();
            fragment.setArguments(bundle);

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.emptyFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}