package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ArticleViewFragment extends Fragment {

    private TextView articleTitle;
    private ImageView articleImage;
    private ImageButton savedButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_view, container, false);

        articleTitle = rootView.findViewById(R.id.articleTitle);
        articleImage = rootView.findViewById(R.id.articleImage);
        savedButton = rootView.findViewById(R.id.savedButton);

        // Setup back button
        ImageButton backButton = rootView.findViewById(R.id.backButtonView);
        backButton.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        // Handle saved button click
        savedButton.setOnClickListener(v -> {
            // Handle the save functionality (e.g. change the button icon)
        });

        return rootView;
    }
}
