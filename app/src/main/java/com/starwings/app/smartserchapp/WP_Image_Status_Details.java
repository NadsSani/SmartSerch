package com.starwings.app.smartserchapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.starwings.app.smartserchapp.components.TouchImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 01-02-2018.
 */

public class WP_Image_Status_Details extends AppCompatActivity {

    TouchImageView fullview;
    String imgpath;
    ImageButton btnDownload;
    TextView txtCaption;
    ProgressBar prgProgress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail_view);
        imgpath=getIntent().getStringExtra("PathImg");
        fullview = (TouchImageView) findViewById(R.id.imgfullscreen);
        Glide.with(this)
                .load(imgpath)
                .into(fullview);
        btnDownload=(ImageButton)findViewById(R.id.imgDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile();
            }
        });

        txtCaption=(TextView)findViewById(R.id.txtprgcaption);
        prgProgress=(ProgressBar)findViewById(R.id.prgbar);
    }
    private void saveFile()
    {
        txtCaption.setVisibility(View.VISIBLE);
        prgProgress.setVisibility(View.VISIBLE);
                AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(imgpath, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
              Log.e("Error",throwable.getClass().getName());
                txtCaption.setVisibility(View.INVISIBLE);
                prgProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {


                    downloadFile(file);


            }
        });
    }
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
    private void downloadFile(File file) {

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Need Permission to access Storage", Toast.LENGTH_SHORT).show();
                openSettings();
                txtCaption.setVisibility(View.INVISIBLE);
                prgProgress.setVisibility(View.INVISIBLE);
                return;
            }


            try {
                FileInputStream instream = null;
                FileOutputStream outstream = null;
                Log.e("FileName", file.getName());
                Log.e("FilePath", file.getPath());

                File dir=new File(Environment.getExternalStorageDirectory()+"/smartserch" );
                if(!dir.exists())
                    dir.mkdir();

                File dir2=new File(Environment.getExternalStorageDirectory()+"/smartserch/wpimages" );
                if(!dir2.exists())
                    dir2.mkdir();
                File outfile = new File(Environment.getExternalStorageDirectory()+"/smartserch/wpimages/" + file.getName()+".jpg");

                instream = new FileInputStream(file);
                outstream = new FileOutputStream(outfile);
                byte[] buffer = new byte[1024];

                int length;
    	    /*copying the contents from input stream to
    	     * output stream using read and write methods
    	     */
                while ((length = instream.read(buffer)) > 0) {
                    outstream.write(buffer, 0, length);
                }

                //Closing the input/output file streams
                instream.close();
                outstream.close();

                Toast.makeText(this, "Download Completed", Toast.LENGTH_SHORT).show();
            }catch (Exception et)
            {
                et.printStackTrace();
            }
        txtCaption.setVisibility(View.INVISIBLE);
        prgProgress.setVisibility(View.INVISIBLE);
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
