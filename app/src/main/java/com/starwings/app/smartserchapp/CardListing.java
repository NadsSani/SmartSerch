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
import com.starwings.app.smartserchapp.adapter.CardsAdapter;
import com.starwings.app.smartserchapp.data.Cards;
import com.starwings.app.smartserchapp.data.Category;
import com.starwings.app.smartserchapp.links.ApiLinks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 24-10-2017.
 */

public class CardListing extends AppCompatActivity implements View.OnClickListener {

    private ListView lstCards;
    ArrayList<Cards> cardsList;
    ArrayList<Cards> filteredcardsList;
    ViewFlipper flipper;
    private int mFlipping;
    CardsAdapter cardsadapter;
    TextView txtnocards;
    int keyword;
    String keywordText;
    ProgressBar catLoading;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        keyword=getIntent().getIntExtra("keyword",0);
        keywordText=getIntent().getStringExtra("keywordText");
                filteredcardsList=new ArrayList<Cards>();
        cardsList=((SmartSerch)getApplication()).getCurrent();
        setTitle(keywordText);
        lstCards=(ListView)findViewById(R.id.lstcards);
        catLoading = (ProgressBar) findViewById(R.id.catLoading);
        txtnocards=(TextView)findViewById(R.id.txtNone) ;
        flipper = (ViewFlipper) findViewById(R.id.cardlist_top_layout);
        loadGallery();

        fetchCardsByCategory();

    }

    private void fetchCardsByCategory()
    {
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        String apikey=appPreferences.getString("apikey","NA");

        if(!apikey.equals("NA"))
        {
            catLoading.setVisibility(View.VISIBLE);
            RequestParams params=new RequestParams();
            params.put("Authorization",apikey);
            params.put("searchKey",keyword);
            AsyncHttpClient cardsClient=new AsyncHttpClient();
            cardsClient.setResponseTimeout(50000);
            cardsClient.setConnectTimeout(50000);
            cardsClient.setTimeout(50000);

            cardsClient.post(this, ApiLinks.baseLink + ApiLinks.searchLink, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        prepareCardListing(responseBody);
                    } catch (Exception e) {
                        Snackbar.make(lstCards,"An Error Occurred. Please Try Later",Snackbar.LENGTH_SHORT).show();
                        catLoading.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    processFailure(error);
                }
            });
        }
    }

    private void processFailure(Throwable error) {
        Snackbar.make(lstCards,"An Error Occurred. Please Try Later",Snackbar.LENGTH_SHORT).show();
        catLoading.setVisibility(View.GONE);
    }

    private void prepareCardListing(byte[] responseBody) throws Exception{


        cardsList=new ArrayList<>();
        SmartSerch appobj=((SmartSerch)getApplication());
        String response=new String(responseBody);
        JSONArray cardArray=new JSONArray(response);
        if(cardArray.length()>0) {
            for (int i = 0; i < cardArray.length(); i++) {
                JSONObject card = cardArray.getJSONObject(i);
                Cards temp = new Cards();
                temp.setId(card.getString("id"));
                temp.setCardname(card.getString("caname"));
                temp.setPaidstatus(card.getString("pstatus"));
                temp.setFrontImage(card.getString("fimage"));
                Log.e("FrontImage", card.getString("fimage"));
                temp.setBackImage(card.getString("bimage"));
                temp.setDateOfEntry(card.getString("dofentry"));
                temp.setDistrict(card.getString("distid"));
                temp.setPlace(card.getString("place"));
                temp.setWeb(card.getString("web"));
                temp.setWhatsapp(card.getString("whatsapp"));
                JSONArray mails = card.getJSONArray("mails");
                JSONArray phones = card.getJSONArray("mobiles");
                JSONArray keyarray = card.getJSONArray("keywords");

                ArrayList<Category> tempkeywords = new ArrayList<Category>();
                for (int j = 0; j < keyarray.length(); j++) {
                    Category tmp = new Category();
                    tmp.setCategoryNumber(keyarray.getJSONObject(j).getInt("id"));
                    tmp.setCategoryName(keyarray.getJSONObject(j).getString("cname"));
                    tmp.setCategoryImage(keyarray.getJSONObject(j).getString("cimage"));
                    tmp.setCardCount(keyarray.getJSONObject(j).getInt("countCard"));
                    tmp.setCardColor(keyarray.getJSONObject(j).getString("colorvalue"));
                    tmp.setHasSub(keyarray.getJSONObject(j).getInt("hasSub"));
                    tmp.setParentCategory(keyarray.getJSONObject(j).getInt("parentCategory"));
                    tempkeywords.add(tmp);
                }
                temp.setKeywords(tempkeywords);
                ArrayList tempmail = new ArrayList();
                for (int j = 0; j < mails.length(); j++) {
                    tempmail.add(mails.getJSONObject(j).getString("mailid"));
                }
                temp.setMail(tempmail);
                ArrayList tempphone = new ArrayList();
                for (int j = 0; j < phones.length(); j++) {
                    tempphone.add(phones.getJSONObject(j).getString("mobile"));
                }
                temp.setPhone(tempphone);
                temp.setDistrictname(card.getString("district"));

                cardsList.add(temp);

            }


            CardsAdapter cardadapter=new CardsAdapter(cardsList,this);

            lstCards.setAdapter(cardadapter);
        }
       catLoading.setVisibility(View.GONE);
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
                        Snackbar.make(lstCards,"Failed to Load Ads.",Snackbar.LENGTH_SHORT).show();

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

        Snackbar.make(lstCards,"Failed to Load Ads.",Snackbar.LENGTH_SHORT).show();
    }





    @Override
    public void onClick(View v) {

    }
}
