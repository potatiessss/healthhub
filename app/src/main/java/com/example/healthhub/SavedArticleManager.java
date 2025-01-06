package com.example.healthhub;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SavedArticleManager {
    private static final String TAG = "SavedArticleManager";
    private DatabaseReference savedArticleRef;
    private String userId;

    public SavedArticleManager() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser () != null ? firebaseAuth.getCurrentUser ().getUid() : null;
        savedArticleRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId).child("savedArticles");
        Log.d(TAG, "SavedArticleManager initialized for user: " + userId);
    }

    public void saveArticle(String articleId) {
        if (userId == null) {
            Log.e(TAG, "User is not logged in.");
            return;
        }

        Log.d(TAG, "Attempting to save article: " + articleId);
        savedArticleRef.child(articleId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                } else {
                }
            } else {
            }
        });
    }
}
