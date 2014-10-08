package com.codepath.apps.basictwitter.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ProfileFragment extends Fragment {

    private User        user;
    public  ImageView   ivProfileImage;
    public  TextView    tvUserName;
    public  TextView    tvTag;
    public  TextView    tvFollowing;
    public  TextView    tvFollowers;
    public  TextView    tvTweets;

    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(User.ARG_PARAM, user);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(User.ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        ivProfileImage = (ImageView)v.findViewById(R.id.ivProfileImage);
        tvUserName = (TextView)v.findViewById(R.id.tvUserName);
        tvTag = (TextView)v.findViewById(R.id.tvTag);
        tvFollowing = (TextView)v.findViewById(R.id.tvFollowing);
        tvFollowers = (TextView)v.findViewById(R.id.tvFollowers);
        tvTweets = (TextView)v.findViewById(R.id.tvTweets);

        try{
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(user.getProfileImageUrl(), ivProfileImage);
        } catch (Exception e) {

        }

        tvUserName.setText(user.getName());
        tvTag.setText(user.getDescription());
        tvFollowing.setText(getFormattedCount(user.getFriendsCount())+" Following");
        tvFollowers.setText(getFormattedCount(user.getFollowersCount())+" Followers");
        tvTweets.setText(getFormattedCount(user.getStatusesCount())+" Tweets");
        return v;
    }

    private String getFormattedCount(int count) {
        if (count>1000) {
            return Integer.toString(count/1000)+"k";
        } else {
            return Integer.toString(count);
        }
    }
}
