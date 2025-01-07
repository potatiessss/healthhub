package com.example.healthhub;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.Locale;

public class OrderDetailsFragment extends Fragment {
    private static final String TAG = "OrderDetails";
    private TextView tvOrderId;
    private TextView tvShippingAddress;
    private TextView tvPaymentMethod;
    private TextView tvTotalAmount;
    private RecyclerView recyclerViewItems;
    ImageView backbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        backbutton = view.findViewById(R.id.back_button);
        backbutton.setOnClickListener(v -> requireActivity().onBackPressed());
        initializeViews(view);

        String orderId = getArguments() != null ? getArguments().getString("orderId") : null;
        if (orderId != null) {
            loadOrderDetails(orderId);
        }

        return view;
    }

    private void initializeViews(View view) {
        tvOrderId = view.findViewById(R.id.tvOrderId);
        tvShippingAddress = view.findViewById(R.id.tvShippingAddress);
        tvPaymentMethod = view.findViewById(R.id.tvPaymentMethod);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        recyclerViewItems = view.findViewById(R.id.recyclerViewItems);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void displayOrderDetails(Order order) {
        tvOrderId.setText("Order #" + order.getOrderId());
        tvShippingAddress.setText(order.getShippingAddress());
        tvPaymentMethod.setText(order.getPaymentMethod());
        tvTotalAmount.setText(String.format(Locale.getDefault(), "RM %.2f", order.getTotalAmount()));

        OrderItemAdapter adapter = new OrderItemAdapter(order.getItems());
        recyclerViewItems.setAdapter(adapter);
    }

    private void loadOrderDetails(String orderId) {
        if (orderId == null || getContext() == null) return;

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference orderRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Registered Users")
                .child(userId)
                .child("orders")
                .child(orderId);

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    // Get basic order details
                    String shippingAddress = snapshot.child("shippingAddress").getValue(String.class);
                    String paymentMethod = snapshot.child("paymentMethod").getValue(String.class);
                    Double totalAmount = snapshot.child("totalAmount").getValue(Double.class);

                    // Get items
                    List<OrderItem> items = new ArrayList<>();
                    DataSnapshot itemsSnapshot = snapshot.child("items");
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

                    Order order = new Order(
                            orderId,
                            shippingAddress != null ? shippingAddress : "",
                            paymentMethod != null ? paymentMethod : "",
                            items,
                            totalAmount != null ? totalAmount : 0.0
                    );

                    displayOrderDetails(order);

                } catch (Exception e) {
                    Log.e(TAG, "Error loading order details", e);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error loading order details", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to load order details", error.toException());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load order details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}