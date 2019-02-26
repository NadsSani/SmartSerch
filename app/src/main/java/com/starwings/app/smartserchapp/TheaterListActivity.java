package com.starwings.app.smartserchapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.starwings.app.smartserchapp.adapter.TheaterAdapter;
import com.starwings.app.smartserchapp.data.Theater;
import com.starwings.app.smartserchapp.links.ApiLinks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 06-12-2017.
 */

public class TheaterListActivity extends AppCompatActivity {
    ProgressBar catLoading;
    SearchView sv;
    private RecyclerView recyclerView;
    ArrayList<Theater> theatersArray;
    TheaterAdapter theaterAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_list);
        sv = (SearchView) findViewById(R.id.sv);
        sv.setFocusable(false);
        sv.setFocusableInTouchMode(false);
        catLoading = (ProgressBar) findViewById(R.id.catLoading);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //SEARCH FILTER

                    theaterAdapter.getFilter().filter(newText);


                return false;
            }
        });
        fetchTheaterList();
    }

    private void fetchTheaterList() {
        catLoading.setVisibility(View.VISIBLE);
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        String apikey=appPreferences.getString("apikey","NA");
        if(!apikey.equals("NA")) {
            RequestParams params=new RequestParams();
            params.put("Authorization",apikey);

            AsyncHttpClient theaterclient = new AsyncHttpClient();
            theaterclient.post(ApiLinks.baseLink + ApiLinks.theaterLink, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        parseTheaterResponse(responseBody);
                    } catch (Exception e) {
                        Snackbar.make(recyclerView,"An Error Occurred. Please Try Later"+e.getClass().getName(),Snackbar.LENGTH_SHORT).show();
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
/*
 "slno": 1,
            "TheaterCode": "XQC0225",
            "City": "Angamaly",
            "District": "Ernakulam",
            "Name": "Carnival Cinemas - Screen 1",
            "Location": "KSRTC bus stand complex",
            "Seating": 204,
            "Rating": "AA+",
            "ShowTimes": "NA"
 */
    private void parseTheaterResponse(byte[] response) throws Exception {

        theatersArray=new ArrayList<Theater>();
        catLoading.setVisibility(View.GONE);
        SmartSerch appobj=((SmartSerch)getApplication());
        String responseString=new String(response);
        JSONObject responseobj=new JSONObject(responseString);
        JSONArray theaterArray=responseobj.getJSONArray("theaters");
        if(theaterArray.length()>0) {
            for (int i = 0; i < theaterArray.length(); i++) {
                JSONObject theater = theaterArray.getJSONObject(i);
                Theater tmpTheater=new Theater();
                tmpTheater.setSlno(theater.getInt("slno"));
                tmpTheater.setTheaterCode(theater.getString("TheaterCode"));
                tmpTheater.setCity(theater.getString("City"));
                tmpTheater.setDistrict(theater.getString("District"));
                tmpTheater.setName(theater.getString("Name"));
                tmpTheater.setLocation(theater.getString("Location"));
                tmpTheater.setSeating(theater.getString("Seating"));
                tmpTheater.setRating(theater.getString("Rating"));
                tmpTheater.setShowTimes(theater.getString("ShowTimes"));
                theatersArray.add(tmpTheater);

            }
        }
        theaterAdapter=new TheaterAdapter(this,theatersArray);
        recyclerView.setAdapter(theaterAdapter);

    }
    private void processFailure(Throwable error) {
        Snackbar.make(recyclerView,"An Error Occurred. Please Try Later"+error.getClass().getName(),Snackbar.LENGTH_SHORT).show();
        catLoading.setVisibility(View.GONE);
    }
}
