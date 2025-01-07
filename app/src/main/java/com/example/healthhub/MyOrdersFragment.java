package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.models.Order;
import com.example.healthhub.models.OrderItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyOrdersFragment extends Fragment implements OrderAdapter.OnOrderClickListener {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // In a real app, you would fetch this from a database or API
        loadOrders();

        adapter = new OrderAdapter(orders, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadOrders() {
        orders = new ArrayList<>();
        // Sample data - replace with actual data from your database
        List<OrderItem> items1 = new ArrayList<>();
        items1.add(new OrderItem("Product 1", 2, 29.99));
        items1.add(new OrderItem("Product 2", 1, 19.99));

        List<OrderItem> items2 = new ArrayList<>();
        items2.add(new OrderItem("Product 3", 1, 39.99));

        orders.add(new Order("ORD001", new Date(), "123 Main St, City, State 12345",
                "Credit Card", items1, 79.97));
        orders.add(new Order("ORD002", new Date(), "456 Oak St, City, State 12345",
                "Online Banking", items2, 39.99));
    }

    @Override
    public void onOrderClick(Order order) {
        Bundle bundle = new Bundle();
        bundle.putString("orderId", order.getOrderId());
        //Navigation.findNavController(getView()).navigate(R.id.action_myOrders_to_orderDetails, bundle);
    }
}
