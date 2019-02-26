package com.starwings.app.smartserchapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.starwings.app.smartserchapp.fragments.MapFragment;

/**
 * Created by user on 29-11-2017.
 */

public class LocationActivity extends AppCompatActivity {

    String name;
    String location;
    MapFragment mapview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        name=getIntent().getStringExtra("Name");
        setTitle(name);
        location=getIntent().getStringExtra("Location");
        showFragment();
    }

    private void showFragment() {
        FragmentManager fragmanager=getSupportFragmentManager();
        mapview=new MapFragment();
        Bundle params=new Bundle();
        params.putString("Name",name);
        params.putString("Location",location);
        mapview.setArguments(params);
        FragmentTransaction transaction = fragmanager.beginTransaction();
        transaction.add(R.id.container,mapview,"MAP Fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
