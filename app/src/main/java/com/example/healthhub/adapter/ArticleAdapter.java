package com.example.healthhub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.R;
import com.example.healthhub.model.ArticleModel;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private Context context;
    private List<ArticleModel> articleList;
    private List<ArticleModel> originalArticles;
    private List<ArticleModel> filteredArticles;

    public ArticleAdapter(Context context, List<ArticleModel> articleList) {
        this.context = context;
        this.articleList = articleList;
        this.originalArticles = new ArrayList<>(articleList);
        this.filteredArticles = new ArrayList<>(articleList);
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.articles_item_layout, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ArticleModel article = filteredArticles.get(position);
        holder.title.setText(article.getTitle());
        holder.image.setImageResource(article.getImageID());
    }

    @Override
    public int getItemCount() {
        return filteredArticles != null ? filteredArticles.size() : 0;
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.articleTitleTV);
            image = itemView.findViewById(R.id.articleImage);
        }
    }

    public void filterList(String query) {
        filteredArticles.clear();

        if (query.isEmpty()) {
            filteredArticles.addAll(originalArticles);
        } else {
            for (ArticleModel article : originalArticles) {
                if (article.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredArticles.add(article);
                }
            }
        }
        notifyDataSetChanged();
    }
}
