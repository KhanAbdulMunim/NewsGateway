package com.abdulmunimkhan.newsgateway;

import android.content.Intent;
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
import java.util.List;
//code working correctly
public class SourceRunnable implements Runnable {

    private String TAG = "SourceRunnable";
    private MainActivity mainActivity;

    SourceRunnable(MainActivity ma){
        mainActivity = ma;
    }

    @Override
    public void run() {

        String category = "";
        String apiKey = "26367432808e457b9ab6e9b32f4e9606";
        String url = "https://newsapi.org/v2/sources?language=en&country=us&category=" + category + "&apiKey=" + apiKey;

        Uri.Builder buildURL = Uri.parse(url).buildUpon();
        String urlToUse = buildURL.build().toString();
        StringBuilder sb = new StringBuilder();

        try {
//            Log.d(TAG, urlToUse);
            java.net.URL urlFinal = new URL(urlToUse);

            HttpURLConnection connection = (HttpURLConnection) urlFinal.openConnection();

            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent","");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, String.valueOf(connection.getResponseCode()));
                Log.d(TAG, "not connected");
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
        final ArrayList<NewsSource> sourcesList = parseJSON(jsonString);
//        int length = sourcesList.size();
//        for(int i = 0; i<length; i++){
//            Log.d(TAG, sourcesList.get(i).getName());
//        }
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.addSources(sourcesList);    //passing official data back to mainActivity
            }
        });
    }

    private ArrayList parseJSON(String s) {
        ArrayList<NewsSource> news = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(s);
            String sources = jsonObj.getString("sources");
            JSONArray jSources = new JSONArray(sources);
            for (int i = 0; i < jSources.length(); i++) {
                JSONObject jObj = (JSONObject) jSources.get(i);
                String id = jObj.getString("id");
                String name = jObj.getString("name");
                String category = jObj.getString("category");
//                Log.d(TAG, "parseJSON: " + id + " " + name + " " + category);
                NewsSource newsSource = new NewsSource(id, name, category);
                newsSource.setName(name);
                newsSource.setId(id);
                newsSource.setCategory(category);
                news.add(newsSource);
            }

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return news;
    }
}

