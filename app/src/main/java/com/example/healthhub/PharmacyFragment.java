package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import com.example.healthhub.models.Product;
import com.example.healthhub.models.Product_Firebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PharmacyFragment extends Fragment {
    private List<CardView> productCards;
    private List<ImageView> productImages;
    private List<TextView> productNames;
    private List<TextView> productPrices;

    public PharmacyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pharmacy, container, false);
        initializeViews(view);
        setupClickListeners(view);
        loadRandomProducts();
        return view;
    }

    private void initializeViews(View view) {
        productCards = new ArrayList<>();
        productImages = new ArrayList<>();
        productNames = new ArrayList<>();
        productPrices = new ArrayList<>();

        // Initialize product card views
        productCards.add(view.findViewById(R.id.product1Card));
        productCards.add(view.findViewById(R.id.product2Card));
        productCards.add(view.findViewById(R.id.product3Card));

        // Initialize product images
        productImages.add(view.findViewById(R.id.product1Image));
        productImages.add(view.findViewById(R.id.product2Image));
        productImages.add(view.findViewById(R.id.product3Image));

        // Initialize product names
        productNames.add(view.findViewById(R.id.product1Name));
        productNames.add(view.findViewById(R.id.product2Name));
        productNames.add(view.findViewById(R.id.product3Name));

        // Initialize product prices
        productPrices.add(view.findViewById(R.id.product1Price));
        productPrices.add(view.findViewById(R.id.product2Price));
        productPrices.add(view.findViewById(R.id.product3Price));
    }

    private void setupClickListeners(View view) {
        requireActivity().findViewById(R.id.bottomNavi).setVisibility(View.GONE);
        // Search button
        view.findViewById(R.id.search_menu).setOnClickListener(v -> navigateToSearch());

        // Category buttons
        view.findViewById(R.id.allCategoriesButton).setOnClickListener(v ->
                navigateToMLTList(null));
        view.findViewById(R.id.medicineButton).setOnClickListener(v ->
                navigateToMLTList("MEDICINE"));
        view.findViewById(R.id.labTestButton).setOnClickListener(v ->
                navigateToMLTList("LAB TEST"));
        view.findViewById(R.id.devicesButton).setOnClickListener(v ->
                navigateToMLTList("MEDICAL DEVICE"));

        // Order now buttons
        view.findViewById(R.id.orderNowButton1).setOnClickListener(v ->
                navigateToMLTList(null));
        view.findViewById(R.id.orderNowButton2).setOnClickListener(v ->
                navigateToMLTList(null));

        // See all button
        view.findViewById(R.id.seeAllButton).setOnClickListener(v ->
                navigateToMLTList(null));

        /*view.findViewById(R.id.product1Image).setOnClickListener(v ->
                navigateToMLTDetails(product));
        view.findViewById(R.id.product2Image).setOnClickListener(v ->
                navigateToMLTDetails(product));
        view.findViewById(R.id.product3Image).setOnClickListener(v ->
                navigateToMLTDetails(product));*/
    }



    private void loadRandomProducts() {
        Product_Firebase.fetchProductsFromFirebase(new Product_Firebase.ProductDataCallback() {
            @Override
            public void onProductsFetched(List<Product> products) {
                if (products.size() >= 3) {
                    // Shuffle the products list to get random products
                    List<Product> shuffledProducts = new ArrayList<>(products);
                    Collections.shuffle(shuffledProducts);

                    // Display the first 3 random products
                    for (int i = 0; i < 3; i++) {
                        final Product product = shuffledProducts.get(i);
                        final int index = i;

                        // Update UI on main thread
                        requireActivity().runOnUiThread(() -> {
                            displayProduct(product, index);
                            setupProductClickListener(product, productCards.get(index));
                        });
                    }
                }
            }

            @Override
            public void onProductsFetchFailed(Exception exception) {
                // Handle the error appropriately
            }
        });
    }

    private void displayProduct(Product product, int index) {
        // Load product image using Picasso
        Picasso.get()
                .load(product.getImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(productImages.get(index));


        // Set product name and price
        productNames.get(index).setText(product.getName());
        productPrices.get(index).setText(String.format("RM %.2f", product.getPrice()));
    }

    private void setupProductClickListener(Product product, CardView productCard) {
        productCard.setOnClickListener(v -> {
            requireActivity().findViewById(R.id.bottomNavi).setVisibility(View.GONE);
            MLTDetailsFragment fragment = new MLTDetailsFragment();
            Bundle args = new Bundle();
            args.putString("productId", product.getProductId());
            args.putString("name", product.getName());
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.emptyFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void navigateToSearch() {
        SearchFragment fragment = new SearchFragment();
        navigateToFragment(fragment);
    }

    private void navigateToMLTList(@Nullable String category) {
        MLTListFragment fragment = new MLTListFragment();
        if (category != null) {
            Bundle args = new Bundle();
            args.putString("category", category);
            fragment.setArguments(args);
        }
        navigateToFragment(fragment);
    }

    private void navigateToMLTDetails(Product product) {
        MLTDetailsFragment fragment = new MLTDetailsFragment();
        Bundle args = new Bundle();
        args.putString("productId", product.getProductId());
        args.putString("name", product.getName());
        args.putString("category", product.getCategory());
        args.putDouble("price", product.getPrice());
        args.putString("usefulFor", product.getUsefulFor());
        args.putString("dosage", product.getDosage());
        args.putString("postage", product.getPostage());
        args.putString("imageUrl", product.getImage());
        fragment.setArguments(args);
        navigateToFragment(fragment);
    }

    private void navigateToFragment(Fragment fragment) {
        requireActivity().findViewById(R.id.bottomNavi).setVisibility(View.GONE);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emptyFragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.bottomNavi).setVisibility(View.VISIBLE);
}
}