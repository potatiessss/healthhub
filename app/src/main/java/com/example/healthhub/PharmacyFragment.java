package com.example.healthhub;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.example.healthhub.models.Product;
import com.example.healthhub.models.Product_Dummy;

public class PharmacyFragment extends Fragment {

    public PharmacyFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pharmacy, container, false);

        ImageButton backButton = view.findViewById(R.id.hamburger_menu);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        ImageButton viewmoreButton = view.findViewById(R.id.viewmore_button);
        viewmoreButton.setOnClickListener(v -> {
            Product product = Product_Dummy.getProductByName("Ibuprofen (400mg)");
            if (product != null) {
                navigateToMLTList(product);
            }
        });

        ImageButton productButton1 = view.findViewById(R.id.popular_product1);
        productButton1.setOnClickListener(v -> {
            Product product = Product_Dummy.getProductByName("Ibuprofen (400mg)");
            if (product != null) {
                navigateToMLTDetails(product);
            }
        });

        ImageButton productButton2 = view.findViewById(R.id.popular_product2);
        productButton2.setOnClickListener(v -> {
            Product product = Product_Dummy.getProductByName("Pregnancy Test");
            if (product != null) {
                navigateToMLTDetails(product);
            }
        });

        ImageButton productButton3 = view.findViewById(R.id.popular_product3);
        productButton3.setOnClickListener(v -> {
            Product product = Product_Dummy.getProductByName("Salbutamol Inhaler (100mcg)");
            if (product != null) {
                navigateToMLTDetails(product);
            }
        });

        return view;
    }

    private void navigateToMLTDetails(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString("name", product.getName());
        bundle.putString("category", product.getCategory());
        bundle.putString("price", product.getPrice());
        bundle.putString("usefulFor", product.getUsefulFor());
        bundle.putString("dosage", product.getDosage());
        bundle.putString("postage", product.getPostage());
        bundle.putInt("imageResId", product.getImageResId());

        MLTDetailsFragment fragment = new MLTDetailsFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToMLTList(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString("name", product.getName());
        bundle.putString("category", product.getCategory());
        bundle.putString("price", product.getPrice());
        bundle.putString("usefulFor", product.getUsefulFor());
        bundle.putString("dosage", product.getDosage());
        bundle.putString("postage", product.getPostage());
        bundle.putInt("imageResId", product.getImageResId());

        MLTListFragment fragment = new MLTListFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
