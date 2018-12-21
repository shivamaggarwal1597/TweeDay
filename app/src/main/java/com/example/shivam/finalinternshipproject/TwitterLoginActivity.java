package com.example.shivam.finalinternshipproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.shivam.finalinternshipproject.DataModels.SharedPrefObject;
import com.example.shivam.finalinternshipproject.DataModels.User_config;
import com.example.shivam.finalinternshipproject.utils.TinyDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;
import java.util.List;

public class TwitterLoginActivity extends AppCompatActivity {
    TwitterLoginButton loginButton;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        //user_config = new User_config();
        tinyDB = new TinyDB(getApplicationContext());
        if (tinyDB.getObject("user_config",SharedPrefObject.class)!=null){
            String name  = tinyDB.getObject("user_config",SharedPrefObject.class).user_name;
            Toast.makeText(TwitterLoginActivity.this,"Welcome "+name,Toast.LENGTH_SHORT).show();
            login();
        }
        setContentView(R.layout.activity_twitter_login);
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);


        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                User_config user_config = new User_config();
                String secret = authToken.secret;
                user_config.setName_of_user(session.getUserName());
                user_config.setId_of_user(session.getUserId());
                user_config.setUser_login_count(1);
                databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            User_config user_config = ds.getValue(User_config.class);
                            ds.child("name_of_user").getValue(String.class);
                            if (session.getUserName().equals(user_config.getName_of_user())){
                                SharedPrefObject spo = new SharedPrefObject(session.getUserName(),user_config.getUser_login_count());
                                tinyDB.putObject("user_config",spo);
                                login();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                databaseReference.child("users").child(session.getUserName()).setValue(user_config);
                tinyDB.putObject("user_config",new SharedPrefObject(session.getUserName(),user_config.getUser_login_count()));
                login();

            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(TwitterLoginActivity.this,"Failure",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void login(){
       Intent intent = new Intent(TwitterLoginActivity.this,NavigationActivity.class);
        startActivity(intent);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
