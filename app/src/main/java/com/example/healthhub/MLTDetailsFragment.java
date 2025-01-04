package com.example.healthhub;

import android.content.Context;
import android.os.Bundle; import androidx.annotation.NonNull; import androidx.annotation.Nullable; import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater; import android.view.View; import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton; import android.widget.ImageView; import android.widget.TextView;
import android.widget.Toast;

import com.example.healthhub.models.Product; import com.example.healthhub.models.Product_Firebase; import com.squareup.picasso.Picasso;

import java.util.List;

public class MLTDetailsFragment extends Fragment {

    private CartManager cartManager;
    private String productId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_l_t_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView productName = view.findViewById(R.id.product_name);
        TextView productCategory = view.findViewById(R.id.product_category);
        TextView productPrice = view.findViewById(R.id.product_price);
        TextView usefulForTextDetails = view.findViewById(R.id.useful_for_text_details);
        TextView dosageTextDetails = view.findViewById(R.id.dosage_text_details);
        TextView postageTextDetails = view.findViewById(R.id.postage_text_details);
        ImageView productImage = view.findViewById(R.id.product_image);
        Button addToCartButton = view.findViewById(R.id.add_to_cart_button);

        cartManager = new CartManager();

        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString("name");
            productId = args.getString("productId");

            // Fetch product from Firebase
            Product_Firebase.fetchProductsFromFirebase(new Product_Firebase.ProductDataCallback() {
                @Override
                public void onProductsFetched(List<Product> products) {
                    for (Product product : products) {
                        if (product.getName().equals(name)) {
                            productName.setText(product.getName());
                            productCategory.setText(product.getCategory());
                            productPrice.setText(String.valueOf(product.getPrice()));
                            usefulForTextDetails.setText(product.getUsefulFor());
                            dosageTextDetails.setText(product.getDosage());
                            postageTextDetails.setText(product.getPostage());

                            // Use Picasso to load the image from the URL
                            Picasso.get().load(product.getImage()).into(productImage);
                            break;
                        }
                    }
                }

                @Override
                public void onProductsFetchFailed(Exception exception) {
                    // Handle error if fetching fails
                }
            });
        }

        addToCartButton.setOnClickListener(v -> {
            Log.d("MLTDetailsFragment", "Add to cart button clicked for product ID: " + productId);
            if (productId != null) {
                cartManager.addToCart(productId);
                Toast.makeText(requireContext(), "Added to cart!", Toast.LENGTH_SHORT).show(); // Show the Toast message
            } else {
                Log.e("MLTDetailsFragment", "Product ID is null, cannot add to cart.");
            }
        });

        // Back button functionality
        ImageButton backButton = view.findViewById(R.id.back_button_MLTDetails);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());
    }
}
