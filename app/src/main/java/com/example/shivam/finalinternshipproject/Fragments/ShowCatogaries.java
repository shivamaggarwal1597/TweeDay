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
import com.example.shivam.finalinternshipproject.R;
import com.example.shivam.finalinternshipproject.utils.TinyDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowCatogaries extends Fragment {


    private int mColumnCount = 1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPrefObject sharedPrefObject;
    TinyDB tinyDB;
    Context context;
    List<CategoryObject> categoryObjectList;
    boolean set_catogaries;
    private OnListFragmentInteractionListener mListener;
    View view;
    public ShowCatogaries() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void getCatogaries(){
       // Log.e("Set Categories: ",set_catogaries+" ");
        if (set_catogaries){

           databaseReference.child("catogaries").child(sharedPrefObject.getUser_name()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    categoryObjectList.clear();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        CategoryObject categoryObject =ds.getValue(CategoryObject.class);
                        categoryObjectList.add(categoryObject);
                    }
                    int size = categoryObjectList.size();
                 //   Log.e("Size of List : ",size+" if case");
                    setCustomAdapter();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            categoryObjectList.add(new CategoryObject("news"));
            categoryObjectList.add(new CategoryObject("education"));
            categoryObjectList.add(new CategoryObject("science"));
            categoryObjectList.add(new CategoryObject("sports"));
            categoryObjectList.add(new CategoryObject("politics"));
            for (CategoryObject co : categoryObjectList){
               databaseReference.child("catogaries").child(sharedPrefObject.getUser_name()).child(co.getCategory_name()).setValue(co);
            }
            databaseReference.child("users").child(sharedPrefObject.getUser_name()).child("set_catogaries").setValue(true);
           // Log.e("Size of List : ",categoryObjectList.size()+" else case");
            setCustomAdapter();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_catogaries_list, container, false);
        sharedPrefObject= tinyDB.getObject("user_config", SharedPrefObject.class);
        firebaseDatabase = FirebaseDatabase.getInstance();
        categoryObjectList = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                set_catogaries = (Boolean)dataSnapshot.child(sharedPrefObject.getUser_name()).child("set_catogaries").getValue();
              //  Log.e("Set Categories: ",set_catogaries+" ");
                getCatogaries();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    public void setCustomAdapter(){
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(new CategoryRecyclerViewAdapter(categoryObjectList, mListener,context));
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
        this.context = context;
        tinyDB = new TinyDB(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(CategoryObject item);
    }
}
