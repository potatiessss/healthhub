package com.example.healthhub;

import android.os.Bundle;
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
import com.example.healthhub.model.ArticleModel;

import java.util.ArrayList;
import java.util.List;

public class ArticleSearchFragment extends Fragment {
    private EditText searchBar;
    private ImageButton searchButton;
    private RecyclerView searchResultsRecyclerView;
    private ArticleAdapter articleAdapter;
    private List<ArticleModel> allArticles;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_search, container, false);

        searchBar = rootView.findViewById(R.id.searchBar);
        searchButton = rootView.findViewById(R.id.searchButton);
        searchResultsRecyclerView = rootView.findViewById(R.id.searchResultsRecyclerView);

        allArticles = new ArrayList<>();  // Add your article list here
        articleAdapter = new ArticleAdapter(getContext(), allArticles);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultsRecyclerView.setAdapter(articleAdapter);

        searchButton.setOnClickListener(v -> {
            String query = searchBar.getText().toString();
            articleAdapter.filterList(query);
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton backButtonSearch = view.findViewById(R.id.backButtonSearch);
        backButtonSearch.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }
}
