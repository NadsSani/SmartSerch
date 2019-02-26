package com.starwings.app.smartserchapp.data;

import java.io.Serializable;

/**
 * Created by user on 06-12-2017.
 */

public class Theater implements Serializable {
    private int slno;
    private String TheaterCode;
    private String City;
    private String District;
    private String Name;
    private String Location;
    private String Seating;
    private String Rating;
    private String ShowTimes;

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getTheaterCode() {
        return TheaterCode;
    }

    public void setTheaterCode(String theaterCode) {
        TheaterCode = theaterCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getSeating() {
        return Seating;
    }

    public void setSeating(String seating) {
        Seating = seating;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getShowTimes() {
        return ShowTimes;
    }

    public void setShowTimes(String showTimes) {
        ShowTimes = showTimes;
    }
}
