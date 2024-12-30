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

import java.util.List;

//this class is for recyclerview purposes
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
        holder.priceTextView.setText(product.getPrice());
        holder.productImageView.setImageResource(product.getImage());

        //imagebutton clicklistener to product details
        holder.productImageView.setOnClickListener(v -> {
            navigateToMLTDetails(product.getName(), "MEDICINE", product.getPrice(), "Useful info", "Dosage info", "Postage info", product.getImage());
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

    private void navigateToMLTDetails(String name, String category, String price, String usefulFor, String dosage, String postage, int imageResId) {
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("category", category);
            bundle.putString("price", price);
            bundle.putString("usefulFor", usefulFor);
            bundle.putString("dosage", dosage);
            bundle.putString("postage", postage);
            bundle.putInt("imageResId", imageResId);

            MLTDetailsFragment fragment = new MLTDetailsFragment();
            fragment.setArguments(bundle);

            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
