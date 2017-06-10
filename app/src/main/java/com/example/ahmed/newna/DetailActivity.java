package com.example.ahmed.newna;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.example.ahmed.newna.newsItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
public class DetailActivity extends ActionBarActivity {

    private ImageView NewsImage;
    private TextView newsHeading;
    private TextView newsDesc;
    private TextView date;
    private TextView url;
    private TextView author;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // brg3 lil main acticity
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // byzbt el view
        setUpUIViews();

        // bageb el data mn el main activty
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String json = bundle.getString("newsItem");
            newsItem newsItem = new Gson().fromJson(json, newsItem.class);

            // byzhr el image
            ImageLoader.getInstance().displayImage(newsItem.getImageURL(), NewsImage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            });
            // by7ot el data eli fe el newsItem fe el textviews eli hatt3rd

            newsHeading.setText(newsItem.getNewsHeading());
            newsDesc.setText(newsItem.getNewsDesc());
            date.setText("Date: " + newsItem.getDate());
            //url.setText("Url:" + newsItem.getUrl());
            String t=newsItem.getUrl();
            String text = "<a href='"+t+"'> NEWS LINK </a>";
            url.setText(Html.fromHtml(text));
            author.setText("Author:" + newsItem.getAuthor());




        }

    }


    private void setUpUIViews() {

        NewsImage = (ImageView)findViewById(R.id.NewsImage);
        newsHeading = (TextView)findViewById(R.id.heading);
        newsDesc = (TextView)findViewById(R.id.newsDesc);
        date = (TextView)findViewById(R.id.date);
        url = (TextView)findViewById(R.id.url);
        url.setClickable(true);
        url.setMovementMethod(LinkMovementMethod.getInstance());



        author = (TextView)findViewById(R.id.author);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }
//3shan lama ados 3ala item fe el main activity yft7 el details
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
