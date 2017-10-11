package com.example.sanghoyoun.myanim;

/**
 * Created by sanghoyoun on 2017. 10. 4..
 */

public class ListItem {

    private String profileImage;
    private String animTitle;
    private String animYear;
    private Double favorRate;

    public ListItem() {
    }

    public ListItem(String profileImage, String animTitle, String animYear, Double favorRate) {
        this.profileImage = profileImage;
        this.animTitle = animTitle;
        this.animYear = animYear;
        this.favorRate = favorRate;
    }


    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAnimTitle() {
        return animTitle;
    }

    public void setAnimTitle(String animTitle) {
        this.animTitle = animTitle;
    }

    public String getAnimYear() {
        return animYear;
    }

    public void setAnimYear(String animYear) {
        this.animYear = animYear;
    }

    public Double getFavorRate() {
        return favorRate;
    }

    public void setFavorRate(Double favorRate) {
        this.favorRate = favorRate;
    }
}
