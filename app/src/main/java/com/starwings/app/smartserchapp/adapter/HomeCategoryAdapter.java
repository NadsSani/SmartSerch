package com.starwings.app.smartserchapp.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.starwings.app.smartserchapp.CardListing;
import com.starwings.app.smartserchapp.HomePageActivity;
import com.starwings.app.smartserchapp.R;
import com.starwings.app.smartserchapp.SmartSerch;
import com.starwings.app.smartserchapp.SubCategoryList;
import com.starwings.app.smartserchapp.TheaterListActivity;
import com.starwings.app.smartserchapp.data.Category;
import com.starwings.app.smartserchapp.links.ApiLinks;

import java.util.ArrayList;

/**
 * Created by user on 23-10-2017.
 */

public class HomeCategoryAdapter extends BaseAdapter implements Filterable {


   
    ArrayList<Category> categoryList;
    HomePageActivity parentScreen;
    private ValueFilter valueFilter;
    private ArrayList<Category> mStringFilterList;
    public HomeCategoryAdapter(ArrayList<Category> catList, HomePageActivity parent)
    {
        categoryList=catList;
        mStringFilterList=catList   ;
        parentScreen=parent;
        getFilter();
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
            convertView=parentScreen.getLayoutInflater().inflate(R.layout.gridviewrow,null);
        }


        LinearLayout wrapper=(LinearLayout)convertView.findViewById(R.id.wrapperlayout);
        ImageView imgIcon=(ImageView)convertView.findViewById(R.id.imgIcon);
        TextView txtCaption=(TextView)convertView.findViewById(R.id.txtCaption);
        Glide.with(parent.getContext())
                .load(ApiLinks.basegalLink+categoryList.get(position).getCategoryImage())
                .into(imgIcon);
        txtCaption.setText(categoryList.get(position).getCategoryName().toUpperCase());
        SmartSerch appobj=(SmartSerch)parentScreen.getApplication();
        txtCaption.setTypeface(appobj.getLatobold());

        if(position%2==0)
        {
            convertView.setBackgroundColor(parentScreen.getResources().getColor(R.color.colorCatEVE));
        }
        else
        {
            convertView.setBackgroundColor(parentScreen.getResources().getColor(R.color.colorCatODD));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(categoryList.get(position).getCategoryNumber()==1)
                {
                    handleTheaterSelection(categoryList.get(position));
                }
                else
                {
                    handleCategorySelection(categoryList.get(position));
                }
            }
        });
     return convertView;
    }

    private void handleTheaterSelection(Category category) {

        Intent theaterIntent=new Intent(parentScreen, TheaterListActivity.class);
        parentScreen.startActivity(theaterIntent);

    }

    private void handleCategorySelection(Category category) {
        if(category.getHasSub()==1)
        {
            callSubCategoryPage(category);
        }
        else
        {
            callCardListPage(category);
        }
    }

    private void callCardListPage(Category category) {
        int subcategoryId=category.getCategoryNumber();
        Intent cardListing=new Intent(parentScreen, CardListing.class);
        cardListing.putExtra("keyword",subcategoryId);
        cardListing.putExtra("keywordText",category.getCategoryName());
        parentScreen.startActivity(cardListing);
    }

    private void callSubCategoryPage(Category selected) {

        Intent subCategoryIntent=new Intent(parentScreen,SubCategoryList.class);
        subCategoryIntent.putExtra("categoryselection",selected);
        parentScreen.startActivity(subCategoryIntent);
    }

    @Override
    public Filter getFilter() {
        if(valueFilter==null) {

            valueFilter=new ValueFilter();
        }

        return valueFilter;
    }

    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                ArrayList<Category> filterList=new ArrayList<Category>();
                for(int i=0;i<mStringFilterList.size();i++){
                    if((mStringFilterList.get(i).getCategoryName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        Category categories = new Category();
                        categories.setCategoryName(mStringFilterList.get(i).getCategoryName());
                        categories.setCategoryNumber(mStringFilterList.get(i).getCategoryNumber());
                        categories.setCategoryImage(mStringFilterList.get(i).getCategoryImage());

                        categories.setCardCount(mStringFilterList.get(i).getCardCount());
                        categories.setCardColor(mStringFilterList.get(i).getCardColor());
                        categories.setHasSub(mStringFilterList.get(i).getHasSub());
                        filterList.add(categories);
                    }
                }
                results.count=filterList.size();
                results.values=filterList;
            }else{
                results.count=mStringFilterList.size();
                results.values=mStringFilterList;
            }
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            categoryList=(ArrayList<Category>) results.values;
            notifyDataSetChanged();
        }
    }
}
