package com.example.healthhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_card, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.tvTitle.setText(article.getTitle());

        // Load image using Picasso or Glide
        if (article.getArticleImage() != null && !article.getArticleImage().isEmpty()) {
            Picasso.get()
                    .load(article.getArticleImage())
                    .placeholder(R.drawable.placeholder_image) // Replace with a drawable resource for a placeholder
                    .error(R.drawable.error_image) // Replace with a drawable resource for error
                    .into(holder.imgArticle);
        } else {
            holder.imgArticle.setImageResource(R.drawable.placeholder_image);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void updateArticles(List<Article> updatedArticles) {
        this.articles = updatedArticles;
        notifyDataSetChanged(); // Notify the adapter that the dataset has changed
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgArticle;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_article_title);
            imgArticle = itemView.findViewById(R.id.img_article);
        }
    }
}
