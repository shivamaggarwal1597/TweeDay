package com.example.shivam.finalinternshipproject.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shivam.finalinternshipproject.DataModels.CategoryObject;
import com.example.shivam.finalinternshipproject.DataModels.SharedPrefObject;
import com.example.shivam.finalinternshipproject.DataModels.WrapperTwitterHandle;
import com.example.shivam.finalinternshipproject.Fragments.AddHandlesToCategoryList.OnListAddHandlesCategoryInteractionListener;
import com.example.shivam.finalinternshipproject.R;
import com.example.shivam.finalinternshipproject.utils.TinyDB;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddHandlesCategoryRecyclerViewAdapter extends RecyclerView.Adapter<AddHandlesCategoryRecyclerViewAdapter.ViewHolder> {

    private final List<WrapperTwitterHandle> mValues;
    private final OnListAddHandlesCategoryInteractionListener mListener;
    FirebaseDatabase firebaseDatabase;
    Context context;
    CategoryObject categoryObject;
    TinyDB tinyDB;
    DatabaseReference databaseReference;
    SharedPrefObject sharedPrefObject;
    public AddHandlesCategoryRecyclerViewAdapter(List<WrapperTwitterHandle> items, OnListAddHandlesCategoryInteractionListener listener,Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        tinyDB = new TinyDB(context);
        sharedPrefObject = tinyDB.getObject("user_config",SharedPrefObject.class);
        categoryObject = tinyDB.getObject("category_object",CategoryObject.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_handles_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        holder.mItem = mValues.get(position);
        Picasso.with(context).load(mValues.get(position).getProfilePictureUrl()).into(holder.imageView);
        holder.mContentView.setText(mValues.get(position).getName());
        //Log.e("Debug Handles: ",mValues.get(position).isIs_active()+" ");
        if (mValues.get(position).getParent_category_name().equals(categoryObject.getCategory_name())){
            holder.mView.setAlpha(1.0f);

        }
        else if (mValues.get(position).getParent_category_name().equals("none")){
            holder.mView.setAlpha(0.4f);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            //TODO: Position Values cause the updation of wrong node
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    if (mValues.get(position).getParent_category_name().equals("none")){
                        holder.mView.setAlpha(1.0f);
                        databaseReference.child("handles")
                                .child(sharedPrefObject.getUser_name())
                                .child("all_handles")
                                .child(mValues.get(position).getScreenName())
                                .child("parent_category_name")
                                .setValue(categoryObject.getCategory_name());

                    }
                    else
                    {
                        holder.mView.setAlpha(0.4f);
                        databaseReference.child("handles")
                                .child(sharedPrefObject.getUser_name())
                                .child("all_handles")
                                .child(mValues.get(position).getScreenName())
                                .child("parent_category_name")
                                .setValue("none");
                    }
                    mListener.onListAddHandlesCategoryInteraction(holder.mItem);
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
