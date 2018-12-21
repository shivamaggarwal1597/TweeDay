package com.example.shivam.finalinternshipproject.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.shivam.finalinternshipproject.DataModels.SharedPrefObject;
import com.example.shivam.finalinternshipproject.DataModels.WrapperTwitterHandle;
import com.example.shivam.finalinternshipproject.Fragments.ShowHandles.OnListHandlesInteractionListener;

import com.example.shivam.finalinternshipproject.NavigationActivity;
import com.example.shivam.finalinternshipproject.R;
import com.example.shivam.finalinternshipproject.utils.TinyDB;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HandlesRecyclerViewAdapter extends RecyclerView.Adapter<HandlesRecyclerViewAdapter.ViewHolder> {

    private final List<WrapperTwitterHandle> mValues;
    private final OnListHandlesInteractionListener mListener;
    FirebaseDatabase firebaseDatabase;
    Context context;
    TinyDB tinyDB;
    DatabaseReference databaseReference;
    SharedPrefObject sharedPrefObject;

    public HandlesRecyclerViewAdapter(List<WrapperTwitterHandle> items, OnListHandlesInteractionListener listener,Context context) {
        mValues = items;
        mListener = listener;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        this.context = context;
        tinyDB = new TinyDB(context);
        sharedPrefObject = tinyDB.getObject("user_config",SharedPrefObject.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_handles, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        Picasso.with(context).load(mValues.get(position).getProfilePictureUrl()).into(holder.imageView);
        holder.mContentView.setText(mValues.get(position).getName());
        if (mValues.get(position).isIs_active()){
            holder.mView.setAlpha(1.0f);

        }
        else if (!mValues.get(position).isIs_active()){
            holder.mView.setAlpha(0.4f);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    if (!mValues.get(position).isIs_active()){
                       holder.mView.setAlpha(1.0f);
                       databaseReference.child("handles").child(sharedPrefObject.getUser_name()).child("all_handles").child(mValues.get(position).getScreenName()).child("is_active").setValue(true);
                    }
                    else
                    {
                        holder.mView.setAlpha(0.4f);
                        databaseReference.child("handles").child(sharedPrefObject.getUser_name()).child("all_handles").child(mValues.get(position).getScreenName()).child("is_active").setValue(false);
                    }
                    mListener.onListHandlesInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public WrapperTwitterHandle mItem;
        public ImageView imageView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.name_of_handle);
            imageView = (ImageView)view.findViewById(R.id.profile_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
        }
    }

