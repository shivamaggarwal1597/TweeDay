package com.example.shivam.finalinternshipproject.utils;

import com.example.shivam.finalinternshipproject.DataModels.FriendsResponseModel;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by shivam on 21/12/17.
 */

public interface FollowingHandlesListService {
    @GET("users/show.json")
    Call<User> getUserDetails(@Query("screen_name") String screenName);
    @GET("1.1/friends/list.json")
    Call<FriendsResponseModel> list(@Query("user_id") long id, @Query("cursor") long cursor);
    @GET("/1.1/friends/list.json")
    Call<List<User>> friends(@Query("user_id") long user_id,
                             @Query("screen_name") String screen_name,
                             @Query("cursor")long cursor,
                             @Query("count") int count,
                             @Query("skip_status") boolean skip_stasus,
                             @Query("include_user_entities") boolean include_user_entities);


}
