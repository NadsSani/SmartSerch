package com.starwings.app.smartserchapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * Created by user on 29-01-2018.
 */

public class TipsDetailPage extends AppCompatActivity {

    WebView detailview;
    String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(getIntent().getStringExtra("blogtitle"));
        detailview=(WebView)findViewById(R.id.blogdetail);
        content=getIntent().getStringExtra("blogcontent");
        String data = "<html><body><h1>Hello, Javatpoint!</h1></body></html>";
        detailview.loadData(content, "text/html", "UTF-8");


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
