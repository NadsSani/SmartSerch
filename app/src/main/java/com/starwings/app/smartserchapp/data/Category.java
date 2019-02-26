package com.starwings.app.smartserchapp.data;

import java.io.Serializable;

/**
 * Created by user on 23-10-2017.
 */

public class Category implements Serializable {

    private String CategoryName;
    private String CategoryImage;
    private int CategoryNumber;
    private int CardCount;
    private String CardColor;
    private int hasSub;

    private int parentCategory;

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    public int getCategoryNumber() {
        return CategoryNumber;
    }

    public void setCategoryNumber(int categoryNumber) {
        CategoryNumber = categoryNumber;
    }

    public int getCardCount() {
        return CardCount;
    }

    public void setCardCount(int cardCount) {
        CardCount = cardCount;
    }

    public String getCardColor() {
        return CardColor;
    }

    public void setCardColor(String cardColor) {
        CardColor = cardColor;
    }

    public int getHasSub() {
        return hasSub;
    }

    public void setHasSub(int hasSub) {
        this.hasSub = hasSub;
    }


    public int getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(int parentCategory) {
        this.parentCategory = parentCategory;
    }

}
