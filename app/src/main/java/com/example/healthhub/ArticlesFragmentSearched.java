package com.example.healthhub;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.healthhub.models.Article_Firebase;
import com.example.healthhub.models.Article_List;

import java.util.ArrayList;
import java.util.List;

public class ArticlesFragmentSearched extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles_searched, container, false);

        // RecyclerView setup
        RecyclerView recyclerView = view.findViewById(R.id.articlesRecyclerView);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("filteredArticles")) {
            // Use the filtered products from the arguments
            List<Article_List> filteredArticles = arguments.getParcelableArrayList("filteredArticles");

            // Log to ensure products are received correctly
            Log.d(TAG, "Received filtered products: " + (filteredArticles != null ? filteredArticles.size() : 0));

            displayArticles(filteredArticles, recyclerView);

        } else {
            // Fetch products from Firebase if no filtered products are passed
            Article_Firebase.fetchArticlesFromFirebase(new Article_Firebase.ArticleDataCallback() {
                @Override
                public void onArticlesFetched(List<com.example.healthhub.Article> articles) {
                    List<Article_List> articleList = new ArrayList<>();
                    for (com.example.healthhub.Article article : articles) {
                        articleList.add(new Article_List(
                                article.getTitle(),
                                article.getArticleImage(),
                                article.getArticleId() // Pass the productId here
                        ));
                        Log.d(TAG, "Product added to list: " + article.getTitle());  // Log each product added to the list
                    }

                    // Log the total number of products added to the adapter
                    Log.d(TAG, "Total products added to RecyclerView: " + articleList.size());

                    displayArticles(articleList, recyclerView);
                }

                @Override
                public void onArticlesFetchedFailed(Exception exception) {
                    // Handle error fetching Articles
                    exception.printStackTrace();
                }
            });
        }

        ImageButton searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            ArticleSearchFragment fragment = new ArticleSearchFragment();

            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.emptyFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

            return view;
        }

    private void displayArticles(List<Article_List> articles, RecyclerView recyclerView){
        // Log the total number of products added to the adapter
        Log.d(TAG, "Total products to display in RecyclerView: " + articles.size());

        // Set up RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        ArticleAdapter adapter = new ArticleAdapter(requireContext(), articles);
        recyclerView.setAdapter(adapter);
    }


    }

