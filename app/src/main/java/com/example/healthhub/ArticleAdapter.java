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

import com.example.healthhub.models.Article_List;
import com.squareup.picasso.Picasso;

import java.util.List;

// This class is for RecyclerView purposes
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private Context context;
    private List<Article_List> articleList;

    public ArticleAdapter(Context context, List<Article_List> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    public ArticleAdapter(List<com.example.healthhub.models.Article> articles, Context context) {
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_article_card, parent, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article_List article = articleList.get(position);
        holder.nameTextView.setText(article.getTitle());

        // Use Picasso to load image from URL
        Picasso.get().load(article.getArticleImage()).into(holder.articleImageView);

        holder.articleImageView.setOnClickListener(v -> {
            navigateToArticleView(
                    article.getTitle(),
                    article.getArticleImage(),  // Pass the image URL as String
                    article.getArticleId() // Pass the article ID
            );
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView articleImageView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_article_title);
            articleImageView = itemView.findViewById(R.id.img_article);
        }
    }

    // Adjusted method to accept String for image URL instead of int
    private void navigateToArticleView(String title, String articleImage, String articleId) {
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("articleImage", articleImage);  // Store the image URL as String
            bundle.putString("articleId", articleId); // Store the article ID

            com.example.healthhub.ArticleViewFragment fragment = new com.example.healthhub.ArticleViewFragment();
            fragment.setArguments(bundle);

            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.emptyFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
