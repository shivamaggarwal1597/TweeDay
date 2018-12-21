package com.example.shivam.finalinternshipproject.DataModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shivam on 20/12/17.
 */

public class User_config {

    //public boolean isLoggedIn = false;
    public int no_of_tweets_selected = 5;
    public int no_of_notifications = 4;
    public String name_of_user = "";
    public boolean set_catogaries = false;
    public boolean set_handles = false;
    public int user_login_count=0;
    public long id_of_user = 0l;
    public User_config() {

        //for firebase
    }

    public User_config( String name_of_user, long id_of_user) {
      //  this.isLoggedIn = isLoggedIn;
        this.name_of_user = name_of_user;
        this.id_of_user = id_of_user;
    }

    public int getNo_of_tweets_selected() {
        return no_of_tweets_selected;
    }

    public void setNo_of_tweets_selected(int no_of_tweets_selected) {
        this.no_of_tweets_selected = no_of_tweets_selected;
    }

    public int getNo_of_notifications() {
        return no_of_notifications;
    }

    public void setNo_of_notifications(int no_of_notifications) {
        this.no_of_notifications = no_of_notifications;
    }

    public String getName_of_user() {
        return name_of_user;
    }

    public void setName_of_user(String name_of_user) {
        this.name_of_user = name_of_user;
    }

    public boolean isSet_catogaries() {
        return set_catogaries;
    }

    public void setSet_catogaries(boolean set_catogaries) {
        this.set_catogaries = set_catogaries;
    }

    public boolean isSet_handles() {
        return set_handles;
    }

    public void setSet_handles(boolean set_handles) {
        this.set_handles = set_handles;
    }

    public int getUser_login_count() {
        return user_login_count;
    }

    public void setUser_login_count(int user_login_count) {
        this.user_login_count = user_login_count;
    }

    public long getId_of_user() {
        return id_of_user;
    }

    public void setId_of_user(long id_of_user) {
        this.id_of_user = id_of_user;
    }
}
