package com.starwings.app.smartserchapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starwings.app.smartserchapp.R;
import com.starwings.app.smartserchapp.SmartSerch;
import com.starwings.app.smartserchapp.TheaterDetailPage;
import com.starwings.app.smartserchapp.data.Cards;
import com.starwings.app.smartserchapp.data.Theater;

/**
 * Created by user on 07-12-2017.
 */

public class TheaterSlideFragment extends Fragment implements View.OnClickListener{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    Theater current;
    TheaterDetailPage parent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_theater_details, container, false);

        return rootView;
    }
    @Override
    public void onClick(View v) {

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        current=(Theater)getArguments().getSerializable("current");


        parent=(TheaterDetailPage)((AppCompatActivity)getActivity());
        parent.getSupportActionBar().setTitle(current.getName());


        TextView txtname =(TextView)view.findViewById(R.id.txtname);
        TextView txtplace=(TextView) view.findViewById(R.id.txtlocation);
        TextView city=(TextView)view.findViewById(R.id.txtcity);
        TextView txtdist=(TextView)view.findViewById(R.id.txtdistrict);
        TextView txtnumber=(TextView)view.findViewById(R.id.txtseating);
        TextView txtrate=(TextView)view.findViewById(R.id.txtrating);
        TextView txtshowtimes=(TextView)view.findViewById(R.id.txtshowtime);
        // ImageButton btnLocation=(ImageButton)view.findViewById(R.id.imglocation);




        SmartSerch appobj=(SmartSerch)parent.getApplication();
        txtname.setTypeface(appobj.getLatoregular());

        txtname.setText(current.getName());

        txtplace.setText(current.getLocation());
        txtplace.setTypeface(appobj.getLatoregular());
        city.setText(current.getCity());
        city.setTypeface(appobj.getLatoregular());
        txtdist.setText(current.getDistrict());
        txtdist.setTypeface(appobj.getLatoregular());
        txtnumber.setText(current.getSeating());
        txtnumber.setTypeface(appobj.getLatoregular());

        txtrate.setText(current.getRating());
        txtrate.setTypeface(appobj.getLatoregular());

        txtshowtimes.setText(current.getShowTimes());
        txtshowtimes.setTypeface(appobj.getLatoregular());



    }

    public interface BottomNavigationInterface
    {
        public void initiatePhone(String phone);
        public void sendMail(String mail);
        public void openSite(String site);
        public void openWhatsapp(String phone);
        public void previewImage(Cards currentcard);
        public void sendReport(Cards currentcard);
        public  void shareContent(Cards current);
        public void showMap(Cards current);

    }
}
