package com.example.healthhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthhub.adapter.ArticleAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArticlesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private List<Article> articleList = new ArrayList<>();
    private TextView tabRecent, tabSaved;
    private ImageButton searchButton;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles, container, false);

        // Initialize UI components
        recyclerView = view.findViewById(R.id.articlesRecyclerView);
        tabRecent = view.findViewById(R.id.tabRecent);
        tabSaved = view.findViewById(R.id.tabSaved);
        searchButton = view.findViewById(R.id.searchButton);
        progressBar = view.findViewById(R.id.progressBar);

        // Set RecyclerView properties
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        articleList = new ArrayList<>();
        adapter = new ArticleAdapter(getContext(), articleList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("articles");

        // Fetch articles from Firebase
        fetchArticlesFromFirebase();

        // Set tab listeners
        setTabListeners();

        searchButton.setOnClickListener(v -> navigateToFragment(new ArticleSearchFragment()));

        // Handle item clicks
        adapter.setOnItemClickListener(article -> {
            ArticleViewFragment articleViewFragment = ArticleViewFragment.newInstance(article.getArticleId(), article.getTitle(), article.getArticleImage());
            navigateToFragment(articleViewFragment);
        });

        return view;
    }

    private void setTabListeners() {
        tabRecent.setOnClickListener(v -> {
            tabRecent.setTextColor(ContextCompat.getColor(requireContext(), R.color.activeTabColor));
            tabSaved.setTextColor(ContextCompat.getColor(requireContext(), R.color.inactiveTabColor));
            // Load recent articles logic
            fetchArticlesFromFirebase();
        });

        tabSaved.setOnClickListener(v -> {
            tabSaved.setTextColor(ContextCompat.getColor(requireContext(), R.color.activeTabColor));
            tabRecent.setTextColor(ContextCompat.getColor(requireContext(), R.color.inactiveTabColor));
            // Load saved articles logic
            fetchArticlesFromFirebase();
        });
    }

    private void fetchArticlesFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    if (article != null) {
                        articleList.add(article);
                    }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to load articles: " + databaseError.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void navigateToFragment(Fragment fragment) {
        Log.d("Navigation", "Navigating to: " + fragment.getClass().getSimpleName());
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.emptyFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static ArticlesFragment newInstance() {
        return new ArticlesFragment();
    }
}
