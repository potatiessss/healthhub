package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import com.bumptech.glide.Glide;
import com.example.healthhub.models.Product;
import com.example.healthhub.models.Product_Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private String userId;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView subtotalText, postageText, discountText, totalText;
    private double postage = 10.00; // Example fixed postage
    private double discount = 5.00; // Example fixed discount


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_new, container, false);

        // Initialize the drawer layout and navigation view
        drawerLayout = view.findViewById(R.id.drawerlayout);
        navigationView = view.findViewById(R.id.navigation_view);

        // Set up the hamburger button click to open the drawer
        ImageButton sideDrawerButton = view.findViewById(R.id.sidedrawer);
        sideDrawerButton.setOnClickListener(v -> {
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Button checkoutButton = view.findViewById(R.id.checkoutbutton);
        checkoutButton.setOnClickListener(v -> {
            if (productList.isEmpty()) {
                Toast.makeText(getContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                // Calculate the subtotal
                double subtotal = 0.0;
                for (Product product : productList) {
                    subtotal += product.getPrice() * product.getQuantity();
                }

                // Navigate to CheckoutFragment with data
                Bundle bundle = new Bundle();
                bundle.putDouble("subtotal", subtotal);
                bundle.putDouble("postage", postage);  // Pass the fixed postage value
                bundle.putDouble("discount", discount); // Pass the fixed discount value

                CheckoutFragment checkoutFragment = new CheckoutFragment();
                checkoutFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Home, checkoutFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;

        // Check if the user is authenticated
        if (userId == null) {
            // Handle case where the user is not authenticated
            // You could show a message or redirect them to login
            return view;
        }

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // Initialize TextViews
        subtotalText = view.findViewById(R.id.subtotal_price);
        postageText = view.findViewById(R.id.postage_price);
        discountText = view.findViewById(R.id.discount_price);
        totalText = view.findViewById(R.id.total_price);

        // Fetch products and calculate totals
        Product_Firebase.fetchProductsFromFirebase(new Product_Firebase.ProductDataCallback() {
            @Override
            public void onProductsFetched(List<Product> products) {
                calculateAndDisplayTotals(products);
            }

            @Override
            public void onProductsFetchFailed(Exception exception) {
                // Handle fetch error
            }
        });

        // Fetch products from Firebase using Product_Firebase class
        fetchProducts();

        return view;
    }

    // Method to fetch product data from Firebase
    private void fetchProducts() {
        Product_Firebase.fetchProductsFromFirebase(new Product_Firebase.ProductDataCallback() {
            @Override
            public void onProductsFetched(List<Product> products) {
                productList.clear(); // Clear previous products
                productList.addAll(products); // Add the new list of products
                productAdapter.notifyDataSetChanged(); // Update RecyclerView
            }

            @Override
            public void onProductsFetchFailed(Exception exception) {
                // Handle failure to fetch products (e.g., show an error message)
            }
        });
    }

    private void calculateAndDisplayTotals(List<Product> products) {
        double subtotal = 0.0;

        // Calculate subtotal
        for (Product product : products) {
            subtotal += product.getPrice() * product.getQuantity(); // Assuming Product has a quantity field
        }

        // Calculate total
        double total = subtotal + postage - discount;

        // Update UI
        subtotalText.setText(String.format("RM%.2f", subtotal));
        postageText.setText(String.format("RM%.2f", postage));
        discountText.setText(String.format("RM%.2f", discount));
        totalText.setText(String.format("RM%.2f", total));
    }

    // Adapter class for RecyclerView
    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

        private List<Product> productList;

        public ProductAdapter(List<Product> productList) {
            this.productList = productList;
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate the product layout
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_layout, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {
            // Bind the product data to the views
            Product product = productList.get(position);
            holder.productName.setText(product.getName());
            holder.productCategory.setText(product.getCategory());
            holder.productPrice.setText("RM" + product.getPrice());

            // Load product image using Glide
            Glide.with(holder.itemView.getContext())
                    .load(product.getImage()) // Image URL is stored in Firebase
                    .into(holder.productImage);

            holder.productName.setText(product.getName());
            holder.productCategory.setText(product.getCategory());
            holder.productPrice.setText("RM" + product.getPrice());

        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        // ViewHolder class
        public class ProductViewHolder extends RecyclerView.ViewHolder {

            TextView productName, productCategory, productPrice;
            ImageView productImage;

            public ProductViewHolder(View itemView) {
                super(itemView);
                productImage = itemView.findViewById(R.id.product_image);
                productName = itemView.findViewById(R.id.product_name);
                productCategory = itemView.findViewById(R.id.product_category);
                productPrice = itemView.findViewById(R.id.product_price);
            }
        }
        private void performCheckout() {
            // Validate the cart
            if (productList == null || productList.isEmpty()) {
                Toast.makeText(getContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate the total price
            double subtotal = 0.0;
            for (Product product : productList) {
                subtotal += product.getPrice() * product.getQuantity(); // Assuming Product has a quantity field
            }
            double total = subtotal + postage - discount;

            // Proceed to checkout (example: navigate to a payment or summary screen)
            navigateToCheckout(total);
        }

        private void navigateToCheckout(double totalAmount) {
            // Example: Navigate to a checkout summary fragment using Navigation Component
            Bundle bundle = new Bundle();
            bundle.putDouble("totalAmount", totalAmount);
            bundle.putSerializable("productList", new ArrayList<>(productList)); // Pass product list

            // Not working yet because haven't implemented in nav.xml
            /*NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_cartFragment_to_checkoutFragment, bundle);*/
        }

        private void clearCart() {
            productList.clear();
            productAdapter.notifyDataSetChanged();

            // Reset totals
            subtotalText.setText("RM0.00");
            postageText.setText(String.format("RM%.2f", postage));
            discountText.setText(String.format("RM%.2f", discount));
            totalText.setText("RM0.00");
        }
    }
}