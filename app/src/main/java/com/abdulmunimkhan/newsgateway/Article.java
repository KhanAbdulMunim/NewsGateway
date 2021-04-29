package com.abdulmunimkhan.newsgateway;

import android.graphics.Bitmap;
import java.io.Serializable;
import java.util.ArrayList;

public class Article implements Serializable {

    private String title;
    private String author;
    private String url;
    private String urlToImage;
    private String description;
    private String time;

    public static ArrayList<Article> all = new ArrayList<>();

    public Article(String title, String author, String description, String time, String url, String urlToImage) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.time = time;
        this.url = url;
        this.urlToImage = urlToImage;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
}



















