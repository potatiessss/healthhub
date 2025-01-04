package com.example.healthhub;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.adapter.ArticleAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArticleSearchFragment extends Fragment {
    private EditText searchBar;
    private ImageButton searchButton, backButtonSearch;
    private RecyclerView searchResultsRecyclerView;
    private ArticleAdapter articleAdapter;
    private List<Article> allArticles;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_search, container, false);

        // Initialize views
        searchBar = rootView.findViewById(R.id.searchBar);
        searchButton = rootView.findViewById(R.id.searchButton);
        backButtonSearch = rootView.findViewById(R.id.backButtonSearch);
        searchResultsRecyclerView = rootView.findViewById(R.id.searchResultsRecyclerView);

        // Set up RecyclerView
        allArticles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(allArticles, requireContext());
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultsRecyclerView.setAdapter(articleAdapter);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("articles");

        // Fetch all articles from Firebase
        fetchArticlesFromFirebase();

        // Set up the search functionality
        setupSearchFunctionality();

        // Back button click listener
        backButtonSearch.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        return rootView;
    }

    private void fetchArticlesFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allArticles.clear(); // Clear the existing list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    if (article != null) {
                        allArticles.add(article); // Add article to the list
                    }
                }
                articleAdapter.notifyDataSetChanged(); // Notify the adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log the error or display a message
            }
        });
    }

    private void setupSearchFunctionality() {
        // TextWatcher for live search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterArticles(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Optional: Set up click listener for manual search if needed
        searchButton.setOnClickListener(v -> {
            String query = searchBar.getText().toString();
            filterArticles(query);
        });
    }

    private void filterArticles(String query) {
        List<Article> filteredList = new ArrayList<>();
        for (Article article : allArticles) {
            if (article.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(article);
            }
        }
        articleAdapter.updateList(filteredList); // Ensure your adapter has an updateList method
    }
}
