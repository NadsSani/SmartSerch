package com.starwings.app.smartserchapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.appbrain.AdId;
import com.appbrain.InterstitialBuilder;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;

import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.github.clans.fab.FloatingActionMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.starwings.app.smartserchapp.adapter.CardAdapter;
import com.starwings.app.smartserchapp.adapter.HomeCategoryAdapter;
import com.starwings.app.smartserchapp.data.Cards;
import com.starwings.app.smartserchapp.data.Category;
import com.starwings.app.smartserchapp.links.ApiLinks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class HomePageActivity extends AppCompatActivity {


    ArrayList<Cards> cardsList;
    ArrayList<Category> categories;
   SearchView sv;
    HomeCategoryAdapter categoryAdapter;
    ProgressBar catLoading;
    ViewFlipper flipper,offerflipper;
    private RecyclerView recyclerView;
    private GridView categoryGrid;
    private ToggleButton viewToggle;
    private FloatingActionButton mFab;
    private int mFlipping;
    CardAdapter cardAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private InterstitialBuilder interstitialBuilder;
    private MediaController ctlr;
    LinearLayout linupdate;
    Button btnupdate,btnViewOffer;
    Handler updatehandler;
    FloatingActionMenu menuFloating;
    FloatingActionButton currency_rate_menu,gold_rate_menu,stips_menu,menu_wpimages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        menuFloating=findViewById(R.id.menu_floating);


        sv = (SearchView) findViewById(R.id.sv);
        sv.setFocusable(false);
        sv.setFocusableInTouchMode(false);

        interstitialBuilder = InterstitialBuilder.create().setAdId(AdId.EXIT)
                .setFinishOnExit(this).preload(this);



        linupdate=(LinearLayout)findViewById(R.id.updateapplayout);

//        btnViewOffer =(Button)findViewById(R.id.btnviewoffer);
//        btnViewOffer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callOfferPage();
//            }
//        });

        gold_rate_menu=(FloatingActionButton)findViewById(R.id.menu_grate);
        gold_rate_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyExchangeForm();
                menuFloating.close(true);
            }
        });

        currency_rate_menu=(FloatingActionButton)findViewById(R.id.menu_crate);
        currency_rate_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyExchangeForm();
                menuFloating.close(true);
            }
        });

        stips_menu=(FloatingActionButton)findViewById(R.id.menu_stips);
        stips_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTipsForm();
                menuFloating.close(true);
            }
        });

        menu_wpimages=(FloatingActionButton)findViewById(R.id.menu_wpimages);
        menu_wpimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWhatsappStatus();
                menuFloating.close(true);
            }
        });

        btnupdate=(Button)findViewById(R.id.btnupdateapp);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 callPlayStore();
            }
        });
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        btnupdate.startAnimation(anim);

        catLoading = (ProgressBar) findViewById(R.id.catLoading);

        categoryGrid=(GridView)findViewById(R.id.category_grid);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);



        flipper = (ViewFlipper) findViewById(R.id.home_top_layout);

        if ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            Snackbar snackbar = Snackbar
                    .make(flipper, "Need Permission To Access Storage", Snackbar.LENGTH_LONG)
                    .setAction("SETTINGS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            openSettings();
                        }
                    });
        }

        showUpdateView();
        loadGallery();


        viewToggle=(ToggleButton)findViewById(R.id.btn_view_toggle) ;
        viewToggle.setChecked(true);
        fetchCards();
        viewToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    sv.setQueryHint("Filter By Name, Place, District OR Category");
                    fetchCards();
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    categoryGrid.setVisibility(View.GONE);
                }
                else
                {
                    sv.setQueryHint("Type Category");
                    fetchCategories();
                    recyclerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    categoryGrid.setVisibility(View.VISIBLE);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshCardsList();
            }
        });
        generateNotification();
        generateTipsNotification();
    }

    private void showWhatsappStatus() {
        Intent intent = new Intent(this, WPStatusPage.class);
        startActivity(intent);
    }

    private void showTipsForm() {
        Intent intent = new Intent(this, TipsListing.class);
        startActivity(intent);
    }

    private void generateTipsNotification() {

        AsyncHttpClient tipsclient=new AsyncHttpClient();
        tipsclient.get(ApiLinks.baseLink + ApiLinks.tipsLink, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    parseFeed(responseBody);
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

    private void parseFeed(byte[] responseBody) throws Exception{

        String feedString=new String(responseBody);
        JSONObject feedjson=new JSONObject(feedString);
        JSONArray feedArray=feedjson.getJSONArray("items");
        for(int i=0;i<feedArray.length();i++)
        {
            JSONObject blogitem=feedArray.getJSONObject(i);
            String dopublis=blogitem.getString("date_published");
            Date currentdate=new Date();
            String newstring = new SimpleDateFormat("yyyy-MM-dd").format(currentdate);
            if(dopublis.startsWith(newstring))
            {
                generateFeedNotification(blogitem,i+1);
            }
        }
    }

    private void generateFeedNotification(JSONObject blogitem,int pos) throws Exception {
        Uri notification=null;
        Intent intent = new Intent(this, TipsDetailPage.class);
        intent.putExtra("blogcontent",blogitem.getString("content_html"));
        intent.putExtra("blogtitle",blogitem.getString("title"));
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        try {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Notification n  = new Notification.Builder(this)
                .setContentTitle(blogitem.getString("title"))
                .setContentText("Click Here To Open")
                .setContentIntent(pIntent)
                .setSound(notification)
                .setSmallIcon(R.drawable.ic_stat_logo)
                .build();
         notificationManager.notify(pos, n);

    }

    private void showCurrencyExchangeForm() {
        Intent marketRateForm=new Intent(this,MarketRatesForm.class);
        startActivity(marketRateForm);
    }


    private void callOfferPage() {
        Intent intent = new Intent(this,SmartOffersActivity.class);
        startActivity(intent);
    }

    private void callPlayStore() {
        String url=getGooglePlayStoreUrl();
        if (url != null) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (Exception e) {

            }
        }
    }
    private String getGooglePlayStoreUrl() {
        String id = getApplicationInfo().packageName; // current google
        // play is using
        // package name
        // as id
        return "market://details?id=" + id;
    }
    private void generateNotification()
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
    private void showFailedResponse(Throwable error) {
        Log.e("ErrorClass",error.getClass().getName());
    }
    private void processGoldResponse(byte[] responseBody) throws Exception {
        Uri notification=null;
        Intent intent = new Intent(this, MarketRatesForm.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        String responseString=new String(responseBody);
        JSONObject responseJSON=new JSONObject(responseString);
        JSONArray responseArray=responseJSON.getJSONArray("GoldRate");
        String datestr=responseArray.getJSONObject(0).getString("dateofrate");
        String goldrate=responseArray.getJSONObject(0).getString("goldrate");
        try {
             notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Gold Rate")
                .setContentText("Todays Gold Rate - "+(Double.valueOf(goldrate.trim())/8)+" Per Gram")
                .setContentIntent(pIntent)
                .setSmallIcon(R.drawable.ic_stat_logo)
                .setSound(notification)
                .setOngoing(true)
                .build();
        notificationManager.notify(0, n);
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
    private void fetchCategories() {
        catLoading.setVisibility(View.VISIBLE);
        categories=new ArrayList<>();
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        String apikey=appPreferences.getString("apikey","NA");
        if(!apikey.equals("NA"))
        {
            RequestParams params=new RequestParams();
            params.put("Authorization",apikey);

            AsyncHttpClient cardsClient=new AsyncHttpClient();
            cardsClient.setResponseTimeout(50000);
            cardsClient.setConnectTimeout(50000);
            cardsClient.setTimeout(50000);

            cardsClient.post(this, ApiLinks.baseLink + ApiLinks.fetchCategories, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {

                        prepareCategories(responseBody);

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

    @Override
    public void onBackPressed() {
        if (!interstitialBuilder.show(this)) {
            super.onBackPressed();
        }
    }

    private void prepareOfferFlipper()
    {
        //View One


        LinearLayout linOne=new LinearLayout(this);
        linOne.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linOne.setLayoutParams(linLayoutParam);

        ImageView imgPhoto = new ImageView(this);
        imgPhoto.setAdjustViewBounds(true);
        imgPhoto.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        imgPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imgPhoto.setBackgroundResource(R.drawable.smartofferlogo);
        linOne.addView(imgPhoto);

        TextView textCaption=new TextView(this);
        textCaption.setText("Testing Caption");

        Button btnView=new Button(this);
        btnView.setText("View");
        linOne.addView(textCaption);
        linOne.addView(btnView);


    }
    private void prepareCategories(byte[] responseBody)  throws Exception{
        String response=new String(responseBody);
        JSONArray categoryArray=new JSONArray(response);
        if(categoryArray.length()>0) {
            for (int i = 0; i < categoryArray.length(); i++) {

                Category tmp=new Category();
                JSONObject catObject=categoryArray.getJSONObject(i);
                int parentexists=catObject.getInt("parent");
                if(parentexists==0)
                {
                    tmp.setCategoryNumber(catObject.getInt("id"));
                    tmp.setCategoryName(catObject.getString("cname"));
                    tmp.setCategoryImage(catObject.getString("cimage"));
                    tmp.setCardCount(catObject.getInt("countCard"));
                    tmp.setCardColor(catObject.getString("colorvalue"));
                    tmp.setHasSub(catObject.getInt("hasSub"));
                    tmp.setParentCategory(catObject.getInt("parent"));
                    categories.add(tmp);
                }
            }
        }
        categoryAdapter=new HomeCategoryAdapter(categories,this);
        categoryGrid.setAdapter(categoryAdapter);
        catLoading.setVisibility(View.GONE);
    }


    private void refreshCardsList() {
        cardsList=new ArrayList<>();

        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        String apikey=appPreferences.getString("apikey","NA");

        if(!apikey.equals("NA"))
        {
            RequestParams params=new RequestParams();
            params.put("Authorization",apikey);

            AsyncHttpClient cardsClient=new AsyncHttpClient();
            cardsClient.setResponseTimeout(50000);
            cardsClient.setConnectTimeout(50000);
            cardsClient.setTimeout(50000);

            cardsClient.post(this, ApiLinks.baseLink + ApiLinks.cardsLink, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        renderRefreshedList(responseBody);
                    } catch (Exception e) {
                        Snackbar.make(recyclerView,"An Error Occurred. Please Try Later",Snackbar.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    processFailure(error);
                }
            });
        }

    }
    private void fetchCards() {
        cardsList=new ArrayList<>();
        catLoading.setVisibility(View.VISIBLE);
        SharedPreferences appPreferences=getSharedPreferences("APP_PREFERENCES",MODE_PRIVATE);
        String apikey=appPreferences.getString("apikey","NA");

        if(!apikey.equals("NA"))
        {
            RequestParams params=new RequestParams();
            params.put("Authorization",apikey);

            AsyncHttpClient cardsClient=new AsyncHttpClient();
            cardsClient.setResponseTimeout(50000);
            cardsClient.setConnectTimeout(50000);
            cardsClient.setTimeout(50000);

            cardsClient.post(this, ApiLinks.baseLink + ApiLinks.cardsLink, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        prepareCardListing(responseBody);
                    } catch (Exception e) {
                        Snackbar.make(recyclerView,"An Error Occurred. Please Try Later",Snackbar.LENGTH_SHORT).show();
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
        Snackbar.make(recyclerView,"An Error Occurred. Please Try Later"+error.getClass().getName(),Snackbar.LENGTH_SHORT).show();
        catLoading.setVisibility(View.GONE);
    }
    private void renderRefreshedList(byte[] responseBody) throws Exception {

        SmartSerch appobj=((SmartSerch)getApplication());
        String response=new String(responseBody);
        JSONArray cardArray=new JSONArray(response);
        if(cardArray.length()>0)
        {
            for(int i=0;i<cardArray.length();i++)
            {
                JSONObject card=cardArray.getJSONObject(i);
                Cards temp=new Cards();
                temp.setId(card.getString("id"));
                temp.setCardname(card.getString("caname"));
                temp.setPaidstatus(card.getString("pstatus"));
                temp.setFrontImage(card.getString("fimage"));
                Log.e("FrontImage",card.getString("fimage"));
                temp.setBackImage(card.getString("bimage"));
                temp.setDateOfEntry(card.getString("dofentry"));
                temp.setDistrict(card.getString("distid"));
                temp.setPlace(card.getString("place"));
                temp.setWeb(card.getString("web"));
                temp.setWhatsapp(card.getString("whatsapp"));
                JSONArray mails=card.getJSONArray("mails");
                JSONArray phones=card.getJSONArray("mobiles");
                JSONArray keyarray=card.getJSONArray("keywords");

                ArrayList<Category> tempkeywords=new ArrayList<Category>();
                for(int j=0;j<keyarray.length();j++)
                {
                    Category tmp=new Category();
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
                ArrayList tempmail=new ArrayList();
                for(int j=0;j<mails.length();j++)
                {
                    tempmail.add(mails.getJSONObject(j).getString("mailid"));
                }
                temp.setMail(tempmail);
                ArrayList tempphone=new ArrayList();
                for(int j=0;j<phones.length();j++)
                {
                    tempphone.add(phones.getJSONObject(j).getString("mobile"));
                }
                temp.setPhone(tempphone);
                temp.setDistrictname(card.getString("district"));

                cardsList.add(temp);

            }
            appobj.setCurrent(cardsList);
            cardAdapter=new CardAdapter(this,cardsList);
            recyclerView.setAdapter(cardAdapter);
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {


                    Log.e("Called","TextCalled");


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    if(viewToggle.isChecked()) {


                        cardAdapter.getFilter().filter(newText);


                    }
                    else
                    {
                        categoryAdapter.getFilter().filter(newText);
                    }
                    return false;
                }
            });

        }
        else
        {

            recyclerView.setVisibility(View.GONE);
        }

        swipeRefreshLayout.setRefreshing(false);
        catLoading.setVisibility(View.GONE);
    }
    private void prepareCardListing(byte[] responseBody) throws Exception {

        SmartSerch appobj=((SmartSerch)getApplication());
        String response=new String(responseBody);
        JSONArray cardArray=new JSONArray(response);
        if(cardArray.length()>0)
        {
            for(int i=0;i<cardArray.length();i++)
            {
                JSONObject card=cardArray.getJSONObject(i);
                Cards temp=new Cards();
                temp.setId(card.getString("id"));
                temp.setCardname(card.getString("caname"));
                temp.setPaidstatus(card.getString("pstatus"));
                temp.setFrontImage(card.getString("fimage"));
                Log.e("FrontImage",card.getString("fimage"));
                temp.setBackImage(card.getString("bimage"));
                temp.setDateOfEntry(card.getString("dofentry"));
                temp.setDistrict(card.getString("distid"));
                temp.setPlace(card.getString("place"));
                temp.setWeb(card.getString("web"));
                temp.setWhatsapp(card.getString("whatsapp"));
                JSONArray mails=card.getJSONArray("mails");
                JSONArray phones=card.getJSONArray("mobiles");
                JSONArray keyarray=card.getJSONArray("keywords");

                ArrayList<Category> tempkeywords=new ArrayList<Category>();
                for(int j=0;j<keyarray.length();j++)
                {
                    Category tmp=new Category();
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
                ArrayList tempmail=new ArrayList();
                for(int j=0;j<mails.length();j++)
                {
                    tempmail.add(mails.getJSONObject(j).getString("mailid"));
                }
                temp.setMail(tempmail);
                ArrayList tempphone=new ArrayList();
                for(int j=0;j<phones.length();j++)
                {
                    tempphone.add(phones.getJSONObject(j).getString("mobile"));
                }
                temp.setPhone(tempphone);
                temp.setDistrictname(card.getString("district"));

                cardsList.add(temp);

            }
            appobj.setCurrent(cardsList);
            cardAdapter=new CardAdapter(this,cardsList);
            recyclerView.setAdapter(cardAdapter);
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {




                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    Log.e("Called","TextCalled");
                    if(viewToggle.isChecked()) {


                        cardAdapter.getFilter().filter(newText);


                    }
                    else
                    {
                        categoryAdapter.getFilter().filter(newText);
                    }
                    return false;
                }
            });

        }
        else
        {

            recyclerView.setVisibility(View.GONE);
        }


        catLoading.setVisibility(View.GONE);
    }

//    private void checkAppVersion() {
//
//        WVersionManager versionManager = new WVersionManager(this);
//        versionManager.setVersionContentUrl(ApiLinks.versionCheck); // your update content url, see the response format below
//        versionManager.checkVersion();
//        versionManager.setUpdateNowLabel("Stay Updated");
//        versionManager.setRemindMeLaterLabel("Not Now");
//        versionManager.setIgnoreThisVersionLabel("Ignore");
//        versionManager.setUpdateUrl(ApiLinks.appLink); // this is the link will execute when update now clicked. default will go to google play based on your package name.
//        versionManager.setReminderTimer(10); // this mean checkVersion() will not take effect within 10 minutes
//    }

    public void showUpdateView()
    {
        SharedPreferences appPreferences = getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE);
        String apikey = appPreferences.getString("apikey", "NA");
        RequestParams params = new RequestParams();
        params.put("Authorization", apikey);
        AsyncHttpClient versionclient=new AsyncHttpClient();
        versionclient.post(ApiLinks.baseLink + ApiLinks.versionCheck, params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    parseVersion(responseBody);
                } catch (Exception e) {
                   Log.e("ExSuccess",e.getClass().getName());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.e("ExFail",error.getClass().getName());
            }
        });

    }

    private void showParseFailure(byte[] responseBody) throws Exception {

    }

    private void parseVersion(byte[] responseBody) throws Exception {
        JSONObject versionObj=new JSONObject(new String(responseBody));
        int newVersion=versionObj.getInt("version");
        int currentVer=getCurrentVersionCode();
        if(currentVer<newVersion)
        {
            linupdate.setVisibility(View.VISIBLE);
            Snackbar temp=Snackbar.make(menuFloating,"New Version Available",Snackbar.LENGTH_SHORT);
            temp.setAction("Update", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPlayStore();
                }
            });
            temp.show();
        }

    }

    private void checkPreferences() {
        SharedPreferences appversion=getSharedPreferences("appPreference", Context.MODE_PRIVATE);
        boolean status=appversion.getBoolean("updatestatus",false);
        if(status)
        {
            linupdate.setVisibility(View.VISIBLE);
        }

        Log.e("UpdateStatus",""+status);

    }
    public int getCurrentVersionCode() {
        int currentVersionCode = 0;
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // return 0
        }
        return currentVersionCode;
    }
    private void loadGallery() {
        SharedPreferences appPreferences = getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE);
        String apikey = appPreferences.getString("apikey", "NA");

        if (!apikey.equals("NA")) {
            AsyncHttpClient galleryClient = new AsyncHttpClient();
            galleryClient.setResponseTimeout(50000);
            galleryClient.setConnectTimeout(50000);
            galleryClient.setTimeout(50000);
            RequestParams params = new RequestParams();
            params.put("Authorization", apikey);
            Log.e("API", ApiLinks.baseLink + ApiLinks.homePageGallery);
            galleryClient.post(this, ApiLinks.baseLink + ApiLinks.homePageGallery, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        Log.e("API", "Response" + new String(responseBody));
                        loadGalleryImages(responseBody);
                    } catch (Exception e) {
                        Log.e("API Exception", ApiLinks.baseLink + ApiLinks.homePageGallery);
                        Snackbar.make(menuFloating, "Failed to Load Ads.", Snackbar.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    showFailure(error);
                }


            });
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
        Intent aboutIntent = new Intent(this, AboutPageScreen.class);
        startActivity(aboutIntent);
    }

    private void homeIntent() {
        Intent homeIntent = new Intent(this, HomePageActivity.class);
        startActivity(homeIntent);
    }

    private void showFailure(Throwable error) {

        Snackbar.make(menuFloating, "Failed to Load Ads.", Snackbar.LENGTH_SHORT).show();
    }

    private void loadGalleryImages(byte[] responseBody) throws Exception {
        String galResponse = new String(responseBody);
        JSONArray galArray = new JSONArray(galResponse);
        for (int i = 0; i < galArray.length(); i++) {
            int adType = galArray.getJSONObject(i).getInt("adType");
            switch (adType) {
                case 0:
                    ImageView imgPhoto = new ImageView(this);
                    imgPhoto.setAdjustViewBounds(true);
                    imgPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
                    Glide.with(this)
                            .load(ApiLinks.basegalLink + galArray.getJSONObject(i).getString("path"))

                            .into(imgPhoto);
                    flipper.addView(imgPhoto);
                    flipper.setFlipInterval(10000);
                    break;
            //    case 1:
//                    ctlr=new MediaController(this);
//                    LinearLayout linearLayout = new LinearLayout(this);
//
//                    // Setting the orientation to vertical
//                    linearLayout.setOrientation(LinearLayout.VERTICAL);
//
//                    // Defining the LinearLayout layout parameters to fill the parent.
//                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                    // Defining the layout parameters of the TextView
//                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT);
//
//
//                    final VideoView adVideo=new VideoView(this);
//                    Log.e("VideoPath",ApiLinks.basegalLink + galArray.getJSONObject(i).getString("path"));
//                    Uri video = Uri.parse(ApiLinks.basegalLink + galArray.getJSONObject(i).getString("path"));
//                   // Uri video=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.testad);
//                    adVideo.setVideoURI(video);
//                    adVideo.requestFocus();
//                    ctlr.setMediaPlayer(adVideo);
//                    adVideo.setMediaController(ctlr);
//                    flipper.setFlipInterval(10000);
//                    linearLayout.addView(adVideo);
//                    flipper.addView(linearLayout);
//                    adVideo.setLayoutParams(lp);
//
//                    adVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        // Close the progress bar and play the video
//                        public void onPrepared(MediaPlayer mp) {
//                            Log.e("VideoPath","Prepared");
//                            flipper.stopFlipping();
//                            mp.start();
//                            mp.setLooping(true);
//
//
//
//                        }
//                    });
//                    adVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            Log.e("VideoPath","Completed");
//                            flipper.startFlipping();
//                            mp.reset();
//
//
//                        }
//                    });
//                    break;

            }

        }
        if (mFlipping == 0) {
            /** Start Flipping */
            flipper.startFlipping();
            mFlipping = 1;

        } else {
            /** Stop Flipping */
            flipper.stopFlipping();
            mFlipping = 0;

        }
    }






//    private class ProgressBack extends AsyncTask<String, String, String> {
//        ProgressDialog PD;
//
//        @Override
//        protected void onPreExecute() {
//            PD = ProgressDialog.show(HomePageActivity.this, null, "Please Wait ...", true);
//            PD.setCancelable(true);
//        }
//
//        @Override
//        protected void doInBackground(String... arg0) {
//            downloadFile("http://beta-vidizmo.com/hilton.mp4", "Sample.mp4");
//
//        }
//
//        protected void onPostExecute(Boolean result) {
//            PD.dismiss();
//
//        }
//
//        private void downloadFile(String fileURL, String fileName) {
//            try {
//                String rootDir = Environment.getExternalStorageDirectory()
//                        + File.separator + "Video";
//                File rootFile = new File(rootDir);
//                rootFile.mkdir();
//                URL url = new URL(fileURL);
//                HttpURLConnection c = (HttpURLConnection) url.openConnection();
//                c.setRequestMethod("GET");
//                c.setDoOutput(true);
//                c.connect();
//                FileOutputStream f = new FileOutputStream(new File(rootFile,
//                        fileName));
//                InputStream in = c.getInputStream();
//                byte[] buffer = new byte[1024];
//                int len1 = 0;
//                while ((len1 = in.read(buffer)) > 0) {
//                    f.write(buffer, 0, len1);
//                }
//                f.close();
//            } catch (IOException e) {
//                Log.d("Error....", e.toString());
//            }
//
//        }
//    }
}


