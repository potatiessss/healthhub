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
import android.widget.Toast;
import com.example.healthhub.models.Product;
import com.example.healthhub.models.Product_Firebase;
import com.example.healthhub.models.Product_List;
import java.util.ArrayList;
import java.util.List;

public class MLTListFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private String category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_m_l_t_list, container, false);

        initViews(view);
        setupRecyclerView();
        loadProducts();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);

        // Get category from arguments if it exists
        Bundle args = getArguments();
        if (args != null) {
            category = args.getString("category");
        }

        // Setup click listeners
        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        ImageButton searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> navigateToSearch());
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // Add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
    }

    private void loadProducts() {
        Product_Firebase.fetchProductsFromFirebase(new Product_Firebase.ProductDataCallback() {
            @Override
            public void onProductsFetched(List<Product> products) {
                List<Product_List> productList = new ArrayList<>();

                for (Product product : products) {
                    // Filter by category if specified
                    if (category == null || product.getCategory().equals(category)) {
                        productList.add(new Product_List(
                                product.getName(),
                                product.getPrice(),  // Keep as double
                                product.getImage(),
                                product.getProductId()
                        ));
                    }
                }

                if (productList.isEmpty()) {
                    showNoProductsMessage();
                } else {
                    displayProducts(productList);
                }
            }

            @Override
            public void onProductsFetchFailed(Exception exception) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayProducts(List<Product_List> products) {
        if (!isAdded()) return;

        requireActivity().runOnUiThread(() -> {adapter = new MyAdapter(requireContext(), products);
            recyclerView.setAdapter(adapter);
        });
    }

    private void showNoProductsMessage() {
        if (isAdded()) {
            Toast.makeText(requireContext(), "No products found", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToSearch() {
        SearchFragment fragment = new SearchFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emptyFragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}