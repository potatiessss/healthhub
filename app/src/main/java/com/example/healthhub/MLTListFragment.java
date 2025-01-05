package com.example.healthhub;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.healthhub.models.Product;
import com.example.healthhub.models.Product_Firebase;
import com.example.healthhub.models.Product_List;

import java.util.ArrayList;
import java.util.List;

public class MLTListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_m_l_t_list, container, false);

        // RecyclerView setup
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);

        // Check if filtered products were passed in the arguments
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("filteredProducts")) {
            // Use the filtered products from the arguments
            List<Product_List> filteredProducts = arguments.getParcelableArrayList("filteredProducts");

            // Log to ensure products are received correctly
            Log.d(TAG, "Received filtered products: " + (filteredProducts != null ? filteredProducts.size() : 0));

            displayProducts(filteredProducts, recyclerView);
        } else {
            // Fetch products from Firebase if no filtered products are passed
            Product_Firebase.fetchProductsFromFirebase(new Product_Firebase.ProductDataCallback() {
                @Override
                public void onProductsFetched(List<Product> products) {
                    List<Product_List> productList = new ArrayList<>();
                    for (Product product : products) {
                        productList.add(new Product_List(
                                product.getName(),
                                product.getPrice(),
                                product.getImage(),
                                product.getProductId() // Pass the productId here
                        ));
                        Log.d(TAG, "Product added to list: " + product.getName());  // Log each product added to the list
                    }

                    // Log the total number of products added to the adapter
                    Log.d(TAG, "Total products added to RecyclerView: " + productList.size());

                    displayProducts(productList, recyclerView);
                }

                @Override
                public void onProductsFetchFailed(Exception exception) {
                    // Handle error fetching products
                    exception.printStackTrace();
                }
            });
        }

        // Back button setup to go back
        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        // Search button setup to go to search fragment
        ImageButton searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            SearchFragment fragment = new SearchFragment();

            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.emptyFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void displayProducts(List<Product_List> products, RecyclerView recyclerView) {
        // Log the total number of products added to the adapter
        Log.d(TAG, "Total products to display in RecyclerView: " + products.size());

        // Set up RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        MyAdapter adapter = new MyAdapter(requireContext(), products);
        recyclerView.setAdapter(adapter);
    }
}
