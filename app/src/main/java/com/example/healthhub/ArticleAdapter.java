package com.example.healthhub.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentActivity;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.healthhub.ArticleViewFragment;
import com.example.healthhub.R;
import com.example.healthhub.Article;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private Context context;
    private List<Article> articleList;
    private List<Article> originalArticles;
    private List<Article> filteredArticles;
    private OnItemClickListener listener;

    private DatabaseReference articleRef;

    public ArticleAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;  // Assign context for operations like image loading, etc.
        this.originalArticles = new ArrayList<>(articleList);
        this.filteredArticles = new ArrayList<>(articleList);
        this.articleRef = FirebaseDatabase.getInstance().getReference().child("articles");
    }


    // Fetch articles from Firebase Realtime Database
    private void fetchArticlesFromFirebase() {
        articleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    articleList.add(article);
                }
                originalArticles.addAll(articleList);
                filteredArticles.addAll(articleList);
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.articles_item_layout, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = filteredArticles.get(position);

        holder.title.setText(article.getTitle());

        // Use Picasso to load the image
        if (article.getArticleImage() != null && !article.getArticleImage().isEmpty()) {
            Picasso.get()
                    .load(article.getArticleImage()) // URL for the image
                    .placeholder(R.drawable.placeholder_image) // Placeholder while loading
                    .error(R.drawable.error_image) // Fallback if URL fails
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.placeholder_image); // Fallback if no image URL
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(article);
            }
        });// Set the click listener for the CardView
        holder.cardView.setOnClickListener(v -> {
            FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            ArticleViewFragment fragment = ArticleViewFragment.newInstance(
                    article.getArticleId(),
                    article.getTitle(),
                    article.getArticleImage()
            );
            transaction.replace(R.id.emptyFragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });


    }


    @Override
    public int getItemCount() {
        return filteredArticles.size();
    }

    // Filter method for search functionality
    public void filterArticles(String query) {
        filteredArticles.clear();
        if (query.isEmpty()) {
            filteredArticles.addAll(articleList); // If no query, show all articles
        } else {
            for (Article article : articleList) {
                if (article.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredArticles.add(article); // Add article if it matches query
                }
            }
        }
        notifyDataSetChanged(); // Notify RecyclerView to update
    }
    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        ImageButton savedButton;
        CardView cardView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.articleTitleTV);
            image = itemView.findViewById(R.id.articleImage);
            savedButton = itemView.findViewById(R.id.bookmarkButton);
            cardView = itemView.findViewById(R.id.articleCardView);
        }
    }

}