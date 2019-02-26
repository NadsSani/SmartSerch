package com.starwings.app.smartserchapp.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.starwings.app.smartserchapp.CardDetailPage;
import com.starwings.app.smartserchapp.CardListing;
import com.starwings.app.smartserchapp.R;
import com.starwings.app.smartserchapp.SmartSerch;
import com.starwings.app.smartserchapp.data.Cards;
import com.starwings.app.smartserchapp.data.Category;
import com.starwings.app.smartserchapp.links.ApiLinks;

import java.util.ArrayList;

/**
 * Created by user on 24-10-2017.
 */

public class CardsAdapter extends BaseAdapter implements Filterable {


    ArrayList<Cards> cardsList;
    CardListing parentscreen;
    private ValueFilter valueFilter;
    private ArrayList<Cards> mStringFilterList;
    public CardsAdapter(ArrayList<Cards> cList, CardListing pscreen)
    {
        cardsList=cList;
        parentscreen=pscreen;
        mStringFilterList=cList   ;
        getFilter();
    }
    @Override
    public int getCount() {
        return cardsList.size();
    }

    @Override
    public Object getItem(int position) {
        return cardsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView=parentscreen.getLayoutInflater().inflate(R.layout.lstcardrow,null);
        }



        ImageView imgIcon=(ImageView)convertView.findViewById(R.id.imgIcon);
        TextView txtCaption=(TextView)convertView.findViewById(R.id.txtCaption);
        Glide.with(parent.getContext())
                .load(ApiLinks.basegalLink+cardsList.get(position).getFrontImage())
                .into(imgIcon);
        txtCaption.setText(cardsList.get(position).getCardname().toUpperCase());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 callCardDetailPage(position);
            }
        });

        return convertView;
    }

    private void callCardDetailPage(int position) {
        SmartSerch appobj=(SmartSerch)parentscreen.getApplication();
        appobj.setCurrent(cardsList);
        Intent cdpage=new Intent(parentscreen,CardDetailPage.class);
        cdpage.putExtra("selected",""+position);
        parentscreen.startActivity(cdpage);
    }

    @Override
    public Filter getFilter() {
        if(valueFilter==null) {

            valueFilter=new ValueFilter();
        }

        return valueFilter;
    }
    private boolean filterByKeywords(Cards card,CharSequence keyword)
    {
        boolean matched=false;
        ArrayList<Category> keywords=card.getKeywords();
        for(int j=0;j<keywords.size();j++)
        {
            if(keywords.get(j).getCategoryName().trim().toLowerCase().equals(keyword.toString().trim().toLowerCase()))
            {
                matched=true;
                break;
            }
        }
        return matched;
    }
    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                ArrayList<Cards> filterList=new ArrayList<Cards>();
                for(int i=0;i<mStringFilterList.size();i++){
                    if((mStringFilterList.get(i).getDistrictname().toUpperCase())
                            .contains(constraint.toString().toUpperCase())||(mStringFilterList.get(i).getCardname().toUpperCase())
                            .contains(constraint.toString().toUpperCase())||filterByKeywords(mStringFilterList.get(i),constraint)||mStringFilterList.get(i).getPlace().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        Cards temp = new Cards();
                        temp.setId(mStringFilterList.get(i).getId());
                        temp.setCardname(mStringFilterList.get(i).getCardname());

                        temp.setPaidstatus(mStringFilterList.get(i).getPaidstatus());
                        temp.setFrontImage(mStringFilterList.get(i).getFrontImage());
                        temp.setBackImage(mStringFilterList.get(i).getBackImage());
                        temp.setDateOfEntry(mStringFilterList.get(i).getDateOfEntry());
                        temp.setDistrict(mStringFilterList.get(i).getDistrict());
                        temp.setPhone(mStringFilterList.get(i).getPhone());
                        temp.setPlace(mStringFilterList.get(i).getPlace());
                        temp.setWeb(mStringFilterList.get(i).getWeb());
                        temp.setMail(mStringFilterList.get(i).getMail());
                        temp.setWhatsapp(mStringFilterList.get(i).getWhatsapp());
                        temp.setDistrictname(mStringFilterList.get(i).getDistrictname());
                        filterList.add(temp);
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
            cardsList=(ArrayList<Cards>) results.values;
            notifyDataSetChanged();
        }
    }
}
