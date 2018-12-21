package com.example.shivam.finalinternshipproject.DataModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivam on 21/12/17.
 */

public class CategoryObject {

    public String category_name;
    public boolean active_category = false;

    public CategoryObject() {
    }

    public CategoryObject(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public boolean active_category() {
        return active_category;
    }

    public void setActive_category(boolean active_category) {
        this.active_category = active_category;
    }
}
