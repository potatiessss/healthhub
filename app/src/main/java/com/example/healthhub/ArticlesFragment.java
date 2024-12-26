package com.example.healthhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthhub.adapter.ArticleAdapter;
import com.example.healthhub.model.ArticleModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticlesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private List<ArticleModel> articleList;
    private TextView tabRecent, tabSaved;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles, container, false);

        // Find the RecyclerView and tabs
        recyclerView = view.findViewById(R.id.articlesRecyclerView);
        tabRecent = view.findViewById(R.id.tabRecent);
        tabSaved = view.findViewById(R.id.tabSaved);

        // Set RecyclerView properties
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        articleList = new ArrayList<>();
        adapter = new ArticleAdapter(getContext(), articleList);
        recyclerView.setAdapter(adapter);

        // Set tab listeners
        setTabListeners();

        return view;

    }

    private void setTabListeners() {
        // 'Recent' tab click listener
        tabRecent.setOnClickListener(v -> {
            tabRecent.setTextColor(ContextCompat.getColor(requireContext(), R.color.activeTabColor));  // Active color
            tabSaved.setTextColor(ContextCompat.getColor(requireContext(), R.color.inactiveTabColor));   // Inactive color

            // Update RecyclerView data for recent articles
            updateRecyclerViewData("recent");
        });

        // 'Saved' tab click listener
        tabSaved.setOnClickListener(v -> {
            tabSaved.setTextColor(ContextCompat.getColor(requireContext(), R.color.activeTabColor));    // Active color
            tabRecent.setTextColor(ContextCompat.getColor(requireContext(), R.color.inactiveTabColor));    // Inactive color

            // Update RecyclerView data for saved articles
            updateRecyclerViewData("saved");
        });
    }


    private void updateRecyclerViewData(String tab) {
        // Clear the current list
        articleList.clear();

        // Populate the list based on the tab (Recent or Saved)
        if ("recent".equals(tab)) {
            articleList.add(new ArticleModel("Healthy Eating Tips", R.drawable.article_example));
            articleList.add(new ArticleModel("Exercise Guide", R.drawable.article_example));
        } else if ("saved".equals(tab)) {
            articleList.add(new ArticleModel("Nutrition for Athletes", R.drawable.article_example));
            articleList.add(new ArticleModel("Yoga Benefits", R.drawable.article_example));
        }

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArticlesFragment.
     */
    public static ArticlesFragment newInstance(String param1, String param2) {
        ArticlesFragment fragment = new ArticlesFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Retrieve parameters if needed
        }
    }



}
