package com.example.shivam.finalinternshipproject.DataModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shivam on 21/12/17.
 */

public class DatabaseObject {

    public List<TwitterFriends> active_list = new ArrayList<>();
    public List<TwitterFriends> all_handles_list = new ArrayList<>();

    public DatabaseObject() {

    }
    public List<TwitterFriends> getActive_list() {
        return active_list;
    }

    public void setActive_list(List<TwitterFriends> active_list) {
        this.active_list = active_list;
    }

    public List<TwitterFriends> getAll_handles_list() {
        return all_handles_list;
    }

    public void setAll_handles_list(List<TwitterFriends> all_handles_list) {
        this.all_handles_list = all_handles_list;
    }
}
