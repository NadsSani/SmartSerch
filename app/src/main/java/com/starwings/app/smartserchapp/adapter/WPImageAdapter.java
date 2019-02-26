package com.starwings.app.smartserchapp.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.starwings.app.smartserchapp.R;
import com.starwings.app.smartserchapp.WPStatusPage;
import com.starwings.app.smartserchapp.WP_Image_Status_Details;
import com.starwings.app.smartserchapp.data.WPImages;
import com.starwings.app.smartserchapp.links.ApiLinks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by user on 31-01-2018.
 */

public class WPImageAdapter extends BaseAdapter {
    Context parentobject;
    ArrayList<WPImages> data;
    private ShareActionProvider miShareAction;
    Intent shareIntent;
    public WPImageAdapter(Context parentobj, ArrayList<WPImages> dataobj)
    {
        parentobject=parentobj;
        data=dataobj;
        Log.e("Size",data.size()+"");
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos=position;
        if (convertView == null) {
            convertView=((AppCompatActivity)parentobject).getLayoutInflater().inflate(R.layout.wpimage_grid_row,null);
        }
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imgItem);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);



        Log.e("Link",ApiLinks.basegalLink+"wpstatus/images/"+data.get(position).getPath());


        Glide.with(parentobject)
                .load(ApiLinks.basegalLink+"wpstatus/images/"+data.get(position).getPath())
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewImage(ApiLinks.basegalLink+"wpstatus/images/"+data.get(pos).getPath());
            }
        });
        return convertView;
    }

    private void previewImage(String path) {

//        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
//        client.get(path, new FileAsyncHttpResponseHandler(parentobject) {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
//              Log.e("Error",throwable.getClass().getName());
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, File file) {
//
//
//                    saveFile(file);
//
//
//            }
//        });
        Intent fullviewintent=new Intent(parentobject, WP_Image_Status_Details.class);
        fullviewintent.putExtra("PathImg",path);
        parentobject.startActivity(fullviewintent);
    }
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", parentobject.getPackageName(), null);
        intent.setData(uri);
        parentobject.startActivity(intent);
    }
    private void saveFile(File file)  {
        if (ActivityCompat.checkSelfPermission(parentobject,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(parentobject, "Need Permission to access Storage", Toast.LENGTH_SHORT).show();
                            openSettings();
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

            Toast.makeText(parentobject, "Need Permission to access Storage", Toast.LENGTH_SHORT).show();
        }catch (Exception et)
        {
            et.printStackTrace();
        }
    }


}
