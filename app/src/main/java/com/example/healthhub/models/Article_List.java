package com.example.healthhub.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Article_List implements Parcelable{

    String title;
    String articleImage;  // Image URL as String

    String articleId;

    public Article_List(String title, String articleImage, String articleId) {
        this.title = title;
        this.articleImage = articleImage;
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleImage() {  // Return the articleImage URL as String
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public String getArticleId() {  // Return the articleImage URL as String
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    // Parcelable implementation
    protected Article_List(Parcel in) {
        title = in.readString();
        articleImage = in.readString();
        articleId = in.readString();
    }

    public static final Parcelable.Creator<Article_List> CREATOR = new Parcelable.Creator<Article_List>() {
        @Override
        public Article_List createFromParcel(Parcel in) {
            return new Article_List(in);
        }

        @Override
        public Article_List[] newArray(int size) {
            return new Article_List[size];
        }
    };

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(articleImage);
        dest.writeString(articleId);
    }

    public int describeContents() {
        return 0;
    }
}