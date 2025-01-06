package com.example.healthhub;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.healthhub.models.Article_Firebase;
import com.example.healthhub.models.Article_List;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArticleSearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_search, container, false);

        EditText searchBar = view.findViewById(R.id.search_bar_product);
        ImageButton searchButton = view.findViewById(R.id.search_button_search);

        searchButton.setOnClickListener(v -> {
            String searchQuery = searchBar.getText().toString().trim();

            if (!TextUtils.isEmpty(searchQuery)) {
                // Log the search query for debugging
                Log.d("ArticleSearchFragment", "Search Query: " + searchQuery);
                searchArticles(searchQuery.toLowerCase(Locale.ROOT));
            }
        });

        ImageButton backButton = view.findViewById(R.id.back_button_search);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());



        return view;
    }

    private void searchArticles(String searchQuery) {
        // Fetch all articles from Firebase
        Article_Firebase.fetchArticlesFromFirebase(new Article_Firebase.ArticleDataCallback() {
            @Override
            public void onArticlesFetched(List<com.example.healthhub.Article> articles) {
                List<Article_List> filteredArticles = new ArrayList<>();

                for (com.example.healthhub.Article article : articles) {
                    // Log product details to verify values for debugging
                    Log.d("ArticleSearchFragment", "Checking product: " + article.getTitle());

                    // Check if the search query matches any field (case-insensitive)
                    if (article.getTitle().toLowerCase(Locale.ROOT).contains(searchQuery) ||
                            article.getArticleId().toLowerCase(Locale.ROOT).contains(searchQuery)) {

                        filteredArticles.add(new Article_List(
                                article.getTitle(),
                                article.getArticleImage(),
                                article.getArticleId()
                        ));
                    }
                }

                // Log filtered articles size for debugging
                Log.d("ArticleSearchFragment", "Filtered articles size: " + filteredArticles.size());

                // Pass filtered articles to ArticleViewFragment
                loadArticlesFragment(filteredArticles);
            }

            @Override
            public void onArticlesFetchedFailed(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private void loadArticlesFragment(List<Article_List> filteredArticles) {
        // Log to verify the articles being passed
        Log.d("ArticleSearchFragment", "Loading ArticleViewFragment with filtered articles.");

        // Create ArticleViewFragment instance
        ArticlesFragmentSearched articlesFragmentSearched = new ArticlesFragmentSearched();

        // Pass filtered articles to ArticleViewFragment via arguments
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("filteredArticles", (ArrayList<? extends Parcelable>) filteredArticles);
        articlesFragmentSearched.setArguments(bundle);

        // Replace current fragment with ArticleViewFragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.emptyFragment, articlesFragmentSearched);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}