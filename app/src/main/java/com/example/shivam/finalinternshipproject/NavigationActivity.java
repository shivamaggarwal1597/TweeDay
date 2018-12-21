package com.example.shivam.finalinternshipproject;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.shivam.finalinternshipproject.DataModels.CategoryObject;
import com.example.shivam.finalinternshipproject.DataModels.SharedPrefObject;
import com.example.shivam.finalinternshipproject.DataModels.User_config;
import com.example.shivam.finalinternshipproject.DataModels.WrapperTwitterHandle;
import com.example.shivam.finalinternshipproject.Fragments.AddHandlesToCategoryList;
import com.example.shivam.finalinternshipproject.Fragments.FinalTweetShowingFragment;
import com.example.shivam.finalinternshipproject.Fragments.ShowCatogaries;
import com.example.shivam.finalinternshipproject.Fragments.ShowHandles;
import com.example.shivam.finalinternshipproject.utils.TinyDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity implements
        ShowCatogaries.OnListFragmentInteractionListener
        ,ShowHandles.OnListHandlesInteractionListener
        ,AddHandlesToCategoryList.OnListAddHandlesCategoryInteractionListener
        ,FinalTweetShowingFragment.OnListFragmentInteractionListener{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    User_config user_config_o;
    TinyDB tinyDB;
    Map<String,List<String>> my_map;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                 //Fragment of catogaries would come here
                    mTextMessage.setText(R.string.title_home);
                    ShowCatogaries showCatogaries = new ShowCatogaries();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame,showCatogaries,"Catogaries");
                    fragmentTransaction.commit();

                    return true;
                case R.id.navigation_dashboard:
                    //Fragment of handles would come here
                    ShowHandles showHandles = new ShowHandles();
                    android.support.v4.app.FragmentTransaction fragmentHandlesTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentHandlesTransaction1.replace(R.id.frame,showHandles,"Handles");
                    fragmentHandlesTransaction1.commit();
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    //Fragment of Tweets would come here
                    FinalTweetShowingFragment tweetShowFragment = new FinalTweetShowingFragment();
                    android.support.v4.app.FragmentTransaction fragmentHandlesTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentHandlesTransaction2.replace(R.id.frame,tweetShowFragment,"Tweets");
                    fragmentHandlesTransaction2.commit();
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    public void checkMethod(){
        Log.e("Method Check:",user_config_o.getUser_login_count()+" ");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        tinyDB = new TinyDB(NavigationActivity.this);
        databaseReference.push().setValue(my_map);
        String name = tinyDB.getObject("user_config", SharedPrefObject.class).getUser_name();
        databaseReference.child("users").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               user_config_o = dataSnapshot.getValue(User_config.class);
                Log.e("My Tag: ",  user_config_o.getName_of_user());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mTextMessage = (TextView) findViewById(R.id.heading_text);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ShowCatogaries showCatogaries = new ShowCatogaries();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,showCatogaries,"Catogaries");
        fragmentTransaction.commit();
        mTextMessage.setText(R.string.title_home);



    }

    @Override
    public void onListFragmentInteraction(CategoryObject item) {
        String name = item.category_name;
        Log.e("CHECK ADD",name);
        AddHandlesToCategoryList addHandlesToCategoryList = new AddHandlesToCategoryList();
        tinyDB.putObject("category_object",item);
        android.support.v4.app.FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,addHandlesToCategoryList,"AddHandles");
        fragmentTransaction.commit();
        mTextMessage.setText(item.getCategory_name());

    }

    @Override
    public void onListHandlesInteraction(WrapperTwitterHandle item) {

    }

    @Override
    public void onListAddHandlesCategoryInteraction(WrapperTwitterHandle item) {

    }

    @Override
    public void onListFragmentInteraction(Tweet item) {

    }
}
