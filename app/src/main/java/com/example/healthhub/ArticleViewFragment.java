package com.example.healthhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ArticleViewFragment extends Fragment {

    private static final String ARG_TITLE = "article_title";
    private static final String ARG_IMAGE_URL = "article_image_url";
    private static final String ARG_ARTICLE_ID = "article_id";

    private TextView articleTitle;
    private ImageView articleImage;
    private ImageButton savedButton, searchButtonView, backButton;
    private boolean isSaved = false;

    private DatabaseReference savedArticlesReference;

    public static ArticleViewFragment newInstance(String articleId, String title, String imageUrl) {
        ArticleViewFragment fragment = new ArticleViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ARTICLE_ID, articleId);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_view, container, false);

        // Initialize views
        articleTitle = rootView.findViewById(R.id.articleTitle);
        articleImage = rootView.findViewById(R.id.articleImage);
        savedButton = rootView.findViewById(R.id.savedButton);
        searchButtonView = rootView.findViewById(R.id.searchButtonView);
        backButton = rootView.findViewById(R.id.backButtonView);

        // Firebase reference for saved articles
        savedArticlesReference = FirebaseDatabase.getInstance().getReference("saved_articles");

        // Retrieve and display article data
        if (getArguments() != null) {
            String articleId = getArguments().getString(ARG_ARTICLE_ID);
            String title = getArguments().getString(ARG_TITLE);
            String imageUrl = getArguments().getString(ARG_IMAGE_URL);

            articleTitle.setText(title); // Update title
            loadImageWithPicasso(imageUrl, articleImage); // Update image

            setupSaveButton(articleId, title, imageUrl);
        }

        // Setup back button click listener
        backButton.setOnClickListener(v -> requireActivity().onBackPressed()); // This triggers the back press

        // Setup search button click listener
        searchButtonView.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.emptyFragment, new ArticleSearchFragment()); // Ensure emptyFragment is correct
            transaction.addToBackStack(null); // Add the transaction to the back stack
            transaction.commit();
        });

        return rootView;
    }

    private void setupSaveButton(String articleId, String title, String imageUrl) {
        // Check initial save state (implement Firebase logic if needed)
        savedButton.setOnClickListener(v -> {
            isSaved = !isSaved; // Toggle save state
            if (isSaved) {
                savedButton.setImageResource(R.drawable.bookmark_filled); // Set filled icon

                // Save article to Firebase using the Article class
                Article article = new Article(articleId, title, imageUrl);  // Use Article class here
                savedArticlesReference.child(articleId).setValue(article);
            } else {
                savedButton.setImageResource(R.drawable.bookmark); // Set unfilled icon

                // Remove article from Firebase
                savedArticlesReference.child(articleId).removeValue();
            }
        });
    }

    private void loadImageWithPicasso(String imageUrl, ImageView imageView) {
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // Placeholder while loading
                .error(R.drawable.error_image) // Error image
                .into(imageView);
    }
}
