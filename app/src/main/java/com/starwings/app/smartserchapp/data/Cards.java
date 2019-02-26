package com.starwings.app.smartserchapp.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 25-10-2017.
 */

public class Cards implements Serializable {
    private String id;
    private String cardname;
    private String paidstatus;
    private String frontImage;
    private String backImage;
    private String dateOfEntry;
    private String place;
    private String district;
    private ArrayList<String> phone;
    private ArrayList<String> mail;
    private String web;
    private String districtname;
    private String whatsapp;
    private ArrayList<Category> keywords;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getPaidstatus() {
        return paidstatus;
    }

    public void setPaidstatus(String paidstatus) {
        this.paidstatus = paidstatus;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public String getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(String dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }



    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }



    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    public void setPhone(ArrayList<String> phone) {
        this.phone = phone;
    }

    public ArrayList<String> getMail() {
        return mail;
    }

    public void setMail(ArrayList<String> mail) {
        this.mail = mail;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public ArrayList<Category> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<Category> keywords) {
        this.keywords = keywords;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }
}
