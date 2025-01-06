package com.example.healthhub.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Product_List implements Parcelable {

    String name;
    double price;
    String image;  // Image URL as String
    String productId;

    public Product_List(String name, double price, String image, String productId) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {  // Return the image URL as String
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductId() {  // Return the image URL as String
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    // Parcelable implementation
    protected Product_List(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        image = in.readString();
        productId = in.readString();
    }

    public static final Creator<Product_List> CREATOR = new Creator<Product_List>() {
        @Override
        public Product_List createFromParcel(Parcel in) {
            return new Product_List(in);
        }

        @Override
        public Product_List[] newArray(int size) {
            return new Product_List[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(image);
        dest.writeString(productId);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}