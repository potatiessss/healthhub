package com.example.healthhub;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.example.healthhub.models.Product;
import com.example.healthhub.models.Product_Firebase;

import java.util.List;

public class PharmacyFragment extends Fragment {

    public PharmacyFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pharmacy, container, false);

        //ImageButton backButton = view.findViewById(R.id.hamburger_menu);
        //backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        ImageButton viewmoreButton = view.findViewById(R.id.viewmore_button);
        viewmoreButton.setOnClickListener(v -> {
            MLTListFragment fragment = new MLTListFragment();

            Bundle bundle = new Bundle();

            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.emptyFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });


        ImageButton productButton1 = view.findViewById(R.id.popular_product1);
        productButton1.setOnClickListener(v -> {
            Product_Firebase.fetchProductsFromFirebase(new Product_Firebase.ProductDataCallback() {
                @Override
                public void onProductsFetched(List<Product> products) {
                    Product product = findProductByName(products, "Ibuprofen (400mg)");
                    if (product != null) {
                        navigateToMLTDetails(product);
                    }
                }

                @Override
                public void onProductsFetchFailed(Exception exception) {
                    // Handle error if fetching fails
                }
            });
        });

        ImageButton productButton2 = view.findViewById(R.id.popular_product2);
        productButton2.setOnClickListener(v -> {
            Product_Firebase.fetchProductsFromFirebase(new Product_Firebase.ProductDataCallback() {
                @Override
                public void onProductsFetched(List<Product> products) {
                    Product product = findProductByName(products, "Pregnancy Test");
                    if (product != null) {
                        navigateToMLTDetails(product);
                    }
                }

                @Override
                public void onProductsFetchFailed(Exception exception) {
                    // Handle error if fetching fails
                }
            });
        });

        ImageButton productButton3 = view.findViewById(R.id.popular_product3);
        productButton3.setOnClickListener(v -> {
            Product_Firebase.fetchProductsFromFirebase(new Product_Firebase.ProductDataCallback() {
                @Override
                public void onProductsFetched(List<Product> products) {
                    Product product = findProductByName(products, "Salbutamol Inhaler (100mcg)");
                    if (product != null) {
                        navigateToMLTDetails(product);
                    }
                }

                @Override
                public void onProductsFetchFailed(Exception exception) {
                    // Handle error if fetching fails
                }
            });
        });

        return view;
    }

    private void navigateToMLTDetails(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString("name", product.getName());
        bundle.putString("category", product.getCategory());
        bundle.putDouble("price", product.getPrice());
        bundle.putString("usefulFor", product.getUsefulFor());
        bundle.putString("dosage", product.getDosage());
        bundle.putString("postage", product.getPostage());
        bundle.putString("imageUrl", product.getImage());
        bundle.putString("productId", product.getProductId());

        MLTDetailsFragment fragment = new MLTDetailsFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.emptyFragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToMLTList(Product product) {
        if (product == null) {
            Log.w("PharmacyFragment", "Product is null, cannot navigate");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("name", product.getName() != null ? product.getName() : "Unknown");
        bundle.putString("category", product.getCategory() != null ? product.getCategory() : "Unknown");
        bundle.putString("price", product.getPrice() != 0 ? String.valueOf(product.getPrice()) : "Unknown");
        bundle.putString("usefulFor", product.getUsefulFor() != null ? product.getUsefulFor() : "Unknown");
        bundle.putString("dosage", product.getDosage() != null ? product.getDosage() : "Unknown");
        bundle.putString("postage", product.getPostage() != null ? product.getPostage() : "Unknown");
        bundle.putString("imageUrl", product.getImage());
        bundle.putString("productId", product.getProductId() != null ? product.getProductId() : "Unknown");


        MLTListFragment fragment = new MLTListFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.emptyFragment, fragment)
                .addToBackStack(null)
                .commit();
    }


    private Product findProductByName(List<Product> products, String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
}

