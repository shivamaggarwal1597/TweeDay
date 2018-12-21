package com.example.shivam.finalinternshipproject.DataModels;

/**
 * Created by shivam on 21/12/17.
 */

public class SharedPrefObject {
    public String user_name =" ";
    public int login_count=0;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getLogin_count() {
        return login_count;
    }

    public void setLogin_count(int login_count) {
        this.login_count = login_count;
    }

    public SharedPrefObject(String user_name, int login_count) {
        this.user_name = user_name;
        this.login_count = login_count;
    }
}
