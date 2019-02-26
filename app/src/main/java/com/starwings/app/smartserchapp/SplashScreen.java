package com.starwings.app.smartserchapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.starwings.app.smartserchapp.links.ApiLinks;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SplashScreen extends AppCompatActivity {

    ProgressBar loadProgress;

    TextView txtcright;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {

                registerApp();
            }
        }, 1*1000); // wait for 5 seconds

        loadProgress=(ProgressBar)findViewById(R.id.progressBar);


        txtcright=(TextView)findViewById(R.id.txtcright);


        SmartSerch appobj=(SmartSerch)getApplication();
        txtcright.setTypeface(appobj.getLatobold());

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }


    private void registerApp() {
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        if(appPreferences.getString("apikey","NA")=="NA")
        {
            loadProgress.setVisibility(View.VISIBLE);
            AsyncHttpClient registerClient=new AsyncHttpClient();
            registerClient.setResponseTimeout(50000);
            registerClient.setConnectTimeout(50000);
            registerClient.setTimeout(50000);
            RequestParams registerParams=new RequestParams();
            registerParams.put("apirequest",true);
            registerClient.post(ApiLinks.baseLink+ApiLinks.register,registerParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        processResponse(responseBody);
                    } catch (Exception e) {

                        Toast.makeText(SplashScreen.this,"Unknown Error. Please Try Later",Toast.LENGTH_SHORT).show();
                        resetPreferences();
                        loadProgress.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        showFailure(responseBody);
                }
            });
        }
        else
        {
            Intent i = new Intent(SplashScreen.this, HomePageActivity.class);
            startActivity(i);
            loadProgress.setVisibility(View.GONE);
            finish();
        }

    }

    private void showFailure(byte[] error) {

        loadProgress.setVisibility(View.GONE);
        Toast.makeText(this,"Failure. Try Later",Toast.LENGTH_SHORT).show();
    }

    private void resetPreferences() {
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=appPreferences.edit();
        prefEdit.putString("apikey","NA");
        prefEdit.commit();
        finish();
    }

    /*
    {
        "error": false,
        "message": "NA",
        "api_key": "6c236222869701f7ebc2eff87bf2d800"
    }
     */
    private void processResponse(byte[] responseBody) throws Exception{

        String response=new String(responseBody);
        JSONObject respJson=new JSONObject(response);
        String apiKey=respJson.getString("api_key");
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=appPreferences.edit();
        prefEdit.putString("apikey",apiKey);
        prefEdit.commit();
        loadProgress.setVisibility(View.GONE);
        Intent i = new Intent(SplashScreen.this, HomePageActivity.class);
        startActivity(i);
        finish();
    }
}
