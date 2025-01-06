package com.example.healthhub;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.healthhub.models.Product;
import com.example.healthhub.models.Product_Firebase;
import com.example.healthhub.models.Product_List;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        EditText searchBar = view.findViewById(R.id.search_bar_product);
        ImageButton searchButton = view.findViewById(R.id.search_button_search);

        searchButton.setOnClickListener(v -> {
            String searchQuery = searchBar.getText().toString().trim();

            if (!TextUtils.isEmpty(searchQuery)) {
                // Log the search query for debugging
                Log.d("SearchFragment", "Search Query: " + searchQuery);
                searchProducts(searchQuery.toLowerCase(Locale.ROOT));
            }
        });

        ImageButton backButton = view.findViewById(R.id.back_button_search);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    private void searchProducts(String searchQuery) {
        // Fetch all products from Firebase
        Product_Firebase.fetchProductsFromFirebase(new Product_Firebase.ProductDataCallback() {
            @Override
            public void onProductsFetched(List<Product> products) {
                List<Product_List> filteredProducts = new ArrayList<>();

                for (Product product : products) {
                    // Log product details to verify values for debugging
                    Log.d("SearchFragment", "Checking product: " + product.getName());

                    // Check if the search query matches any field (case-insensitive)
                    if (product.getName().toLowerCase(Locale.ROOT).contains(searchQuery) ||
                            product.getCategory().toLowerCase(Locale.ROOT).contains(searchQuery) ||
                            product.getProductId().toLowerCase(Locale.ROOT).contains(searchQuery) ||
                            product.getDosage().toLowerCase(Locale.ROOT).contains(searchQuery) ||
                            product.getPostage().toLowerCase(Locale.ROOT).contains(searchQuery) ||
                            product.getUsefulFor().toLowerCase(Locale.ROOT).contains(searchQuery)) {

                        filteredProducts.add(new Product_List(
                                product.getName(),
                                product.getPrice(),
                                product.getImage(),
                                product.getProductId()
                        ));
                    }
                }

                // Log filtered products size for debugging
                Log.d("SearchFragment", "Filtered products size: " + filteredProducts.size());

                // Pass filtered products to MLTListFragment
                loadMLTListFragment(filteredProducts);
            }

            @Override
            public void onProductsFetchFailed(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private void loadMLTListFragment(List<Product_List> filteredProducts) {
        // Log to verify the products being passed
        Log.d("SearchFragment", "Loading MLTListFragment with filtered products.");

        // Create MLTListFragment instance
        MLTListFragment mltListFragment = new MLTListFragment();

        // Pass filtered products to MLTListFragment via arguments
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("filteredProducts", (ArrayList<? extends Parcelable>) filteredProducts);
        mltListFragment.setArguments(bundle);

        // Replace current fragment with MLTListFragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.emptyFragment, mltListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}