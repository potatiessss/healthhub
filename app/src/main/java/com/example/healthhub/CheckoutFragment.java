package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CheckoutFragment extends Fragment {

    private TextView subtotalValue, postageValue, discountValue, totalValue;
    private Button checkoutButton;

    private double subtotal = 100.00; // Example values, replace with actual data
    private double postage = 10.00;
    private double discount = 5.00;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        // Initialize Views
        // Initialize Views
        subtotalValue = view.findViewById(R.id.subtotal_value);
        postageValue = view.findViewById(R.id.postage_value);
        discountValue = view.findViewById(R.id.discount_value);
        totalValue = view.findViewById(R.id.total_value);
        checkoutButton = view.findViewById(R.id.checkout_button);

        // Get data from arguments
        if (getArguments() != null) {
            subtotal = getArguments().getDouble("subtotal", 0.0);
            postage = getArguments().getDouble("postage", 0.0);
            discount = getArguments().getDouble("discount", 0.0);
        }

        // Populate Summary
        updateSummary();

        // Handle Checkout Button Click
        checkoutButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Order Placed!", Toast.LENGTH_SHORT).show();
            // Navigate to a confirmation screen or perform order placement logic
        });

        return view;
    }

    private void updateSummary() {
        subtotalValue.setText(String.format("RM%.2f", subtotal));
        postageValue.setText(String.format("RM%.2f", postage));
        discountValue.setText(String.format("RM%.2f", discount));
        double total = subtotal + postage - discount;
        totalValue.setText(String.format("RM%.2f", total));
    }
}
