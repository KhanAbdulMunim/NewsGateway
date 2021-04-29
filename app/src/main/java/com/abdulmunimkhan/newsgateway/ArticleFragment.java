package com.abdulmunimkhan.newsgateway;

import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class ArticleFragment extends Fragment {

    String urlGlobal;

    private static final String TAG = "ArticleFragment";

    private static final String ARG_SECTION_NUMBER = "article number";

    public ArticleFragment(){
        //empty constructor
    }

    static ArticleFragment newInstance(Article article, int index, int max) {

//        Log.d(TAG, article.getAuthor());

        ArticleFragment fragment = new ArticleFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("ARTICLE_DATA", article);
        bdl.putSerializable("INDEX", index);
        bdl.putSerializable("TOTAL_COUNT", max);
        fragment.setArguments(bdl);
        return fragment;
    }

//    public static ArticleFragment newInstance(int articleNumber) {
//
//        ArticleFragment fragment = new ArticleFragment();
//
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, articleNumber);
//        fragment.setArguments(args);
//
//        return fragment;
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Use the inflater passed in to build (inflate) the fragment
        View fragment_layout = inflater.inflate(R.layout.fragment_main, container, false);

        Bundle args = getArguments();
        if (args != null){
            final Article currentArticle = (Article) args.getSerializable("ARTICLE_DATA");
            if (currentArticle == null){
//                Log.d(TAG, "null returned");
                return null;
            }

            String urlToImage = currentArticle.getUrlToImage();
            String url = currentArticle.getUrl();
            urlGlobal = url;

            int index = args.getInt("INDEX");
            int total = args.getInt("TOTAL_COUNT");

            TextView title = fragment_layout.findViewById(R.id.titleText);
            title.setText(currentArticle.getTitle());
            Pattern patternTitle = Pattern.compile(currentArticle.getTitle());
            Linkify.addLinks(title, patternTitle, url);
//            Log.d(TAG, title.toString());

            TextView date = fragment_layout.findViewById(R.id.date);
            date.setText(currentArticle.getTime());

            TextView author = fragment_layout.findViewById(R.id.author);
            author.setText(currentArticle.getAuthor());

            TextView articleText = fragment_layout.findViewById(R.id.articleText);
            articleText.setText(currentArticle.getDescription());
            Pattern patternDescription = Pattern.compile(currentArticle.getDescription());
            Linkify.addLinks(articleText, patternDescription, url);

            TextView count = fragment_layout.findViewById(R.id.count);
            count.setText(String.format("%d of %d", index, total));

            ImageView image = fragment_layout.findViewById(R.id.image);
            Picasso.get().load(urlToImage)
                    .into(image,
                            new Callback() {
                                @Override
                                public void onSuccess() {
                                }
                                @Override
                                public void onError(Exception e) {
                                }
                            });

            return fragment_layout;
        } else{
            return null;
        }
    }
}
