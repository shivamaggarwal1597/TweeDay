package com.example.shivam.finalinternshipproject.DataModels;

/**
 * Created by shivam on 21/12/17.
 */

public class TweetCompareModel {

    public long id = 0l;
    public int fav_count = 0;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFav_count() {
        return fav_count;
    }

    public void setFav_count(int fav_count) {
        this.fav_count = fav_count;
    }

    public TweetCompareModel(long id, int fav_count) {
        this.id = id;
        this.fav_count = fav_count;
    }
}