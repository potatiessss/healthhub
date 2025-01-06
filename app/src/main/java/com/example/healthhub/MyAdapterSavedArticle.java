package com.example.healthhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.models.SavedArticle;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapterSavedArticle extends RecyclerView.Adapter<MyAdapterSavedArticle.SavedArticleViewHolder> {

    private Context context;
    private List<SavedArticle> savedArticleList;

    public MyAdapterSavedArticle(Context context, List<SavedArticle> savedArticleList) {
        this.context = context;
        this.savedArticleList = savedArticleList;
    }

    @NonNull
    @Override
    public SavedArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_article_card, parent, false);
        return new SavedArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedArticleViewHolder holder, int position) {
        SavedArticle savedArticle = savedArticleList.get(position);

        holder.nameTextView.setText(savedArticle.getTitle());
        Picasso.get().load(savedArticle.getArticleImage()).into(holder.articleImageView);
    }

    @Override
    public int getItemCount() {
        return savedArticleList.size();
    }

    public static class SavedArticleViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView articleImageView;

        public SavedArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_article_title);
            articleImageView = itemView.findViewById(R.id.img_article);
        }
    }
}
