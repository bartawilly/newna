package com.example.ahmed.newna;

import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Created by ahmed on 12/23/2016.
 */

public class newsItem {
    private String newsHeading;
    private String newsDesc;
    private String date;
    private String url;
    private String imageURL;
    private String author;



    public newsItem(String author, String newsHeading, String newsDesc, String url, String imageURL, String date) {
        this.author=author;
        this.date = date;
        this.imageURL = imageURL;
        this.newsDesc = newsDesc;
        this.newsHeading = newsHeading;
        this.url = url;


    }

    public String getNewsHeading() {
        return newsHeading;
    }

    public void setNewsHeading(String newsHeading) {
        this.newsHeading = newsHeading;
    }

    public String getNewsDesc() {
        return newsDesc;
    }

    public void setNewsDesc(String newsDesc) {
        this.newsDesc = newsDesc;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
