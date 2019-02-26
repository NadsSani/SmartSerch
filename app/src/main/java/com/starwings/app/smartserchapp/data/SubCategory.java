package com.starwings.app.smartserchapp.data;

/**
 * Created by user on 24-10-2017.
 */

public class SubCategory {

    private String SubCategoryName;
    private String SubCategoryImage;
    private int SubCategoryNumber;
    private int SubCardCount;
    private int CatId;

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public String getSubCategoryImage() {
        return SubCategoryImage;
    }

    public void setSubCategoryImage(String subCategoryImage) {
        SubCategoryImage = subCategoryImage;
    }

    public int getSubCategoryNumber() {
        return SubCategoryNumber;
    }

    public void setSubCategoryNumber(int subCategoryNumber) {
        SubCategoryNumber = subCategoryNumber;
    }

    public int getSubCardCount() {
        return SubCardCount;
    }

    public void setSubCardCount(int subCardCount) {
        SubCardCount = subCardCount;
    }

    public int getCatId() {
        return CatId;
    }

    public void setCatId(int catId) {
        CatId = catId;
    }
}
