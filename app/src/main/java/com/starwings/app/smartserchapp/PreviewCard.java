package com.starwings.app.smartserchapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.starwings.app.smartserchapp.components.TouchImageView;
import com.starwings.app.smartserchapp.data.Cards;
import com.starwings.app.smartserchapp.links.ApiLinks;

/**
 * Created by user on 26-10-2017.
 */

public class PreviewCard extends AppCompatActivity implements View.OnClickListener{
    ViewFlipper flipper;

   Cards current;
    private int mFlipping;
    Button frontButton,backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        current=(Cards)getIntent().getSerializableExtra("currentCard");
        flipper = (ViewFlipper) findViewById(R.id.preview_flip);

        frontButton=(Button)findViewById(R.id.btnFront);
        backButton=(Button)findViewById(R.id.btnBack);

        TouchImageView imgfront = new TouchImageView(this);
        imgfront.setMaxZoom(4f);
        Glide.with(this)
                .load(ApiLinks.basegalLink+current.getFrontImage())
                .into(imgfront);
        flipper.addView(imgfront);

        TouchImageView imgback= new TouchImageView(this);
        String backLink=ApiLinks.basegalLink+current.getBackImage();
        if(current.getBackImage().contains("."))
        {
            imgback.setMaxZoom(4f);
            Glide.with(this)
                    .load(ApiLinks.basegalLink+current.getBackImage())
                    .into(imgback);
            flipper.addView(imgback);
            backButton.setVisibility(View.VISIBLE);
        }
        else
        {
            backButton.setVisibility(View.GONE);
        }






        backButton.setOnClickListener(this);
        frontButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
       callClickEvent(v);
    }

    private void callClickEvent(View v) {
        switch (v.getId())
        {
            case R.id.btnFront:
                flipper.startFlipping();
                flipper.setDisplayedChild(0);
                flipper.stopFlipping();
                break;
            case R.id.btnBack:
                flipper.startFlipping();
                flipper.setDisplayedChild(1);
                flipper.stopFlipping();
                break;
        }
    }
}
