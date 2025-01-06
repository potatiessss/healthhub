package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.models.Order;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderDetailsFragment extends Fragment {
    private TextView tvOrderId;
    private TextView tvOrderDate;
    private TextView tvShippingAddress;
    private TextView tvPaymentMethod;
    private TextView tvTotalAmount;
    private RecyclerView recyclerViewItems;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        initializeViews(view);

        String orderId = getArguments().getString("orderId");
        // In a real app, fetch order details from database using orderId
        Order order = findOrderById(orderId);

        if (order != null) {
            displayOrderDetails(order);
        }

        return view;
    }

    private void initializeViews(View view) {
        tvOrderId = view.findViewById(R.id.tvOrderId);
        tvOrderDate = view.findViewById(R.id.tvOrderDate);
        tvShippingAddress = view.findViewById(R.id.tvShippingAddress);
        tvPaymentMethod = view.findViewById(R.id.tvPaymentMethod);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        recyclerViewItems = view.findViewById(R.id.recyclerViewItems);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private Order findOrderById(String orderId) {
        // In a real app, this would fetch from your database
        // This is just a sample implementation
        return null; // Replace with actual implementation
    }

    private void displayOrderDetails(Order order) {
        tvOrderId.setText("Order #" + order.getOrderId());
        tvOrderDate.setText(dateFormat.format(order.getOrderDate()));
        tvShippingAddress.setText(order.getShippingAddress());
        tvPaymentMethod.setText(order.getPaymentMethod());
        tvTotalAmount.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalAmount()));

        OrderItemAdapter adapter = new OrderItemAdapter(order.getItems());
        recyclerViewItems.setAdapter(adapter);
    }
}
