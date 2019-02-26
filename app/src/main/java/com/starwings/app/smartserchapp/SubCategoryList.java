package com.starwings.app.smartserchapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.starwings.app.smartserchapp.adapter.SubCategoryAdapter;
import com.starwings.app.smartserchapp.data.Category;
import com.starwings.app.smartserchapp.links.ApiLinks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 24-10-2017.
 */

public class SubCategoryList extends AppCompatActivity implements View.OnClickListener {

    Category selectedCategory;
    ProgressBar progressLoad;
    ListView lstSubCategories;
    ArrayList<Category> subcategoryList;
    TextView txtNone;

    ViewFlipper flipper;
    private int mFlipping;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        selectedCategory=(Category)getIntent().getSerializableExtra("categoryselection");
        setTitle(selectedCategory.getCategoryName());

        progressLoad=(ProgressBar)findViewById(R.id.prgLoader);
        lstSubCategories=(ListView)findViewById(R.id.lstsubcategory);
        flipper = (ViewFlipper) findViewById(R.id.subcat_top_layout);
        txtNone=(TextView)findViewById(R.id.txtNone);

        loadGallery();
        fetchSubCategory();

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
    private void homeIntent() {
        Intent homeIntent=new Intent(this,HomePageActivity.class);
        startActivity(homeIntent);
        finish();
    }
    private void showAboutPage() {
        Intent aboutIntent=new Intent(this,AboutPageScreen.class);
        startActivity(aboutIntent);
        finish();
    }
    private void loadGalleryImages(byte[] responseBody) throws Exception {
        String galResponse=new String(responseBody);
        JSONArray galArray=new JSONArray(galResponse);
        for (int i=0;i<galArray.length();i++)
        {
            ImageView imgPhoto=new ImageView(this);
            imgPhoto.setAdjustViewBounds(true);
            imgPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this)
                    .load(ApiLinks.basegalLink+galArray.getJSONObject(i).getString("path"))
                    .into(imgPhoto);
            flipper.addView(imgPhoto);
        }
        if(mFlipping==0){
            /** Start Flipping */
            flipper.startFlipping();
            mFlipping=1;

        }
        else{
            /** Stop Flipping */
            flipper.stopFlipping();
            mFlipping=0;

        }
    }
    private void loadGallery()
    {
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        String apikey=appPreferences.getString("apikey","NA");

        if(!apikey.equals("NA"))
        {
            AsyncHttpClient galleryClient=new AsyncHttpClient();
            galleryClient.setResponseTimeout(50000);
            galleryClient.setConnectTimeout(50000);
            galleryClient.setTimeout(50000);
            RequestParams params=new RequestParams();
            params.put("Authorization",apikey);
            Log.e("API",ApiLinks.baseLink + ApiLinks.homePageGallery);
            galleryClient.post(this, ApiLinks.baseLink + ApiLinks.homePageGallery, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        Log.e("API","Response"+new String(responseBody ));
                        loadGalleryImages(responseBody);
                    } catch (Exception e) {
                        Log.e("API Exception",ApiLinks.baseLink + ApiLinks.homePageGallery);
                        Snackbar.make(lstSubCategories,"Failed to Load Ads."+e.getClass(),Snackbar.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    showFailure(error);
                }


            });
        }
    }
    private void showFailure(Throwable error) {

        Log.i("Error",error.getClass().getName());
    }

    public void fetchSubCategory()
    {

        subcategoryList=new ArrayList<Category>();
        progressLoad.setVisibility(View.VISIBLE);
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        String apikey=appPreferences.getString("apikey","NA");

        if(!apikey.equals("NA"))
        {
            AsyncHttpClient subCategoryClient=new AsyncHttpClient();
            RequestParams params=new RequestParams();
            params.put("Authorization",apikey);
            params.put("categoryId",selectedCategory.getCategoryNumber());
            subCategoryClient.post(this, ApiLinks.baseLink + ApiLinks.subcategoryLink, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        processSubCategories(responseBody);
                    } catch (Exception e) {
                        txtNone.setVisibility(View.VISIBLE);
                        lstSubCategories.setVisibility(View.GONE);
                        Snackbar.make(lstSubCategories,"An Error Occurred. Please Try Later",Snackbar.LENGTH_SHORT).show();
                        progressLoad.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    processFailure();
                }
            });
        }
        else
        {
            lstSubCategories.setVisibility(View.GONE);
            txtNone.setVisibility(View.VISIBLE);
            progressLoad.setVisibility(View.GONE);
        }


    }

    private void processFailure() {
        txtNone.setVisibility(View.VISIBLE);
        lstSubCategories.setVisibility(View.GONE);

        progressLoad.setVisibility(View.GONE);
    }

    private void processSubCategories(byte[] response) throws Exception{

        String responseString=new String(response);
        JSONArray subcategoryJSON=new JSONArray(responseString);

        if(subcategoryJSON.length()==0)
        {
            txtNone.setVisibility(View.VISIBLE);
            lstSubCategories.setVisibility(View.GONE);
            Snackbar.make(lstSubCategories,"No SubCategories ..",Snackbar.LENGTH_SHORT).show();
        }

        for (int i=0;i<subcategoryJSON.length();i++)
        {
            JSONObject current=subcategoryJSON.getJSONObject(i);
            Category tmp=new Category();
            tmp.setCategoryNumber(current.getInt("id"));
            tmp.setCategoryName(current.getString("cname"));
            tmp.setCategoryImage(current.getString("cimage"));
            tmp.setCardCount(current.getInt("countCard"));
            tmp.setCardColor(current.getString("colorvalue"));
            tmp.setHasSub(current.getInt("hasSub"));
            tmp.setParentCategory(current.getInt("parent"));
            subcategoryList.add(tmp);

        }
        SubCategoryAdapter categoryAdapter=new SubCategoryAdapter(subcategoryList,this);
        lstSubCategories.setAdapter(categoryAdapter);
        progressLoad.setVisibility(View.GONE);
    }



    @Override
    public void onClick(View v) {

    }
}
