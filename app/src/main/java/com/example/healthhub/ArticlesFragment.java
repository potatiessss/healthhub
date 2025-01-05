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

    private boolean isShowingSavedArticles = false; // To track tab state

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new ArticleAdapter(articleList, getContext());
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("articles");

        // Fetch articles from Firebase
        fetchArticlesFromFirebase(recyclerView);

        // Set tab listeners
        setTabListeners();

        // Search button logic
        searchButton.setOnClickListener(v -> navigateToFragment(new ArticleSearchFragment()));

        // Handle item clicks for articles
        adapter.setOnItemClickListener(article -> {
            ArticleViewFragment articleViewFragment = ArticleViewFragment.newInstance(article.getArticleId(), article.getTitle(), article.getArticleImage());
            navigateToFragment(articleViewFragment);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentsContainer activity = (FragmentsContainer) requireActivity();
        activity.updateToolbar(
                "ARTICLES", // Title
                R.drawable.hamburger, // Left Icon
                R.drawable.search, // Right Icon
                v -> {
                    // Handle left icon click (e.g., open a drawer)
                },
                v -> {
                    // Handle right icon click (e.g., show notifications)
                }
        );
    }
    private void setTabListeners() {
        tabRecent.setOnClickListener(v -> {
            tabRecent.setTextColor(ContextCompat.getColor(requireContext(), R.color.activeTabColor));
            tabSaved.setTextColor(ContextCompat.getColor(requireContext(), R.color.inactiveTabColor));
            isShowingSavedArticles = false; // Set to recent
            fetchArticlesFromFirebase(); // Load recent articles
        });

        tabSaved.setOnClickListener(v -> {
            tabSaved.setTextColor(ContextCompat.getColor(requireContext(), R.color.activeTabColor));
            tabRecent.setTextColor(ContextCompat.getColor(requireContext(), R.color.inactiveTabColor));
            isShowingSavedArticles = true; // Set to saved
            fetchArticlesFromFirebase(); // Load saved articles
        });
    }

    private void fetchArticlesFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articleList.clear(); // Clear the current list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    if (article != null) {
                        // If showing saved articles, apply filtering logic here
                        if (isShowingSavedArticles && article.isSaved()) {
                            articleList.add(article); // Only add saved articles
                        } else if (!isShowingSavedArticles) {
                            articleList.add(article); // Add recent articles (all articles)
                        }
                    }
                }



                adapter.notifyDataSetChanged(); // Update the adapter with new data
                progressBar.setVisibility(View.GONE); // Hide progress bar
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to load articles: " + databaseError.getMessage());
                progressBar.setVisibility(View.GONE); // Hide progress bar in case of error
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

    private void fetchArticlesFromFirebase(RecyclerView rvArticles) {
        DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference("Articles");

        // Check if the adapter is already set
        if (adapter == null) {
            // Create adapter only if it hasn't been set before
            adapter = new ArticleAdapter(articleList, requireContext());
            rvArticles.setAdapter(adapter);
        }

        articlesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articleList.clear();  // Clear previous data

                // Loop through Firebase data and add to list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    if (article != null) {
                        articleList.add(article);  // Add article to the list
                    }
                }

                // Notify adapter about data change
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("FirebaseError", "Failed to load articles: " + databaseError.getMessage());
            }
        });
    }

}
