package com.starwings.app.smartserchapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.starwings.app.smartserchapp.CardDetailPage;
import com.starwings.app.smartserchapp.R;
import com.starwings.app.smartserchapp.SmartSerch;
import com.starwings.app.smartserchapp.data.Cards;
import com.starwings.app.smartserchapp.links.ApiLinks;

/**
 * Created by user on 25-10-2017.
 */

public class CardSliderFragment extends Fragment implements View.OnClickListener {
    Cards current;
    CardDetailPage parent;
    BottomNavigationInterface mCallback;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_details, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        current=(Cards)getArguments().getSerializable("current");


        parent=(CardDetailPage)((AppCompatActivity)getActivity());
        parent.getSupportActionBar().setTitle(current.getCardname());
        ImageView imgcard=(ImageView)view.findViewById(R.id.imgCard);
//        Glide.with(getContext())
//                .load(ApiLinks.basegalLink+current.getFrontImage())
//                .placeholder(R.drawable.placeholder)
//                .into(imgcard);
        Glide.with(getContext())
                .load(ApiLinks.basegalLink+current.getFrontImage())
                .into(imgcard);

        TextView txtname =(TextView)view.findViewById(R.id.txtname);
        TextView txtplace=(TextView) view.findViewById(R.id.txtlocation);
        TextView txtweb=(TextView)view.findViewById(R.id.txtweb);
        TextView txtmail=(TextView)view.findViewById(R.id.txtmail);
        TextView txtnumber=(TextView)view.findViewById(R.id.txtnumber);
        TextView txtwhatsapp=(TextView)view.findViewById(R.id.txtwhatsapp);

        ImageButton btnCall=(ImageButton)view.findViewById(R.id.imgcall);
        ImageButton btnWeb=(ImageButton)view.findViewById(R.id.imgweb);
        ImageButton btnMail=(ImageButton)view.findViewById(R.id.imgemail);
        ImageButton btnWhatsapp=(ImageButton)view.findViewById(R.id.imgwhatsapp);
        ImageButton btnreport=(ImageButton)view.findViewById(R.id.imgreport);
        ImageButton btnSMS=(ImageButton)view.findViewById(R.id.imgsms);
       // ImageButton btnLocation=(ImageButton)view.findViewById(R.id.imglocation);




        SmartSerch appobj=(SmartSerch)parent.getApplication();
        txtname.setTypeface(appobj.getLatoregular());

        txtname.setText(current.getCardname());

        txtplace.setText(current.getPlace());
        txtplace.setTypeface(appobj.getLatoregular());


        if(!(current.getWeb().equals("")||current.getWeb().equals("NA"))) {
            String tmp=current.getWeb();
            tmp=tmp.replace("http://","");
            txtweb.setText(tmp);
        }
        else
        {
            txtweb.setText("No Website");
        }
        txtweb.setTypeface(appobj.getLatoregular());

        if(!(current.getWhatsapp().equals("")||current.getWhatsapp().equals("NA"))) {
            txtwhatsapp.setText(current.getWhatsapp());
        }
        else
        {
            txtwhatsapp.setText("No Whatsapp Number");
        }
        txtwhatsapp.setTypeface(appobj.getLatoregular());


        String phoneText="";
        for (int i=0;i<current.getPhone().size();i++)
        {
            phoneText=phoneText+current.getPhone().get(i)+" / ";
        }
        phoneText=phoneText.substring(0,phoneText.lastIndexOf("/"));
        txtnumber.setText(phoneText);

        String mailText="";
        for (int i=0;i<current.getMail().size();i++)
        {
            mailText=mailText+current.getMail().get(i)+" / ";
        }

        Log.e("MIDL",mailText);
        if(mailText.lastIndexOf("/")>=0) {
            mailText = mailText.substring(0, mailText.lastIndexOf("/"));
        }
        txtmail.setText(mailText);

        txtnumber.setTypeface(appobj.getLatoregular());
        txtmail.setTypeface(appobj.getLatoregular());


        btnCall.setOnClickListener(this);
        btnWeb.setOnClickListener(this);
        btnMail.setOnClickListener(this);
        btnWhatsapp.setOnClickListener(this);
        btnreport.setOnClickListener(this);
        btnSMS.setOnClickListener(this);
       // btnLocation.setOnClickListener(this);
        imgcard.setOnClickListener(this);


    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (BottomNavigationInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BottomNavigationInterface");
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.imgcall:
                mCallback.initiatePhone(current.getPhone().get(0));
                break;
            case R.id.imgweb:
                mCallback.openSite(current.getWeb());
                break;
            case R.id.imgemail:
                mCallback.sendMail(current.getMail().get(0));
                break;
            case R.id.imgCard:
                mCallback.previewImage(current);
                break;
            case R.id.imgwhatsapp:
                mCallback.openWhatsapp(current.getWhatsapp());
                break;
            case R.id.imgreport:
                mCallback.sendReport(current);
                break;
            case R.id.imgsms:
                mCallback.shareContent(current);
                break;
//            case R.id.imglocation:
//                mCallback.showMap(current);
//                break;
        }

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
