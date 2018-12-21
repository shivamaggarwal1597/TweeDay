package com.example.shivam.finalinternshipproject.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.shivam.finalinternshipproject.DataModels.CategoryObject;
import com.example.shivam.finalinternshipproject.DataModels.SharedPrefObject;
import com.example.shivam.finalinternshipproject.Fragments.ShowCatogaries.OnListFragmentInteractionListener;
import com.example.shivam.finalinternshipproject.R;
import com.example.shivam.finalinternshipproject.utils.TinyDB;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<CategoryObject> mValues;
    private final OnListFragmentInteractionListener mListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TinyDB tinyDB;
    SharedPrefObject sharedPrefObject;
    public CategoryRecyclerViewAdapter(List<CategoryObject> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        tinyDB = new TinyDB(context);
        sharedPrefObject = tinyDB.getObject("user_config",SharedPrefObject.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_catogaries, parent, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.category_name.setText(mValues.get(position).getCategory_name());
        if (mValues.get(position).active_category()){
            holder.is_active.setChecked(true);
        }
        else {
            holder.is_active.setChecked(false);
        }
        holder.is_active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    databaseReference.child("catogaries").child(sharedPrefObject.getUser_name()).
                            child(mValues.get(position).getCategory_name()).child("active_category").setValue(true);
                }
                else {
                    databaseReference.child("catogaries").child(sharedPrefObject.getUser_name()).
                            child(mValues.get(position).getCategory_name()).child("active_category").setValue(false);
                }
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView category_name;
        public final CheckBox is_active;
        public CategoryObject mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            category_name = (TextView)view.findViewById(R.id.category_name_text_view);
            is_active= (CheckBox)view.findViewById(R.id.radio_category);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + category_name.getText() + "'";
        }
    }
}
