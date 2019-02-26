package com.starwings.app.smartserchapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.starwings.app.smartserchapp.data.Cards;
import com.starwings.app.smartserchapp.data.Theater;
import com.starwings.app.smartserchapp.fragments.TheaterSlideFragment;

import java.util.ArrayList;

/**
 * Created by user on 07-12-2017.
 */

public class TheaterDetailPage extends AppCompatActivity implements TheaterSlideFragment.BottomNavigationInterface {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private PagerAdapter mPagerAdapter;
    private ArrayList<Theater> theaters;
    private ViewPager mPager;
    private int selectedPosition;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        SmartSerch appobj=(SmartSerch)getApplication();
        theaters=appobj.getTheaterlist();
        selectedPosition=Integer.parseInt(getIntent().getStringExtra("selected").trim());



        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new TheaterSlidePagerAdapter(getSupportFragmentManager(),theaters.size());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(selectedPosition);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // change your title
                // inflate menu
                // customize your toolbar

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initiatePhone(String phone) {

    }

    @Override
    public void sendMail(String mail) {

    }

    @Override
    public void openSite(String site) {

    }

    @Override
    public void openWhatsapp(String phone) {

    }

    @Override
    public void previewImage(Cards currentcard) {

    }

    @Override
    public void sendReport(Cards currentcard) {

    }

    @Override
    public void shareContent(Cards current) {

    }

    @Override
    public void showMap(Cards current) {

    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class TheaterSlidePagerAdapter extends FragmentStatePagerAdapter {
        public TheaterSlidePagerAdapter(FragmentManager fm, int size) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            TheaterSlideFragment current=new TheaterSlideFragment();
            Bundle params=new Bundle();
            params.putSerializable("current",theaters.get(position));
            current.setArguments(params);
            return current;
        }


        @Override
        public int getCount() {
            return theaters.size();
        }
    }
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
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

