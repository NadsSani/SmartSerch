package com.starwings.app.smartserchapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.starwings.app.smartserchapp.adapter.WPImageAdapter;
import com.starwings.app.smartserchapp.data.WPImages;
import com.starwings.app.smartserchapp.links.ApiLinks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 31-01-2018.
 */

public class WPStatusPage extends AppCompatActivity {
    GridView wpGrid;
    Button btn_img_more;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wp_status);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("SmartSerch - WhatsApp Status");
        wpGrid=(GridView)findViewById(R.id.imgGrid);
        btn_img_more=(Button)findViewById(R.id.btn_img_more);
        btn_img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreStatusPage();
            }
        });
        getImages();
    }

    private void showMoreStatusPage() {
        Intent imgIntent=new Intent(this,WPImageStatus.class);
        startActivity(imgIntent);
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
    private void getImages() {
        AsyncHttpClient wpImageclient=new AsyncHttpClient();
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        String apikey=appPreferences.getString("apikey","NA");
        if(!apikey.equals("NA")) {
            RequestParams params = new RequestParams();
            params.put("Authorization", apikey);
            wpImageclient.post(ApiLinks.baseLink + ApiLinks.wpstatusLink, params,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        processFeed(responseBody);
                    } catch (Exception e) {
                       Log.e("Error","Fetching Exception"+e.getClass().getName());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    ShowFailedResponse(error);
                }
            });
        }
    }

    private void ShowFailedResponse(Throwable error) {
    }

    private void processFeed(byte[] responseBody) throws Exception{
        String responseString=new String(responseBody);
        JSONObject responsejson=new JSONObject(responseString);
        JSONArray imgarray=responsejson.getJSONArray("Images");
        ArrayList<WPImages> lsImages=new ArrayList<WPImages>();
        for (int i=0;i<8;i++)
        {
            WPImages wimage=new WPImages();
            wimage.setSlno(imgarray.getJSONObject(i).getInt("slno"));
            wimage.setPath(imgarray.getJSONObject(i).getString("path"));
            lsImages.add(wimage);
        }
        WPImageAdapter imgAdapter=new WPImageAdapter(this,lsImages);
        wpGrid.setAdapter(imgAdapter);
    }

}
