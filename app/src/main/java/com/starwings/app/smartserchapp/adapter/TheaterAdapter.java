package com.starwings.app.smartserchapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.starwings.app.smartserchapp.R;
import com.starwings.app.smartserchapp.SmartSerch;
import com.starwings.app.smartserchapp.TheaterDetailPage;
import com.starwings.app.smartserchapp.TheaterListActivity;
import com.starwings.app.smartserchapp.data.Theater;

import java.util.ArrayList;

/**
 * Created by user on 06-12-2017.
 */

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder> implements Filterable {
    ArrayList<Theater> theatersList;
    private Context mContext;
    private TheaterAdapter.ValueFilter valueFilter;
    private ArrayList<Theater> mStringFilterList;
    public TheaterAdapter(Context mContext, ArrayList<Theater> theaters) {
        this.mContext = mContext;
        this.theatersList = theaters;
        this.mStringFilterList=theaters;
    }
    @Override
    public TheaterAdapter.TheaterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_theater_card, parent, false);


        return new TheaterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TheaterViewHolder holder, final int position) {
        Theater theater = theatersList.get(position);
        holder.title.setText(theater.getName());
        holder.place.setText(theater.getLocation());
        holder.Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailPage(position);
            }
        });
    }

    private void showDetailPage(int position) {
        SmartSerch appobj=(SmartSerch)((TheaterListActivity)mContext).getApplication();
        appobj.setTheaterlist(theatersList);
        Intent cdpage=new Intent(mContext,TheaterDetailPage.class);
        cdpage.putExtra("selected",""+position);
        mContext.startActivity(cdpage);

    }

    @Override
    public int getItemCount() {
        return theatersList.size();
    }

    @Override
    public Filter getFilter() {
        if(valueFilter==null) {

            valueFilter=new TheaterAdapter.ValueFilter();
        }

        return valueFilter;
    }
    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                ArrayList<Theater> filterList=new ArrayList<Theater>();
                for(int i=0;i<mStringFilterList.size();i++){
                    if((mStringFilterList.get(i).getDistrict().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        Theater tmpTheater=new Theater();
                        tmpTheater.setSlno(mStringFilterList.get(i).getSlno());
                        tmpTheater.setTheaterCode(mStringFilterList.get(i).getTheaterCode());
                        tmpTheater.setCity(mStringFilterList.get(i).getCity());
                        tmpTheater.setDistrict(mStringFilterList.get(i).getDistrict());
                        tmpTheater.setName(mStringFilterList.get(i).getName());
                        tmpTheater.setLocation(mStringFilterList.get(i).getLocation());
                        tmpTheater.setSeating(mStringFilterList.get(i).getSeating());
                        tmpTheater.setRating(mStringFilterList.get(i).getRating());
                        tmpTheater.setShowTimes(mStringFilterList.get(i).getShowTimes());
                        filterList.add(tmpTheater);
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
            theatersList=(ArrayList<Theater>) results.values;
            notifyDataSetChanged();
        }


    }
    public class TheaterViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public TextView place;
        public Button Details;

        public TheaterViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtName);
            thumbnail = (ImageView) view.findViewById(R.id.imgIcon);
            place = (TextView) view.findViewById(R.id.txtLocation);
            Details=(Button)view.findViewById(R.id.btn_details);

        }
    }
}
