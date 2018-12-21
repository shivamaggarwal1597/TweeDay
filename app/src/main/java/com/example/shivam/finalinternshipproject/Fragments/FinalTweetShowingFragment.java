package com.example.shivam.finalinternshipproject.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shivam.finalinternshipproject.DataModels.CategoryObject;
import com.example.shivam.finalinternshipproject.DataModels.SharedPrefObject;
import com.example.shivam.finalinternshipproject.DataModels.TweetCompareModel;
import com.example.shivam.finalinternshipproject.DataModels.User_config;
import com.example.shivam.finalinternshipproject.DataModels.WrapperTwitterHandle;
import com.example.shivam.finalinternshipproject.R;

import com.example.shivam.finalinternshipproject.utils.TinyDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;

public class FinalTweetShowingFragment extends Fragment {
    TinyDB tinyDB;
    View view;
    int no_of_tweets_selected,no_of_notifications;
    SharedPrefObject config;
    List<TweetCompareModel> comp_list,sub_list;
    TwitterApiClient twitterApiClient;
    List<CategoryObject> categoryObjectList;
    List<Tweet> homeList,final_list_tweet,adapter_pass_list;
    FirebaseDatabase firebaseDatabase ;
    DatabaseReference databaseReference;
    List<WrapperTwitterHandle> active_list_handles;
    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;

    public FinalTweetShowingFragment() {
    }
    public void getNoOfTweetsSelected(){
        databaseReference.child("users").child(config.getUser_name()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User_config user_config = dataSnapshot.getValue(User_config.class);
                no_of_tweets_selected = user_config.getNo_of_tweets_selected();
                no_of_notifications = user_config.getNo_of_notifications();
                getActiveCatogariesList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void getActiveCatogariesList(){
    databaseReference.child("catogaries").child(config.getUser_name()).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
           CategoryObject categoryObject = dataSnapshot1.getValue(CategoryObject.class);
           if (categoryObject.active_category()){
               categoryObjectList.add(categoryObject);
           }
        }
        getHandles();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list3, container, false);
        twitterApiClient = TwitterCore.getInstance().getApiClient();
        final TwitterSession activeSession = TwitterCore.getInstance()
                .getSessionManager().getActiveSession();
        comp_list = new ArrayList<>();
        sub_list = new ArrayList<>();
        config = tinyDB.getObject("user_config",SharedPrefObject.class);
        active_list_handles = new ArrayList<>();

        adapter_pass_list = new ArrayList<>();
        final_list_tweet = new ArrayList<>();
        categoryObjectList = new ArrayList<>();
        getNoOfTweetsSelected();
        return view;
    }
    public void setCustomAdapt(){
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter2( mListener,adapter_pass_list,context));

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        tinyDB = new TinyDB(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void getHandles(){

        databaseReference.child("handles").child(config.getUser_name()).child("all_handles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    WrapperTwitterHandle twitterFriend = new WrapperTwitterHandle(
                            ds.child("id").getValue(Long.class)
                            , ds.child("parent_category_name").getValue(String.class)
                            , (Boolean) ds.child("is_active").getValue()
                            , ds.child("name").getValue(String.class)
                            , ds.child("profilePictureUrl").getValue(String.class)
                            , ds.child("screenName").getValue(String.class));
                    for (CategoryObject categoryObject: categoryObjectList){
                        if (twitterFriend.getParent_category_name().equals(categoryObject.getCategory_name())){
                            if (twitterFriend.isIs_active()) {
                                active_list_handles.add(twitterFriend);
                            }
                        }
                    }

                }
                make_call();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void make_call(){
        StatusesService ss = twitterApiClient.getStatusesService();
        final Call<List<Tweet>> homeCall = ss.homeTimeline(199,null,null,null,null,null,null);
        homeCall.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                homeList = result.data;
                int size = homeList.size();
                for (Tweet t : homeList){
                    for (WrapperTwitterHandle tf: active_list_handles){
                        if (t.user.name.equals(tf.getName())){
                            final_list_tweet.add(t);
                            comp_list.add(new TweetCompareModel(t.getId(),t.favoriteCount));


                        }
                    }
                }
                Collections.sort(comp_list, new Comparator<TweetCompareModel>() {
                    @Override
                    public int compare(TweetCompareModel s,TweetCompareModel t1) {
                        return t1.getFav_count()-s.getFav_count();
                    }
                });
                if(comp_list.size()<no_of_tweets_selected){
                    sub_list =  comp_list;
                }else{
                    sub_list = comp_list.subList(0,no_of_tweets_selected);

                }
                for (TweetCompareModel tcm: sub_list){
                    for (Tweet tweet: final_list_tweet){
                        if (tweet.id==tcm.id){
                            adapter_pass_list.add(tweet);
                        }
                    }
                }
                setCustomAdapt();


            }

            @Override
            public void failure(TwitterException exception) {

            }
        });



    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Tweet item);
    }


}
