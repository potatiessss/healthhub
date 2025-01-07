package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class PurchaseCompleteFragment extends Fragment {
    private Button btnViewOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchase_complete, container, false);

        // Initialize the button
        btnViewOrders = view.findViewById(R.id.btnViewOrders);

        // Set an OnClickListener for the "View Orders" button
        btnViewOrders.setOnClickListener(v -> {
            // Pop the fragment stack 3 times when the button is clicked
            popBackStack(3);
        });

        return view;
    }

    private void popBackStack(int count) {
        // Get the FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Pop the fragments from the back stack 'count' times
        for (int i = 0; i < count; i++) {
            fragmentManager.popBackStack();
        }
    }
}
