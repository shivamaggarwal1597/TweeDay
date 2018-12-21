package com.example.shivam.finalinternshipproject.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shivam.finalinternshipproject.Fragments.FinalTweetShowingFragment.OnListFragmentInteractionListener;

import com.example.shivam.finalinternshipproject.R;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;
public class MyItemRecyclerViewAdapter2 extends RecyclerView.Adapter<MyItemRecyclerViewAdapter2.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    Context context;
    List<Tweet> final_list;
    public MyItemRecyclerViewAdapter2(OnListFragmentInteractionListener listener,List<Tweet> tweets,Context context) {

        mListener = listener;
        final_list = tweets;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item3, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = final_list.get(position);
        holder.tweet_content.setText(final_list.get(position).text);
        holder.tweet_time_text.setText(final_list.get(position).createdAt);
        holder.tweeter_name.setText(final_list.get(position).user.name);
        Picasso.with(context).load(final_list.get(position).user.profileImageUrl).into(holder.profile_image);
        if (final_list.get(position).entities.media.size()!=0){
            Picasso.with(context).load(final_list.get(position).entities.media.get(0).mediaUrl).into(holder.tweet_content_image);
        }
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
        return final_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Tweet mItem;
        public ImageView profile_image;
        public TextView tweeter_name;
        public TextView tweet_content;
        public ImageView tweet_content_image;
        public TextView tweet_time_text;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            profile_image = (ImageView)view.findViewById(R.id.tweet_profile_image);
            tweeter_name = (TextView)view.findViewById(R.id.user_name_text_view);
            tweet_content = (TextView)view.findViewById(R.id.tweet_content_text_view);
            tweet_content_image = (ImageView)view.findViewById(R.id.tweet_image_view);
            tweet_time_text = (TextView)view.findViewById(R.id.tweet_time_text_view);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + tweet_content.getText() + "'";
        }
    }
}
