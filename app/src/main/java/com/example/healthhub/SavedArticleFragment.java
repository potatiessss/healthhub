package com.example.healthhub;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;


import com.example.healthhub.models.SavedArticle;
import com.example.healthhub.models.SavedArticle_Firebase;

import java.util.List;

public class SavedArticleFragment extends Fragment {

    private static final String TAG = "SavedArticleFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_article, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.articlesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        SavedArticle_Firebase.fetchSavedArticleWithDetails(new SavedArticle_Firebase.SavedArticleDataCallback() {
            @Override
            public void onSavedArticleFetched(List<SavedArticle> savedArticleItems) {
                Log.d(TAG, "Cart fetched successfully with size: " + savedArticleItems.size());
                MyAdapterSavedArticle adapter = new MyAdapterSavedArticle(requireContext(), savedArticleItems);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSavedArticleFetchedFailed(Exception exception) {
                Log.e(TAG, "Failed to fetch cart: ", exception);
            }
        });

        return view;
    }
}
