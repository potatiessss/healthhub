package com.example.healthhub;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthhub.models.Product;
import com.example.healthhub.models.Product_Firebase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MLTDetailsFragment extends Fragment {
    private static final String TAG = "MLTDetailsFragment";
    private CartManager cartManager;
    private String productId;
    private Product currentProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_l_t_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        loadProductData();
        setupClickListeners(view);
    }

    private void initializeViews(View view) {
        cartManager = new CartManager();
    }

    private void loadProductData() {
        Bundle args = getArguments();
        if (args == null) {
            Log.e(TAG, "No arguments provided to fragment");
            showError("Error loading product details");
            return;
        }

        productId = args.getString("productId");
        String name = args.getString("name");

        if (productId == null && name == null) {
            Log.e(TAG, "Neither productId nor name provided");
            showError("Error loading product details");
            return;
        }

        Product_Firebase.fetchProductsFromFirebase(new Product_Firebase.ProductDataCallback() {
            @Override
            public void onProductsFetched(List<Product> products) {
                Product matchedProduct = null;
                for (Product product : products) {
                    if ((productId != null && product.getProductId().equals(productId)) ||
                            (name != null && product.getName().equals(name))) {
                        matchedProduct = product;
                        break;
                    }
                }

                if (matchedProduct != null) {
                    currentProduct = matchedProduct;
                    updateUI(matchedProduct);
                } else {
                    Log.e(TAG, "Product not found in database");
                    showError("Product not found");
                }
            }

            @Override
            public void onProductsFetchFailed(Exception exception) {
                Log.e(TAG, "Failed to fetch product data", exception);
                showError("Failed to load product details");
            }
        });
    }

    private void updateUI(Product product) {
        if (!isAdded() || getView() == null) return;

        View view = getView();

        // Update text views
        setText(view, R.id.product_name, product.getName());
        setText(view, R.id.product_category, product.getCategory());
        setText(view, R.id.product_price, String.format("RM %.2f", product.getPrice()));
        setText(view, R.id.useful_for_text_details, product.getUsefulFor());
        setText(view, R.id.dosage_text_details, product.getDosage());
        setText(view, R.id.postage_text_details, product.getPostage());

        // Load image
        ImageView productImage = view.findViewById(R.id.product_image);
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Picasso.get()
                    .load(product.getImage())
                    .error(R.drawable.placeholder_image)
                    .into(productImage);
        }
    }

    private void setText(View view, int id, String text) {
        TextView textView = view.findViewById(id);
        if (textView != null && text != null) {
            textView.setText(text);
        }
    }

    private void setupClickListeners(View view) {
        // Add to Cart button
        Button addToCartButton = view.findViewById(R.id.add_to_cart_button);
        addToCartButton.setOnClickListener(v -> {
            if (currentProduct != null) {
                cartManager.addToCart(currentProduct.getProductId());
                Toast.makeText(requireContext(), "Added to cart!", Toast.LENGTH_SHORT).show();
            }
        });





        // Back button
        ImageButton backButton = view.findViewById(R.id.back_button_MLTDetails);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void navigateToCart() {
        CartFragment fragment = new CartFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.emptyFragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showError(String message) {
        if (isAdded()) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}