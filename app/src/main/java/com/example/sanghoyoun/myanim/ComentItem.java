package com.example.sanghoyoun.myanim;

/**
 * Created by sanghoyoun on 2017. 10. 13..
 */

public class ComentItem {
    String user_ID;
    String coment;
    int    anim_ID;
    int    country;

    public ComentItem(){}

    public ComentItem(String user_ID, String coment, int anim_ID, int country) {
        this.user_ID = user_ID;
        this.coment = coment;
        this.anim_ID = anim_ID;

        if(country == 0){
            this.country = R.drawable.japan;
        } else if(country == 1){
            this.country = R.drawable.korea;
        }
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public int getAnim_ID() {
        return anim_ID;
    }

    public void setAnim_ID(int anim_ID) {
        this.anim_ID = anim_ID;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        if(country == 0){
            this.country = R.drawable.japan;
        } else if(country == 1){
            this.country = R.drawable.korea;
        }
    }
}
