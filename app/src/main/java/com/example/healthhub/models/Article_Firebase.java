package com.example.healthhub.models;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Centralized class to retrieve Articles from Firebase
public class Article_Firebase {

    private static final String TAG = "Article_Firebase";

    // Fetch all articles from Firebase
    public static void fetchArticlesFromFirebase(ArticleDataCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference articlesRef = database.getReference("Articles");

        articlesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    List<Article> articles = new ArrayList<>();
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

                    callback.onDataFetched(articles);
                } else {
                    Log.w(TAG, "No articles found in Firebase");
                    callback.onDataFetched(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error fetching articles: " + databaseError.getMessage());
                callback.onDataFetchFailed(databaseError.toException());
            }
        });
    }

    // Save an article to a user's savedArticles in Firebase using the existing articleID
    public static void saveArticleToFirebase(String userId, String articleId, Article article) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userSavedArticlesRef = database.getReference("Registered Users").child(userId).child("savedArticles");

        userSavedArticlesRef.child(articleId).setValue(article).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Article saved successfully");
            } else {
                Log.e(TAG, "Error saving article");
            }
        });
    }

    // Unsave an article from a user's savedArticles
    public static void unsaveArticleFromFirebase(String userId, String articleId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userSavedArticlesRef = database.getReference("Registered Users").child(userId).child("savedArticles").child(articleId);

        userSavedArticlesRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Article unsaved successfully");
            } else {
                Log.e(TAG, "Error unsaving article");
            }
        });
    }

    // Callback interface to handle the result of data fetch
    public interface ArticleDataCallback {
        void onDataFetched(List<Article> articles);
        void onDataFetchFailed(Exception exception);
    }
}