package com.starwings.app.smartserchapp.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.starwings.app.smartserchapp.CardListing;
import com.starwings.app.smartserchapp.R;
import com.starwings.app.smartserchapp.SubCategoryList;
import com.starwings.app.smartserchapp.data.Category;
import com.starwings.app.smartserchapp.links.ApiLinks;

import java.util.ArrayList;

/**
 * Created by user on 24-10-2017.
 */

public class SubCategoryAdapter extends BaseAdapter {


    ArrayList<Category> categoryList;
    SubCategoryList parentscreen;

    public SubCategoryAdapter(ArrayList<Category> cList,SubCategoryList pscreen)
    {
        categoryList=cList;
        parentscreen=pscreen;

    }
    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView=parentscreen.getLayoutInflater().inflate(R.layout.lstsubcategoryrow,null);
        }


        LinearLayout wrapper=(LinearLayout)convertView.findViewById(R.id.wrapperlayout);
        ImageView imgIcon=(ImageView)convertView.findViewById(R.id.imgIcon);
        TextView txtCaption=(TextView)convertView.findViewById(R.id.txtCaption);
        Glide.with(parent.getContext())
                .load(ApiLinks.basegalLink+categoryList.get(position).getCategoryImage())
                .into(imgIcon);
        txtCaption.setText(categoryList.get(position).getCategoryName().toUpperCase());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 callCardListingPage(position);
            }
        });

        return convertView;
    }

    private void callCardListingPage(int position) {


        int subcategoryId=categoryList.get(position).getCategoryNumber();
        Intent cardListing=new Intent(parentscreen, CardListing.class);
        cardListing.putExtra("keyword",subcategoryId);
        cardListing.putExtra("keywordText",categoryList.get(position).getCategoryName());
        parentscreen.startActivity(cardListing);
    }
}
