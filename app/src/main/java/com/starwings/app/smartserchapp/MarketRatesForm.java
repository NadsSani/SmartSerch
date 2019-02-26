package com.starwings.app.smartserchapp;

import android.app.PendingIntent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.starwings.app.smartserchapp.data.Currency;
import com.starwings.app.smartserchapp.links.ApiLinks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 29-01-2018.
 */

public class MarketRatesForm extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        showCurrencyExchangeForm();
        showGoldRate();
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
    private void showGoldRate()
    {
        AsyncHttpClient currencyRates=new AsyncHttpClient();
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        String apikey=appPreferences.getString("apikey","NA");
        if(!apikey.equals("NA")) {
            RequestParams params = new RequestParams();
            params.put("Authorization", apikey);
            currencyRates.post(ApiLinks.baseLink + ApiLinks.goldrates, params,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        processGoldResponse(responseBody);
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
    }
    private void processGoldResponse(byte[] responseBody) throws Exception {
        Intent intent = new Intent(this, MarketRatesForm.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        String responseString=new String(responseBody);
        JSONObject responseJSON=new JSONObject(responseString);
        JSONArray responseArray=responseJSON.getJSONArray("GoldRate");
        String datestr=responseArray.getJSONObject(0).getString("dateofrate");
        String goldrate=responseArray.getJSONObject(0).getString("goldrate");
        TextView txtrate=(TextView)findViewById(R.id.txtGoldRate);
        txtrate.setText((Double.valueOf(goldrate.trim())/8)+" Per Gram");

    }
    private void showCurrencyExchangeForm() {

        AsyncHttpClient currencyRates=new AsyncHttpClient();
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        String apikey=appPreferences.getString("apikey","NA");
        if(!apikey.equals("NA")) {
            RequestParams params = new RequestParams();
            params.put("Authorization", apikey);
            currencyRates.post(ApiLinks.baseLink + ApiLinks.currencyrates, params,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        processResponse(responseBody);
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
    }
    private void processResponse(byte[] response) throws Exception {

        final ArrayList<Currency> ratelist=new ArrayList<Currency>();
        String responseString=new String(response);
        JSONObject responseJSON=new JSONObject(responseString);
        JSONArray responseArray=responseJSON.getJSONArray("exchangerates");
        for(int i=0;i<responseArray.length();i++)
        {
            Currency temp=new Currency();
            temp.setSlno(responseArray.getJSONObject(i).getInt("slno"));
            temp.setCurrencycode(responseArray.getJSONObject(i).getString("currencycode"));
            temp.setRate(responseArray.getJSONObject(i).getString("rate"));
            temp.setDescription(responseArray.getJSONObject(i).getString("description"));
            ratelist.add(temp);
        }



        final EditText edAmount=(EditText)findViewById(R.id.edAmount);
        final Spinner spnFrom=(Spinner)findViewById(R.id.spnFrom);
        final Spinner spnTo=(Spinner)findViewById(R.id.spnTo);
        final TextView txtResult=(TextView)findViewById(R.id.txtResilt);

        Button findratebtn=(Button)findViewById(R.id.btnfindrate);
        findratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double fromRate=0.0;
                double ToRate=0.0;
                if(spnFrom.getSelectedItemPosition()==0)
                {
                    Snackbar.make(spnFrom,"Select Currency to Convert From",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(spnTo.getSelectedItemPosition()==0)
                {
                    Snackbar.make(spnFrom,"Select Currency to Convert To",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(edAmount.getText().toString().isEmpty())
                {
                    Snackbar.make(edAmount,"Enter Amount",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                for(int k=0;k<ratelist.size();k++)
                {
                    Currency temp=ratelist.get(k);
                    if(temp.getDescription().trim().equals(spnFrom.getSelectedItem().toString().trim()))
                    {
                        fromRate=Double.valueOf(temp.getRate().trim()).doubleValue();
                    }
                    if(temp.getDescription().trim().equals(spnTo.getSelectedItem().toString().trim()))
                    {
                        ToRate=Double.valueOf(temp.getRate().trim()).doubleValue();
                    }
                }
                double convertedamount=Double.valueOf(edAmount.getText().toString().trim())*(ToRate/fromRate);
                txtResult.setText(""+String.format("%.2f", convertedamount));
            }
        });


    }
    private void showFailedResponse(Throwable error) {
        Log.e("ErrorClass",error.getClass().getName());
    }
}
