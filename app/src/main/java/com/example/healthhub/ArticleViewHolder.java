package com.example.healthhub;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameView, priceView;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.img_article);
        nameView = itemView.findViewById(R.id.tv_article_title);
    }
}
