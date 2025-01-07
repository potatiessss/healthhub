package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class PurchaseCompleteFragment extends Fragment {
    private Button btnHome;
    private Button btnViewOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_complete, container, false);

        btnHome = view.findViewById(R.id.btnHome);
        btnViewOrders = view.findViewById(R.id.btnViewOrders);

        //btnHome.setOnClickListener(v ->
                //Navigation.findNavController(v).navigate(R.id.action_purchaseComplete_to_home));

        //btnViewOrders.setOnClickListener(v ->
                //Navigation.findNavController(v).navigate(R.id.action_purchaseComplete_to_myOrders));

        return view;
    }
}
