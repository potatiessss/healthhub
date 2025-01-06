package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.R;
import com.example.healthhub.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private FirebaseAuth firebaseAuth;
    private TextView noOrdersText;

    private DatabaseReference ordersRef;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_orders, container, false);

        // Initialize views
        ordersRecyclerView = rootView.findViewById(R.id.ordersRecyclerView);
        //noOrdersText = rootView.findViewById(R.id.noOrdersText);

        // Initialize the order list and set the RecyclerView
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersRecyclerView.setAdapter(orderAdapter);

        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize Firebase reference for the current user's orders
        ordersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("orders");

        // Retrieve orders from Firebase
        // fetchOrdersFromFirebase();

        return rootView;
    }

    /*firebaseAuth = FirebaseAuth.getInstance();
    userId = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;

    if (userId == null) {
        Toast.makeText(getContext(), "Please log in to view your cart", Toast.LENGTH_SHORT).show();
        return null; // Exit early if no user is logged in
    }

    private void fetchOrdersFromFirebase() {
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Order order = snapshot.getValue(Order.class);
                        if (order != null) {
                            orderList.add(order);
                        }
                    }
                    orderAdapter.notifyDataSetChanged();
                    // Hide the "No Orders" message if there are orders
                    noOrdersText.setVisibility(orderList.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    // Show "No Orders" message if there are no orders for the user
                    noOrdersText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }*/

    // OrderAdapter class to display orders in RecyclerView
    public static class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

        private List<Order> orderList;

        public OrderAdapter(List<Order> orderList) {
            this.orderList = orderList;
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_layout, parent, false);
            return new OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            Order order = orderList.get(position);

            // Bind order details to the product_layout
            holder.productName.setText(order.getName());
            holder.productCategory.setText(order.getCategory());
            holder.productPrice.setText("RM " + order.getPrice());

            // You can add image loading here if you have product image URLs
            // Picasso.get().load(order.getImage()).into(holder.productImage);
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        public static class OrderViewHolder extends RecyclerView.ViewHolder {

            TextView productName, productCategory, productPrice;

            public OrderViewHolder(View itemView) {
                super(itemView);
                productName = itemView.findViewById(R.id.product_name);
                productCategory = itemView.findViewById(R.id.product_category);
                productPrice = itemView.findViewById(R.id.product_price);
            }
        }
    }
}
