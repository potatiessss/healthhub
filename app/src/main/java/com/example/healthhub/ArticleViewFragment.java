package com.example.healthhub;

import android.content.Context;
import android.os.Bundle; import androidx.annotation.NonNull; import androidx.annotation.Nullable; import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater; import android.view.View; import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton; import android.widget.ImageView; import android.widget.TextView;
import android.widget.Toast;

import com.example.healthhub.models.Product; import com.example.healthhub.models.Article_Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleViewFragment extends Fragment {

    private SavedArticleManager savedArticleManager;

    private String articleId;

    private com.example.healthhub.Article article;

    private String USER_ID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return inflater.inflate(R.layout.fragment_article_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView articleTitle = view.findViewById(R.id.articleTitle);
        ImageView articleImage = view.findViewById(R.id.articleImage);
        ImageButton saveButton = view.findViewById(R.id.savedButton);

        savedArticleManager = new SavedArticleManager();



        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            articleId = args.getString("articleId");

            // Fetch product from Firebase
            Article_Firebase.fetchArticlesFromFirebase(new Article_Firebase.ArticleDataCallback() {
                @Override
                public void onArticlesFetched(List<com.example.healthhub.Article> articles) {
                    for (com.example.healthhub.Article fetchedArticle : articles) {
                        if (fetchedArticle.getTitle().equals(title)) {
                            articleTitle.setText(fetchedArticle.getTitle());
                            article = fetchedArticle; // Set the article variable

                            // Use Picasso to load the image from the URL
                            Picasso.get().load(fetchedArticle.getArticleImage()).into(articleImage);
                            break;
                        }
                    }
                }

                @Override
                public void onArticlesFetchedFailed(Exception exception) {
                    // Handle error if fetching fails
                }
            });
        }

        saveButton.setOnClickListener(v -> {
            Log.d("ArticleViewFragment", "Save button clicked for product ID: " + articleId);
            if (articleId != null) {
                Article_Firebase.saveArticleToFirebase(USER_ID, articleId, article);
                savedArticleManager.saveArticle(articleId);
                Toast.makeText(requireContext(), "Article Saved!", Toast.LENGTH_SHORT).show(); // Show the Toast message
            } else {
                Log.e("ArticleViewFragment", "Article ID is null, cannot save.");
            }
        });


        // Back button functionality
        ImageButton backButton = view.findViewById(R.id.backButtonView);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());
    }
}
