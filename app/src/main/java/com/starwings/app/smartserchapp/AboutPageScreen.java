package com.starwings.app.smartserchapp;

/**
 * Created by user on 25-10-2017.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.starwings.app.smartserchapp.links.ApiLinks;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutPageScreen extends AppCompatActivity {

    int viewcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simulateDayNight(/* DAY */ 0);

        AsyncHttpClient viewclient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        String apikey=appPreferences.getString("apikey","NA");
        params.put("Authorization",apikey);
        if(!apikey.equals("NA")) {
            viewclient.post(ApiLinks.baseLink + ApiLinks.countlink, params,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        parseResponse(responseBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                }
            });
        }

    }

    private void parseResponse(byte[] responseBody) throws Exception{
        String responseString=new String(responseBody);
        JSONObject countjson=new JSONObject(responseString);
        viewcount=countjson.getInt("viewcount");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logo)
                .setDescription("We offer\n" +
                        "\n" +
                        "> Listing of Business Cards\n" +
                        "> Search Business Cards\n" +
                        "> Interaction via phone call, email and whatsapp")
                .addItem(new Element().setTitle("Version 4.0.4"))
                .addItem(new Element().setTitle("Viewed Today : "+viewcount))
                .addGroup("Connect with us")
                .addEmail("info@smartserch.com")
                .addWebsite("http://smartserch.com/")
                .addPlayStore("com.starwings.app.smartserchapp")

                .addGroup("Marketed By")
                .addEmail("starwings1@gmail.com")

                .create();

        setContentView(aboutPage);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAboutPage() {
        Intent aboutIntent=new Intent(this,AboutPageScreen.class);
        startActivity(aboutIntent);
    }
    private void homeIntent() {
        Intent homeIntent=new Intent(this,HomePageActivity.class);
        startActivity(homeIntent);
        finish();
    }

    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}