package com.starwings.app.smartserchapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.starwings.app.smartserchapp.adapter.BlogAdapter;
import com.starwings.app.smartserchapp.data.Blog;
import com.starwings.app.smartserchapp.links.ApiLinks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 29-01-2018.
 */

public class TipsListing extends AppCompatActivity {
    ListView lstblogs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_listing);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        lstblogs=(ListView)findViewById(R.id.lstTips);
        AsyncHttpClient tipsclient=new AsyncHttpClient();
        tipsclient.get(ApiLinks.baseLink + ApiLinks.tipsLink, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    parseFeed(responseBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                showFailedResponse(error);
            }
        });
    }
    private void showFailedResponse(Throwable error) {
        Log.e("ErrorClass",error.getClass().getName());
    }
    private void parseFeed(byte[] responseBody) throws Exception{

        ArrayList<Blog> blogslist=new ArrayList<Blog>();
        String feedString=new String(responseBody);
        JSONObject feedjson=new JSONObject(feedString);
        JSONArray feedArray=feedjson.getJSONArray("items");
        for(int i=0;i<feedArray.length();i++)
        {
            JSONObject blogitem=feedArray.getJSONObject(i);
            Blog temp=new Blog();
            temp.setTitle(blogitem.getString("title"));
            temp.setContent(blogitem.getString("content_html"));
            temp.setLink(blogitem.getString("url"));
            temp.setPublishDate(blogitem.getString("date_published"));
            blogslist.add(temp);

        }
        BlogAdapter blgadapter=new BlogAdapter(blogslist,this);
        lstblogs.setAdapter(blgadapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.mnhome:
                homeIntent();
                return true;
            case R.id.mnabout:
                showAboutPage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAboutPage() {
        Intent aboutIntent=new Intent(this,AboutPageScreen.class);
        startActivity(aboutIntent);
        finish();

    }
    private void homeIntent() {
        Intent homeIntent=new Intent(this,HomePageActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
