package com.example.healthhub;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.models.Product_List;
import com.squareup.picasso.Picasso;

import java.util.List;

// This class is for RecyclerView purposes
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ProductViewHolder> {

    private Context context;
    private List<Product_List> productList;

    public MyAdapter(Context context, List<Product_List> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product_view, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product_List product = productList.get(position);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.format("$%.2f", product.getPrice()));

        // Use Picasso to load image from URL
        Picasso.get().load(product.getImage()).into(holder.productImageView);

        holder.productImageView.setOnClickListener(v -> {
            navigateToMLTDetails(
                    product.getName(),
                    "MEDICINE", // Replace with actual category if available
                    product.getPrice(), // Pass the price as double
                    "Useful info", // Replace with actual useful info
                    "Dosage info", // Replace with actual dosage info
                    "Postage info", // Replace with actual postage info
                    product.getImage(),  // Pass the image URL as String
                    product.getProductId() // Pass the product ID
            );
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        ImageView productImageView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            priceTextView = itemView.findViewById(R.id.product_price);
            productImageView = itemView.findViewById(R.id.product_image);
        }
    }

    // Adjusted method to accept String for image URL instead of int
    private void navigateToMLTDetails(String name, String category, double price, String usefulFor, String dosage, String postage, String imageUrl, String productId) {
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("category", category);
            bundle.putDouble("price", price); // Store the price as double
            bundle.putString("usefulFor", usefulFor);
            bundle.putString("dosage", dosage);
            bundle.putString("postage", postage);
            bundle.putString("imageUrl", imageUrl);  // Store the image URL as String
            bundle.putString("productId", productId); // Store the product ID

            MLTDetailsFragment fragment = new MLTDetailsFragment();
            fragment.setArguments(bundle);

            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.emptyFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
