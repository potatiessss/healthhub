package com.example.healthhub.models;

import androidx.annotation.NonNull;

import com.example.healthhub.models.Article;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavedArticle_Firebase {

    private static final String TAG = "SavedArticle_Firebase";

    public static void fetchSavedArticleWithDetails(SavedArticleDataCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (userId == null) {
            callback.onSavedArticleFetchedFailed(new Exception("User is not logged in."));
            return;
        }

        DatabaseReference savedArticleRef = database.getReference("Registered Users").child(userId).child("savedArticles");
        DatabaseReference articleRef = database.getReference("Articles");

        savedArticleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot savedArticleSnapshot) {
                if (!savedArticleSnapshot.exists()) {
                    callback.onSavedArticleFetched(new ArrayList<>());
                    return;
                }

                List<SavedArticle> savedArticleItems = new ArrayList<>();

                for (DataSnapshot savedArticleItemSnapshot : savedArticleSnapshot.getChildren()) {
                    String articleId = savedArticleItemSnapshot.getKey();

                    if (articleId != null) {
                        articleRef.child(articleId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot articleSnapshot) {
                                if (articleSnapshot.exists()) {
                                    String title = articleSnapshot.child("title").getValue(String.class);
                                    String articleImage = articleSnapshot.child("articleImage").getValue(String.class);

                                    SavedArticle savedArticleItem = new SavedArticle(articleId, title, articleImage);
                                    savedArticleItems.add(savedArticleItem);


                                    // Notify callback once all cart items are processed
                                    if (savedArticleItems.size() == savedArticleSnapshot.getChildrenCount()) {
                                        callback.onSavedArticleFetched(savedArticleItems);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                callback.onSavedArticleFetchedFailed(databaseError.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onSavedArticleFetchedFailed(databaseError.toException());
            }
        });
    }

    public interface SavedArticleDataCallback {
        void onSavedArticleFetched(List<SavedArticle> savedArticleItems);

        void onSavedArticleFetchedFailed(Exception exception);
    }
}
