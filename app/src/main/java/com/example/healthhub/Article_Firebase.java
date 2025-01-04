package com.example.healthhub;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Article_Firebase {

    private static final String TAG = "Article_Firebase";
    private static final String ARTICLES_PATH = "Articles";
    private static final String USERS_PATH = "Registered Users";

    // Fetch all articles from Firebase
    public static void fetchArticlesFromFirebase(ArticleDataCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference articlesRef = database.getReference(ARTICLES_PATH);

        articlesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Article> articles = new ArrayList<>();
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    for (DataSnapshot articleSnapshot : dataSnapshot.getChildren()) {
                        try {
                            Article article = articleSnapshot.getValue(Article.class);
                            if (article != null) {
                                articles.add(article);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing article data: ", e);
                        }
                    }
                }
                callback.onDataFetched(articles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error fetching articles: " + databaseError.getMessage(), databaseError.toException());
                callback.onDataFetchFailed(databaseError.toException());
            }
        });
    }

    // Save an article to a user's savedArticles in Firebase
    public static void saveArticleToFirebase(String userId, String articleId, Article article) {
        if (userId == null || articleId == null || article == null) {
            Log.e(TAG, "Invalid arguments for saving article");
            return;
        }

        DatabaseReference userSavedArticlesRef = FirebaseDatabase.getInstance()
                .getReference(USERS_PATH).child(userId).child("savedArticles");

        userSavedArticlesRef.child(articleId).setValue(article).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Article saved successfully for user: " + userId);
            } else {
                Log.e(TAG, "Error saving article for user: " + userId, task.getException());
            }
        });
    }

    // Unsave an article from a user's savedArticles
    public static void unsaveArticleFromFirebase(String userId, String articleId) {
        if (userId == null || articleId == null) {
            Log.e(TAG, "Invalid arguments for unsaving article");
            return;
        }

        DatabaseReference userSavedArticlesRef = FirebaseDatabase.getInstance()
                .getReference(USERS_PATH).child(userId).child("savedArticles").child(articleId);

        userSavedArticlesRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Article unsaved successfully for user: " + userId);
            } else {
                Log.e(TAG, "Error unsaving article for user: " + userId, task.getException());
            }
        });
    }

    // Callback interface to handle the result of data fetch
    public interface ArticleDataCallback {
        void onDataFetched(List<Article> articles);
        void onDataFetchFailed(Exception exception);
    }
}
