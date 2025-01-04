package com.example.healthhub.tester;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.healthhub.R;
import com.example.healthhub.Article;
import com.example.healthhub.Article_Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticlesTester extends Fragment {

    private TextView articleTitleText;
    private ImageView articleImageView;
    private Button saveButton;
    private Button unsaveButton;
    private Button nextButton;  // New button for next article
    private Article currentArticle;
    private List<Article> articleList;  // List to store fetched articles
    private int currentArticleIndex = 0;  // To keep track of current article index
    private String USER_ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tester_articles, container, false);

        articleTitleText = rootView.findViewById(R.id.articleTitle);
        articleImageView = rootView.findViewById(R.id.articleImageView);
        saveButton = rootView.findViewById(R.id.saveButton);
        unsaveButton = rootView.findViewById(R.id.unsaveButton);
        nextButton = rootView.findViewById(R.id.next);  // Initialize next button

        // Get current user ID from Firebase Authentication
        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the logged-in user's ID

        // Fetch articles from Firebase
        fetchArticlesFromFirebase();

        // Set up save button to save the article
        saveButton.setOnClickListener(v -> {
            if (currentArticle != null) {
                String articleId = currentArticle.getArticleId();
                if (articleId != null && !articleId.isEmpty()) {
                    Article_Firebase.saveArticleToFirebase(USER_ID, articleId, currentArticle);
                    Toast.makeText(getContext(), "Article Saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to save article", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Set up unsave button to unsave the article
        unsaveButton.setOnClickListener(v -> {
            if (currentArticle != null) {
                String articleId = currentArticle.getArticleId();
                if (articleId != null && !articleId.isEmpty()) {
                    Article_Firebase.unsaveArticleFromFirebase(USER_ID, articleId);
                    Toast.makeText(getContext(), "Article Unsaved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to unsave article", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up next button to display the next article
        nextButton.setOnClickListener(v -> {
            if (articleList != null && currentArticleIndex < articleList.size() - 1) {
                currentArticleIndex++;
                currentArticle = articleList.get(currentArticleIndex); // Get next article
                displayArticle();
                updateNextButtonState();
            }
        });

        return rootView;
    }

    // Fetch articles from Firebase and display the first one
    private void fetchArticlesFromFirebase() {
        Article_Firebase.fetchArticlesFromFirebase(new Article_Firebase.ArticleDataCallback() {
            @Override
            public void onDataFetched(List<Article> articles) {
                if (articles != null && !articles.isEmpty()) {
                    articleList = articles;  // Store the fetched articles
                    currentArticle = articleList.get(0); // Display the first article
                    displayArticle();
                    updateNextButtonState();  // Update next button state
                } else {
                    Toast.makeText(getContext(), "No articles found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDataFetchFailed(Exception exception) {
                Toast.makeText(getContext(), "Failed to fetch articles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display article title and image
    private void displayArticle() {
        if (currentArticle != null) {
            articleTitleText.setText(currentArticle.getTitle());
            String imageUrl = currentArticle.getArticleImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(imageUrl).into(articleImageView);
            }
        }
    }

    // Update the state of the next button
    private void updateNextButtonState() {
        if (articleList != null && currentArticleIndex < articleList.size() - 1) {
            nextButton.setEnabled(true);  // Enable the next button if there are more articles
        } else {
            nextButton.setEnabled(false);  // Disable the next button if there are no more articles
        }
    }
}
