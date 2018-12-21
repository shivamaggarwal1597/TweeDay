package com.example.shivam.finalinternshipproject.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 22/12/17.
 */

public class WrapperTwitterHandle {

    @SerializedName("id")
    @Expose
    private long id;

    public String parent_category_name = "none";

    boolean is_active = false;

    public String getParent_category_name() {
        return parent_category_name;
    }

    public void setParent_category_name(String parent_category_name) {
        this.parent_category_name = parent_category_name;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("profile_image_url_https")
    @Expose
    private String profilePictureUrl;

    @SerializedName("screen_name")
    @Expose
    private String screenName;

    public WrapperTwitterHandle() {
    }

    public WrapperTwitterHandle(long id, String parent_category_name, boolean is_active, String name, String profilePictureUrl, String screenName) {
        this.id = id;
        this.parent_category_name = parent_category_name;
        this.is_active = is_active;
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
        this.screenName = screenName;
    }

    public WrapperTwitterHandle(long id, String name, String profilePictureUrl, String screenName) {
        this.id = id;
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
        this.screenName = screenName;
    }


    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
