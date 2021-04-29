package com.abdulmunimkhan.newsgateway;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
//code working correctly
public class ArticleRunnable implements Runnable{

    private String TAG = "ArticleRunnable";
    private MainActivity mainActivity;
    String sourceID;

    ArticleRunnable(MainActivity ma, String sourceID){
        mainActivity = ma;
        this.sourceID = sourceID;
    }

    @Override
    public void run() {

        String apiKey = "26367432808e457b9ab6e9b32f4e9606";
        String url = "https://newsapi.org/v2/top-headlines?sources=" + sourceID + "&language=en&apiKey=" + apiKey;

        Uri.Builder buildURL = Uri.parse(url).buildUpon();
        String urlToUse = buildURL.build().toString();
        StringBuilder sb = new StringBuilder();

        try {
            java.net.URL urlFinal = new URL(urlToUse);
            HttpURLConnection connection = (HttpURLConnection) urlFinal.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent","");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, String.valueOf(connection.getResponseCode()));
                handleResults(null);
                return;
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            handleResults(null);
            return;
        }
        handleResults(sb.toString());
    }

    public void handleResults(String jsonString){
        final ArrayList<Article> articleList = parseJSON(jsonString);
//        for(int i = 0; i<articleList.size(); i++){
//            Log.d(TAG, articleList.get(i).getAuthor());
//            Log.d(TAG, articleList.get(i).getUrl());
//        }
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.setArticles(articleList);
            }
        });
    }

    private ArrayList<Article> parseJSON(String s) {
        ArrayList<Article> article = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(s);
            String articles = jsonObj.getString("articles");
            JSONArray jSources = new JSONArray(articles);
            for (int i = 0; i < jSources.length(); i++){
                //get data here


                JSONObject jObj = (JSONObject) jSources.get(i);

                String author = jObj.getString("author");

                String title = jObj.getString("title");

                String description = jObj.getString("description");

                String url = jObj.getString("url");

                String urlToImage = jObj.getString("urlToImage");

                String time = jObj.getString("publishedAt");

                Article articleObj = new Article(title, author, description, time, url, urlToImage);

                articleObj.setTitle("");
                articleObj.setAuthor("");
                articleObj.setDescription("");
                articleObj.setUrl("");
                articleObj.setUrlToImage("");
                articleObj.setTime("");

                articleObj.setTitle(title);
                articleObj.setAuthor(author);
                articleObj.setDescription(description);
                articleObj.setUrl(url);
                articleObj.setUrlToImage(urlToImage);
                articleObj.setTime(time);

                article.add(articleObj);
            }

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return article;
    }
}
