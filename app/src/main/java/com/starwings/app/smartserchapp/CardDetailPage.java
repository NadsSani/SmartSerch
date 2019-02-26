package com.starwings.app.smartserchapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.starwings.app.smartserchapp.data.Cards;
import com.starwings.app.smartserchapp.fragments.CardSliderFragment;
import com.starwings.app.smartserchapp.links.ApiLinks;

import java.util.ArrayList;

/**
 * Created by user on 25-10-2017.
 */

public class CardDetailPage extends AppCompatActivity implements CardSliderFragment.BottomNavigationInterface {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private int selectedPosition;
    private ArrayList<Cards> cardsList;
    private static final int NUM_PAGES = 0;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;


    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        SmartSerch appobj=(SmartSerch)getApplication();
        cardsList=appobj.getCurrent();
        selectedPosition=Integer.parseInt(getIntent().getStringExtra("selected").trim());



        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),cardsList.size());
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
    @Override
    public void initiatePhone(String phone) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:"+phone));
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Snackbar snackbar = Snackbar
                    .make(mPager, "Need Permission to initiate Phone Call", Snackbar.LENGTH_LONG)
                    .setAction("SETTINGS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            
                            openSettings();
                        }
                    });

// Changing message text color
            snackbar.setActionTextColor(Color.RED);

// Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
            return;
        }

            startActivity(phoneIntent);

    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void sendReport(Cards content) {
        if(content.equals("NA"))
        {
            Snackbar.make(mPager,"No Mail Address Specified",Snackbar.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@smartserch.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, content.getCardname()+" - Issue Report");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Snackbar.make(mPager,"There are no email clients installed.",Snackbar.LENGTH_SHORT).show();


        }
    }


    @Override
    public void shareContent(Cards current) {
        String card_site_page= ApiLinks.baseLink+"vizcardpage.php?cardid=CARD_ID_"+current.getId();
        Intent intent=new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

// Add data to the intent, the receiving app will decide what to do with it.
        intent.putExtra(Intent.EXTRA_SUBJECT, current.getCardname());
        intent.putExtra(Intent.EXTRA_TEXT, card_site_page);
        startActivity(Intent.createChooser(intent, "How do you want to share?"));
    }
    @Override
    public void showMap(Cards current) {
        Intent mapView=new Intent(this,LocationActivity.class);
        mapView.putExtra("Name",current.getCardname());
        mapView.putExtra("Location",current.getCardname());
        startActivity(mapView);
    }

    @Override
    public void sendMail(String mail) {
        if(mail.equals("NA"))
        {
            Snackbar.make(mPager,"No Mail Address Specified",Snackbar.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mail});
        i.putExtra(Intent.EXTRA_SUBJECT, "Enquiry");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Snackbar.make(mPager,"There are no email clients installed.",Snackbar.LENGTH_SHORT).show();


        }
    }
public void setItemTitle(String title)
{
    setTitle(title);
}
    @Override
    public void openSite(String site) {
        if(site.equals("NA"))
        {
            Snackbar.make(mPager,"No Website Address Specified",Snackbar.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(site));
        // i.setData(Uri.parse(current.getWeb()));
        startActivity(i);
    }

    @Override
    public void openWhatsapp(String phone) {
        if(phone.equals("NA")||phone.equals(""))
        {
            Snackbar.make(mPager,"No Phone Number Specified",Snackbar.LENGTH_SHORT).show();
            return;
        }
        Uri uri = Uri.parse("smsto:" + phone);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));
    }

    @Override
    public void previewImage(Cards currentvalue) {

        Intent previewIntent=new Intent(this,PreviewCard.class);
        previewIntent.putExtra("currentCard",currentvalue);
        startActivity(previewIntent);
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm,int size) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            CardSliderFragment current=new CardSliderFragment();
            Bundle params=new Bundle();
            params.putSerializable("current",cardsList.get(position));
            current.setArguments(params);
            return current;
        }


        @Override
        public int getCount() {
            return cardsList.size();
        }
    }
}
