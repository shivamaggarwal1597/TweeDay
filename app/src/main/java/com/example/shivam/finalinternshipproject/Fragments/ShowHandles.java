package com.example.shivam.finalinternshipproject.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.shivam.finalinternshipproject.DataModels.FriendsResponseModel;
import com.example.shivam.finalinternshipproject.DataModels.SharedPrefObject;
import com.example.shivam.finalinternshipproject.DataModels.TwitterFriends;
import com.example.shivam.finalinternshipproject.DataModels.WrapperTwitterHandle;
import com.example.shivam.finalinternshipproject.R;
import com.example.shivam.finalinternshipproject.utils.MyTwitterApiClient;
import com.example.shivam.finalinternshipproject.utils.TinyDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class ShowHandles extends Fragment {
    private int mColumnCount = 1;
    View view;
    TinyDB tinyDB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPrefObject sharedPrefObject;
    boolean set_handles;
    Context contextl;
    List<TwitterFriends> twitterFriends,tf;
    List<WrapperTwitterHandle> wrapperTwitterHandles;
    long id_of_user,cursor;
    private OnListHandlesInteractionListener mListener;
    public ShowHandles() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    public void getHandles(){
        final TwitterSession activeSession = TwitterCore.getInstance()
                .getSessionManager().getActiveSession();
        MyTwitterApiClient my = new MyTwitterApiClient(activeSession);
        if (set_handles){
        databaseReference.child("handles").child(sharedPrefObject.getUser_name()).child("all_handles").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            wrapperTwitterHandles.clear();
            for (DataSnapshot ds : dataSnapshot.getChildren()){
                WrapperTwitterHandle twitterFriend = new WrapperTwitterHandle(
                         ds.child("id").getValue(Long.class)
                        ,ds.child("parent_category_name").getValue(String.class)
                        ,(Boolean) ds.child("is_active").getValue()
                        ,ds.child("name").getValue(String.class)
                        ,ds.child("profilePictureUrl").getValue(String.class)
                        ,ds.child("screenName").getValue(String.class));
                wrapperTwitterHandles.add(twitterFriend);
                Log.e("If case: ",wrapperTwitterHandles.size()+" ");
                setCustomAdapter();
            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }
        else {
            my.getCustomService().list(id_of_user,-1).
                    enqueue(new retrofit2.Callback<FriendsResponseModel>() {
                        @Override
                        public void onResponse(Call<FriendsResponseModel> call, Response<FriendsResponseModel> response) {
                            tf = fetchResults(response);
                            twitterFriends.addAll(tf);
                            Log.e("onResponse", response.toString() +
                                    " size : " + twitterFriends.size() + " next_cursor : " + response.body().getNextCursorStr());
                            cursor = Long.parseLong(response.body().getNextCursorStr());
                            //Log.e("SHowing Handles", "working");
                          for (TwitterFriends tff:twitterFriends ){

                              WrapperTwitterHandle wrapperTwitterHandle = new WrapperTwitterHandle(tff.getId(),
                                      tff.getName(),
                                      tff.getProfilePictureUrl(),
                                      tff.getScreenName());
                              wrapperTwitterHandles.add(wrapperTwitterHandle);
                          }
                            for (WrapperTwitterHandle wrapperTwitterHandle : wrapperTwitterHandles){
                                databaseReference.child("handles")
                                        .child(sharedPrefObject.getUser_name())
                                        .child("all_handles")
                                        .child(wrapperTwitterHandle.getScreenName())
                                        .setValue(wrapperTwitterHandle);
                          }
                            databaseReference.child("users")
                                    .child(sharedPrefObject.getUser_name())
                                    .child("set_handles")
                                    .setValue(true);
                            setCustomAdapter();
                        }

                        @Override
                        public void onFailure(Call<FriendsResponseModel> call, Throwable t) {
                            Log.e("Showin handles", "Failure");
                        }
                    });

        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_handles_list, container, false);
        sharedPrefObject= tinyDB.getObject("user_config", SharedPrefObject.class);
        twitterFriends = new ArrayList<>();
        wrapperTwitterHandles = new ArrayList<>();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                set_handles = (Boolean)dataSnapshot.child(sharedPrefObject.getUser_name()).child("set_handles").getValue();
              //  Log.e("Set Handles: ",set_handles+" ");
                id_of_user = (Long) dataSnapshot.child(sharedPrefObject.getUser_name()).child("id_of_user").getValue();
                getHandles();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void setCustomAdapter(){
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new HandlesRecyclerViewAdapter(wrapperTwitterHandles, mListener,context));
        }

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListHandlesInteractionListener) {
            mListener = (OnListHandlesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        contextl = context;
        tinyDB = new TinyDB(context);
    }
    private List<TwitterFriends> fetchResults(Response<FriendsResponseModel> response) {
        FriendsResponseModel responseModel = response.body();
        return responseModel.getResults();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnListHandlesInteractionListener {
        void onListHandlesInteraction(WrapperTwitterHandle item);
    }
}
