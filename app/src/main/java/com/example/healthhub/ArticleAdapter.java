package com.example.healthhub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


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

    public ArticleAdapter(Context context) {
        this.context = context;
        this.articleList = articleList;
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
        });
    }


    @Override
    public int getItemCount() {
        return filteredArticles.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        ImageButton savedButton;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.articleTitleTV);
            image = itemView.findViewById(R.id.articleImage);
            savedButton = itemView.findViewById(R.id.bookmarkButton);
        }
    }

    public void filterList(String query) {
        filteredArticles.clear();
        if (query.isEmpty()) {
            filteredArticles.addAll(originalArticles);
        } else {
            for (Article article : originalArticles) {
                if (article.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredArticles.add(article);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateList(List<Article> newList) {
        this.articleList = newList;
        notifyDataSetChanged();
    }

}
